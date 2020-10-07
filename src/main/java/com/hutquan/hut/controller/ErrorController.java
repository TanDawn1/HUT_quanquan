package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.FeedBack;
import com.hutquan.hut.service.IErrorService;
import com.hutquan.hut.vo.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 错误页面
 */
@RestController
public class ErrorController {

    @Autowired
    private IErrorService iErrorService;

    @GetMapping("/error/errorauth")
    @ApiOperation("发送异常错误调用该接口")
    public ResponseBean errorAuth(){
        return new ResponseBean(403,"无权限",null);
    }

    @PostMapping("/error/feedbcak")
    @ApiOperation("反馈接口")
    public ResponseBean feedBack(FeedBack feedBack,MultipartFile[] feedphotos){
        return new ResponseBean(200,"ok",iErrorService.feedBack(feedBack,feedphotos));
    }
}
