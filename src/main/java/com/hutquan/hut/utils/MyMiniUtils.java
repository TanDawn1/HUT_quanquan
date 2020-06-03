package com.hutquan.hut.utils;

import java.util.Random;

public class MyMiniUtils {

    /**
     * 生成验证码
     * @param range
     * @param length
     * @return
     */
    public static String randomNumber(String range, int length) {
        StringBuffer randomNumber = new StringBuffer();
        Random random = new Random();
        for (int n = 0; n < length; n++) {
            //从0~9中依次随机取出6位数
            randomNumber.append(range.charAt(random.nextInt(range.length())));
        }
        range = randomNumber.toString();
        return range;
    }

}
