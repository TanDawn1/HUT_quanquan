package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.ISearchByFaceService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 人脸搜索相关接口
 */
@RestController
public class SearchByFaceController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ISearchByFaceService iSearchByFaceService;

    @PostMapping("/face/search")
    @ApiOperation("根据人脸匹配")
    public ResponseBean searchByFace(HttpServletRequest request, MultipartFile photo){
        User user = (User) redisUtils.get(request.getHeader("token"));
        //不登录不允许返回数据
        if(user == null) return new ResponseBean(200,"ok",null);

        return new ResponseBean(200,"ok",iSearchByFaceService.searchFaces(photo));
    }

    @PostMapping("/face/create")
    @ApiOperation("上传人脸数据")
    public ResponseBean createFace(HttpServletRequest request,MultipartFile[] photo){
        User user = (User) redisUtils.get(request.getHeader("token"));
        //不登录不允许上传
        if(user == null) return new ResponseBean(200,"ok",null);
        return new ResponseBean(200,"ok",iSearchByFaceService.createFace(user,photo));
    }

    @GetMapping("/face/viewface")
    @ApiOperation("查看自己的人脸数据")
    public ResponseBean viewSelfFace(HttpServletRequest request){
        User user = (User)redisUtils.get(request.getHeader("token"));
        if(user == null) return new ResponseBean(403,"未登录",null);
        return new ResponseBean(200,"ok",iSearchByFaceService.viewSelfFace(user));
    }

}
