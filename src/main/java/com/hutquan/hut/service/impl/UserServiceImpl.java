package com.hutquan.hut.service.impl;

import com.hutquan.hut.config.RedisLettuce;
import com.hutquan.hut.mapper.IUserMapper;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IUserService;
import com.hutquan.hut.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper iUserMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public User selectUser(int userId) {
        return iUserMapper.selectUser(userId);
    }

    @Override
    public int insertUser(User user) {
        user.setTime(LocalDateTime.now());
        //存在相同手机号或者相同账号就return 0
        if(iUserMapper.selectTele(user.getTele(),user.getAccount()) != null){
            return 0;
        }else{
            //返回主键的值
//            iUserMapper.insertUser(user);
//            //新建Redis数据库中的该用户的关注库并自动给官方账号添加关注
//            redisUtils.sSet("userConcernId:"+user.getUserId(),1);
            //这里直接返回1
            return iUserMapper.insertUser(user);
        }
    }

    @Override
    public User login(User user) {

        return iUserMapper.login(user);

    }
}
