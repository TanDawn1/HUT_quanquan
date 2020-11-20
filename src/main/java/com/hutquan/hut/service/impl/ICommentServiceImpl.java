package com.hutquan.hut.service.impl;

import com.github.pagehelper.PageHelper;
import com.hutquan.hut.mapper.ICommentMapper;
import com.hutquan.hut.pojo.Comment;
import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.ICommentService;
import com.hutquan.hut.utils.RedisUtils;
import com.hutquan.hut.vo.PageBean;
import com.hutquan.hut.vo.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ICommentServiceImpl implements ICommentService {

    private static final String DYNAMICCOMMENT = "dynamic_comment";

    @Autowired
    private ICommentMapper iCommentMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public int insertDynamicComment(User user, Comment comment) {
        try {
            comment.setUserId(user.getUserId());
            comment.setTime(Instant.now().getEpochSecond());
            //写入数据库
            iCommentMapper.insertDynamicComment(comment);
            //更新Redis
            redisUtils.hincr(DYNAMICCOMMENT,"d" + comment.getDynamicId().toString(),1);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public PageBean<Comment> queryCommentDynamic(int pageNum,int dynamicId) {

        try {
            PageHelper.startPage(pageNum,20);
            return new PageBean<>(iCommentMapper.queryCommentDynamic(dynamicId));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
