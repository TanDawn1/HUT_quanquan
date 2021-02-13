package com.hutquan.hut.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.hutquan.hut.config.MyThreadPoolConfig;
import com.hutquan.hut.mapper.ISearchMapper;
import com.hutquan.hut.pojo.Active;
import com.hutquan.hut.pojo.ActiveRecords;
import com.hutquan.hut.pojo.CaptchaVO;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IActiveService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.PageBean;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class ActiveServiceImpl implements IActiveService {

    @Autowired
    private DefaultKaptcha producer;

    @Autowired
    private ISearchMapper iSearchMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    @Qualifier("threadPoolExecutor")
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private DataSaveThreadImpl dataSaveThread;

    //private static final ThreadLocal<ActiveRecords> threadLocal = new ThreadLocal<>();

    private static final String ACTIVE = "activeSign:";

    private static final String CAPTCHA = "CAPTCHA:";

    private static final String HOTACTIVIVE = "hotactive:";

    private static final String LOCKHOTAC = "LOCK-HOTAC";

    //private boolean flag = true;
    private static final Logger logger = LoggerFactory.getLogger(ActiveServiceImpl.class);

    @Override
    public Double signActive(User user, Integer activeId) {
        //判断活动报名时间及开始时间，从redis中获取值
        Active active = (Active) redisUtils.get(HOTACTIVIVE + activeId);
        if(active == null){
            //非法访问，未经过详情页面
            return -1000.0;
        }
        if(active.getSignStartTime() > Instant.now().getEpochSecond() || active.getSignEndTime() < Instant.now().getEpochSecond()){
            //报名开始时间大于当前时间 或者 结束时间小于当前时间，就结束
            return -1001.0;
        }
        //抢占表，该表在活动创建的时候存在 持续时间 活动结束时间 - 活动创建时间 + 24h
        double val = redisUtils.decr(ACTIVE + activeId, 1);
        //判断val 是否大于等于0
        if (val >= 0) {
            //将用户存储入set表，活动开始时将创建Set表，Set表时间为活动结束时间 - 活动创建时间 + 24h
            redisUtils.sSet("active:" + activeId, user.getUserId());
            //将数据提交给线程池插入数据库
            dataSaveThread.user = user;
            dataSaveThread.active = active;
            threadPoolExecutor.submit(dataSaveThread);
            //TODO 优化成队列插入
            return val;
        }
        return -1.0;
    }

    @Override
    public PageBean<Active> actives(int pageNum) {
        PageHelper.startPage(pageNum, 20);
        List<Active> list = iSearchMapper.queryActive();
        //不显示剩余名额
//        for (Active val : list) {
//            val.setLastPerson(Integer.valueOf(redisUtils.hget(ACTIVE, val.getId().toString()).toString()));
//        }
        return new PageBean<>(list);
    }

    @Override
    public Active activeDetailed(int activeId) {
        //获取详情
        //分布式锁
        Active active = null;
        //获取redis中的缓存
        active = (Active) redisUtils.get(HOTACTIVIVE + activeId);
        if (active == null) {
            String uuid = UUID.randomUUID().toString();
            try {
                //获取锁
                if (!redisUtils.lock(LOCKHOTAC, uuid)) {
                    return null;
                }
                //再次获取redis中的缓存
                active = (Active) redisUtils.get(HOTACTIVIVE + activeId);
                if (active == null) {
                    active = iSearchMapper.queryActiveDetailed(activeId);
                    //将该线程的数据加入threadLocal
                    //threadLocal.set(active);
                    //加缓存
                    long time = active.getEndTime() - Instant.now().getEpochSecond();
                    long OUTTIME;
                    if (time <= 0) {
                        OUTTIME = 30 * 60L;
                    } else {
                        OUTTIME = active.getEndTime() - Instant.now().getEpochSecond() + 30 * 60;
                    }
                    //TODO LU脚本处理
                    redisUtils.set(HOTACTIVIVE + activeId, active, OUTTIME);
                    redisUtils.set(ACTIVE + activeId, active.getLastPerson(), OUTTIME);
                }

            } catch (Exception e) {
                //exception
                logger.info("数据获取异常");
            } finally {
                //释放锁
                redisUtils.unlock(LOCKHOTAC, uuid);
            }
        }
        //剩余名额，如果能获取到缓存中最新的名额则更新，否则就使用原始数据
        Integer last;
        if(active != null && (last = (Integer) redisUtils.get(ACTIVE + activeId)) != null)
            active.setLastPerson(last);
        return active;
    }

    @Override
    public CaptchaVO getCaptchaVO() {
        try {
            String content = producer.createText();
            ByteArrayOutputStream outputStream = null;
            BufferedImage image = producer.createImage(content);

            outputStream = new ByteArrayOutputStream();

            ImageIO.write(image, "jpg", outputStream);
            //对字节进行Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            String str = "data:image/jpeg;base64,";
            String base64Img = str + encoder.encode(outputStream.toByteArray()).replace("\n", "").replace("\r", "");
            //生成一个随机标识符
            String captchaKey = UUID.randomUUID().toString();

            //缓存验证码并设置过期时间
            Long timeout = 300L;
            redisUtils.set(CAPTCHA.concat(captchaKey), content, timeout);

            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaKey(captchaKey);
            captchaVO.setExpire(timeout);

            captchaVO.setBase64Img(base64Img);
            return captchaVO;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
