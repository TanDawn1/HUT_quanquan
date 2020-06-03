package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserMapper {

    User selectUser(int userId);

    int insertUser(User user);

    Integer selectTele(String tele,String account);

    User login(User user);

}
