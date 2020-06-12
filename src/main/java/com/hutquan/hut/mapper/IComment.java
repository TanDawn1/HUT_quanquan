package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.Dynamic;
import org.springframework.stereotype.Repository;

@Repository
public interface IComment {

    int insertDynamicComment(String message, int dynamicId,int userId);

}
