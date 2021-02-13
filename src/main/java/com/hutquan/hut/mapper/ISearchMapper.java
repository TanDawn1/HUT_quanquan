package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.Active;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.Search;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISearchMapper {

    List<Search> searchs(@Param("message") String message);

    List<Dynamic> dynamics(@Param("message") String message);
    //暂时使用这块的接口
    List<Active> queryActive();

    Active queryActiveDetailed(int activeId);

    int insert(@Param("activityId")Integer activityId, @Param("userId")Integer userId, @Param("time")Long time);

    int deductionData(Integer activeId);
}
