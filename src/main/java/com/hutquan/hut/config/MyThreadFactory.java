package com.hutquan.hut.config;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadFactory implements ThreadFactory {

    //private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public MyThreadFactory() {
        //group =
        namePrefix = "active-pool-thread-";
    }


    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable,namePrefix + threadNumber.getAndIncrement());
        if(thread.isDaemon()){
            //守护线程设置false
            thread.setDaemon(false);
        }
        if(thread.getPriority() != Thread.NORM_PRIORITY){
            //设置优先级
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }

}
