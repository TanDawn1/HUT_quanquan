package com.hutquan.hut.service.impl;

import com.hutquan.hut.mapper.IErrorMapper;
import com.hutquan.hut.pojo.FeedBack;
import com.hutquan.hut.service.IErrorService;
import com.hutquan.hut.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Service
public class ErrorServiceImpl implements IErrorService {


    private Logger logger = LoggerFactory.getLogger(ErrorServiceImpl.class);

    @Autowired
    private IErrorMapper iErrorMapper;

    @Override
    public boolean feedBack(FeedBack feedBack, MultipartFile[] feedphotos) {
        if(feedBack.getTId() < 0) return false;
        //String newFileName = null;
        String FPhotos = null;
        //上传了图片
        try {
            if (feedphotos != null) {
                //上传图片
                FPhotos = FileUtil.fileUpload(feedBack.getUserId(), feedphotos, 4);

                if (FPhotos == null || FPhotos.equals("")) {
                    logger.info("图片上传失败");
                    return false;
                }
            }
            feedBack.setImages(FPhotos);
            feedBack.setTime(Instant.now().getEpochSecond());
            return iErrorMapper.feedBack(feedBack) > 0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
