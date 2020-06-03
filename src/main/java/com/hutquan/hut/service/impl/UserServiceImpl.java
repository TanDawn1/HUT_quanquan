package com.hutquan.hut.service.impl;

import com.hutquan.hut.mapper.IUserMapper;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper iUserMapper;

    @Override
    public User selectUser(int userId) {
        return iUserMapper.selectUser(userId);
    }

    @Override
    public int insertUser(User user) {
        //存在相同手机号或者相同账号就return 0
        if(iUserMapper.selectTele(user.getTele(),user.getAccount()) != null){
            return 0;
        }else{
            return iUserMapper.insertUser(user);
        }
    }

    @Override
    public User login(User user) {

        return iUserMapper.login(user);

    }
}
