package com.hutquan.hut.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class TimeTest {

    @Around("execution(* com.hutquan.hut.controller.WithFriendsHomeController.releaseDynamic())")
    public Object handleControllerMethod(ProceedingJoinPoint pjp){
        //增强
        Object object = null;
        System.out.println("time aspect start");
        long start = new Date().getTime();
        try {
            object = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("耗时:"+(new Date().getTime() - start));
        System.out.println("time aspect end");
        return object;
    }

}
