package com.hutquan.hut.service.impl;

import com.hutquan.hut.mapper.ISearchByFaceMapper;
import com.hutquan.hut.pojo.UserSearchFace;
import com.hutquan.hut.service.ISearchByFaceService;
import com.hutquan.hut.utils.MyMiniUtils;
import com.hutquan.hut.utils.UserFaceUtils;
import com.tencentcloudapi.iai.v20180301.models.Candidate;
import com.tencentcloudapi.iai.v20180301.models.Result;
import com.tencentcloudapi.iai.v20180301.models.SearchFacesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class SearchByFaceServiceImpl implements ISearchByFaceService {

    @Autowired
    private ISearchByFaceMapper iSearchByFaceMapper;

    @Autowired
    private UserFaceUtils userFaceUtils;

    /**
     * 人脸匹配接口
     * @param face
     * @return
     */
    @Override
    public List<UserSearchFace> searchFaces(MultipartFile face) {
        if (face.isEmpty()) {
            return null;
        }
        String fileBase64 = null;
        try {
            //腾讯接口需要 Base64编码 将MultipatrFile转为Base64编码
            fileBase64 = MyMiniUtils.multipartFileToBase64(face);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //腾讯提供的SDK
        SearchFacesResponse resp = null;
        try {
            //获取人员库ID,腾讯充钱解锁十万个人员库
            List<Integer> groups = iSearchByFaceMapper.selectGroupId();
            //调用腾讯接口
            resp = userFaceUtils.SearchFaces(groups, fileBase64);
            //results的大小为1
            Result[] results = resp.getResults();
            Candidate[] candidates = results[0].getCandidates();
            List<Integer> userId = new ArrayList<>();
            //长度最大为5
            int len = candidates.length;
            for(int i = 0; i < candidates.length; i++){
                //将userId添加进list 方便查找用户数据
                userId.add(Integer.valueOf(candidates[i].getPersonId()));
            }
            //获取用户数据 都是按user_id从小到大排序的
            //我们需要的是 按照score的匹配值从大到小进行排序 所以需要重新排序
            ArrayList<UserSearchFace> userSearchFaces =  iSearchByFaceMapper.selectUserById(userId);
            //由于获取出来的值是按主键顺序排列的，所以需要重新去排序判断赋值
            //通过candidates中的userId和score来定位比较,candidates中的位置即list中的排序位置
            //new 一个新的list 用于存储处理好顺序的值
            List<UserSearchFace> userSearchFacesCom = new ArrayList<>();
            for(int i = 0; i < len; i++){
//                //获取personId
                //二分查找 + 辅助list 用空间换取时间效率 O(log n) 空间O(n)
                binarySearch(userSearchFaces,userSearchFacesCom,candidates[i]);
            }
            return userSearchFacesCom;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object searchFaces1(MultipartFile face) {
        if (face.isEmpty()) {
            return null;
        }
        String fileBase64 = null;
        try {
            //腾讯接口需要 Base64编码 将MultipatrFile转为Base64编码
            fileBase64 = MyMiniUtils.multipartFileToBase64(face);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //腾讯提供的SDK
        SearchFacesResponse resp = null;
        try {
            //获取人员库ID,腾讯充钱解锁十万个人员库
            List<Integer> groups = iSearchByFaceMapper.selectGroupId();
            //调用腾讯接口
            resp = userFaceUtils.SearchFaces(groups, fileBase64);
            return resp;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param list 原 list
     * @param list1 新 list
     * @param candidate 数据
     * @param i 在candidates中的位置
     * @return
     */
    public void binarySearch(List<UserSearchFace> list,
                                             List<UserSearchFace> list1,
                                             Candidate candidate){
        Integer low = 0;
        Integer high = list.size() - 1;
        Integer mid = 0;
        while(low <= high){
            mid = (low + high)/2;
            Integer val= list.get(mid).getUserId();
            if(val == Integer.valueOf(candidate.getPersonId())){
                //获取原有属性
                UserSearchFace userSearchFace = list.get(mid);
                userSearchFace.setScore(candidate.getScore());
                //存储入新列表
                list1.add(userSearchFace);
                break;
            }
            else if( val < Integer.valueOf(candidate.getPersonId())){
                low = mid + 1;
            }
            else if(val > Integer.valueOf(candidate.getPersonId()) ){
                high = mid - 1;
            }
        }
    }
}
