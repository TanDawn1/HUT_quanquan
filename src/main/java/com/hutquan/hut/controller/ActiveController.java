package com.hutquan.hut.controller;

import com.hutquan.hut.pojo.CaptchaVO;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IActiveService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ActiveController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private IActiveService iActiveService;

    private static final String ACTIVESUM = "active:";

    private static final String CAPTCHA = "CAPTCHA:";

    private static final String ACTIVEBLACK = "ACTIVEBLACK:";

    //private static final String BLOCK = "active:";

    private static final Long TIME = 24 * 60 * 60L;

    @GetMapping("/active/getdata/{pageNum}")
    @ApiOperation("获取最新的活动 一次20条")
    public ResponseBean getData(HttpServletRequest request, @PathVariable("pageNum")Integer pageNum){
        //未登录用户禁止查看
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null) return new ResponseBean(401,"未登录",null);
        if(pageNum == null) return new ResponseBean(400,"fail",null);
        return new ResponseBean(200,"ok",iActiveService.actives(pageNum));
    }

    @GetMapping("/active/getDetailed/{activeId}")
    @ApiOperation("活动详情")
    public ResponseBean getDetailed(HttpServletRequest request, @PathVariable("activeId") Integer activeId){
        //未登录用户禁止访问详情页
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null) return new ResponseBean(401,"未登录",null);

//        if(redisUtils.get(ACTIVESUM + user.getUserId()) == null){
//            redisUtils.set(ACTIVESUM + user.getUserId(),1, TIME);
//        }else{
//            //如果有编码，先判断
//            if(captchaVO != null){
//                if(captchaVO.getCode().equals(redisUtils.get(CAPTCHA.concat(captchaVO.getCaptchaKey())))){
//                    return new ResponseBean(200,"ok",iActiveService.activeDetailed(activeId));
//                }
//            }
//            //判断用户访问次数，10分钟内达到10次则要求提交验证码才能访问活动详情
//            if(redisUtils.incr(ACTIVESUM + user.getUserId(),1) > 10 && captchaVO == null){
//                return new ResponseBean(401,"验证码",iActiveService.getCaptchaVO());
//            }
//        }
        //获取详细信息
        return new ResponseBean(200,"ok",iActiveService.activeDetailed(activeId));
    }

    @PostMapping("/active/signActive/{activeId}")
    @ApiOperation("抢占名额,通过技术手段多次调用该接口的封禁到活动开始")
    public ResponseBean signActive(HttpServletRequest request, @PathVariable("activeId")Integer avtiveId){
        //判断用户
        User user = (User) redisUtils.get(request.getHeader("token"));
        if(user == null) return new ResponseBean(401,"未登录",null);
        //判断是否重复报名
        if(redisUtils.sHasKey("active:"+ avtiveId,user.getUserId())){
            //如果存在，提示禁止重复报名
            return new ResponseBean(401,"重复报名！", null);
        }
//        //多次调用该接口去尝试抢占名额的，直接封禁，24h之后解禁
//        if (redisUtils.get(ACTIVESUM + user.getUserId()) == null) {
//            redisUtils.set(ACTIVESUM + user.getUserId(), 1, TIME);
//        } else {
//            //判断用户访问次数，超过1次直接封禁
//            return new ResponseBean(401,"请等待24h",null);
//        }

        return new ResponseBean(200, "ok", iActiveService.signActive(user,avtiveId));
    }



}
