package com.hutquan.hut.service;

import com.github.pagehelper.PageInfo;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.User;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;

import java.util.List;

public interface IGeoFriendsCircleService {

    PageInfo<Dynamic> nearybyDynamic(User user, int pageNum, int pageSize);

    Boolean updateGpsData(User user, Point point);

}
