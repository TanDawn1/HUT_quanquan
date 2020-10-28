package com.hutquan.hut.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Aspect
@Component
public class TimeTest {

    @Around("execution(* com.hutquan.hut.controller.*.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp){
        //增强
        Object object = null;
        long start = Instant.now().getEpochSecond();
        try {
            object = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println(pjp.getTarget()+"耗时:"+(Instant.now().getEpochSecond() - start));
        return object;
    }

}
