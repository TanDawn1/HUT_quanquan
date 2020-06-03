package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.ITeleService;
import com.hutquan.hut.vo.EnumStatus;
import com.hutquan.hut.vo.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TeleController {

    @Autowired
    private ITeleService iTeleService;

    /**
     * 发送验证码
     * @param tele
     * @return
     */
    @PostMapping("/tele/yzm")
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
     * @param request
     * @return
     */
    @PostMapping("/tele/login")
    public ResponseBean yzmLogin(@RequestBody String tele , @RequestBody String yzm, HttpServletRequest request){

        User user = iTeleService.teleLogin(tele,yzm);
        if(user != null) {
            request.getSession().setAttribute("user", user);
            return new ResponseBean(200,EnumStatus.SUCCESS.getMessage(),user);
        }else{
            return new ResponseBean(400,"验证码错误",null);
        }
    }

}
