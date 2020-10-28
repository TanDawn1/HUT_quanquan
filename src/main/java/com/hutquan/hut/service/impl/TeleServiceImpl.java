package com.hutquan.hut.service.impl;

import com.hutquan.hut.mapper.IUserMapper;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.ITeleService;
import com.hutquan.hut.utils.HttpUtils;
import com.hutquan.hut.utils.MyMiniUtils;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.ResponseBean;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

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
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //TODO 将用户信息封装发送到MQ，拆分到消息发送系统
    @Override
    public String sendTele(String tele) {
        String telepd = "^[1][3,4,5,7,8][0-9]{9}$";
        if(tele == null || !Pattern.matches(telepd,tele)) return "错误的手机号";

        String teleR = "yzm:"+tele;
        String host = "http://intlsms.market.alicloudapi.com";
        String path = "/comms/sms/groupmessaging";
        String method = "POST";
        String appcode = "a68b52d6bb0346bdb20cba6fd6a7bf79";
        //取验证码
        String code = MyMiniUtils.randomNumber("0123456789", 6);

//        iUserMapper.insertTele(tele, code, LocalDateTime.now());
        //把验证码存入redis数据库 如果存在就更新 5min过期
        redisUtils.set(teleR,code,300L);
        System.out.println("验证码是" + code);
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Set<String> telenumber = new HashSet<>();
        //+86
        telenumber.add(86+tele);
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
}
