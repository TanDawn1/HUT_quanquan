package com.hutquan.hut.redis;

import com.hutquan.hut.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class LettuceRedis {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisUtils red;

    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void get(){

//        String s = "123";
//        red.set("yzm:18973326132","123",30);
////        red.expire("yzm:18973326132",30);
//        //直接替换
//        red.set("yzm:18973326132","12345",60);
//
//        String s1 = (String) red.get("yzm:18973326132");
//        System.out.println(s1);
//        System.out.println(s1.equals("123"));
//


    }

    @Test
    public void hashget(){
       // System.out.println(redisUtils.hget("dynamic_comment","d1"));
        //System.out.println(redisTemplate.opsForHash().get("dynamic_comment","d1"));
        System.out.println(redisUtils.hset("dynamic_comment","dtest",30));
    }

    @Test
    public void set(){
        System.out.println(redisUtils.set("18973326132","abc"));
    }

    @Test
    public void test(){
        System.out.println(redisUtils.zscore("star:0", 4));
    }

    @Test
    public void testGeo(){
//        redisTemplate.opsForGeo().radius("location","g0",new Distance(200D, Metrics.KILOMETERS)), RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().sortDescending().includeCoordinates();
////        //redisTemplate.opsForGeo().geoRadiusByMember()
        GeoResults<RedisGeoCommands.GeoLocation<Object>> geoResults = redisTemplate.opsForGeo().radius("location","0",new Distance(200D,Metrics.KILOMETERS),RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().sortDescending().includeDistance());
        //()geoResults.getContent().get(0)
//        System.out.println(geoResults.getContent());
//        System.out.println(geoResults.getContent().size());
//        System.out.println(geoResults.getContent().get(0));
//        GeoResult geoResult = (GeoResult) geoResults.getContent().get(0);
//        System.out.println(geoResult.getDistance().getValue());
//        System.out.println(geoResult.getContent());
//        RedisGeoCommands.GeoLocation<String> location = (RedisGeoCommands.GeoLocation<String>) geoResult.getContent();
        GeoResult<RedisGeoCommands.GeoLocation<Object>> location = geoResults.getContent().get(0);
        System.out.println(Integer.valueOf(location.getContent().getName().toString()));
        //RedisGeoCommands.GeoLocation geoLocation = geoResult.getDistance();
//        System.out.println(redisTemplate.opsForGeo().
//                radius("location","g1",new Distance(200D)));
//        System.out.println(redisTemplate.opsForGeo().add("location",new Point(117.21547633409500122,39.01475126149295392),"1"));
//        System.out.println(redisTemplate.opsForGeo().add("location",new Point(116.41231566667556763,39.89279387941491706),"0"));
       /* 2) 1) "g1"
        2) 1) "117.21547633409500122"
        2) "39.01475126149295392"
        3) 1) "g0"
        2) 1) "116.41231566667556763"
        2) "39.89279387941491706"*/
    }

    @Test
    public void login(){
        //System.out.println(redisUtils.hset("userToken","1","daedqfeqf"));
        System.out.println(redisUtils.hget("userToken","1"));
    }


}
