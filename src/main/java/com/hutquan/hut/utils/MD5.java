package com.hutquan.hut.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    public final static String getMD5(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");//创建具有指定算法名称的摘要
            md.update(str.getBytes());                    //使用指定的字节数组更新摘要
            byte mdBytes[] = md.digest();                 //进行哈希计算并返回一个字节数组

            String hash = "";
            for(int i= 0;i<mdBytes.length;i++){           //循环字节数组
                int temp;
                if(mdBytes[i]<0)                          //如果有小于0的字节,则转换为正数
                    temp =256+mdBytes[i];
                else
                    temp=mdBytes[i];
                if(temp<16)
                    hash+= "0";
                hash+=Integer.toString(temp,16);         //将字节转换为16进制后，转换为字符串
            }
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getMD5("tzlqq5201314."));
    }

}
