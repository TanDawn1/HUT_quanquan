package com.hutquan.hut.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hutquan.hut.mapper.ISearchMapper;
import com.hutquan.hut.pojo.Dynamic;
import com.hutquan.hut.pojo.Search;
import com.hutquan.hut.service.ISearchService;
import com.hutquan.hut.vo.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private ISearchMapper iSearchMapper;

    @Override
    public PageBean<Search> searchs(String message, int pageNum, int pageSize) {

        if(message.equals("")) return null;
        //敏感词限制
        PageHelper.startPage(pageNum,pageSize);
        List<Search> list = iSearchMapper.searchs(message);
        return new PageBean<>(list);
    }

    @Override
    public PageBean<Dynamic> dynamics(String message,int pageNum,int pageSize) {
        if(message.equals("")) return null;
        PageHelper.startPage(pageNum,pageSize);
        List<Dynamic> list = iSearchMapper.dynamics(message);
        return new PageBean<>(list);
    }

}
