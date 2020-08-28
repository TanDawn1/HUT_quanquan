package com.hutquan.hut.controller;

import com.github.pagehelper.PageInfo;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IWithFriendsService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * 发布查看动态信息模块
 *
 */
@RestController
public class WithFriendsHomeController {

    @Autowired
    private IWithFriendsService iWithFriendsService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 发布动态
     * @param dynamic
     * @return
     */
    @PostMapping("/withfriend/releasedc")
    @ApiOperation("发布动态")
    public  ResponseBean releaseDynamic(Dynamic dynamic, @RequestParam(value = "file",required = false) MultipartFile[] files, HttpServletRequest request){
        User user = (User) redisUtils.get(request.getHeader("token"));
        //User user = (User) request.getSession().getAttribute("user");
        try {
            return new ResponseBean(200,"ok",iWithFriendsService.addDynamic(user,dynamic,files));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean(400, "ok", null);
        }

    }

    /**
     * 按热度查询动态排序

     * @return
     */
    @GetMapping("/withfriend/dynamic")
    @ApiOperation("热点动态")
    public ResponseBean Dynamic(HttpServletRequest request){
        User user = (User) redisUtils.get(request.getHeader("token"));

        return new ResponseBean(200,"ok",iWithFriendsService.dynamicsByHot(user));

    }

    /**
     * 按时间顺序
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/withfriend/dynamicbytime")
    @ApiOperation("按照时间排序动态")
    public ResponseBean DynamicByTime(@RequestParam int pageNum, @RequestParam int pageSize,HttpServletRequest request){
        try{
            PageInfo<Dynamic> dynamicPageInfo = iWithFriendsService.dynamicsByTime(pageNum,pageSize,request);

            return new ResponseBean(200, "ok", dynamicPageInfo);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseBean(400,"ok",null);
        }
    }


    /**
     * 关注的人的动态
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/withfriend/condynamic")
    @ApiOperation("关注的人的动态")
    public ResponseBean ConDynamic(@RequestParam int pageNum,@RequestParam int pageSize, HttpServletRequest request){
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null){
            return new ResponseBean(403,"未登录",null);
        }
        try {
            PageInfo<Dynamic> dynamicPageInfo = iWithFriendsService.condynamic(pageNum, pageSize, user);
            if(dynamicPageInfo != null) {
                return new ResponseBean(200, "ok", dynamicPageInfo);
            }else {
                return new ResponseBean(200,"ok",null); //没有关注的人
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseBean(400,"ok",null);
        }
    }


    /**
     * 添加关注
     * @param concernUserId
     * @param request
     * @return
     */
    @GetMapping("/withfriend/addconcern")
    @ApiOperation("添加关注")
    public ResponseBean Concern(@RequestParam int concernUserId, HttpServletRequest request){
        //User user = (User) request.getSession().getAttribute("user");
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null){
            return new ResponseBean(403,"未登录",null);
        }
        if(iWithFriendsService.addConcern(user,concernUserId)){
            return new ResponseBean(200,"ok",null);
        }else {
            return new ResponseBean(-1,"fail",null);
        }
    }

    /**
     * 取消关注
     * @param concernUserId
     * @param request
     * @return
     */
    @GetMapping("/withfriend/remconcern")
    @ApiOperation("取消关注")
    public ResponseBean remConcern(@RequestParam int concernUserId, HttpServletRequest request){
        //User user = (User) request.getSession().getAttribute("user");
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null){
            return new ResponseBean(301,"未登录",null);
        }
        if(iWithFriendsService.remConcern(user,concernUserId)){
            return new ResponseBean(200,"ok",null);
        }else {
            return new ResponseBean(-1,"fail",null);
        }
    }

    /**
     * 添加喜欢
     * @param dynamicId
     * @param request
     * @return
     */
    @GetMapping("/withfriend/like/{dynamicId}")
    @ApiOperation("对动态添加喜欢")
    public ResponseBean likeDynamic(@PathVariable("dynamicId") int dynamicId,HttpServletRequest request){
        //User user = (User) request.getSession().getAttribute("user");
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null){
            return new ResponseBean(301,"未登录",null);
        }
        return new ResponseBean(200,"ok",
                iWithFriendsService.likeDynamic(user,dynamicId));
    }




}
