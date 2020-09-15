package com.hutquan.hut.service;

import com.hutquan.hut.pojo.Comment;
import com.hutquan.hut.pojo.User;

public interface ICommentService {

    int insertDynamicComment(User user, Comment comment);

}
