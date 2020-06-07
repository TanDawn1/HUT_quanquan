package com.hutquan.hut.service;

import com.github.pagehelper.PageInfo;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;

import java.util.List;

public interface IWithFriendsService {

    PageInfo<Dynamic> dynamicsByLike(int pageNum, int pageSize);

    boolean addConcern(User user,int concernUserId);

    boolean remConcern(User user,int concernUserId);
}
