package com.hutquan.hut.utils;

import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20180301.IaiClient;
import com.tencentcloudapi.iai.v20180301.models.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RedisUtils redisUtils;

    @Value("${tencent.secret.id}")
    private String secretId;

    @Value("${tencent.secret.key}")
    private String sercretKey;

    //TODO 受限于腾讯的API接口这里需要客户端保证：存在一张人脸数据，只能删除之后在重新增加
    //创建人员 添加人脸
    public CreatePersonResponse CreateFace(String uId, String realName, Integer sex,String groupId, String imgBase64) {
        try {
            //密钥和密匙
            Credential cred = new Credential(secretId, sercretKey);
            //调用的接口
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            IaiClient client = new IaiClient(cred, "", clientProfile);
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put("GroupId", groupId);
            inputMap.put("PersonName", realName);
            inputMap.put("PersonId", uId);
            inputMap.put("Gender",sex);
            inputMap.put("Image", imgBase64);
            System.out.println("map:" + JSON.toJSONString(inputMap));
            String params = JSON.toJSONString(inputMap);
            CreatePersonRequest req = CreatePersonRequest.fromJsonString(params, CreatePersonRequest.class);

            CreatePersonResponse resp = client.CreatePerson(req);
            //System.out.println(CreatePersonRequest.toJsonString(resp));
            return resp;
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    //删除人员数据
    public DeleteFaceResponse DeletePerson(Integer uId, String faceIdList) {
        try {
            Credential cred = new Credential(secretId, sercretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("iai.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            IaiClient client = new IaiClient(cred,"", clientProfile);
            Map map = new HashMap();
            map.put("PersonId", uId);

            String params = JSON.toJSONString(map);
            DeleteFaceRequest req = DeleteFaceRequest.fromJsonString(params, DeleteFaceRequest.class);

            DeleteFaceResponse resp = client.DeleteFace(req);

            return resp;
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return null;
        }
    }

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
