package com.hutquan.hut.service;

import com.hutquan.hut.pojo.User;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {

    User selectUser(int userId);

    int insertUser(User user);

    User login(User user);

    Boolean updataUser(User user);

    String updataHeadPhoto(User user, MultipartFile file);

}
