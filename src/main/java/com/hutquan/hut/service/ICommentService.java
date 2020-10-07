package com.hutquan.hut.service;

import com.hutquan.hut.pojo.Comment;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.vo.PageBean;

public interface ICommentService {

    int insertDynamicComment(User user, Comment comment);

    PageBean<Comment> queryCommentDynamic(int pageNum,int dynamicId);
}
