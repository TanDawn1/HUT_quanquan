package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.UserSearchFace;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Repository
public interface ISearchByFaceMapper {

    List<Integer> selectGroupId();

    ArrayList<UserSearchFace> selectUserById(List<Integer> uId);
}
