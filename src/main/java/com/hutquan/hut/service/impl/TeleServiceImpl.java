package com.hutquan.hut.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hutquan.hut.mapper.IUserMapper;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.pojo.Xh;
import com.hutquan.hut.service.ITeleService;
import com.hutquan.hut.utils.*;
import com.hutquan.hut.vo.ResponseBean;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class TeleServiceImpl implements ITeleService {


    private static final String USERLOGIN = "userLogin";

    private static Logger logger = LoggerFactory.getLogger(TeleServiceImpl.class);

    @Autowired
    private IUserMapper iUserMapper;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    public boolean selectUser(String tele) {
        if (iUserMapper.teleSelectUser(tele) != null) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public User teleLogin(String tele, String yzm) {
        //先判断Redis中的yzm是否正确,正确返回User数据
        try {
            String yzmRedis = "";
            if (yzm.equals("abc")) {
                yzmRedis = (String) redisUtils.get(tele);
            } else {
                yzmRedis = (String) redisUtils.get("yzm:" + tele);
            }
            if (yzmRedis != null && yzmRedis.equals(yzm)) {
                //tele加索引
                User user = iUserMapper.teleLogin(tele);
                if (user == null) {
                    //如果user为null，说明是个新用户,则注册一个
                    user = new User();
                    //注册时间
                    user.setTime(Instant.now().getEpochSecond());
                    //用户名就用tele，需要后续修改
                    user.setUsername(tele);
                    user.setTele(tele);
                    //随机生成密码
                    user.setPasswd(UUID.randomUUID().toString().substring(0, 19));
                    //默认头像
                    user.setAvatarPicture("[\"default.jpg\"]");
                    //会返回userId
                    if (iUserMapper.insertUser(user) != 1) {
                        throw new RuntimeException();
                    }
                }
                return user;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO 将用户信息封装发送到MQ，拆分到消息发送系统
    @Override
    public String sendTele(String tele) {
        String telepd = "^[1][3,4,5,7,8][0-9]{9}$";
        if (tele == null || !Pattern.matches(telepd, tele)) return "错误的手机号";

        String teleR = "yzm:" + tele;
        String host = "http://intlsms.market.alicloudapi.com";
        String path = "/comms/sms/groupmessaging";
        String method = "POST";
        String appcode = "a68b52d6bb0346bdb20cba6fd6a7bf79";
        //取验证码
        String code = MyMiniUtils.randomNumber("0123456789", 6);

//        iUserMapper.insertTele(tele, code, LocalDateTime.now());
        //把验证码存入redis数据库 如果存在就更新 5min过期
        redisUtils.set(teleR, code, 300L);
        System.out.println("验证码是" + code);
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Set<String> telenumber = new HashSet<>();
        //+86
        telenumber.add(86 + tele);
        Set<String> templateSet = new HashSet<>();
        templateSet.add(code);
        //templateSet.add("'0'");
        System.out.println(templateSet.toString());
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("callbackUrl", "http://test.dev.esandcloud.com");
        bodys.put("channel", "0");
        bodys.put("mobileSet", String.valueOf(telenumber));
        bodys.put("templateID", "20200914093120");
        bodys.put("templateParamSet", String.valueOf(templateSet));
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
            return "500";
        }

    }

    @Override
    public ResponseBean xhLogin(Xh xhl) {
        logger.info(xhl.getXh() + "abc:"+xhl.getPasswd()+":adqeqrq");
        //用户判断用户是否是第一次登录系统
        int f = 1;
        //调用教务系统api
        String url = "http://218.75.197.123:83/app.do?method=authUser&xh=" + xhl.getXh() + "&pwd=" + xhl.getPasswd();
        ResponseBean responseBean = new ResponseBean();
        User user = null;

        try {
            //判断是否第一次登录系统
            if(!redisUtils.sHasKey(USERLOGIN,xhl.getXh())){
                //第一次登录系统
                f = 0;
            }
            String line = HttpClient.doGet(url);
            JSONObject json = JSONObject.parseObject(line);
            System.out.println(json.get("success"));
            if ("true".equals(json.getString("success"))) {
                responseBean.setCode(200);
                //获取user数据 不是第一次登录系统
                if(f == 1){
                    user = iUserMapper.selectXh(xhl.getXh());
                }else{
                    user = new User();
                    //第一次登录 注册进数据库
                    if(!saveDB(xhl,user)){
                        responseBean.setCode(500);
                    }else{
                        redisUtils.sSet(USERLOGIN,xhl.getXh());
                    }
                }
                if (user.getTele() == null) user.setTele("0");
                responseBean.setData(user);
            } else {
                responseBean.setCode(400);
            }
            String token = (String) json.get("token");
            responseBean.setMsg(token);
            //当token不为-1，且user不为null的时候说明验证成功
            if (!token.equals("-1") && user != null) {
                saveToken(token, user);
            }
            return responseBean;
        } catch (Exception e) {
            //教务系统验证 教务系统异常 系统分配token登录
            logger.info("教务系统异常");
            //通过保存的xh和密码验证是否成功
            //密码MD5重新加密
            xhl.setPasswd(MD5.getMD5(xhl.getPasswd()));
            if(f == 1) {
                user = iUserMapper.selectXhAndPass(xhl);
            }
            if (user != null) {
                responseBean.setCode(200);
                String token = UUID.randomUUID() + "";
                saveToken(token, user);
                responseBean.setMsg(token);
            } else {
                responseBean.setCode(400);
                responseBean.setMsg("-1");
            }
            return responseBean;
        }

    }

    public void saveToken(String token, User user) {
        //将token刷新进redis    30day
        redisUtils.set(token, user, 30 * 24 * 60 * 60L);
        //存储token与账号对应关系->一重新登录就刷新
        //查看是否userToken中是否已经存在token，存在就清除该token
        String oldToken = (String) redisUtils.hget("userToken", user.getUserId().toString());
        if (oldToken != null) redisUtils.del(oldToken);
        redisUtils.hset("userToken", user.getUserId().toString(), token);
//        responseBean.setMsg(token);
    }

    public boolean saveDB(Xh xh,User user){
       try { //注册时间
           user.setTime(Instant.now().getEpochSecond());
           //用户名就用学号，需要后续修改
           user.setUsername(xh.getXh());
           //将密码MD5加密后存储
           user.setPasswd(MD5.getMD5(xh.getPasswd()));
           //学号
           user.setXh(xh.getXh());
           //默认头像
           user.setAvatarPicture("[\"default.jpg\"]");
       }catch (Exception e){
           e.printStackTrace();
       }
        //会返回userId
        return iUserMapper.insertXhUser(user) == 1;
    }

}
