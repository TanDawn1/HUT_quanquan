package com.hutquan.hut.utils;

import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20180301.IaiClient;
import com.tencentcloudapi.iai.v20180301.models.SearchFacesRequest;
import com.tencentcloudapi.iai.v20180301.models.SearchFacesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人脸相关方法
 */
@Component
public class UserFaceUtils {

    @Value("${tencent.secret.id}")
    private String secretId;

    @Value("${tencent.secret.key}")
    private String sercretKey;

//    @Value("${tencent.service.region}")
//    private String region;

    //人脸搜索
    public SearchFacesResponse SearchFaces(List<Integer> groups, String fileBase64) {
        try {
            Credential cred = new Credential(secretId, sercretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            IaiClient client = new IaiClient(cred, "", clientProfile);
            Map<String,Object> map = new HashMap<>();
            //人脸库Id
            map.put("GroupIds", groups);
            //图片Base64
            map.put("Image", fileBase64);
            //返回相似度最高的多少人
            map.put("MaxPersonNum", 5);
            String params = JSON.toJSONString(map);
            //用官方提供的请求类
            SearchFacesRequest req = SearchFacesRequest.fromJsonString(params, SearchFacesRequest.class);

            return client.SearchFaces(req);
        } catch (TencentCloudSDKException e) {
            //出错
            e.printStackTrace();
            return null;
        }
    }
}
