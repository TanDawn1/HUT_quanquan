package com.hutquan.hut.service.impl;

import com.hutquan.hut.mapper.ISearchMapper;
import com.hutquan.hut.pojo.Active;
import com.hutquan.hut.pojo.ActiveRecords;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IActivityDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Scope("prototype")
public class DataSaveThreadImpl implements Runnable{

    @Autowired
    private IActivityDataService iActivityDataService;

    Active active;

    User user;

    private static final Logger logger = LoggerFactory.getLogger(DataSaveThreadImpl.class);

    public DataSaveThreadImpl(){
    }

    public DataSaveThreadImpl(Active active, User user){
        this.active = active;
        this.user = user;
    }

    @Override
    public void run() {
        //插入数据库
        logger.info(Thread.currentThread().getName() + "执行处理:" + user.getUserId());
        if(iActivityDataService.saveData(active, user)){
            logger.info(Thread.currentThread().getName() + "执行成功:" + user.getUserId());
        }else{
            logger.info(Thread.currentThread().getName() + "执行失败:" + user.getUserId());
        }
    }

}
