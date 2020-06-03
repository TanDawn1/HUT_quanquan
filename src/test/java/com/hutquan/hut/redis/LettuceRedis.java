package com.hutquan.hut.redis;

import com.hutquan.hut.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class LettuceRedis {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisUtils red;

    @Test
    public void get(){

        String s = "123";
        red.set("yzm:18973326132","123",30);
//        red.expire("yzm:18973326132",30);
        //直接替换
        red.set("yzm:18973326132","12345",60);

        String s1 = (String) red.get("yzm:18973326132");
        System.out.println(s1);
        System.out.println(s1.equals("123"));



    }

}
