package com.hutquan.hut.pojo;

import lombok.Data;

@Data
public class CaptchaVO {

    /**
     * 验证码标识符
     */
    private String captchaKey;
    /**
     * 验证码过期时间
     */
    private Long expire;
    /**
     * base64字符串
     */
    private String base64Img;
    /**
     * 客户端需要给出的验证码值
     */
    private String code;

}
