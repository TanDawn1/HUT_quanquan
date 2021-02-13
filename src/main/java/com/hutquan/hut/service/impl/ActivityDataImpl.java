package com.hutquan.hut.service.impl;

import com.hutquan.hut.mapper.ISearchMapper;
import com.hutquan.hut.pojo.Active;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IActivityDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ActivityDataImpl implements IActivityDataService {

    @Autowired
    private ISearchMapper iSearchMapper;

    private static final Logger logger = LoggerFactory.getLogger(ActivityDataImpl.class);

    @Override
    @Transactional
    public boolean saveData(Active active, User user) {
        //扣减库存
        //其实可以在数据库做一次判断，但是数据库不能够承载
        //事务处理，在出现异常时回滚
        try {
            if (iSearchMapper.insert(active.getId(), user.getUserId(), Instant.now().getEpochSecond()) >= 1 && iSearchMapper.deductionData(active.getId()) >= 1) {
                logger.info(user.getUsername() + ":处理完成");
                return true;
            }
        }catch (Exception e){
            logger.info(user.getUsername() + ":处理异常");
            throw new RuntimeException();
        }
        return false;
    }

}
