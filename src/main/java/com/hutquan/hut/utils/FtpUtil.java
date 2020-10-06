package com.hutquan.hut.utils;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Properties;

@Component
@Configuration
@ConfigurationProperties(prefix = "ftp")
@PropertySource(value = "classpath:application.properties")
public class FtpUtil {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    public static Logger getLogger() {
        return logger;
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getRootPath() {
        return rootPath;
    }

    public static String getImgUrl() {
        return imgUrl;
    }

    /**
     * ftp服务器ip地址
     */
    private static String host;

    public void setHost(String val) {
       FtpUtil.host = val;
    }

    /**
     * 端口
     */
    private static int port;

    public void setPort(int val) {
        FtpUtil.port = val;
    }

    /**
     * 用户名
     */
    private static String userName;

    public void setUserName(String val) {
        FtpUtil.userName = val;
    }

    /**
     * 密码
     */
    private static String password;

    public void setPassword(String val) {
        FtpUtil.password = val;
    }

    /**
     * 存放图片的根目录
     */
    private static String rootPath;

    public void setRootPath(String val) {
        FtpUtil.rootPath = val;
    }

    /**
     * 存放图片的路径
     */
    private static String imgUrl;

    public void setImgUrl(String val) {
        FtpUtil.imgUrl = val;
    }

    private static ChannelSftp getChannel() throws Exception {
        JSch jsch = new JSch();

        // ->ssh root@host:port
        Session sshSession = jsch.getSession(userName, host, port);
        // 密码
        sshSession.setPassword(password);

        Properties sshConfig = new Properties();
        // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
        sshSession.setConfig("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        //设置登陆超时时间
        // 注意！！这里不设置超时间会报错
        sshSession.connect(60000);

        Channel channel = sshSession.openChannel("sftp");
        channel.connect(1000);

        return (ChannelSftp) channel;
    }

    /**
     * ftp上传图片
     *
     * @param inputStream 图片io流
     * @param imagesName  图片名称
     * @return urlStr 图片的存放路径
     */
    public static String putImages(InputStream inputStream, String imagesName) {
        ChannelSftp sftp = null;
        try {
            sftp = getChannel();
            System.out.println(sftp);
            String path = rootPath + "/";
            //创建文件
            //createDir("/home/image/test", sftp);
            System.out.println(path + imagesName);
            // 上传文件
            sftp.put(inputStream, path + imagesName);
            logger.info("上传成功！");

            // 处理返回的路径
            String resultFile;
            resultFile = imgUrl + imagesName;

            return resultFile;

        } catch (Exception e) {
            logger.error("上传失败：" + e.getMessage());
        }finally {
            if (sftp != null) {
                sftp.quit();
            }
            if (sftp != null) {
                sftp.exit();
            }
        }
        return "";
    }

    /**
     * 创建目录
     */
    private static void createDir(String path, ChannelSftp sftp) throws SftpException {
        String[] folders = path.split("/");
        sftp.cd("/");
        for (String folder : folders) {
            if (folder.length() > 0) {
                try {
                    sftp.cd(folder);
                } catch (SftpException e) {
                    sftp.mkdir(folder);
                    sftp.cd(folder);
                }
            }
        }
    }

    /**
     * 删除图片
     */
    public static void delImages(String imagesName) {
        try {
            ChannelSftp sftp = getChannel();
            String path = rootPath + "/" + imagesName;
            sftp.rm(path);
            sftp.quit();
            sftp.exit();
        } catch (Exception e) {
            logger.error(" 删除失败：" + e.getMessage());
        }
    }

}
