package com.hutquan.hut.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;
import java.util.Objects;
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

    /**
     * 将MultipatrFile转为Base64编码
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String multipartFileToBase64(MultipartFile file) throws Exception {
        return ImageToBase64(multipartFileToFile(file));
    }

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * file转base64
     *
     * @param file
     * @return
     */
    public static String ImageToBase64(File file) {
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        Base64.Encoder encoder = Base64.getEncoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encodeToString(Objects.requireNonNull(data));
    }

}
