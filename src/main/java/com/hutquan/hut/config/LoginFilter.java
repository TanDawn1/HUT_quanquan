package com.hutquan.hut.config;

import com.alibaba.fastjson.JSONObject;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 负责刷新 Redis中的token时间
 * Filter是单例的
 * 过滤器会在服务器开启时启动 关闭的时销毁
 */
@Component
public class LoginFilter implements Filter {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        int f = 1;
        //httpsr 是 sr的子类，是更适用于http请求的方法
        //里面有转为http设计的接口 getHeard、getSession、getMethod
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = httpServletRequest.getHeader("token");
        token = token == null ? "" : token;
        //为了防止token被获取 从而非法请求我们的服务器
        if(!token.equals("")){
            //System.out.println("进行token用户的校验");
            User user = (User)redisUtils.get(token);
            if(user != null) {
                if(!token.equals(redisUtils.hget("userToken",user.getUserId().toString()))){
                    //token 不相同
                    String str = JSONObject.toJSONString(new ResponseBean(403,"非法的请求",null));
                    servletResponse.setContentType("json/text;charset=utf-8");
                    PrintWriter out = servletResponse.getWriter();
                    //响应数据
                    out.write(str);
                    f = 0;
                }else{
                    //获取时间
                    long expire = redisUtils.getExpire(token);
                    //User user = (User) redisUtils.get(httpServletRequest.getHeader("token"));
                    if(expire > 0 && expire <= 10 * 24 * 60 * 60 ){ //小于10天才会去刷新
                        //每次请求都要过滤器，所以在过滤器中刷新token的时间 刷新为30天
                        redisUtils.expire(token,30L);
                    }
                }
            }
        }

        //无论带没带token都放行
        if(f == 1) filterChain.doFilter(servletRequest,servletResponse);
    }

}
