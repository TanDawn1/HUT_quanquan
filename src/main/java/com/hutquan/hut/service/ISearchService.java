package com.hutquan.hut.service;

import com.github.pagehelper.PageInfo;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.Search;
import com.hutquan.hut.vo.PageBean;


public interface ISearchService {

    PageBean<Search> searchs(String message, int pageNum, int pageSize);

    PageBean<Dynamic> dynamics(String message,int pageNum,int pageSize);

}
