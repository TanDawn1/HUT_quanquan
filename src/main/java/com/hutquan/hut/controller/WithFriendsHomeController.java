package com.hutquan.hut.controller;

import com.github.pagehelper.PageInfo;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IWithFriendsService;
import com.hutquan.hut.vo.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 校园交友模块
 *
 */
@RestController
public class WithFriendsHomeController {

    @Autowired
    private IWithFriendsService iWithFriendsService;


    /**
     * 发布动态
     * @param dynamic
     * @return
     */
    @PostMapping("/withfriend/releasedc")
    public  ResponseBean releaseDynamic(Dynamic dynamic, @RequestParam(value = "file",required = false) List<MultipartFile> file, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");

        try {
            return new ResponseBean(200,"ok",iWithFriendsService.addDynamic(user,dynamic,file));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean(400, "ok", null);
        }

    }

    /**
     * 按热度查询动态排序
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/withfriend/dynamic")
    public ResponseBean Dynamic(@RequestParam int pageNum,@RequestParam int pageSize){
//        try {
//
//            PageInfo<Dynamic> dynamicPageInfo = iWithFriendsService.dynamicsByLike(pageNum,pageSize);
//
//            return new ResponseBean(200,"ok",dynamicPageInfo);
//
//        } catch (Exception e){
//            e.printStackTrace();
//            return new ResponseBean(400,"ok",null);
//        }
        return new ResponseBean(500,"fail",null);
    }

    /**
     * 按时间顺序
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/withfriend/dynamicbytime")
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
    public ResponseBean ConDynamic(@RequestParam int pageNum,@RequestParam int pageSize, HttpServletRequest request){
        try {
            PageInfo<Dynamic> dynamicPageInfo = iWithFriendsService.condynamic(pageNum, pageSize, request);
            if(dynamicPageInfo != null) {
                return new ResponseBean(200, "ok", dynamicPageInfo);
            }else {
                return new ResponseBean(300,"ok",null); //没有关注的人
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
    public ResponseBean Concern(@RequestParam int concernUserId, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
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
    public ResponseBean remConcern(@RequestParam int concernUserId, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
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
    @GetMapping("/withfriend/like")
    public ResponseBean likeDynamic(@RequestParam int dynamicId,HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        return new ResponseBean(200,"ok",
                iWithFriendsService.likeDynamic(user,dynamicId));
    }



}
