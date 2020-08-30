package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IUserService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.EnumStatus;
import com.hutquan.hut.vo.ResponseBean;
import com.hutquan.hut.vo.UserStatus;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 账号相关
 */
@RestController
public class UserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 通过Id查询用户数据
     * 测试用
     * @param userId
     * @return
     */
    @GetMapping("/user/selectuser")
    @ApiOperation("测试用")
    public UserStatus selectUser(@RequestParam int userId){
        System.out.println(userId);
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
    @ApiOperation("注册 -> 该接口已弃用")
    public ResponseBean res(@RequestBody User user){
        //TODO 输入了手机号应该验证一遍
        int useId = iUserService.insertUser(user);
        if(useId == 1){
            return new ResponseBean(200,"弃用的接口",null);
        }else if(useId == 0){
            return new ResponseBean(200,"弃用的接口",null);
        }
        return new ResponseBean(200,"弃用的接口",null);
    }


    /**
     * 登录
     * @param user
     * @return
     */
    @GetMapping("/user/login")
    @ApiOperation("登录")
    public ResponseBean login(@RequestBody User user, HttpServletRequest request){
        User user1 = iUserService.login(user);
        if(user1 != null) {
            //request.getSession().setAttribute("user", user);
            String token = UUID.randomUUID() + "";
            //把token存储到Redis   30min
            redisUtils.set(token,user1, 30 * 60L);
            //存储token与账号对应关系
            redisUtils.hset("userToken",user1.getUserId().toString(),token);
            return new ResponseBean(200,"ok",user1);
        }else{
            return new ResponseBean(200,"密码或者账号错误",null);
        }
    }

    /**
     * 未登录
     * @return
     */
    @GetMapping("/nologin")
    @ApiOperation("未登录")
    public ResponseBean loginPage() {
        return new ResponseBean(400,"未登录",null);
    }


    /**
     * 退出登录
     * @return
     */
    @GetMapping("/user/out")
    @ApiOperation("退出登录")
    public void out(HttpServletRequest request){
        request.getSession().removeAttribute("user");
    }

    /**
     * 更新资料
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/user/updata")
    @ApiOperation("更新资料")
    public ResponseBean updataUser(@RequestBody User user, HttpServletRequest request){
        User user1 = (User) redisUtils.get(request.getHeader("token"));
        if(user1 != null) {
            if (iUserService.updataUser(user)) {
                //更新Redis中的数据  对user1和user进行对比
                if(!user.getSex().equals(user1.getSex()) && user.getSex() != null) user1.setSex(user.getSex());
                if(!user.getUsername().equals(user1.getUsername()) && user.getUsername() != null) user1.setUsername(user.getUsername());
                if(!user.getSignature().equals(user1.getSignature()) && user.getSignature() != null) user1.setSignature(user.getSignature());
                //刷新token对应的user数据
                redisUtils.set(request.getHeader("token"),user1);
                return new ResponseBean(200, "ok", user1);
            } else {
                return new ResponseBean(400, "ok", null); //错误
            }
        }else{
            return new ResponseBean(300,"fail",null); //未登录
        }
    }

    /**
     * 更新头像
     * @param
     * @param request
     * @return
     */
    @PostMapping("/user/upheadphoto")
    @ApiOperation("更新头像")
    public ResponseBean updataHeadPhoto(@RequestParam("multipartfile") MultipartFile file, HttpServletRequest request){
        if(file.isEmpty()){
            return new ResponseBean(400,"fail,文件上传失败",null);
        }
        User user1 = (User) redisUtils.get(request.getHeader("token"));
        if(user1 != null) {
            String url = iUserService.updataHeadPhoto(user1,file);
            if(url != null){
                return new ResponseBean(200,"ok",url);
            }else{
                return new ResponseBean(500,"fail",null);
            }
        }else{
            return new ResponseBean(300,"fail，未登录",null); //未登录
        }
    }

    /**
     * 查看关注的用户
     */
    @GetMapping("/user/follower")
    @ApiOperation("查看的关注的用户")
    public ResponseBean queryFollower(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return new ResponseBean(400,"fail,未登录",null);
        }
        return new ResponseBean(200,"success",iUserService.queryFollower(user.getUserId(),0L,-1L));
    }
}
