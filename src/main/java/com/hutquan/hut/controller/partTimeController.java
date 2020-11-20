package com.hutquan.hut.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 兼职信息接口
 */
@RestController
public class partTimeController {

    @RequestMapping("/test")
    public String test(@RequestParam("test") String test,@RequestParam("ok") String ok){
        System.out.println(test);
        return test;
    }

    @GetMapping("/test1")
    public String test1(String test1){
        System.out.println(test1);
        return test1;
    }


}
