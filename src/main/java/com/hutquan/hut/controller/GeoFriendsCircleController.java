package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IGeoFriendsCircleService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 带有位置的相关动态
 */
@RestController
public class GeoFriendsCircleController {


    private Logger logger = LoggerFactory.getLogger(GeoFriendsCircleController.class);

    @Autowired
    private IGeoFriendsCircleService iGeoFriendsCircleService;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/geo/nearby/{pageNum}")
    @ApiOperation("附近的人的动态 100KM,一页最多显示20个")
    public ResponseBean nearby(HttpServletRequest request, @PathVariable("pageNum") int pageNum){
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null){
            //未登录则不获取
            return new ResponseBean(401,"未登录",null);
        }
        return new ResponseBean(200,"ok",iGeoFriendsCircleService.nearybyDynamic(user,pageNum,20));
    }

    @PostMapping("/geo/upGpsData")
    @ApiOperation("上传位置数据，经纬度 x经度 y纬度")
    public ResponseBean updateGpsData(HttpServletRequest request, @RequestBody Point point){
        logger.info(point.toString());
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null) return new ResponseBean(401,"未登录",null);
        return new ResponseBean(200,"ok",iGeoFriendsCircleService.updateGpsData(user,point));
    }

}
