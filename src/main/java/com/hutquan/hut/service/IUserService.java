package com.hutquan.hut.service;

import com.hutquan.hut.pojo.Follower;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.pojo.Xh;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {

    User selectUser(int userId);

    int insertUser(User user);

    User login(User user);

    Boolean updataUser(User user);

    String updataHeadPhoto(User user, MultipartFile file);

    List<Follower> queryFollower(Integer userId, Long l, Long l1);

    Long querySelfFollow(User user);

    Long querySelfFollowed(User user);

    Boolean followed(Integer user1Id,Integer user2Id);

}
