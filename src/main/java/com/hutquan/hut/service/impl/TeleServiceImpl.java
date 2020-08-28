package com.hutquan.hut.service.impl;

import com.hutquan.hut.mapper.IUserMapper;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.ITeleService;
import com.hutquan.hut.utils.HttpUtils;
import com.hutquan.hut.utils.MyMiniUtils;
import com.hutquan.hut.utils.RedisUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TeleServiceImpl implements ITeleService {

    @Autowired
    private IUserMapper iUserMapper;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    public boolean selectUser(String tele) {
        if(iUserMapper.teleSelectUser(tele) != null){
            return true;
        }
        return false;
    }

    @Override
    public User teleLogin(String tele, String yzm) {
        //先判断Redis中的yzm是否正确,正确返回User数据
        String yzmRedis = (String) redisUtils.get(tele);
        if(yzmRedis != null && yzmRedis.equals(yzm)){
            User user = iUserMapper.teleLogin(tele);
            if(user == null){
                //如果user为null，说明是个新用户,则注册一个
                user = new User();
                //注册时间
                user.setTime(Instant.now().getEpochSecond());
                //用户名就用tele，需要后续修改
                user.setUsername(tele);
                //随机生成密码
                user.setPasswd(UUID.randomUUID().toString());
                //默认头像
                user.setAvatarPicture("default.jpg");
                if(iUserMapper.insertUser(user) != 1) {
                    Exception e = new Exception();
                    e.printStackTrace();
                    return null;
                }
            }
            return user;
        }else {
            return null;
        }
    }

    @Override
    public String sendTele(String tele) {
        String teleR = "yzm:"+tele;
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";
        String appcode = "teleAppcode";
        //取验证码
        String code = MyMiniUtils.randomNumber("0123456789", 6);

//        iUserMapper.insertTele(tele, code, LocalDateTime.now());
        //把验证码存入redis数据库 如果存在就更新 5min过期
        redisUtils.set(teleR,code,300L);
        System.out.println("验证码是" + code);
        //userDAO.saveYZM(tele, code);
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", tele);
        querys.put("param", "code:" + code);
        querys.put("tpl_id", "TP1711063");
        Map<String, String> bodys = new HashMap<String, String>();
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "500";
    }
}
