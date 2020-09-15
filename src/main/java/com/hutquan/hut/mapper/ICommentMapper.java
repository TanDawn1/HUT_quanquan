package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentMapper {

    int insertDynamicComment(Comment comment);

}
