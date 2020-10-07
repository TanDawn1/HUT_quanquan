package com.hutquan.hut.mapper;

import com.hutquan.hut.pojo.Dynamic;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * 校园交友模块
 */
@Repository
public interface IWithFriensMapper {

    List<Dynamic>  dynamicsByLike(String idList);

    List<Dynamic>  dynamicsByTime();

    List<Dynamic> condynamic(List<Object> idList);

    int addDynamic(Dynamic dynamic);

    List<Dynamic> dynamicsByHot(Set<Object> idSet);

    List<Dynamic> dynamicsBySelf(Integer userId);

    int delDynamic(int dynamicId);
}
