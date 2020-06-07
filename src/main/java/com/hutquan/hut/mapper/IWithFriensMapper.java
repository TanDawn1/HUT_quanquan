package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.Dynamic;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface IWithFriensMapper {

    List<Dynamic>  dynamicsByLike();

}
