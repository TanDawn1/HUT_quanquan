package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.Search;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILostAndFoundMapper {
    //查询所有失物
    List<Search> selectAllLost();
    //查询所有招领
    List<Search> selectAllFound();

    int putLost(Search search);

    int putFound(Search search);
}
