package com.hutquan.hut.service;

import com.hutquan.hut.pojo.User;

public interface IUserService {

    User selectUser(int userId);

    int insertUser(User user);

    User login(User user);

}
