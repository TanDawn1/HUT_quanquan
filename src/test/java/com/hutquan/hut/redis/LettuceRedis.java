package com.hutquan.hut.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

import java.io.Serializable;
import java.util.Set;

@SpringBootTest
public class LettuceRedis {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Test
    public void get(){

        SetOperations<String, Serializable> ops = redisTemplate.opsForSet();

        Set<Serializable> opsset = ops.members("test1");

        for(Serializable value: opsset){
            System.out.println(value);
        }
    }

}
