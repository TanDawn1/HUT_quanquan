package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IUserService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.ResponseBean;
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
     * 通过token获取用户数据
     * 测试用
     * @return
     */
    @GetMapping("/user/selfuser")
    @ApiOperation("通过token获取用户数据")
    public ResponseBean selfUser(HttpServletRequest request){
        User user = (User) redisUtils.get(request.getHeader("token"));
        try {
            user.setFollowCount(Double.valueOf(iUserService.querySelfFollow(user)));
            user.setSelfFollowCount(Double.valueOf(iUserService.querySelfFollowed(user)));
            //自己不会关注自己，所以不添加是否关注的字段
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseBean(500,"未知错误",user);
        }
        return new ResponseBean(200,"ok",user);
    }

    @GetMapping("/user/selectuser/{otherUserId}")
    @ApiOperation("查看其他用户的信息首页，状态必须为已登录")
    public ResponseBean selectUser(HttpServletRequest request,@PathVariable Integer otherUserId){
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null) return new ResponseBean(401,"未登录,无权限",null);
        try {
            User othUser = iUserService.selectUser(otherUserId);
            othUser.setFollowCount(Double.valueOf(iUserService.querySelfFollow(othUser)));
            othUser.setSelfFollowCount(Double.valueOf(iUserService.querySelfFollowed(othUser)));
            //是否关注了别人 判断user是否关注otherUser
            othUser.setFollowed(iUserService.followed(user.getUserId(),otherUserId));
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseBean(500,"未知错误",user);
        }
        return new ResponseBean(200,"ok",otherUserId);
    }


    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/user/res")
    @ApiOperation("注册 -> 该接口已弃用")
    public ResponseBean res(@RequestBody User user){
        return new ResponseBean(302,"弃用的接口",null);
    }


    /**
     * 登录
     * @param user
     * @return
     */
    @GetMapping("/user/login")
    @ApiOperation("登录 -> 该接口弃用")
    public ResponseBean login(@RequestBody User user, HttpServletRequest request){
        if(user != null) return new ResponseBean(302,"接口弃用",null);
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
    @ApiOperation("退出登录 -> 清除用户token的操作")
    public void out(HttpServletRequest request){
        User user = (User) redisUtils.get(request.getHeader("token"));
        String token = (String) redisUtils.hget("userToken",user.getUserId().toString());
        if (token != null) redisUtils.del(token);
        redisUtils.hdel("userToken",user.getUserId().toString());
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
        //获取老的数据
        User olduser = (User) redisUtils.get(request.getHeader("token"));
        if(olduser != null) {
            if (iUserService.updataUser(user)) {
                //更新Redis中的数据  对user1和user进行对比
                if(!user.getSex().equals(olduser.getSex()) && user.getSex() != null)
                    olduser.setSex(user.getSex());
                if(!user.getUsername().equals(olduser.getUsername()) && user.getUsername() != null)
                    olduser.setUsername(user.getUsername());
                if(!user.getSignature().equals(olduser.getSignature()) && user.getSignature() != null)
                    olduser.setSignature(user.getSignature());
                //刷新token对应的user数据
                redisUtils.set(request.getHeader("token"),olduser);
                return new ResponseBean(200, "ok", olduser);
            } else {
                return new ResponseBean(500, "未知错误", null); //错误
            }
        }else{
            return new ResponseBean(401,"未登录",null); //未登录
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
    @ApiOperation("查看自己关注的用户")
    public ResponseBean queryFollower(HttpServletRequest request){
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null){
            return new ResponseBean(400,"fail,未登录",null);
        }
        return new ResponseBean(200,"success",iUserService.queryFollower(user.getUserId(),0L,-1L));
    }
}
