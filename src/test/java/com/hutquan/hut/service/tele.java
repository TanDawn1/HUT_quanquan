package com.hutquan.hut.service;

import com.hutquan.hut.utils.HttpUtils;
import com.hutquan.hut.utils.MyMiniUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 短信测试
 */
public class tele {

    public static void main(String[] args) {
        String host = "https://intlsms.market.alicloudapi.com";
        String path = "/comms/sms/groupmessaging";
        String method = "POST";
        String appcode = "a68b52d6bb0346bdb20cba6fd6a7bf79";
        //取验证码
        String code = MyMiniUtils.randomNumber("0123456789", 6);

        System.out.println("验证码是" + code);

        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Set<String> tele = new HashSet<>();
        tele.add("8618973326132");
        Set<String> templateSet = new HashSet<>();
        templateSet.add(code);
        templateSet.add("'1'");
        System.out.println(templateSet.toString());
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("callbackUrl", "http://test.dev.esandcloud.com");
        bodys.put("channel", "0");
        bodys.put("mobileSet", String.valueOf(tele));
        bodys.put("templateID", "0000000");
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
//            System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
