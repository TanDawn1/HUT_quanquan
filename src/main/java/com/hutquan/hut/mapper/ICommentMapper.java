package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentMapper {

    int insertDynamicComment(Comment comment);

    List<Comment> queryCommentDynamic(int dynamicId);
}
