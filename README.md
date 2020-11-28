# Minds
个人一直想实现的一个项目：
服务包括话题讨论、失物招领、消息推送、附近话题(Redis的Geo类型)、依托第三方AI技术实现以脸寻人、依托物联网技术实现实时校车信息(这是另外一个系统了，因为涉及到个人的大创项目可能要一段时间才会把代码开源到GitHub)等功能

JDK版本1.8
项目主要技术栈: SpringBoot、Mybatis、Redis、Mysql

代码有主要的注释，静态资源获取系统使用的是Nginx代理请求
Nginx版本1.16.1
在对接小程序的时候也使用Nginx反向代理https

数据库设计(Redis部分可能还会去修改，不过不会修改太多)：
sql文件在项目文档中
![image](https://github.com/TanDawn1/HUT_quanquan/blob/master/Minds%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E8%AE%A1%20(1).png)

成本原因，后续会将短信验证全部转为学校教务系统验证或者邮箱验证(没有相关的实名验证会受到相关部门的监管)

短信验证接口使用的是阿里云市场提供的：https://market.aliyun.com/products/57126001

人脸搜索接口使用的是腾讯开放平台的: https://cloud.tencent.com/product/facerecognition

学校教务系统验证(强智)接口: https://github.com/TLingC/QZAPI
- 兼职信息和对动态信息的审查(对于信息的审查我认为应该拆分到一个新系统)并未完成
- 兼职信息管理系统并未完成
- 特色功能中的心情日记没有暴露接口，service层已经完成
- 消息分发系统还未达到预期效果: https://github.com/TanDawn1/imserver
![image](https://github.com/TanDawn1/HUT_quanquan/blob/master/Minds%20.png)


待优化项

- 对于图片数据应该存储两份: 一份缩略图、一份去除相关信息的原图 这样客户端在点击详情之后在获取原图，在网络不好的情况下体验会好一点

- 对于消息通知系统，其实也可以实现让点赞通知、评论通知，只不过得每个用户评论之后把数据推送至队列，在消息通知系统一条条去消费，效率可能比较低

注意项

- Java 9版本之后Base64Encoder和Base64Decoder无法继续使用，在MyMiniUtils中BASE64Encoder可以修改为Encoder



有任何问题，也可以邮我一起交流：codegun@yeah.net

