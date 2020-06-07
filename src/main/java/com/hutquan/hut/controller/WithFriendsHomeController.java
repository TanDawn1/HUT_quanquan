package com.hutquan.hut.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IWithFriendsService;
import com.hutquan.hut.vo.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * 按热度查询动态排序
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/withfriend/dynamic")
    public ResponseBean Dynamic(@RequestBody int pageNum,@RequestBody int pageSize){
        try {
            //分页处理
//            PageHelper.startPage(pageNum, pageSize);
            PageInfo<Dynamic> dynamicPageInfo = iWithFriendsService.dynamicsByLike(pageNum,pageSize);
            
            return new ResponseBean(200,"ok",dynamicPageInfo);

        } catch (Exception e){
            e.printStackTrace();
            return new ResponseBean(400,"ok",null);
        }
    }

    @GetMapping("/withfriend/condynamic")
    public ResponseBean ConDynamic(@RequestParam int pageNum,@RequestParam int pageSize, HttpServletRequest request){
        return null;
    }


    /**
     * 添加关注
     * @param concernUserId
     * @param request
     * @return
     */
    @GetMapping("/withfriend/addconcern")
    public ResponseBean Concern(@RequestBody int concernUserId, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(iWithFriendsService.addConcern(user,concernUserId)){
            return new ResponseBean(200,"ok",null);
        }else {
            return new ResponseBean(-1,"fail",null);
        }
    }
}
