package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.ITeleService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.EnumStatus;
import com.hutquan.hut.vo.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
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
    @PostMapping("/tele/yzm")
    @ApiOperation("发送验证码")
    public ResponseBean yzmSend(@RequestBody String tele){
        if(iTeleService.selectUser(tele)){
            return new ResponseBean(
                    Integer.valueOf(iTeleService.sendTele(tele).substring(16,21)),
                    "ok",null);
        }
        // 0 NULL 手机号错误
       return new ResponseBean(EnumStatus.RESNULL.getCode(),EnumStatus.RESNULL.getMessage(),null);
    }

    /**
     * 手机号验证码登录
     * @param tele
     * @param yzm
     * @param
     * @return
     */
    @PostMapping("/tele/login")
    @ApiOperation("手机号验证码登录")
    public ResponseBean yzmLogin(String tele , String yzm){

        User user = iTeleService.teleLogin(tele,yzm);
        if(user != null) {
            //request.getSession().setAttribute("user", user);
            String token = UUID.randomUUID() + "";
            //把token存储到Redis   30min
            redisUtils.set(token,user, 30 * 60L);
            //用户位置存储到Redis
//            redisUtils
            return new ResponseBean(200,token,user);

        }else{
            return new ResponseBean(400,"验证码错误",null);
        }
    }

}
