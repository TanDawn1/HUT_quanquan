package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IUserService;
import com.hutquan.hut.vo.EnumStatus;
import com.hutquan.hut.vo.ResponseBean;
import com.hutquan.hut.vo.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 通过Id查询用户数据
     * @param userId
     * @return
     */
    @GetMapping("/user/selectuser")
    public UserStatus selectUser(int userId){
        try {
            User user = iUserService.selectUser(userId);
            return new UserStatus(EnumStatus.SUCCESS.getCode(),EnumStatus.SUCCESS.getMessage(),user);
        }catch (Exception e){
            e.printStackTrace();
            return new UserStatus(EnumStatus.Fail.getCode(),EnumStatus.Fail.getMessage(),null);
        }
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/user/res")
    public ResponseBean res(@RequestBody User user){
        //TODO 输入了手机号应该验证一遍
        int useId = iUserService.insertUser(user);
        if(useId == 1){
            return new ResponseBean(EnumStatus.SUCCESS.getCode(),EnumStatus.SUCCESS.getMessage(),null);
        }else if(useId == 0){
            return new ResponseBean(EnumStatus.Repetition.getCode(),EnumStatus.Repetition.getMessage(),null);
        }
        return new ResponseBean(EnumStatus.Fail.getCode(),EnumStatus.Fail.getMessage(),null);
    }

    /**
     * 登录
     * @param user
     * @return
     */
    @GetMapping("/user/login")
    public ResponseBean login(@RequestBody User user, HttpServletRequest request){
        User user1 = iUserService.login(user);
        if(user1 != null) {
            request.getSession().setAttribute("user", user1);
            return new ResponseBean(EnumStatus.SUCCESS.getCode(),EnumStatus.SUCCESS.getMessage(),user1);
        }else{
            return new ResponseBean(EnumStatus.Fail.getCode(),EnumStatus.Fail.getMessage(),null);
        }
    }
}
