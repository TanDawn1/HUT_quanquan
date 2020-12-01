package com.hutquan.hut.controller;

import com.github.pagehelper.PageInfo;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.pojo.Xh;
import com.hutquan.hut.service.IUserService;
import com.hutquan.hut.service.IWithFriendsService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.PageBean;
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

    @Autowired
    private IWithFriendsService iWithFriendsService;

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
            Long selfFollow = iUserService.querySelfFollowed(user);
            Long selfFollowed = iUserService.querySelfFollow(user);
            if(selfFollow == null) selfFollow = 0L;
            if(selfFollowed == null) selfFollowed = 0L;
            //该用户关注了多少人
            user.setFollowCount(Double.valueOf(selfFollowed));
            //有多少人关注了该用户
            user.setSelfFollowCount(Double.valueOf(selfFollow));
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
        User othUser = null;
        if(user == null) return new ResponseBean(401,"未登录,无权限",null);
        try {
            //TODO 可优化
            othUser = iUserService.selectUser(otherUserId);
            if(othUser == null) return new ResponseBean(400,"无此用户",null);
            //该用户关注了多少人
            othUser.setFollowCount(Double.valueOf(iUserService.querySelfFollowed(othUser)));
            //有多少人关注了该用户
            othUser.setSelfFollowCount(Double.valueOf(iUserService.querySelfFollow(othUser)));
            //是否关注了别人 判断user是否关注otherUser
            othUser.setFollowed(iUserService.followed(user.getUserId(),otherUserId));
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseBean(500,"未知错误",user);
        }
        return new ResponseBean(200,"ok",othUser);
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
            user.setUserId(olduser.getUserId());
            if (iUserService.updataUser(user)) {
                //更新Redis中的数据  对user1和user进行对比
                if(user.getSex() != null && !user.getSex().equals(olduser.getSex()))
                    olduser.setSex(user.getSex());
                if(user.getUsername() != null && !user.getUsername().equals(olduser.getUsername()))
                    olduser.setUsername(user.getUsername());
                if(user.getSignature() != null && !user.getSignature().equals(olduser.getSignature()))
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
                //更新token对应的数据
                user1.setAvatarPicture(url);
                redisUtils.set(request.getHeader("token"),user1);
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

    @GetMapping("/user/followered/{pageNum}")
    @ApiOperation("查看哪些用户关注了自己")
    public ResponseBean queryFollowered(HttpServletRequest request,@PathVariable("pageNum") Long pageNum){
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null || pageNum == null){
            return new ResponseBean(400,"fail",null);
        }
        return new ResponseBean(200,"success",iUserService.queryFollowered(user.getUserId(),pageNum));
    }

    /**
     * 查询自己的动态
     * @param request
     * @return
     */
    @GetMapping("/user/selfdynamic/{pageNum}")
    @ApiOperation("自己的动态,一页最多20条")
    public ResponseBean selfDynamic(HttpServletRequest request,@PathVariable("pageNum") int pageNum){
        try{
            User user = (User) redisUtils.get(request.getHeader("token"));
            if(user == null || user.getUserId() == null) return new ResponseBean(400,"未登录",null);

            PageBean<Dynamic> dynamicPageInfo = iWithFriendsService.dynamicsBySelf(pageNum,20,user);

            return new ResponseBean(200, "ok", dynamicPageInfo);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseBean(500,"未知错误",null);
        }
    }
}
