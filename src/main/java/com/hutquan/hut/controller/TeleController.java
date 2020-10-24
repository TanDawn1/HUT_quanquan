package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.ITeleService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 短信相关
 */
@RestController
public class TeleController {

    @Autowired
    private ITeleService iTeleService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 发送验证码
     * @param tele
     * @return
     */
    @PostMapping("/tele/yzm/{tele}")
    @ApiOperation("发送验证码," +
            "为了防止恶意访问，做了个限制，同一个号码1小时内最多只能调用该接口5次" +
            "如果在一个小时之内同一个号码请求次数达到60则该号码永久不能登录")
    public String yzmSend(@PathVariable("tele") String tele, HttpServletRequest request){

        if(redisUtils.hasKey("number:"+tele)){
            redisUtils.incr("number:"+tele,1);
            if((Integer)redisUtils.get("number:"+tele) >= 5) {
                if((Integer)redisUtils.get("number:"+tele) >= 60) {
                    redisUtils.set("number:"+tele,60);
                    return "号码已永久拉入黑名单";
                }
                return "请求次数达到上限";
            }
        }else {
            redisUtils.set("number:"+tele,1, 60 * 60L);
        }

        //if(iTeleService.selectUser(tele)){
        //不判断是否为已存在用户直接发送验证码
            return iTeleService.sendTele(tele);
        //}
        // 0 NULL 手机号错误
       //return new ResponseBean(EnumStatus.RESNULL.getCode(),EnumStatus.RESNULL.getMessage(),null);
    }

    /**
     * 手机号验证码登录
     * @param tele
     * @param yzm
     * @param
     * @return
     */
    @PostMapping("/tele/login")
    @ApiOperation("手机号验证码登录,token保留30天")
    public ResponseBean yzmLogin(String tele , String yzm){
        if(tele == null || yzm == null) return new ResponseBean(400,"手机号码或者验证码格式异常",null);
        User user = iTeleService.teleLogin(tele,yzm);
        if(user != null) {
            //生成token
            String token = UUID.randomUUID() + "";
            //把token存储到Redis   30day
            redisUtils.set(token,user, 30 * 24 * 60 * 60L);
            //存储token与账号对应关系->一重新登录就刷新
            //查看是否userToken中是否已经存在token，存在就清除该token
            String oldToken = (String) redisUtils.hget("userToken",user.getUserId().toString());
            if(oldToken != null) redisUtils.del(oldToken);
            redisUtils.hset("userToken",user.getUserId().toString(),token);
            if(user.getPasswd() != null){
                //2001 说明是新用户
                return new ResponseBean(2001,token,user);
            }
            return new ResponseBean(200,token,user);
        }else{
            return new ResponseBean(400,"验证码错误",null);
        }
    }

}
