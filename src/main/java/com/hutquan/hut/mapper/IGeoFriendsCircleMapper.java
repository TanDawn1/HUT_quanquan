package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.Dynamic;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGeoFriendsCircleMapper {


    List<Dynamic> nearybyDynamic(List<Integer> idList);
}
