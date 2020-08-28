package com.hutquan.hut.utils;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    // flag: 0->headPhoto 1->dynamicPhoto 2->feedBack 3->userFace
    public static String fileUpload(int id, MultipartFile[] multipartFile,int flag){
        String result = "";
        String pathSuffix = "";
        if(flag == 0) pathSuffix = "headPhoto/";
        if(flag == 1) pathSuffix = "dynamicPhoto/";
        if(flag == 2) pathSuffix = "feedBack/";
        if(flag == 3) pathSuffix = "userFace/";
        List<String> list = new ArrayList<>();
        long time = System.currentTimeMillis();
        int i = 0;
        for(MultipartFile file: multipartFile){
            String name = time+"-"+id+"-"+i;
            String suffix = getSuffix(file.getOriginalFilename());
            String imageName = name + suffix;

            InputStream input = null;
            try {
                input = file.getInputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = FtpUtil.putImages(input, pathSuffix + imageName);
            if(result == ""){
                logger.info("文件上传失败！");
                return null;
            }
            list.add(imageName);
            i++;
        }
        //把图片列表转化为JSON格式
        return JSON.toJSONString(list);
    }

    /**
     * 文件名处理方法
     */
    public static String getSuffix(String originalname){
        int su = originalname.lastIndexOf(".");
        String suffix  = originalname.substring(su);
        return suffix;
    }

}
