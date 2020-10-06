package com.hutquan.hut.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hutquan.hut.mapper.ILostAndFoundMapper;
import com.hutquan.hut.pojo.Search;
import com.hutquan.hut.service.ILostAndFoundService;
import com.hutquan.hut.utils.FileUtil;
import com.hutquan.hut.vo.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Service
public class LostAndFoundServiceImpl implements ILostAndFoundService {

    private Logger logger = LoggerFactory.getLogger(LostAndFoundServiceImpl.class);

    @Autowired
    private ILostAndFoundMapper iLostAndFoundMapper;

    /**
     * 1 查询招领
     * 0 查询失物
     * @param type
     * @return
     */
    @Override
    public PageBean<Search> selectAllLostAndFound(Integer type, Integer pageNum, Integer pageSize) {
        //使用PageHelper分页
        PageHelper.startPage(pageNum, pageSize);
        if(type == 1){
            List<Search> list = iLostAndFoundMapper.selectAllFound();
            return new PageBean<>(list);
        }else if(type == 0){
            List<Search> list = iLostAndFoundMapper.selectAllLost();
            return new PageBean<>(list);
        }
        return null;
    }

    /**
     * 1 发布招领信息
     * 0 发布失物信息
     * @param search
     * @return
     */
    @Override
    public boolean putLostOrFound(Search search, MultipartFile[] photos) {
        //String newFileName = null;
        String searchPhotos = null;
        //上传了图片
        if(photos != null){
            //上传图片
            searchPhotos = FileUtil.fileUpload(search.getUserId(),photos,4);

            if( searchPhotos == null || searchPhotos.equals("")){
                logger.info("图片上传失败");
                return false;
            }
        }
        search.setSPhoto(searchPhotos);
        search.setTime(Instant.now().getEpochSecond());
        if(search.getType() == 1){
            return iLostAndFoundMapper.putFound(search) > 0;
        }else if(search.getType() == 0){
            return iLostAndFoundMapper.putLost(search) > 0;
        }
        return false;
    }

}
