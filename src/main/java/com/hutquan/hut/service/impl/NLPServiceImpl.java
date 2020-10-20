package com.hutquan.hut.service.impl;

import com.hutquan.hut.pojo.User;
import com.hutquan.hut.service.INLPService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.nlp.v20190408.NlpClient;
import com.tencentcloudapi.nlp.v20190408.models.*;;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class NLPServiceImpl implements INLPService {

    private Logger logger = LoggerFactory.getLogger(NLPServiceImpl.class);


    @Override
    public String NLPAnalysis(String str, User user) {

        try {
            Credential cred = new Credential("AKIDSjT0wfIRRE2fKnEvyf9Yhh3A8qHUqMab", "u63j9LI3YnlW3JJ9i83i6E4KI0Unpa6R");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("nlp.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            NlpClient client = new NlpClient(cred, "ap-guangzhou", clientProfile);

            SentimentAnalysisRequest req = new SentimentAnalysisRequest();
            req.setFlag(4L);
            req.setText(str);

            SentimentAnalysisResponse resp = client.SentimentAnalysis(req);
            return  SentimentAnalysisResponse.toJsonString(resp);

        } catch (TencentCloudSDKException e) {
            logger.info(e.toString());
        }
        return null;
    }

}
