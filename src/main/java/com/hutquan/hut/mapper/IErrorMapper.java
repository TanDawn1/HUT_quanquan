package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.FeedBack;
import org.springframework.stereotype.Repository;

@Repository
public interface IErrorMapper {

    int feedBack(FeedBack feedBack);

}
