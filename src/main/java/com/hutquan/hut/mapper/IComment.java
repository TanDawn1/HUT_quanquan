package com.hutquan.hut.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface IComment {

    int insertDynamicComment(String message, int dynamicId,int userId);

}
