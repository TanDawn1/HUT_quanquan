/*
 Navicat Premium Data Transfer

 Source Server         : AliYun
 Source Server Type    : MySQL
 Source Server Version : 50647
 Source Host           : 123.56.160.202:3306
 Source Schema         : HUT_quan

 Target Server Type    : MySQL
 Target Server Version : 50647
 File Encoding         : 65001

 Date: 29/10/2020 14:24:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for claim
-- ----------------------------
DROP TABLE IF EXISTS `claim`;
CREATE TABLE `claim`  (
  `c_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `s_photo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `provine` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `district` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lon_lat` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`c_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of claim
-- ----------------------------
INSERT INTO `claim` VALUES (1, 0, 'test', 'dada.jpn', '湖南', '株洲', '天元区', '湖南工大', '31231,131231', 1598451136);

-- ----------------------------
-- Table structure for claim_comment
-- ----------------------------
DROP TABLE IF EXISTS `claim_comment`;
CREATE TABLE `claim_comment`  (
  `claim_c_id` int(11) NOT NULL AUTO_INCREMENT,
  `c_id` int(11) NOT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `to_user_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`claim_c_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of claim_comment
-- ----------------------------
INSERT INTO `claim_comment` VALUES (1, 1, 'test', 1598451136, 0, NULL);
INSERT INTO `claim_comment` VALUES (2, 1, 'test1', 1598451136, 1, 0);

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `dynamic_id` int(11) NULL DEFAULT NULL COMMENT '动态父表id',
  `time` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `to_user_id` int(11) NULL DEFAULT NULL,
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `to_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `index_dynamic_id`(`dynamic_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 'test', 1, 1598451136, 0, NULL, 'test', NULL);
INSERT INTO `comment` VALUES (2, 'test1', 1, 1598451136, 1, 0, 'test1', 'test');
INSERT INTO `comment` VALUES (3, 'haohaooa', 0, 1602034727, 0, NULL, 'test', NULL);
INSERT INTO `comment` VALUES (4, '好家伙', 32, 1603905468, 2, NULL, 'test2', NULL);
INSERT INTO `comment` VALUES (5, '测试1', 1, 1603905993, 2, NULL, 'test2', NULL);
INSERT INTO `comment` VALUES (6, '好家伙1', 6, 1603906065, 2, NULL, 'test2', NULL);
INSERT INTO `comment` VALUES (7, 'hello', 31, 1603906198, 2, NULL, 'test2', NULL);

-- ----------------------------
-- Table structure for dynamic
-- ----------------------------
DROP TABLE IF EXISTS `dynamic`;
CREATE TABLE `dynamic`  (
  `dynamic_id` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL,
  `time` int(11) NULL DEFAULT NULL,
  `images` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `label` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签',
  PRIMARY KEY (`dynamic_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of dynamic
-- ----------------------------
INSERT INTO `dynamic` VALUES (1, 'test', 0, 1598521444, '[\"1598521442297-0-0.jpg\",\"1.jpg\",\"2.jpg\",\"3.jpg\",\"4.jpg\",\"5.jpg\",\"6.jpg\",\"7.jpg\"]', '测试');
INSERT INTO `dynamic` VALUES (4, '好好学习，天天UP', 1, 1598521444, '[\"1598521442297-0-0.jpg\"]', '测试');
INSERT INTO `dynamic` VALUES (6, 'dadadadsa', 0, 1603585511, '[\"1603585509601-0-0.jpeg\",\"1603585509601-0-1.jpg\",\"1603585509601-0-2.jpg\",\"1603585509601-0-3.jpg\",\"1603585509601-0-4.jpg\",\"1603585509601-0-5.jpg\"]', NULL);
INSERT INTO `dynamic` VALUES (7, '额外额外额外哦', 1, 1603594036, '[]', NULL);
INSERT INTO `dynamic` VALUES (8, 'WWW｀', 1, 1603594091, '[]', NULL);
INSERT INTO `dynamic` VALUES (9, '汪汪汪汪汪', 1, 1603594702, '[]', NULL);
INSERT INTO `dynamic` VALUES (10, '呜呜呜', 1, 1603594833, '[]', NULL);
INSERT INTO `dynamic` VALUES (11, '22222我', 1, 1603596314, '[]', NULL);
INSERT INTO `dynamic` VALUES (12, '测试只发送文字', 1, 1603596908, '[]', NULL);
INSERT INTO `dynamic` VALUES (13, '呜呜呜', 1, 1603597267, '[]', NULL);
INSERT INTO `dynamic` VALUES (14, '测试1文字1202010251142', 1, 1603597378, '[]', NULL);
INSERT INTO `dynamic` VALUES (15, '测试final\n202010251517', 1, 1603610228, '[]', NULL);
INSERT INTO `dynamic` VALUES (16, '额外额外额外额外\n', 1, 1603617868, '[]', NULL);
INSERT INTO `dynamic` VALUES (17, 'HelloWorld', 1, 1603619131, '[]', NULL);
INSERT INTO `dynamic` VALUES (18, '？？？', 1, 1603619423, '[]', NULL);
INSERT INTO `dynamic` VALUES (19, '孙亦杨臭弟弟', 1, 1603695386, '[]', NULL);
INSERT INTO `dynamic` VALUES (22, '测试故1111', 1, 1603807869, '[]', NULL);
INSERT INTO `dynamic` VALUES (23, '21212121', 1, 1603808247, '[]', NULL);
INSERT INTO `dynamic` VALUES (24, '测试202010272218', 1, 1603808294, '[]', NULL);
INSERT INTO `dynamic` VALUES (25, '2121212121', 1, 1603808474, '[]', NULL);
INSERT INTO `dynamic` VALUES (26, '？？？？？？？', 1, 1603808778, '[]', NULL);
INSERT INTO `dynamic` VALUES (27, '。。。。。。', 1, 1603808810, '[]', NULL);
INSERT INTO `dynamic` VALUES (28, '事实上1', 1, 1603809050, '[]', NULL);
INSERT INTO `dynamic` VALUES (29, '巍峨巍峨awe', 1, 1603809535, '[]', NULL);
INSERT INTO `dynamic` VALUES (31, 'test0086', 0, 1603883135, '[]', NULL);
INSERT INTO `dynamic` VALUES (32, '好家伙', 0, 1603887508, '[]', NULL);
INSERT INTO `dynamic` VALUES (33, '测试发文字No.1\n2020，1028，2215', 2, 1603894535, '[]', NULL);

-- ----------------------------
-- Table structure for face_group
-- ----------------------------
DROP TABLE IF EXISTS `face_group`;
CREATE TABLE `face_group`  (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of face_group
-- ----------------------------
INSERT INTO `face_group` VALUES (1, 'Minds');

-- ----------------------------
-- Table structure for face_user_group
-- ----------------------------
DROP TABLE IF EXISTS `face_user_group`;
CREATE TABLE `face_user_group`  (
  `group_user_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`group_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of face_user_group
-- ----------------------------

-- ----------------------------
-- Table structure for feed_back
-- ----------------------------
DROP TABLE IF EXISTS `feed_back`;
CREATE TABLE `feed_back`  (
  `f_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `QQ` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` int(11) NULL DEFAULT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`f_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of feed_back
-- ----------------------------
INSERT INTO `feed_back` VALUES (1, 0, '2274732355', 'wqrqwe.jpn', 1598451136, 'test');

-- ----------------------------
-- Table structure for label
-- ----------------------------
DROP TABLE IF EXISTS `label`;
CREATE TABLE `label`  (
  `l_id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`l_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of label
-- ----------------------------
INSERT INTO `label` VALUES (1, '生活');
INSERT INTO `label` VALUES (2, '运动');
INSERT INTO `label` VALUES (3, '问答');
INSERT INTO `label` VALUES (4, '游戏');
INSERT INTO `label` VALUES (5, '情感');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `m_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `send_user_id` int(11) NULL DEFAULT NULL,
  `receive_user_id` int(11) NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` bigint(20) NULL DEFAULT NULL,
  `already_sent` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`m_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for search
-- ----------------------------
DROP TABLE IF EXISTS `search`;
CREATE TABLE `search`  (
  `s_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `s_photo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `location` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` int(11) NULL DEFAULT NULL,
  `type` int(1) NULL DEFAULT NULL,
  PRIMARY KEY (`s_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of search
-- ----------------------------
INSERT INTO `search` VALUES (1, 0, '丢了丢了', '[\"default.jpg\"]', '东门路口', 1601972538, 1);
INSERT INTO `search` VALUES (4, 0, '丢了个西瓜', '[\"1601991366773-0-0.jpg\"]', '9栋', 1601991369, 0);
INSERT INTO `search` VALUES (5, 0, '丢了', '[\"1602037017819-0-0.jpg\"]', 'test1', 1602037019, 0);

-- ----------------------------
-- Table structure for search_comment
-- ----------------------------
DROP TABLE IF EXISTS `search_comment`;
CREATE TABLE `search_comment`  (
  `search_c_id` int(11) NOT NULL AUTO_INCREMENT,
  `s_id` int(11) NOT NULL,
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` int(11) NULL DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `to_user_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`search_c_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of search_comment
-- ----------------------------
INSERT INTO `search_comment` VALUES (1, 0, 'test', 1598451136, 0, NULL);
INSERT INTO `search_comment` VALUES (2, 0, 'test1', 1598451136, 1, 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `passwd` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tele` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `avatar_picture` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像图片',
  `signature` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个性签名',
  `sex` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (0, 'test', '13231', '18973326132', '[\"1602691853476-0-0.jpg\"]', 'test', '男', 1598451136);
INSERT INTO `user` VALUES (1, 'test1', 'eqweq', '18608000633', '[\"default.jpg\"]', 'test1', '男', 1598451136);
INSERT INTO `user` VALUES (2, 'test2', 'dada', '123456', '[\"default.jpg\"]', 'test2', '男', 1598451136);
INSERT INTO `user` VALUES (22, '15673329132', 'bf1dcfc6-afeb-4942-', '15673329132', '[\"1600232799991-22-0.jpg\"]', NULL, NULL, 1600224496);
INSERT INTO `user` VALUES (23, '15115239932', 'b9b1ccdc-0236-4a06-', '15115239932', '[\"default.jpg\"]', NULL, NULL, 1603162443);

-- ----------------------------
-- Table structure for user_face
-- ----------------------------
DROP TABLE IF EXISTS `user_face`;
CREATE TABLE `user_face`  (
  `face_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `faceImage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`face_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_face
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
