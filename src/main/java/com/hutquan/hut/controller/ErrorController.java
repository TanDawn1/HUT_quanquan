package com.hutquan.hut.controller;

import com.hutquan.hut.vo.ResponseBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 错误页面
 */
@RestController
public class ErrorController {

    @GetMapping("/error/errorauth")
    public ResponseBean errorAuth(){
        return new ResponseBean(403,"无权限",null);
    }
}
