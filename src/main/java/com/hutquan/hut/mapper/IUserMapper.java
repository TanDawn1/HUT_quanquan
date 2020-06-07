package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


/**
 * 登录与注册
 */
@Repository
public interface IUserMapper {

    User selectUser(int userId);

    Integer teleSelectUser(String tele);

    //返回的是匹配行数 1
    int insertUser(User user);

    Integer selectTele(String tele,String account);

    User login(User user);

    //验证码通过Redis判断了
    User  teleLogin(String tele);

    int insertTele(String tele, String yzm, LocalDateTime time);

}
