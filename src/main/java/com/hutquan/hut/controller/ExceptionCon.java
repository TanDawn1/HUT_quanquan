package com.hutquan.hut.controller;

import com.hutquan.hut.vo.ResponseBean;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理
 */
@ControllerAdvice
public class ExceptionCon implements ErrorController {

    @ExceptionHandler(Exception.class)
    public ResponseBean handleException(Exception e){
        return new ResponseBean(500,"错误的请求",null);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
