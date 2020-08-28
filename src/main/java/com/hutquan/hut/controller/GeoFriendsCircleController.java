package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IGeoFriendsCircleService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 带有位置的相关动态
 */
@RestController
public class GeoFriendsCircleController {

    @Autowired
    private IGeoFriendsCircleService iGeoFriendsCircleService;

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("/geo/nearby/{pageNum}/{pageSize}")
    @ApiOperation("附近的人的动态 100KM")
    public ResponseBean nearby(HttpServletRequest request, @PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize){
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null){
            //未登录则不获取
            return new ResponseBean(200,"ok",null);
        }
        return new ResponseBean(200,"ok",iGeoFriendsCircleService.nearybyDynamic(user,pageNum,pageSize));
    }

}
