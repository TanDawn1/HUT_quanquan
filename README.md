# Minds

项目主要技术栈: SpringBoot、Mybatis、Redis、Mysql

代码有主要的注释，静态资源获取系统使用的是Nginx代理请求

数据库设计(Redis部分可能还会去修改，不过不会修改太多)：
sql文件在项目文档中
![image](https://github.com/TanDawn1/HUT_quanquan/blob/master/Minds%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E8%AE%A1%20(1).png)

项目整体的功能设想，只完成了部分。
成本原因，后续会将短信验证全部转为学校教务系统验证(没有相关的实名验证会受到相关部门的监管)

短信验证接口使用的是阿里云市场提供的：https://market.aliyun.com/products/57126001

人脸搜索接口使用的是腾讯开放平台的: https://cloud.tencent.com/product/facerecognition

学校教务系统验证(强智)接口: https://github.com/TLingC/QZAPI
- 兼职信息和对动态信息的审查并未完成
- 兼职信息管理系统并未完成
- 消息分发系统还未达到预期效果(参考的GitChat中大佬的)
![image](https://github.com/TanDawn1/HUT_quanquan/blob/master/Minds%20.png)

