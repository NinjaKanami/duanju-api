/*
 Navicat Premium Data Transfer

 Source Server         : 服务器数据库
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : 42.193.11.150:3306
 Source Schema         : test01

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 26/04/2024 17:37:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '轮播id',
  `create_at` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '时间',
  `image_url` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '图片',
  `state` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '分类',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '跳转地址',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of activity
-- ----------------------------
INSERT INTO `activity` VALUES (4, '2020-09-03 22:13:42', 'http://shegnqx.oss-cn-beijing.aliyuncs.com/1592358312292.png', '1', '/pages/discovery/list', '报名活动');
INSERT INTO `activity` VALUES (5, '2020-06-17 09:42:36', 'http://shegnqx.oss-cn-beijing.aliyuncs.com/1592358296803.png', '1', '/pages/index/list?title=超值大牌&type=8', '俱乐部介绍');
INSERT INTO `activity` VALUES (6, '2020-06-17 09:42:22', 'http://shegnqx.oss-cn-beijing.aliyuncs.com/1592358282896.png', '1', '/pages/index/list?title=9.9包邮&type=2', '活动公示');
INSERT INTO `activity` VALUES (7, '2020-06-17 09:42:08', 'http://shegnqx.oss-cn-beijing.aliyuncs.com/1592358269302.png', '1', '/pages/index/tuiguang?cid=9', '公益活动');
INSERT INTO `activity` VALUES (8, '2020-07-08 16:03:23', 'http://shegnqx.oss-cn-beijing.aliyuncs.com/1592358254867.png', '1', '/pages/index/list?title=30元精选&type=3', '信息公示');
INSERT INTO `activity` VALUES (9, '2020-07-08 16:02:32', 'http://shegnqx.oss-cn-beijing.aliyuncs.com/1592358240332.png', '1', '/pages/index/list?title=巨划算&type=4', '线下活动');

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'app升级配置',
  `android_wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '安卓升级地址',
  `create_at` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '创建时间',
  `des` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '描述',
  `ios_version` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '苹果版本',
  `ios_wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '苹果升级地址',
  `method` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '是否强制升级',
  `version` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '安卓版本',
  `wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '通用下载地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app
-- ----------------------------
INSERT INTO `app` VALUES (5, 'https://www.pgyer.com/hb2v', '2020-07-20 18:35:00', '升级', '1.0.0', '1.0.0', 'false', '1.0.0', 'https://www.pgyer.com/hb2v');

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `state` int(2) NULL DEFAULT NULL COMMENT '状态1正常2隐藏',
  `classify` int(2) NULL DEFAULT NULL COMMENT '分类1banner图2首页分类',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址 ',
  `sort` int(10) NULL DEFAULT NULL COMMENT '顺序',
  `describes` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (4, '2021-04-27 11:38:20', 'banner4', 'https://shegnqx.oss-cn-beijing.aliyuncs.com/20200804/47ba8f7ed92a4cc79ae5d514cd61f8d4.png', 1, 4, 'https://shegnqx.oss-cn-beijing.aliyuncs.com/20200804/47ba8f7ed92a4cc79ae5d514cd61f8d4.png', 4, '6666');
INSERT INTO `banner` VALUES (13, '2024-04-26 14:53:59', '邀请好友', 'https://fanhua.xianmxkj.com/file/uploadPath/2024/04/26/6f07e6120806d91f8f9584bd206125ab.png', 1, 5, '邀请好友', NULL, '快来和我一起看剧吧');
INSERT INTO `banner` VALUES (19, '2023-08-21 16:51:32', 'c', 'https://jiaoyu.xianmxkj.com/img/20230821/7dfa9f81069a4d999647eef08b83a335.png', 1, 1, 'https://www.xiansqx.com', NULL, '');
INSERT INTO `banner` VALUES (20, '2023-08-21 16:51:32', 'd', 'https://jiaoyu.xianmxkj.com/img/20230821/cecb4781f86c4ba49e256c4b78ebc94d.png', 1, 1, 'https://www.xiansqx.com', NULL, '');
INSERT INTO `banner` VALUES (27, '2023-08-22 17:11:08', '1', 'https://jiaoyu.xianmxkj.com/img/20230822/27d37f847db9481bb3dc54cd12258147.jpg', 1, 3, 'https://www.xiansqx.com', NULL, '1');
INSERT INTO `banner` VALUES (29, '2023-08-30 10:30:35', 'a', 'https://duanju.xianmxkj.com/img/20230830/a2b0ce8729dc4ccb82cab53b171eb979.png', 1, 10, '', NULL, '');
INSERT INTO `banner` VALUES (30, '2023-08-30 10:30:35', '2', 'https://duanju.xianmxkj.com/img/20230830/40ee369b5b704080a7f291fa4b2d6807.png', 1, 10, '', NULL, '');
INSERT INTO `banner` VALUES (31, '2023-08-30 10:30:35', '3', 'https://duanju.xianmxkj.com/img/20230830/459f94b7a9ee4cc08b7a4e268696a2ba.png', 2, 10, '', NULL, '');
INSERT INTO `banner` VALUES (32, '2023-08-30 10:30:35', '4', 'https://duanju.xianmxkj.com/img/20230830/297e10333d4d42a58c6f21ecf160ea8e.png', 2, 10, '', NULL, '');
INSERT INTO `banner` VALUES (34, '2023-12-18 18:45:24', '最新', 'https://duanju.xianmxkj.com/img/20230908/28ad2530c2eb428c9cca9231d3e46d6a.png', 1, 2, '/pages/index/course/courseList?title=最新', 1, '最新');
INSERT INTO `banner` VALUES (35, '2023-09-08 11:36:09', '排行', 'https://duanju.xianmxkj.com/img/20230908/28beeb9fa76944eca29789722e67cbdb.png', 1, 2, '/pages/index/course/courseList?sort=1&title=排行', NULL, '排行');
INSERT INTO `banner` VALUES (36, '2023-09-08 11:36:09', '最热', 'https://duanju.xianmxkj.com/img/20230908/775b7540809b4b53808db9b439f466e9.png', 1, 2, '/pages/index/course/courseList?title=最热&sort=2', NULL, '最热');
INSERT INTO `banner` VALUES (37, '2024-04-15 09:50:50', 'a', 'https://jiaoyu.xianmxkj.com/img/20230821/f79285bf638c4b219e8d9be1b15f675c.png', 1, 1, 'www.taobao.com', NULL, 's');
INSERT INTO `banner` VALUES (38, '2023-08-22 17:12:12', '2', 'https://jiaoyu.xianmxkj.com/img/20230822/466ca0fab359440ca30b1c9696dbe375.jpg', 1, 3, 'https://www.xiansqx.com', NULL, '2');
INSERT INTO `banner` VALUES (40, '2023-09-08 11:36:09', '剧情', 'https://duanju.xianmxkj.com/img/20230908/2980c3be9bc74b77a6900fa2c084d7ea.png', 1, 2, '/me/juqing/juqing', NULL, '剧情');
INSERT INTO `banner` VALUES (41, '2023-09-08 11:36:09', '壁纸', 'https://duanju.xianmxkj.com/img/20230908/295e4de529404dd4af76608255aaf50d.png', 1, 2, '/me/wallpaper/wallpaper', NULL, '壁纸');
INSERT INTO `banner` VALUES (44, '2023-08-30 10:30:35', '第一集', 'https://duanju.xianmxkj.com/img/20230830/a2b0ce8729dc4ccb82cab53b171eb979.png', 1, 20, 'https://duanju.xianmxkj.com/img/20230830/262452f985384d83a814a2b87d95ef92.mp4', 1, '1');
INSERT INTO `banner` VALUES (45, '2023-08-30 10:30:35', '第二集', 'https://duanju.xianmxkj.com/img/20230830/40ee369b5b704080a7f291fa4b2d6807.png', 1, 20, 'https://duanju.xianmxkj.com/img/20230830/262452f985384d83a814a2b87d95ef92.mp4', 2, '2');
INSERT INTO `banner` VALUES (46, '2023-08-30 10:30:35', '第三集', 'https://duanju.xianmxkj.com/img/20230830/459f94b7a9ee4cc08b7a4e268696a2ba.png', 1, 20, 'https://duanju.xianmxkj.com/img/20230830/262452f985384d83a814a2b87d95ef92.mp4', 3, '2');
INSERT INTO `banner` VALUES (47, '2023-08-30 10:30:35', '第四集', 'https://duanju.xianmxkj.com/img/20230830/297e10333d4d42a58c6f21ecf160ea8e.png', 1, 20, 'https://duanju.xianmxkj.com/img/20230830/262452f985384d83a814a2b87d95ef92.mp4', 4, '2');
INSERT INTO `banner` VALUES (48, '2024-04-01 16:39:43', '官配1', 'https://duanju.xianmxkj.com/file/uploadPath/2024/04/01/1cecf2ae0bf476ee3e37daf404f60064.jpg', 1, 11, 'https://duanju.xianmxkj.com/file/uploadPath/2024/04/01/1aec563cd3745c62aca4f9972b1d5413.mp4', NULL, '2');

-- ----------------------------
-- Table structure for cash_out
-- ----------------------------
DROP TABLE IF EXISTS `cash_out`;
CREATE TABLE `cash_out`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请提现id',
  `create_at` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '申请时间',
  `is_out` bit(1) NULL DEFAULT NULL COMMENT '是否转账',
  `money` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '提现金额',
  `out_at` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '转账时间',
  `relation_id` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '会员编号',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `zhifubao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝账号',
  `zhifubao_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝姓名',
  `order_number` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '订单编号',
  `state` int(11) NULL DEFAULT NULL COMMENT '状态 0待转账 1成功 -1退款',
  `refund` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原因',
  `classify` int(11) NULL DEFAULT 1,
  `rate` decimal(10, 2) NULL DEFAULT NULL,
  `sys_user_id` int(11) NULL DEFAULT NULL,
  `user_type` int(11) NULL DEFAULT NULL,
  `wx_img` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_name`(`relation_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 579 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cash_out
-- ----------------------------

-- ----------------------------
-- Table structure for comment_good
-- ----------------------------
DROP TABLE IF EXISTS `comment_good`;
CREATE TABLE `comment_good`  (
  `comment_good_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论点赞id',
  `course_comment_id` int(11) NULL DEFAULT NULL COMMENT '评论id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`comment_good_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 153 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment_good
-- ----------------------------

-- ----------------------------
-- Table structure for common_info
-- ----------------------------
DROP TABLE IF EXISTS `common_info`;
CREATE TABLE `common_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置文件id',
  `create_at` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '创建时间',
  `max` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `min` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置文件名称',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型',
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '值',
  `condition_from` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 890 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of common_info
-- ----------------------------
INSERT INTO `common_info` VALUES (1, '2020-03-29 00:18:08', NULL, '客服二维码', 1, 'https://taobao.xianmxkj.com/custom.jpg', 'image');
INSERT INTO `common_info` VALUES (2, '2024-03-20 13:59:42', NULL, '公众号二维码', 2, 'https://duanju.xianmxkj.com/file/uploadPath/2024/03/20/29f938f27ac9a3b8793c3950c518d5f7.jpg', 'image');
INSERT INTO `common_info` VALUES (3, '2020-11-04 10:31:40', NULL, '注册邀请码是否必填', 3, '否', 'kaiguan');
INSERT INTO `common_info` VALUES (5, '2023-06-05 17:48:37', NULL, '微信公众号APPID	', 5, '', 'weixin');
INSERT INTO `common_info` VALUES (16, '2023-06-05 17:48:23', NULL, '微信公众号秘钥	', 21, '', 'weixin');
INSERT INTO `common_info` VALUES (17, '2020-02-25 20:43:59', NULL, '公众号Token', 16, 'maxd', 'weixin');
INSERT INTO `common_info` VALUES (18, '2020-02-25 20:44:15', NULL, '公众号EncodingAESKey	', 17, 'O8T7NubxpjOd7uNoVV7g01PDpPGUWpiGrLWWIFyaaCH', 'weixin');
INSERT INTO `common_info` VALUES (20, '2024-04-09 14:44:58', NULL, '后台管理平台域名配置	', 20, 'https://fanhuaadmin.xianmxkj.com', 'xitong');
INSERT INTO `common_info` VALUES (21, '2024-04-09 14:44:27', NULL, 'h5服务域名配置	', 19, 'https://fanhua.xianmxkj.com', 'xitong');
INSERT INTO `common_info` VALUES (22, '2020-07-27 15:17', NULL, '短信服务商（1 开启腾讯云 2 开启阿里云 3短信宝）', 79, '3', 'duanxin');
INSERT INTO `common_info` VALUES (23, '2022-06-02 22:15:46', NULL, '后台服务名称	', 12, '省钱兄', 'xitong');
INSERT INTO `common_info` VALUES (33, '2021-03-15 21:19:27', NULL, '腾讯云短信clientId	', 31, '', 'duanxin');
INSERT INTO `common_info` VALUES (34, '2020-03-28 00:47', NULL, '腾讯云短信clientSecret	', 32, '', 'duanxin');
INSERT INTO `common_info` VALUES (47, '2024-04-18 14:22:27', NULL, '微信小程序APPID', 45, '', 'weixin');
INSERT INTO `common_info` VALUES (48, '2024-04-18 14:22:47', NULL, '微信小程序秘钥', 46, '', 'weixin');
INSERT INTO `common_info` VALUES (51, '2023-12-14 14:46:01', NULL, '分享安卓下载地址', 49, 'https://duanju.xianmxkj.com/duanju.apk', 'xitong');
INSERT INTO `common_info` VALUES (52, '2020-03-29 00:21', NULL, '分享苹果下载地址', 50, 'https://www.pgyer.com/c11o', 'xitong');
INSERT INTO `common_info` VALUES (58, '2020-03-29 00:21', NULL, '开启微信登录', 53, '是', 'xitongs');
INSERT INTO `common_info` VALUES (69, '2020-06-04 16:34', NULL, 'APP消息推送PushAppKey', 60, '0n6PRHS4ph85bBI9nbRCs7', 'push');
INSERT INTO `common_info` VALUES (70, '2020-06-04 16:34', NULL, 'APP消息推送PushAppId', 61, 'kmAYxzB5Ys93Kn7ysprSx3', 'push');
INSERT INTO `common_info` VALUES (71, '2020-06-04 16:34', NULL, 'APP消息推送PushMasterSecret', 62, 'KZQn7eeT9I9q6U3CsDjcK2', 'push');
INSERT INTO `common_info` VALUES (72, '2020-06-04 16:34', NULL, '企业支付宝APPID', 63, '', 'zhifubao');
INSERT INTO `common_info` VALUES (73, '2020-06-04 16:34', NULL, '企业支付宝公钥', 64, '', 'zhifubao');
INSERT INTO `common_info` VALUES (74, '2020-06-04 16:34', NULL, '企业支付宝商户秘钥', 65, '', 'zhifubao');
INSERT INTO `common_info` VALUES (77, '2021-03-15 21:22:30', NULL, '文件上传阿里云Endpoint', 68, 'http://oss-cn-beijing.aliyuncs.com', 'oss');
INSERT INTO `common_info` VALUES (78, '2021-03-15 21:22:38', NULL, '文件上传阿里云账号accessKeyId', 69, '', 'oss');
INSERT INTO `common_info` VALUES (79, '2021-03-15 21:22:46', NULL, '文件上传阿里云账号accessKeySecret', 70, '', 'oss');
INSERT INTO `common_info` VALUES (80, '2021-03-15 21:22:53', NULL, '文件上传阿里云Bucket名称', 71, 'shegnqx', 'oss');
INSERT INTO `common_info` VALUES (81, '2021-03-15 21:23:00', NULL, '文件上传阿里云Bucket域名', 72, 'https://shegnqx.oss-cn-beijing.aliyuncs.com', 'oss');
INSERT INTO `common_info` VALUES (83, '2020-07-27 15:17', NULL, '微信APPappId', 74, '', 'weixin');
INSERT INTO `common_info` VALUES (84, '2020-07-27 15:17', NULL, '微信商户key', 75, '', 'weixin');
INSERT INTO `common_info` VALUES (85, '2020-07-27 15:17', NULL, '微信商户号mchId', 76, '', 'weixin');
INSERT INTO `common_info` VALUES (88, '2021-02-24 18:46:57', NULL, '官方邀请码', 88, '666666', 'xitong');
INSERT INTO `common_info` VALUES (89, '2023-08-28 11:23:02', NULL, '会员赏金百分比', 80, '0.5', 'fuwufeis');
INSERT INTO `common_info` VALUES (90, '2021-03-15 21:19:21', NULL, '短信签名', 81, '省钱兄', 'duanxin');
INSERT INTO `common_info` VALUES (96, '2020-07-27 15:17', NULL, '阿里云登陆或注册模板code（开启阿里云短信必须配置）', 82, 'SMS_200190994', 'duanxin');
INSERT INTO `common_info` VALUES (97, '2020-07-27 15:17', NULL, '阿里云找回密码模板code（开启阿里云短信必须配置）', 83, 'SMS_200176048', 'duanxin');
INSERT INTO `common_info` VALUES (98, '2020-07-27 15:17', NULL, '阿里云绑定手机号模板code（开启阿里云短信必须配置）', 84, 'SMS_200186024', 'duanxin');
INSERT INTO `common_info` VALUES (99, '2020-07-27 15:17', NULL, '阿里云短信accessKeyId', 85, '', 'duanxin');
INSERT INTO `common_info` VALUES (100, '2020-07-27 15:17', NULL, '阿里云短信accessSecret', 86, '', 'duanxin');
INSERT INTO `common_info` VALUES (101, '2020-07-27 15:17', NULL, '支付宝转账方式 1证书 2秘钥 3手动', 98, '3', 'zhifubao');
INSERT INTO `common_info` VALUES (102, '2023-06-06 16:50:22', NULL, '初始签到积分', 102, '100', 'fuwufei');
INSERT INTO `common_info` VALUES (103, '2020-07-27 15:17', NULL, '积分签到累计增加', 103, '10', 'fuwufei');
INSERT INTO `common_info` VALUES (104, '2020-07-27 15:17', NULL, '积分兑换比例 1元等于', 104, '100', 'fuwufei');
INSERT INTO `common_info` VALUES (124, '2024-04-17 14:35:21', NULL, '是否开启公众号自动登陆', 108, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (125, '2020-07-27 15:17', NULL, '是否开启公众号自动注册', 109, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (129, '2024-02-02 18:12:27', NULL, '每日可提现次数', 110, '1', 'fuwufei');
INSERT INTO `common_info` VALUES (130, '2020-11-04 10:31:40', NULL, '提现最低额度', 112, '1', 'fuwufei');
INSERT INTO `common_info` VALUES (140, '2020-07-27 15:17', NULL, '腾讯地图key', 128, 'WI7BZ-YZCKF-U3BJQ-JUMBB-XUQ3E-UXFYU', 'xitong');
INSERT INTO `common_info` VALUES (170, '2020-11-04 10:31:40', NULL, '提现手续费', 152, '0.01', 'fuwufei');
INSERT INTO `common_info` VALUES (171, '2020-11-04 10:31:40', NULL, '最高提现金额', 153, '10000', 'fuwufei');
INSERT INTO `common_info` VALUES (172, '2024-04-26 11:05:28', NULL, '用户协议', 154, '<p class=\"ql-align-center\"><strong class=\"ql-size-huge\">梵花视频平台服务协议</strong><strong class=\"ql-size-huge\" style=\"color: rgb(51, 51, 51);\">﻿</strong></p><p><span style=\"color: rgb(51, 51, 51);\">版本生效日期：2024&nbsp;年&nbsp;5&nbsp;月&nbsp;5日</span></p><p><br></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>欢迎您与平台经营者共同签署本《梵花视频平台服务协议》（下称“本协议”）并使用平台服务！为维护您自身权益，请您仔细阅读各条款具体表述。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">【重要提示】</strong><span style=\"color: rgb(51, 51, 51);\">您在点击同意本协议之前，应当认真阅读本协议。</span><strong style=\"color: rgb(51, 51, 51);\"><u>请您务必审慎阅读、充分理解各条款内容</u></strong><span style=\"color: rgb(51, 51, 51);\">（条款前所列标题仅为检索定位之用，不视为条款主旨的概括，请您仔细阅读各条款的具体内容），</span><strong style=\"color: rgb(51, 51, 51);\"><u>特别是免除或者限制责任的条款、法律适用和争议解决条款及其他以粗体/粗体下划线标示的条款，因为这些条款会涉及义务的明确或权利的限制。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">【签约动作】<u>一旦您在网络页面点击同意本协议或以其他方式表示接受本协议或实际使用平台服务的，即表示您已充分阅读、理解并接受本协议的全部内容，并与梵花视频平台经营者达成一致，成为平台用户。阅读本协议的过程中，如果您不同意本协议或其中任何条款约定，您应立即停止使用平台服务。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">您承诺，在您开始注册程序及/或实际使用平台服务时，您应当具备中华人民共和国法律规定的与您行为相适应的民事行为能力。</span><strong style=\"color: rgb(51, 51, 51);\"><u>若您不具备前述与您行为相适应的民事行为能力，则您及您的监护人应依照法律规定承担因此而导致的一切后果。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">一、定义</strong></p><p><span style=\"color: rgb(51, 51, 51);\">1.1平台（或称“梵花视频”）：指标注名称为“梵花视频”的移动客户端应用软件、微信小程序、微信公众号以及将来可能推出的新技术形式。</span></p><p><span style=\"color: rgb(51, 51, 51);\">1.2平台经营者（或称“经营者”、“我们”）：指提供梵花视频平台服务的法律主体，本政策下指“杭州志永网络科技有限公司”，我们可能随时调整经营者，您可随时查看平台公示的证照信息等以确定彼时的实际经营主体。</span></p><p><span style=\"color: rgb(51, 51, 51);\">1.3平台服务（或称“服务”）：指经营者基于互联网，通过平台向用户提供的各项服务。</span></p><p><span style=\"color: rgb(51, 51, 51);\">1.4平台规则：指包括本政策及在平台页面已经发布及后续发布的全部规则、实施细则、行为规范、公告等为有效维持平台运行而制定的文件。</span></p><p><span style=\"color: rgb(51, 51, 51);\">1.5平台规则变更：包括但不限于本政策条款的更改、其他平台规则的更改、发布新的平台规则等。</span></p><p><span style=\"color: rgb(51, 51, 51);\">1.6用户：完成注册程序，获得平台账户的即为平台用户，本协议中也称“您”。</span></p><p><span style=\"color: rgb(51, 51, 51);\">1.7需求方：指通过梵花视频平台，使用平台用户的推广服务，以推广相关内容（含服务）的单位或个人，包括但不限于剧集权利方，为剧集进行推广的主体。</span></p><p><span style=\"color: rgb(51, 51, 51);\">1.8推广信息：指由需求方提供后展示在平台内，供用户自主选择进行推广的素材，包括但不限于剧集图片、文字、视频、flash、链接及内容等。</span></p><p><span style=\"color: rgb(51, 51, 51);\">1.9内容场景：指用户在内容平台通过短视频、图文、音频、文字等方式为需求方进行的内容推广，内容平台包括但不限于抖音、快手、微信视频号、今日头条等。</span></p><p><strong style=\"color: rgb(51, 51, 51);\">二、协议的变更</strong></p><p><span style=\"color: rgb(51, 51, 51);\">考虑国家法律法规、政策变更及互联网环境的发展，本协议并不能囊括全部权利和义务，也不能保证符合未来发展的需求，故</span><strong style=\"color: rgb(51, 51, 51);\"><u>经营者有权不时地制订、修改、补充本协议及/或各类平台规则，届时将公示于梵花视频平台，无需另行单独通知您。除非法律法规另有强制性规定、本协议和/或各类规则另有明确约定生效时间外，否则变更后的协议和规则一经公布即自动生效。您有义务不时关注并阅读最新的平台规则及网站公告。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>梵花视频平台有权根据业务需要调整经营者，经营者的变更不会影响用户本协议项下的权益，变更后的经营者将按届时的生效平台规则向用户提供服务且可能提供新的服务项目。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>若用户不能接受经营者变更及/或平台规则变更的，应立即停止使用梵花视频平台；若用户继续使用梵花视频平台的，即视为同意。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">三、注册与使用</strong></p><p><span style=\"color: rgb(51, 51, 51);\">3.1您同意，在使用梵花视频平台服务时，您应当按照梵花视频平台页面的提示准确、完整地提供您的信息（包括但不限于名称、联系电话、联系邮箱等），以便经营者或他人与您联系。</span><strong style=\"color: rgb(51, 51, 51);\"><u>您了解并同意，您有义务保证您提供信息的真实性及有效性。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">3.2您应当及时更新您提供的信息，以保持您所提供的信息最新、真实、完整、有效。一旦经营者发现您提供的资料错误、不实、过时或不完整的，您将承担因此对您自身、他人及梵花视频平台造成的全部损失与不利后果。经营者有权中止、终止向您提供部分或全部平台服务，您需承担由此造成的全部损失与不利后果，经营者对此不承担任何责任。</span></p><p><span style=\"color: rgb(51, 51, 51);\">3.3您的账户密码由您自行设置并由您自行保管。</span><strong style=\"color: rgb(51, 51, 51);\"><u>您不得将账户交予他人使用（包括但不限于出借、租赁账号及将身份信息借予他人进行认证等使得注册主体与实际使用人不一致的情形，均视为“交予他人使用”），否则经营者有权中止、终止提供部分或全部平台服务，您应承担由此产生的全部责任，并与实际使用人承担连带责任。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">3.4</span><strong style=\"color: rgb(51, 51, 51);\"><u>若您的账户因您自身操作不当或遭受他人攻击、诈骗等导致的损失及后果，经营者并不承担责任，您应通过司法、行政等救济途径向侵权行为人追偿。</u></strong><span style=\"color: rgb(51, 51, 51);\">如您发现账户在本人不知情的情况下被用于登录梵花视频平台或其他任何显示您的账户可能已遭窃、遗失的情形时，应立即修改账户密码并妥善保管，或立即以有效方式通知经营者，请求经营者暂停相关服务。</span><strong style=\"color: rgb(51, 51, 51);\"><u>您理解经营者对您的任何请求采取行动均需要合理时间，且经营者应您请求而采取的行动可能无法避免或阻止侵害后果的形成或扩大，除经营者存在法定过错外，经营者不承担责任。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>3.5&nbsp;任何通过您的账户实施的操作（包括但不限于网上点击同意各类规则协议、绑定第三方账号、使用第三方服务、发布信息、披露信息、支付费用、提现等），均视为是您的操作，由您承担全部责任。您应对使用平台服务时接触到的第三方技能/服务自行加以审慎判断，并承担因使用第三方技能/服务而引起的所有风险，包括对第三方技能/服务的合法性、准确性、完整性或实用性的依赖而产生的风险。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>3.6您理解并知悉，经营者有权在符合相关法律法规的要求下，通过您预留的联系方式向您提供相关广告信息、服务信息、促销优惠信息等商业性信息。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>3.7一旦您接受本协议，即表明同意经营者在诉讼/仲裁需要等必要情况下，使用您的用户名、密码等信息，登录您的账户，进行证据保全，包括但不限于公证、见证等。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>3.8账户不得出售、出租、转许可、转让、赠与、继承（与账户相关的财产权益除外）或以其他形式进行任何让渡。除非有法律规定或司法裁判，且征得经营者的同意。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">3.9经营者非常重视用户个人信息的保护，</span><strong style=\"color: rgb(51, 51, 51);\"><u>用户使用经营者提供的服务，即表明同意经营者按照其规则收集、存储、使用、披露和保护用户的个人信息</u></strong><span style=\"color: rgb(51, 51, 51);\">。同时为保障用户的信息安全，我们努力采取合理的安全措施来保护用户的信息，使用户的信息不会被泄露、毁损或丢失。</span></p><p><span style=\"color: rgb(51, 51, 51);\">我们收集、存储用户的信息仅是为了：向用户提供服务及提升服务质量；向用户及时推送平台新的商品及活动策划等优化宣传内容；用以预防/发现/调查欺诈、危害安全、违法犯罪或违反平台规则的行为，以保护用户及/或平台的合法权益；以及经用户许可的其他用途。</span></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>3.10用户不得发表下列内容，且不得利用本平台从事以下活动：</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（1）违反国家法律法规禁止性规定的，如采用传销模式进行推广等；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（2）政治宣传、封建迷信、淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（3）侵犯他人知识产权或涉及第三方商业秘密及其他专有权利的；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（4）发布欺诈、虚假、不准确或存在误导性信息的；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（5）发布侮辱、诽谤、恐吓、涉及他人隐私等侵害他人合法权益信息的；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（6）未经允许，对进入计算机信息网络中存储、处理或者传输的数据和应用程序进行删除、修改或者增加的；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（7）故意制作、传播计算机病毒等破坏性程序或从事其他危害计算机信息网络安全的行为的；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（8）不法交易行为，如洗钱等；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（9）非法使用他人银行账户（包括信用卡账户）或无效银行账号（包括信用卡账户）交易；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（10）未经经营者事先明示或书面同意，向其他用户等第三人收取或变相收取任何费用；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（11）其他违背社会公共利益或公共道德或依据经营者相关规则不得从事的。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">四、服务内容</strong></p><p><span style=\"color: rgb(51, 51, 51);\">4.1【服务】您可以登陆梵花视频平台，查看平台展示的各项功能。平台所提供的服务包括但不限于：浏览及/或选择推广素材、查看及/或提现收益等，经营者将不定期调整梵花视频平台的服务，因此会存在功能的增加、修改或暂缓提供。</span></p><p><strong style=\"color: rgb(51, 51, 51);\">（一）关于推广信息</strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>1.您充分理解并知悉，平台展示的推广信息为需求方提供，您在进行推广行为前需要秉持审慎态度谨慎处理。经营者作为互联网平台提供者，仅向您提供与梵花视频平台相关的软件服务，鉴于平台内存在海量信息，经营者无法逐一审查推广信息的真实、合法、有效，不对推广信息的真实、合法、有效承担任何保证与承诺。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">2.合同关系说明：用户充分理解并知悉，用户与需求方建立合同关系。平台经营者并不是实际有推广需求的主体，也不是实际进行推广行为的主体。</span></p><p><span style=\"color: rgb(51, 51, 51);\">3.您如果对推广信息进行编辑的，请自行对编辑后的推广信息负责，确保编辑后的推广信息仍符合本协议约定，同时也不会侵害第三方权益以及违背法律法规规定。</span></p><p><span style=\"color: rgb(51, 51, 51);\">4.</span><strong style=\"color: rgb(51, 51, 51);\">【推广变更】</strong><span style=\"color: rgb(51, 51, 51);\">您与需求方建立推广关系后，请您注意：</span></p><p><span style=\"color: rgb(51, 51, 51);\">（1）您可能基于自身业务政策等原因而中止或终止为需求方提供推广服务。</span></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（2）需求方可能随时会对推广计划进行调整（如调整佣金比率、推广信息等），或基于其业务政策等原因而中止或终止使用平台服务，请您自行、定期通过平台展示信息检查推广是否发生变更，包括推广代码、推广链接是否仍然有效、佣金比率是否发生变更、推广的内容是否仍然存在、推广信息是否发生变更等，并视变更情况调整推广方案。因您怠于检查导致预估收入损失、推广行为侵权等问题由您自行承担责任。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">5.您进行推广时，应按法律法规规定履行自身相应的义务。如您发现推广信息不符合本协议约定，或者存在侵害第三方权益/违背相关法律法规情形的，请尽快向经营者反馈。</span></p><p><strong style=\"color: rgb(51, 51, 51);\">（二）关于内容场景</strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>1.您保证对自身使用的推广资源（包括但不限于相关内容平台账号、在账号内所发布的图片、音频、视频、文字等内容）享有合法权利，由您在内容平台发布的内容不违反国家相关政策、法律、法规，不侵犯任何第三方的合法权益，否则，由此引发的一切后果均由您自行承担。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>2.您应秉持提升推广信息被展示/和被点击/和被转化概率为目的，按照梵花视频平台流程及要求（如涉及），选择契合的推广信息发布在内容平台上，您需保证您的数据推广行为真实、合法，不违反内容平台规则。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">3.您应严格按照梵花视频平台流程，执行推广素材的发布，提供稳定可靠的数据推广行为。非平台原因（包括但不限于用户未严格执行平台规定的信息发布及/或推广流程、用户发布内容问题导致被内容平台封禁、删除、断开链接等）导致的一切责任（包括但不限于预期利益损失等），均由用户自行承担，梵花视频平台不对此承担任何责任。</span></p><p><span style=\"color: rgb(51, 51, 51);\">4.为更好的服务用户，梵花视频平台上可能会展示以帮助用户更好的推广为目的导向的内容（包括但不限于推广技巧、推广教学流程、推广课程等）。</span><strong style=\"color: rgb(51, 51, 51);\"><u>经营者不对该等内容的未来效果、收益或者与其相关的情况作出保证性承诺，亦无任何明示或者暗示保本、无风险或者保收益的意思表达，即经营者无需对您的推广收益、效果等承担任何责任。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">（三）收益结算</strong></p><p><span style=\"color: rgb(51, 51, 51);\">1.您理解并同意，收益数据、计算方式等均以梵花视频平台统计并展示的结果为准。当您满足梵花视频平台公示的提现要求后，您有权自行发起提现申请，梵花视频平台将进行审核并对审核通过的用户按届时展示的结算规则、周期、付款方式等向您支付相应费用。</span></p><p><span style=\"color: rgb(51, 51, 51);\">2.梵花视频平台会定期统计并展示预计支付给您的收益，您有权随时登录梵花视频平台查看收益情况。您理解并同意，极个别情况下，梵花视频平台统计数据可能会存在更新延迟。若您对收益结算金额存在任何异议的，您应于该等数据在梵花视频平台展示之日起1日内书面向梵花视频平台进行反馈并提供相关证明材料，逾期未提出异议及相关证明材料或您已实际发起提现申请的（以时间在先者为准），视为您对此予以认可。</span></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>3.您使用平台服务所产生的收益来源于需求方及/或内容平台（包括但不限于抖音、快手、视频号等平台、需求方等），经营者在未实际取得相关收益前，不负有向您支付款项的义务，另有特殊约定除外。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>4.经营者作为平台服务提供方，虽将尽力督促相关付款义务主体及时支付收益，但确无法保证相关款项必然得到给予，您对此充分理解并同意不因此追究经营者的任何责任。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>5.经营者有权根据经营需要随时对该等结算规则进行变更，您应持续、及时关注，了解具体的结算规则，如您不同意变更后的结算规则应立即停止使用，继续使用将视为同意并接受变更后结算规则。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>6.基于平台服务所产生的一切收益，由您自行按照国家有关法律规定履行纳税申报手续及缴纳相应税款，除有特殊约定外，经营者无义务为您代扣代缴相应税费且不对您未履行纳税义务的行为承担任何责任。</u></strong><span style=\"color: rgb(51, 51, 51);\">此外，您可根据实际情况，自行委托第三方代扣代缴税款（包括但不限于通过与第三方签署服务协议，受第三方指派使用平台所提供服务等方式），该等情况下，您理解经营者可按照您的要求将相应收益付至第三方平台，由第三方平台与您实际完成结算并协助您履行相应的纳税义务，若该等服务协议与平台规则存在冲突的，则以该等服务协议约定为准，另有特殊说明除外。</span></p><p><span style=\"color: rgb(51, 51, 51);\">4.2【费用】您理解并同意，梵花视频平台提供的服务中可能存在收费服务，包括但不限于如下服务（具体以平台适时对外公布的信息为准）：</span></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>4.2.1若您需获得平台为会员提供的服务，您可申请开通平台会员并按照页面提示公布的计费标准和方式支付相关费用。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>4.2.2若您需获得平台展示的付费内容（包括但不限于付费课程等），您可根据自身需求予以选择并购买。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>4.2.3您理解并同意，您所支付的费用代表您获取相关服务及/或信息，经营者所付出的整体成本和努力，除法律法规另有强制性规定外，一旦您实际支付费用后，将不获得为开通及/或获取该服务及/或内容而支付费用的退还。您亦认可，不以任何理由（包括但不限于未实际使用等）要求退还部分或全部服务费用，梵花视频平台亦无义务满足您的退款要求。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>4.2.4您理解并同意，经营者可能会根据实际需要对收费服务的收费标准、方式等进行更改，或调整全部或部分收费服务的具体内容、对应权益等。前述修改、变更或开始收费前，经营者将在相应服务页面进行通知或公告。如您拒绝付费，那么梵花视频平台有权停止为您提供该等收费功能；如您继续使用相应功能的，您应按届时有效的收费政策付费。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">五、第三方服务及/或内容说明</strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>5.1您理解并同意，平台服务可能包含指向第三方网络站点以及第三方内容的链接，经营者并不监督第三方服务及/或内容，不对其拥有控制权，也不对第三方服务提供保证或担保，更不承担责任。您应当自行审慎判断第三方服务及/或内容的合法性、准确性、有效性、安全性并承担使用风险和后果。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">5.2您理解并知悉，您利用该等第三方提供的软件及相关服务时，除需遵守包含本协议在内的平台规则外，还可能需要同意并遵守第三方的协议、相关规则。</span><strong style=\"color: rgb(51, 51, 51);\"><u>如因第三方软件及相关服务产生的争议、损失或损害，由您自行与第三方解决，梵花视频平台并不就此而对您或任何第三方承担任何责任。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>5.3为持续改善用户体验，您理解梵花视频平台所包含的第三方服务及/或内容可能出现新增或减少，新增的第三方服务及/或内容同样适用本条约定。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">六、用户行为规范</strong></p><p><span style=\"color: rgb(51, 51, 51);\">6.1您理解并同意，在正常使用平台服务前，您需要自行购买智能设备、手机及其他与接入互联网或移动网有关的装置并承担所需的费用（如为接入互联网而支付的上网费、为使用移动网而支付的手机费）。同时，您知晓并同意您在使用梵花视频平台及相关服务时会耗用您的设备、带宽、流量等资源。</span></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>6.2除非法律允许或经营者书面许可，您不得存在如下任一行为：</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（1）不得参与与平台业务有竞争性的活动，或为第三方谋取与经营者有竞争性的利益（如：不得利用平台资源背书做其他竞品平台，也不得直接或间接向其他平台用户（包括但不限于其团队用户等）推荐非平台展示的推广素材等）；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（2）不得招揽、劝诱、试图劝诱或者帮助他人劝诱其他平台用户、经营者的合作方、员工等参与与平台业务有竞争性的活动；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（3）不得夸大宣传平台功能、服务，作引人误解的宣传、表述等；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（4）不得利用平台谋取不正当利益，如：利用平台名义进行盈利性培训；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（5）不得采用刷单/刷量等作弊方式增加推广数据，实施恶意扰乱推广秩序行为、虚假广告宣传行为、不正当竞争行为、侵害消费者权益行为，以谋取额外收益等不正当利益；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（6）不得以任何方式明示、暗示或诱导其他平台用户（包括但不限于其团队用户等）要求经营者及/或其关联公司承担责任；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（7）不得背离平台规则（包括本协议及在平台内已经发布及后续发布的全部规则、实施细则、公告等为有效维持平台运行而制定的文件）进行推广活动，如：采用暴力营销等易给平台造成不良影响的方式推广等；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（8）不得从事任何有损经营者及/或其关联公司声誉、形象和经济利益的活动。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>6.3未经经营者书面许可，您不得自行或授权、允许、协助任何第三人对平台及其展示的信息内容进行如下行为：</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（1）未经许可，擅自复制、读取、采用平台中展示的信息内容，用于包括但不限于宣传、增加浏览量等商业用途；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（2）擅自编辑、整理、编排平台展示的信息内容后，在平台的源页面以外的渠道进行展示；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（3）其他非法获取或使用平台展示的信息内容的行为。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>6.4您理解并知悉：如您在中华人民共和国大陆地区以外的国家或地区使用平台服务，受限于第三方内容版权、技能服务提供的地域限制以及跨境数据传输相关监管政策要求，您有可能不能享受正常的平台服务。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>七、知识产权</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>7.1本协议未授予您对平台的包括但不限于著作、图片、数据、资讯、资料、网站架构、网站画面的排版、网页设计、商标、标识、logo、域名、专利的任何权益，包括所有权或其他许可（本协议所述除外），除为履行本协议之目的，您未经经营者事先书面同意，不得擅自超出范围使用或授权他人使用。本协议终止后，本条款仍继续有效。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>7.2未经经营者同意，您在任何情况下均不得私自使用平台所涉的“梵花视频”等标识、文字、图形或其组合、或其他具有显著品牌特征等（以下统称为“标识”）。未经经营者事先书面同意，您不得将本条款前述标识以单独或结合任何方式展示、使用或申请注册商标、进行域名注册等，也不得实施向他人明示或暗示有权展示、使用、或其他有权处理该些标识的行为。由于您违反本协议使用公司上述商标、标识等给经营者或他人造成损失的，由您承担全部法律责任。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>7.3您对梵花视频平台上的推广信息不享有任何知识产权，您亦不得对外宣称对推广信息享有任何知识产权，且若因您对推广信息进行增添、删减、修改、二次创作等操作致使您与推广信息原权利人或第三方产生纠纷的，由您自行承担全部法律责任。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>7.4一旦您接受本协议，即表明您同意将在梵花视频平台或关联平台上发表的任何形式的信息（包括但不限于文字、图片、视频等内容）免费授权经营者及/或其关联公司在全球范围内不可撤销的、永久地、可再许可或转让的非独占使用权许可，梵花视频平台有权根据该等许可展示、推广及其他不为我国法律所禁止的方式使用上述内容和作品，您同意授权梵花视频平台及其经营者发布、改编、汇编您提供的素材，同时有权在内容与作品上增加经营者相关水印或标识。当然，这并不意味着您放弃对内容的所有权，您只是授予平台和其他用户使用该等内容的权限。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">八、违约及处理</strong></p><p><span style=\"color: rgb(51, 51, 51);\">8.1发生如下情形之一的，视为违约：</span></p><p><span style=\"color: rgb(51, 51, 51);\">（1）使用平台服务时违反有关法律法规规定的；</span></p><p><span style=\"color: rgb(51, 51, 51);\">（2）违反本协议或平台发布的规则的。</span></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>8.2如果经营者发现或收到他人举报您有违约行为的，经营者有权根据自身的判断，不经通知直接采取以下一项或多项处理措施：</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（1）对相关内容进行删除、屏蔽；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（2）书面警告，以短信、邮件、站内信等一种或多种形式通知您要求您限期纠正该等行为；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（3）对您的会员权益进行降级；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（4）限制、暂停、终止您使用部分或全部服务；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（5）冻结或封禁账户，不予结算违约行为所获得的全部收益；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（6）不退还任何的服务费用，且不进行任何的赔偿或补偿；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（7）在平台上公示违约的用户信息；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（8）对涉嫌违反法律法规、涉嫌违法犯罪的行为保存记录，并有权依法向有关主管部门报告、配合有关主管部门调查、向公安机关报案等；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（9）其他根据平台规则可采取的措施。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">8.3如若经营者就前述违规行为向您发出通知的，</span><strong style=\"color: rgb(51, 51, 51);\"><u>您有责任在经营者要求时间内进行说明并出具证据材料。如若您对经营者的处理措施有异议的，应在知悉该等处理措施之日起3日内书面提出异议及证明材料，未按时提供的，视为您接受相应的处理措施。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>8.4如因您的行为导致第三人向经营者及/或其关联公司主张权利的，经营者及/或其关联公司仅以普通人的认知进行判断，并有权作出是否向第三人进行赔付等决定，您对此无任何异议。若您对第三人主张内容有异议的，您可与第三人自行协商解决，经营者及/或其关联公司不对其作出的任何赔付决定承担任何责任，除存在法定过错外。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>8.5如您的行为使经营者及/或其关联公司遭受损失的（包括但不限于被相关行政机关进行行政处罚或被第三人主张赔偿的），您应赔偿梵花视频及/或其关联公司的全部损失（包括但不限于直接经济损失、商誉损失及对外支付的赔偿金、和解款、律师费、诉讼费等经济损失），经营者及/或其关联公司有权从您的押金、保证金（若有）、或您的平台账户中直接划扣前述款项予以支付，不足部分可继续追偿。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">九、声明</strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>9.1本协议/或平台发布的一切公告等不包含任何可能理解为双方之间设立一种代理关系的内容。您无权代表经营者对外缔结合同。您不得以梵花视频平台的名义及/或利用平台内展示的信息开展任何与约定推广活动无关的活动或者从事违法犯罪活动，否则一切后果由您自行承担，同时梵花视频平台保留追究您相关法律责任的权利。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>9.2您与梵花视频平台之间不存在任何包括但不限于劳动/劳务合同关系或其他类似的劳动人事法律关系、雇佣关系、赞助关系、合伙关系、代理关系、代言关系等，您不得对外宣传与梵花视频平台存在上述关系。梵花视频平台无需也无责任承担您在使用平台服务过程中产生的成本、您的社会保险、福利和医疗保险费用等。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>9.3请注意，与推广信息有关的纠纷请寻求需求方解决（包括但不限于认为推广信息、推广内容侵权，或者演员/配音演员/剧集工作人员等角色就费用存在纠纷等），与平台经营者无关，平台经营者无需就此承担责任。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">十、免责情形与责任限制</strong></p><p><span style=\"color: rgb(51, 51, 51);\">10.1平台致力于不断提高服务的产品与技术质量、优质的用户体验以及系统的安全稳定。</span></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>10.2经营者将按“现状”和按“可得到”的状态提供平台（网络）服务。在法律所允许的最大限度内，经营者对平台服务不做任何明示或暗示的保证或承诺，包括但不限于</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（1）关于适用性、特定用途适用性、准确性和无侵权行为的任何保证或条件；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（2）在服务使用过程或行业惯例中产生的任何保证或条件；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（3）在访问或使用第三方服务时不受干扰、没有错误的任何保证或条件；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（4）在任何书面或者口头的帮助、答疑过程中所作出的陈述、指导或者展示。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">10.3您理解并同意，梵花视频平台及相关服务可能受到多种因素的影响，经营者不保证（包括但不限于），也不对于下述任一情况而导致的损害承担赔偿责任：</span></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（1）受到计算机病毒、木马或其他恶意程序、黑客攻击的破坏；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（2）可能存在的网络通讯故障、系统停机维护；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（3）用户操作不当；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（4）当前行业技术普遍水平所无法避免的，使用有瑕疵或未能使用平台服务；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（5）其他非经营者过错、经营者无法控制或合理预见的情形。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>10.4经营者仅承担平台（网络）服务，平台及相关服务中包含经营者以各种合法方式获取的第三方提供信息或信息内容链接，可能存在风险或瑕疵。经营者无法逐一审查，不具备也不负有审查义务，您需自行审慎判断，对您因此遭受的财产、信息等其他有形或无形损失，经营者不承担直接、间接、附带、补充或惩罚性的赔偿责任。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">10.5除法律有明确规定或有生效裁判外，</span><strong style=\"color: rgb(51, 51, 51);\"><u>经营者不对任何间接性、后果性、惩罚性、偶然性、特殊性或刑罚性的损害赔偿。您理解并同意，经营者对您承担的全部责任，无论因何原因或何种行为方式，始终不超过您因使用梵花视频平台服务期间而支付给经营者的费用。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">十一、协议的终止</strong></p><p><span style=\"color: rgb(51, 51, 51);\">11.1如您需要注销自己的平台账户的，应先向经营者申请注销，经营者审核同意后方可注销平台账户。该账户的注销，即表明梵花视频平台与您之间的协议终止。</span></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>11.2出现下列情况时，经营者可以本协议第十条所列的方式通知您终止本协议：</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（1）您违反本协议约定，经营者依据违约条款终止本协议；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（2）本协议变更时，您明示不愿接受新的变更协议的；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（3）您提供的注册数据中主要内容（身份信息、联系方式等）虚假的；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（4）您违反平台任一规则的；</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>（5）其他应当终止的情况。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>11.3本协议终止后，您仍应对协议存续期间您过往之行为承担相应违约或损害赔偿责任。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">十二、通知和送达</strong></p><p><span style=\"color: rgb(51, 51, 51);\">12.1所有发给您的通知都可通过页面公告、系统消息、站内信息或您预留的有效联系方式（包括您的电子邮件地址、联系电话、联系地址等）发送,</span><strong style=\"color: rgb(51, 51, 51);\"><u>以电子方式发出的通知（包括但不限于在页面公告、短信、电子邮件、系统消息以及站内信信息等），在发送成功后即视为送达；以纸质载体发出的通知，按照您提供的联系地址交邮后的第五个自然日即视为送达。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\"><u>12.2您理解并同意，相关行政机关、司法机关（包括但不限于法院等）有权采取本条第1款所述一种或多种方式向您送达法律文书（包括但不限于诉讼文书等），送达时间以上述送达方式中最先送达的为准。上述送达方式的适用范围包括一审、二审、再审、执行以及督促等在内的各个司法程序阶段。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">12.3您应保证预留的联系方式（包括您的电子邮件地址、联系电话、联系地址等）是正确的、有效的，</span><strong style=\"color: rgb(51, 51, 51);\"><u>如您的联系方式发生变化的，您有义务及时更新并保持可被联系的状态，若因您未尽及时更新有效联系方式之故，导致相关通知、文书等无法送达或及时送达，则自该通知、文书等发至您最后预留的地址、电话号码、邮箱等通讯账号之时，即视为已送达。</u></strong></p><p><strong style=\"color: rgb(51, 51, 51);\">十三、法律适用、管辖与其他</strong></p><p><span style=\"color: rgb(51, 51, 51);\">13.1本协议之订立、生效、解释、修订、补充、终止、执行与争议解决均适用中华人民共和国大陆地区法律；如法律无相关规定的，参照商业惯例及/或行业惯例。</span></p><p><span style=\"color: rgb(51, 51, 51);\">13.2因本协议产生的有关争议，由经营者与您协商解决。</span><strong style=\"color: rgb(51, 51, 51);\"><u>协商不成时，双方一致同意向公司住所地法院提起诉讼来解决。</u></strong></p><p><span style=\"color: rgb(51, 51, 51);\">13.3本协议任一条款被视为废止、无效或不可执行，该条应视为可分的且并不影响本协议其余条款的有效性及可执行性。</span></p><p>&nbsp;</p>', 'xieyi');
INSERT INTO `common_info` VALUES (173, '2024-04-25 16:31:23', NULL, '隐私协议', 155, '<p class=\"ql-align-center\"><span style=\"color: rgb(51, 51, 51);\" class=\"ql-size-huge\">短剧SDK隐私政策</span></p><p><strong style=\"color: rgb(34, 34, 34);\">发布日期：2024年4月26日</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">生效日期：2024年5月5日</strong></p><p><br></p><p><span style=\"color: rgb(34, 34, 34);\">作为短剧SDK产品/服务的提供方，苏州非梵空间网络科技有限公司及其关联方（以下简称“我们”）高度重视个人信息的保护。短剧SDK为开发者提供短剧播放器技术服务</span><strong style=\"color: rgb(34, 34, 34);\">，</strong><span style=\"color: rgb(34, 34, 34);\">本隐私声明所称之短剧SDK产品和/或服务，以下统称“本服务”。 在最终用户（以下简称“您”）使用开发者开发和/或运营的网站或应用软件（包括APP、小程序、网页等，以下简称“开发者应用”）时，如果开发者集成了本服务后，我们将通过开发者应用向您提供相关功能和服务，我们深知按照本隐私政策及法律法规的规定处理您的个人信息，保护您的个人信息及隐私安全。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">特别声明：</strong></p><p>1.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong style=\"color: rgb(34, 34, 34);\">本隐私政策不能替代开发者应用的隐私政策。 开发者应就其应用收集处理个人信息的情况向您披露隐私政策，以向您声明其如何收集、处理及保护您的个人信息。 如果您寻求行使个人信息主体权利，请与相应开发者进行联系。</strong></p><p>2.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong style=\"color: rgb(34, 34, 34);\">您通过开发者应用所使用的本服务，由开发者根据其应用所需自行选择配置，并可能因为您所使用的开发者应用版本不同而有所差异。如果开发者应用版本中不包括我们的某些功能或服务，则本隐私政策中涉及前述功能和服务及相关个人信息的处理内容将不适用。</strong></p><p>3.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong style=\"color: rgb(34, 34, 34);\">开发者在接入短剧SDK前应详细阅读并同意遵守本隐私政策、</strong><a href=\"https://www.csjplatform.com/terms/28122\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(34, 34, 34);\">《短剧SDK合规使用说明》</a><strong style=\"color: rgb(34, 34, 34);\">和</strong><a href=\"https://www.csjplatform.com/supportcenter/5452\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(34, 34, 34);\">《开发者个人信息保护合规指引》</a><strong style=\"color: rgb(34, 34, 34);\">内容，并根据合规说明内容进行相关合规配置。</strong></p><p>4.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong style=\"color: rgb(34, 34, 34);\">请开发者在接入短剧SDK前，务必仔细阅读本隐私政策，特别是以加粗提示的条款应重点阅读，并确认已经充分理解本隐私政策的内容，按照本隐私政策，作出认为适当的选择。同时，开发者应向您告知开发者应用集成短剧SDK的情况，包括短剧SDK名称、主体名称、处理个人信息种类及目的、本隐私政策链接等内容，以便于您了解相关内容并获取您的同意。一旦您接受或使用短剧SDK提供的服务，即表示您已充分理解并同意本政策。除非另有说明，如果开发者或您不同意本隐私政策或其更新，开发者或您应立即停止接入和/或使用本服务。</strong></p><p><span style=\"color: rgb(34, 34, 34);\">本《隐私政策》将帮助您了解以下内容：</span></p><p><span style=\"color: rgb(34, 34, 34);\">我们希望通过本《隐私政策》向您清晰、准确且完整地说明，您在使用集成了短剧SDK的开发者应用时，我们如何采集、处理和保护您的个人信息。</span></p><p><span style=\"color: rgb(34, 34, 34);\">一、我们如何收集和使用个人信息</span></p><p><span style=\"color: rgb(34, 34, 34);\">二、我们如何存储个人信息</span></p><p><span style=\"color: rgb(34, 34, 34);\">三、数据使用过程中涉及的合作方以及转移、公开个人信息</span></p><p><span style=\"color: rgb(34, 34, 34);\">四、我们如何保护个人信息安全</span></p><p><span style=\"color: rgb(34, 34, 34);\">五、个人信息管理</span></p><p><span style=\"color: rgb(34, 34, 34);\">六、未成年人保护条款</span></p><p><span style=\"color: rgb(34, 34, 34);\">七、隐私政策的查阅和修订</span></p><p><span style=\"color: rgb(34, 34, 34);\">八、联系我们</span></p><p><span style=\"color: rgb(34, 34, 34);\">九、其他</span></p><p><strong style=\"color: rgb(34, 34, 34);\">一、我们如何收集和使用个人信息</strong></p><p><span style=\"color: rgb(34, 34, 34);\">在您使用短剧SDK提供的服务过程中，我们将根据合法、正当、必要的原则收集信息。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">1、短剧SDK的功能介绍</strong></p><p><span style=\"color: rgb(34, 34, 34);\">（1）基本功能：短剧SDK的基本业务功能是为开发者提供短剧信息流技术服务，您可在开发者应用内观看相关短剧。</span></p><p><span style=\"color: rgb(34, 34, 34);\">（2）扩展功能：短剧SDK的扩展业务功能主要为提供个性化推荐服务和支付功能，关于扩展功能的配置方式详见</span><a href=\"https://www.csjplatform.com/terms/28122\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(34, 34, 34);\">《短剧SDK合规使用说明》</a><span style=\"color: rgb(34, 34, 34);\">。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">2、个人信息的采集</strong></p><p><span style=\"color: rgb(34, 34, 34);\">如您使用集成短剧SDK的开发者应用，短剧SDK会通过开发者应用采集下列信息并申请相应权限。开发者应向您告知短剧SDK名称、主体名称、处理个人信息种类及目的、隐私政策等内容，取得您的同意后方可使用短剧SDK开展相关业务功能。由于不同SDK版本采集的信息字段与是否可选可能存在一定差异，具体采集情况以您实际使用的开发者应用所接入的SDK版本为准。</span></p><p>  <strong style=\"color: rgb(34, 34, 34);\">SDK名称</strong></p><p> <strong style=\"color: rgb(34, 34, 34);\">合作方式</strong></p><p> <strong style=\"color: rgb(34, 34, 34);\">个人信息类型</strong></p><p> <strong style=\"color: rgb(34, 34, 34);\">个人信息字段</strong></p><p> <strong style=\"color: rgb(34, 34, 34);\">用途和目的</strong></p><p>   <span style=\"color: rgb(31, 35, 41);\">短剧SDK</span></p><p> <span style=\"color: rgb(31, 35, 41);\">短剧输出合作</span></p><p> <strong style=\"color: rgb(31, 35, 41);\">设备信息</strong></p><p> <strong style=\"color: rgb(31, 35, 41);\">必选信息</strong><span style=\"color: rgb(31, 35, 41);\">：</span></p><p> <span style=\"color: rgb(31, 35, 41);\">【双端】设备品牌、设备型号、操作系统、操作系统api版本（系统属性）、系统语言、系统时区、屏幕密度、屏幕分辨率、CPU信息</span></p><p> <span style=\"color: rgb(31, 35, 41);\">【仅iOS】IDFV</span></p><p> <strong style=\"color: rgb(31, 35, 41);\">可选信息</strong><span style=\"color: rgb(31, 35, 41);\">：</span></p><p> <span style=\"color: rgb(31, 35, 41);\">【仅Android】IMEI、IMSI、设备MAC地址、MEID、OAID、AndroidID、ICCID、硬件序列号build_serial</span></p><p> <span style=\"color: rgb(31, 35, 41);\">【仅iOS】IDFA</span></p><p> <span style=\"color: rgb(31, 35, 41);\">用于保障服务正常进行与安全风控，维护服务的安全稳定，以及向用户推送其可能感兴趣的内容。</span></p><p>   <strong style=\"color: rgb(31, 35, 41);\">网络信息</strong></p><p> <strong style=\"color: rgb(31, 35, 41);\">必选信息</strong><span style=\"color: rgb(31, 35, 41);\">：</span></p><p> <span style=\"color: rgb(31, 35, 41);\">【双端】IP地址、WiFi状态、运营商信息</span></p><p> <span style=\"color: rgb(31, 35, 41);\">查询网络状态，并在网络状态发生变化时提供相应的服务，保证服务的安全运行、运营质量和效率。</span></p><p>   <strong style=\"color: rgb(31, 35, 41);\">应用信息</strong></p><p> <strong style=\"color: rgb(31, 35, 41);\">必选信息</strong><span style=\"color: rgb(31, 35, 41);\">：</span></p><p> <span style=\"color: rgb(31, 35, 41);\">【双端】应用版本、应用程序包名</span></p><p> <span style=\"color: rgb(31, 35, 41);\">用于保障服务正常进行与安全风控，维护服务的安全稳定。</span></p><p>   <strong style=\"color: rgb(31, 35, 41);\">服务内容信息</strong></p><p> <strong style=\"color: rgb(31, 35, 41);\">必选信息</strong><span style=\"color: rgb(31, 35, 41);\">：</span></p><p> <span style=\"color: rgb(31, 35, 41);\">【双端】</span><strong style=\"color: rgb(31, 35, 41);\"><u>浏览记录</u></strong><span style=\"color: rgb(31, 35, 41);\">、搜索记录、点击记录、分享记录、下载记录、用户隐私设置、用户反馈。</span></p><p> <strong style=\"color: rgb(31, 35, 41);\">可选信息</strong><span style=\"color: rgb(31, 35, 41);\">：</span></p><p> <span style=\"color: rgb(31, 35, 41);\">【双端】用户模式、用户偏好设置</span></p><p> <span style=\"color: rgb(31, 35, 41);\">为开发者提供短剧视频播放器的技术服务，保障短剧视频播放服务的正常运行并为用户推荐可能感兴趣的短剧视频。</span></p><p>   <strong style=\"color: rgb(31, 35, 41);\">日志信息</strong></p><p> <strong style=\"color: rgb(31, 35, 41);\">必选信息</strong><span style=\"color: rgb(31, 35, 41);\">：</span></p><p> <span style=\"color: rgb(31, 35, 41);\">【双端】用户注册日志、登录日志、用户登出日志、用户操作/行为日志。</span></p><p> <span style=\"color: rgb(31, 35, 41);\">为开发者提供短剧视频播放器的技术服务，保障短剧视频播放服务的正常运行并为用户推荐可能感兴趣的短剧视频。</span></p><p>  <strong style=\"color: rgb(34, 34, 34);\"><u>特别提示您注意，我们不会要求您主动提交个人信息。我们采集的信息不能单独识别特定自然人的身份，并且基于本SDK的技术特性，其在运行过程客观上无法获取任何能够单独识别特定自然人身份的信息。</u></strong></p><p><strong style=\"color: rgb(34, 34, 34);\"><u>我们可能会对短剧SDK的功能和提供的服务有所调整变化，但请您知悉并了解，未经开发者主动集成或同意，我们不会自行变更开发者已设置的各项业务功能及个人信息配置状态。根据开发者所集成的SDK版本不同，本服务功能及个人信息处理情况存在差异。当您使用集成了本服务的开发者应用时，建议您仔细阅读并理解开发者所提供的隐私政策，以便做出适当的选择。 </u></strong></p><p><strong style=\"color: rgb(34, 34, 34);\">3、权限申请</strong></p><p><span style=\"color: rgb(34, 34, 34);\">您可以查阅</span><a href=\"https://www.csjplatform.com/terms/28121\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(34, 34, 34);\">《短剧SDK应用权限申请与使用情况说明》</a><span style=\"color: rgb(34, 34, 34);\">，以了解短剧SDK申请和使用系统权限的情况。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">4、提供个性化推荐服务</strong></p><p><span style=\"color: rgb(34, 34, 34);\">为了展示、推荐相关性更高的信息，提供更契合您的服务，短剧SDK会收集、使用您的个人信息并通过计算机算法模型自动计算、预测您的偏好，以匹配您可能感兴趣的信息或服务，以下我们将详细说明该个性化推荐服务的运行机制以及用户实现控制的方式。</span></p><p><span style=\"color: rgb(34, 34, 34);\">a) 个性化推荐服务的适用范围：我们提供个性化推荐服务的范围包括但不限于展示个性化推荐的短剧等内容。</span></p><p><span style=\"color: rgb(34, 34, 34);\">b) 个性化推荐服务收集的字段：为了提供个性化的服务，我们可能会收集、使用您的设备信息（包括硬件型号、操作系统版本、设备标识符（IMEI、IMSI、MAC地址、OAID、AndroidID、IDFA、IDFV）、您在使用SDK时的操作信息（包括搜索、点击、浏览、分享等互动交流的操作相关记录），以及经您授权由其他合作方提供的其他信息。系统会对上述信息进行自动分析和计算，预测您的偏好特征，根据计算结果提供个性化推荐内容和服务。</span></p><p><span style=\"color: rgb(34, 34, 34);\">c) 个性化推荐服务的训练和干预：我们会根据您在使用过程中的浏览相关行为对推荐模型进行实时训练和反馈，不断调整优化推荐结果。为了满足您的多元需求，避免同类型内容过于集中，我们会综合运用多样化技术对内容进行自动化处理，更好地提供优质内容和服务。</span></p><p><span style=\"color: rgb(34, 34, 34);\">请您理解，由于我们与您并无直接交互对话界面，如您希望退出个性化推荐功能，您可以在开发者应用内关闭。我们已向开发者提供退出个性化推荐服务的接口，并要求开发者确保在您选择关闭时调用该接口，以确保个性化推荐服务真实退出。</span></p><p><span style=\"color: rgb(34, 34, 34);\">请开发者注意，如果您向最终用户提供短剧SDK提供的个性化推荐功能，您应在APP隐私政策中向最终用户告知相应的功能，详细查阅</span><a href=\"https://www.csjplatform.com/terms/28122\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(34, 34, 34);\">《短剧SDK合规使用说明》</a><span style=\"color: rgb(34, 34, 34);\">并根据我们提供配置方式进行相应配置，确保在最终用户选择关闭个性化推荐功能时调用相关接口，停止向最终用户继续提供个性化推荐服务。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">5、其他用途</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">我们可能会对您提供或我们收集的信息进行去标识化地研究、统计分析和预测，用于改善和提升短剧SDK服务，提供产品或技术服务支撑。</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">我们将信息用于本政策未载明的其他用途，或者将基于特定目的收集而来的信息用于其他目的时，会事先征求您的同意。</strong></p><p><span style=\"color: rgb(34, 34, 34);\">我们还可能为了履行服务内容而收集您的其他信息，例如您与我们的客户服务团队联系时提供的相关信息。</span></p><p><span style=\"color: rgb(34, 34, 34);\">与此同时，为提高您使用短剧SDK服务的安全性，更准确地预防钓鱼网站欺诈和木马病毒，我们可能会通过了解一些您的网络使用习惯、您常用的软件信息等手段来判断您在使用短剧SDK服务期间的风险，并可能会记录一些我们认为有风险的URL。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">6、征得授权同意的例外</strong></p><p><span style=\"color: rgb(34, 34, 34);\">请您理解，在下列情形中，根据法律法规及相关国家标准，我们收集和使用您的个人信息不必事先征得您的授权同意：</span></p><p><span style=\"color: rgb(34, 34, 34);\">a.与我们履行法律法规规定的义务相关的；</span></p><p><span style=\"color: rgb(34, 34, 34);\">b.与国家安全、国防安全直接相关的；</span></p><p><span style=\"color: rgb(34, 34, 34);\">c.与公共安全、公共卫生、重大公共利益直接相关的；</span></p><p><span style=\"color: rgb(34, 34, 34);\">d.与刑事侦查、起诉、审判和判决执行等直接相关的；</span></p><p><span style=\"color: rgb(34, 34, 34);\">e.出于维护您或他人的生命、财产等重大合法权益但又很难得到本人授权同意的；</span></p><p><span style=\"color: rgb(34, 34, 34);\">f.您自行向社会公众公开的个人信息；</span></p><p><span style=\"color: rgb(34, 34, 34);\">g.根据个人信息主体要求签订和履行合同所必需的；</span></p><p><span style=\"color: rgb(34, 34, 34);\">h.从合法公开披露的信息中收集的您的个人信息的，如合法的新闻报道、政府信息公开等渠道；</span></p><p><span style=\"color: rgb(34, 34, 34);\">i.用于维护软件及相关服务的安全稳定运行所必需的，例如发现、处置软件及相关服务的故障；</span></p><p><span style=\"color: rgb(34, 34, 34);\">j.为开展合法的新闻报道所必需的；</span></p><p><span style=\"color: rgb(34, 34, 34);\">k.为学术研究机构，基于公共利益开展统计或学术研究所必要，且对外提供学术研究或描述的结果时，对结果中所包含的个人信息进行去标识化处理的；</span></p><p><span style=\"color: rgb(34, 34, 34);\">l.法律法规规定的其他情形。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">特别说明：根据法律规定，如信息无法单独或结合其他信息识别到特定个人的，其不属于个人信息。当信息可以单独或结合其他信息识别到您时，或我们把没有与任何特定个人建立联系的数据与您的个人信息结合使用时，我们会将其作为您的个人信息，按照本隐私政策进行处理与保护。</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">二、我们如何存储个人信息</strong></p><p><span style=\"color: rgb(34, 34, 34);\">我们非常重视信息安全，并遵循严格的安全标准，使用符合业界标准的安全保护措施保护您提供的信息，采用各种合理的技术、运营和管理方面的安全措施来保护我们所采集的信息的安全。防止信息遭到未经授权的访问、公开披露、使用、修改、损坏或丢失。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">（一）信息存储的地点</strong></p><p><span style=\"color: rgb(34, 34, 34);\">我们依照法律法规的规定，将在中华人民共和国境内运营过程中收集和产生的个人信息存储于境内。我们不会将上述信息传输至境外，如果开发者将您的个人信息传输至境外，开发者应履行数据出境相关的合规义务，向最终用户告知境外接收方的名称或者姓名、联系方式、处理目的、处理方式、个人信息的种类以及个人向境外接收方行使本法规定权利的方式和程序以及其他法律要求事项, 并应征得最终用户的单独同意，履行法律法规规定的评估程序，并满足法律法规所规定的其他条件。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">（二）存储期限</strong></p><p><span style=\"color: rgb(34, 34, 34);\">我们仅在为开发者提供服务之目的所必需的期间内保留您的信息。超出与开发者约定的存储期限后，或者接到开发者的相应指令后，我们将对您的个人信息进行删除或匿名化处理，但法律法规另有规定的除外。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">三、数据使用过程中涉及的合作方以及转移、公开个人信息</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">（一）数据使用过程中涉及的合作方</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">1.基本原则</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">a.合法原则：</strong><span style=\"color: rgb(34, 34, 34);\">与合作方合作过程中涉及数据使用活动的，必须具有合法目的、符合法定的合法性基础。如果合作方使用信息不再符合合法原则，则其不应再使用您的个人信息，或在获得相应合法性基础后再行使用。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">b.正当与最小必要原则：</strong><span style=\"color: rgb(34, 34, 34);\">数据使用必须具有正当目的，且应以达成目的必要为限。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">c.安全审慎原则：</strong><span style=\"color: rgb(34, 34, 34);\"> 我们将审慎评估合作方使用数据的目的，对这些合作方的安全保障能力进行综合评估，并要求其遵循合作法律协议。我们会对合作方获取信息的软件工具开发包（SDK）、应用程序接口（API）进行严格的安全监测，以保护数据安全。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">2.委托处理</strong></p><p><span style=\"color: rgb(34, 34, 34);\">对于受托处理个人信息的场景，我们会与委托方根据法律规定签署相关协议并约定各自的权利和义务，确保在使用相关个人信息的过程中遵守法律的相关规定、保护数据安全。非经开发者同意，我们不会对您的信息进行转委托处理。我们仅按照开发者的委托和指示处理您的个人信息，并接受开发者与您对个人信息使用活动的监督。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">3.共同处理</strong></p><p><span style=\"color: rgb(34, 34, 34);\">对于共同处理个人信息的场景，我们会与合作方根据法律规定签署相关协议并约定各自的权利和义务，确保在使用相关个人信息的过程中遵守法律的相关规定，保护数据安全。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">4. 合作方的范围</strong></p><p><span style=\"color: rgb(34, 34, 34);\">若具体功能和场景中涉及由我们的关联方、第三方提供服务，则合作方范围包括我们的关联方与第三方。短剧SDK为实现特定功能需要，可能会接入由合作方提供的SDK以共同为您服务，短剧SDK接入的合作方及其个人信息处理情况请详细查阅</span><a href=\"https://www.csjplatform.com/terms/28120\" rel=\"noopener noreferrer\" target=\"_blank\" style=\"color: rgb(34, 34, 34);\">《短剧SDK第三方信息共享清单》</a><span style=\"color: rgb(34, 34, 34);\">。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">（二）合作的场景</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">1、为实现相关功能或服务与关联方共享</strong></p><p><span style=\"color: rgb(34, 34, 34);\">当您在使用短剧SDK及其关联方产品服务期间，为保障您在我们及关联方提供的产品间所接受服务的一致性，并方便统一管理您的信息，我们会将您去标识化后的个人信息与这些关联方共享。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">2、我们可能会与广告合作方共享信息</strong></p><p><span style=\"color: rgb(34, 34, 34);\">我们可能与委托我们进行推广和广告投放的合作伙伴和其他合作伙伴如广告主、代理商、广告监测服务商等（合称“广告合作方”）共享分析去标识化或匿名化处理的设备信息、统计信息或标签信息，这些信息难以或无法与您的真实身份相关联。这些信息将帮助我们分析、衡量、优化广告和相关服务的有效性，提升广告有效触达率。</span></p><p><span style=\"color: rgb(34, 34, 34);\">a) 广告推送与投放：进行推送/推广和广告投放或提供相关服务的广告合作方可能需要使用您的设备信息、网络信息、渠道信息以及标签信息；与广告主合作的广告推送/推广、投放/分析服务的广告合作方可能需要使用前述信息，以实现广告投放、优化与提升广告有效触达率。</span></p><p><span style=\"color: rgb(34, 34, 34);\">b) 广告统计分析：提供广告统计分析服务的广告合作方可能需要使用您的设备、网络、广告点击、浏览、展示以及广告转化数据等信息用于分析、衡量、优化广告和相关服务的有效性。</span></p><p><span style=\"color: rgb(34, 34, 34);\">c) 广告合作方对信息的使用：广告合作方可能将上述信息与其合法获取的其他数据相结合，以优化广告投放效果，我们会要求其对信息的使用遵循合法、正当、必要原则，保障您的合法权利不受侵犯。</span></p><p><span style=\"color: rgb(34, 34, 34);\">d) 广告留资信息：您在广告中主动填写、提交的联系方式、地址等相关信息，可能被广告主或其委托的合作方收集并使用。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">3、实现安全与统计分析</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">3.1 保障使用安全：</strong><span style=\"color: rgb(34, 34, 34);\">我们非常重视产品和服务的安全性，为保障使用您的正当合法权益免受不法侵害，我们和合作方可能会使用必要的设备、帐号及日志信息。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">3.2 分析产品情况：</strong><span style=\"color: rgb(34, 34, 34);\">为分析短剧SDK产品和服务的稳定性，提供分析服务的合作方可能需要使用服务情况（崩溃、闪退记录）、设备标识信息、应用总体安装使用情况等信息。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">3.3</strong><span style=\"color: rgb(34, 34, 34);\"> </span><strong style=\"color: rgb(34, 34, 34);\">学术科研</strong><span style=\"color: rgb(34, 34, 34);\">：为提升相关领域的科研能力，促进科技发展水平，我们在确保数据安全与目的正当的前提下，可能会与合作的科研院所、高校等机构使用去标识化或匿名化的数据。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">（三）运营主体变更</strong></p><p><span style=\"color: rgb(34, 34, 34);\">随着业务的持续发展，我们将有可能进行合并、收购、资产转让，您的个人信息有可能因此而被转移。在发生前述变更时，我们将按照法律法规及不低于本隐私政策所载明的安全标准要求继受方保护您的个人信息，继受方变更原先的处理目的、处理方式的，我们将要求继受方重新征得您的授权同意。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">（四）停止运营</strong></p><p><span style=\"color: rgb(34, 34, 34);\">如果我们停止运营短剧平台的产品或服务，将及时停止继续收集您的个人信息。我们会逐一送达通知或以公告的形式向您发送停止运营的告知，并对我们所持有的与已关停的产品或服务相关的个人信息进行删除或匿名化处理。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">（五）公开</strong></p><p><span style=\"color: rgb(34, 34, 34);\">我们不会主动公开您未自行公开的信息，除非遵循国家法律法规规定或者获得您的同意。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">（六）依法豁免征得授权同意的情形</strong></p><p><span style=\"color: rgb(34, 34, 34);\">请理解，在下列情形中，根据法律法规及国家标准，合作方使用或我们转移、公开您的个人信息无需征得您的授权同意：</span></p><p><span style=\"color: rgb(34, 34, 34);\">a) 根据您的要求订立或履行合同所必需；</span></p><p><span style=\"color: rgb(34, 34, 34);\">b) 为履行法定职责或者法定义务所必需，例如与国家安全、国防安全、与刑事侦查、起诉、审判和判决执行等直接相关的法定职责或者法定义务；</span></p><p><span style=\"color: rgb(34, 34, 34);\">c) 为应对突发公共卫生事件，或者紧急情况下为保护自然人的生命健康和财产安全所必需；</span></p><p><span style=\"color: rgb(34, 34, 34);\">d) 为公共利益实施新闻报道、舆论监督等行为，在合理的范围内处理个人信息；</span></p><p><span style=\"color: rgb(34, 34, 34);\">e) 在合理的范围内处理您自行公开的个人信息，或者其他已经合法公开的个人信息（如合法的新闻报道、政府信息公开等渠道合法公开的个人信息）；</span></p><p><span style=\"color: rgb(34, 34, 34);\">f) 法律法规规定的其他情形。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">四、管理个人信息</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">（一）用户权利行使</strong></p><p><span style=\"color: rgb(34, 34, 34);\">我们非常重视您对个人信息管理的权利，包括查阅、复制、更正、补充、删除、撤回授权同意等权利，我们将尽全力帮助您管理您的个人信息，以使您有能力保障自身的隐私和信息安全。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">您作为最终用户，请您知悉：由于您不是我们的直接用户，与我们并无直接的交互对话界面，为保障您的权利实现，我们已要求集成我方服务的开发者承诺，应为您提供便于操作的用户权利实现方式。请您知悉并理解，因开发者独立开发和运营其应用并与您直接发生交互，我们无法控制或全面掌握开发者应用中交互界面设计及其对个人信息权益的响应情况。</strong></p><p><span style=\"color: rgb(34, 34, 34);\">作为直接面向最终用户提供应用程序服务的开发者，在集成短剧SDK后为最终用户提供服务时：开发者应根据使用的开发者平台提供的功能设置，为最终用户提供并明确其个人信息管理的路径，并及时响应最终用户个人信息管理请求。</span></p><p><span style=\"color: rgb(34, 34, 34);\">在开发者集成使用本服务过程中，如果最终用户提出管理其个人信息的请求，且开发者已确定该等请求涉及到本SDK产品处理的个人信息、需要我们协助处理时，开发者应当及时通过本隐私政策联系方式联系我们，并附上必要的最终用户请求的书面证明材料。我们将及时核验相关材料，并按照相关法律法规及本隐私政策， 为开发者响应最终用户的行权请求提供相应的支持与配合。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">（二）停止运营</strong></p><p><span style=\"color: rgb(34, 34, 34);\">如果我们停止运营短剧SDK的服务，将及时停止继续收集您的个人信息。我们会向开发者发送通知并要求开发者以公告或其他适当的方式</span></p><p><strong style=\"color: rgb(34, 34, 34);\">五、我们如何保护个人信息安全</strong></p><p><span style=\"color: rgb(34, 34, 34);\">我们非常重视用户信息的安全，将努力采取合理的安全措施（包括技术方面和管理方面）来保护您的信息，防止您提供的信息被不当使用或未经授权的情况下被访问、公开披露、使用、修改、损坏、丢失或泄漏。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">（一）安全技术措施</strong></p><p><span style=\"color: rgb(34, 34, 34);\">我们会使用不低于行业通行的加密技术、匿名化处理等合理可行的手段保护您的信息，并使用安全保护机制防止您的信息遭到恶意攻击。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">（二）安全管理制度</strong></p><p><span style=\"color: rgb(34, 34, 34);\">我们会建立专门的安全部门、安全管理制度、数据安全流程保障您的信息安全。我们采取严格的数据使用和访问制度，确保只有授权人员才可访问您的信息，并适时对数据和技术进行安全审计。</span></p><p><span style=\"color: rgb(34, 34, 34);\">我们会制定应急处理预案，并在发生用户信息安全事件时立即启动应急预案，努力阻止该等安全事件的影响和后果扩大。一旦发生用户信息安全事件（泄露、丢失等）后，我们将按照法律法规的要求，及时向您告知：安全事件的基本情况和可能的影响、我们已经采取或将要采取的处置措施、您可自主防范和降低风险的建议、对您的补救措施等。</span></p><p><span style=\"color: rgb(34, 34, 34);\">我们将及时将事件相关情况以推送通知、邮件、信函、短信等形式告知您，难以逐一告知时，我们会采取合理、有效的方式发布公告。同时，我们还将按照相关监管部门要求，上报用户信息安全事件的处置情况。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">（三）安全提示</strong></p><p><span style=\"color: rgb(34, 34, 34);\">尽管已经采取了上述合理有效措施，并已经遵守了相关法律规定要求的标准，但请您理解，由于技术的限制以及可能存在的各种恶意手段，在互联网行业，即便竭尽所能加强安全措施，也不可能始终保证信息百分之百的安全，我们将尽力确保您提供给我们的信息的安全性。您知悉并理解，您接入我们的服务所用的系统和通讯网络，有可能因我们可控范围外的因素而出现问题。因此，我们强烈建议您采取积极措施保护用户信息的安全，包括但不限于使用复杂密码、定期修改密码、不将自己的账号密码等信息透露给他人。</span></p><p><span style=\"color: rgb(34, 34, 34);\">我们谨此特别提醒您，本隐私政策提供的用户信息保护措施仅适用于本服务。一旦您离开本服务相关页面，浏览或使用其他网站、产品、服务及内容资源，我们即没有能力及义务保护您在本服务之外的软件、网站提交的任何信息，无论您登录、浏览或使用上述软件、网站是否基于本服务的链接或引导。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">六、未成年人保护条款</strong></p><p><span style=\"color: rgb(34, 34, 34);\">如开发者应用涉及处理不满十四周岁未成年人的个人信息，开发者应当向不满十四周岁未成年人的监护人详细告知处理未成年人个人信息的情况，并就该等处理事项获得未成年人监护人的授权同意，同时开发者还应当针对不满十四周岁未成年人制定专门的个人信息处理规则。</span></p><p><span style=\"color: rgb(34, 34, 34);\">若您是不满十四周岁未成年人的监护人，如您发现存在未经您的授权而收集不满十四周岁未成年人的个人信息，或希望查阅、复制、更正、补充、删除、撤回同意所处理的不满十四周岁未成年人的个人信息，您可以联系SDK所接入的应用开发者，或通过本隐私政策中的联系方式与我们联系，我们将根据法律规定及时进行处理。如我们发现SDK所接入的应用开发者未获得未成年人监护人的授权同意而向我们传输不满十四周岁未成年人的个人信息，我们将主动删除。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">七、隐私政策的查阅和修订</strong></p><p><span style=\"color: rgb(34, 34, 34);\">（一）为了提供更好的服务，短剧SDK及相关服务将不时更新与变化，我们会适时对本隐私政策进行修订，这些修订构成本隐私政策的一部分。未经您明确同意，我们不会削减您依据当前生效的隐私政策所应享受的权利。</span></p><p><span style=\"color: rgb(34, 34, 34);\">（二）本隐私政策更新后，我们会在官网隐私政策公示页面及其他相关页面公布更新版本，以便开发者与您及时了解本隐私政策的最新版本。我们会向开发者告知本隐私政策更新的内容，由开发者向您告知并取得您的授权同意。</span></p><p><strong style=\"color: rgb(34, 34, 34);\">八、联系我们</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">如果您对个人信息保护及本隐私政策的内容有任何疑问、意见或建议，您可选择通过如下方式联系我们：</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">a）您可以将问题发送邮件至 fanhuaverse@163.com</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">我们将尽快审核您的问题或建议，并在十五个工作日内予以回复。</strong></p><p><strong style=\"color: rgb(34, 34, 34);\">九、其他</strong></p><p><span style=\"color: rgb(34, 34, 34);\">本政策适用于短剧SDK。 需要特别说明的是， 本政策不适用于其他第三方向您提供的服务，也不适用于我们提供的已另行独立设置隐私权政策的SDK产品或服务。</span></p><p><span style=\"color: rgb(34, 34, 34);\">请您了解，本政策中所述的短剧SDK及相关服务可能会根据您所使用的设备型号、系统版本、软件应用程序版本、移动客户端等因素而有所不同。最终处理个人信息的情况以您使用的开发者应用所实际集成的短剧SDK版本为准。</span></p><p>&nbsp;</p>', 'xieyi');
INSERT INTO `common_info` VALUES (186, '2020-07-27 15:17', NULL, '云购支付宝商户号', 168, '', 'zhifubao');
INSERT INTO `common_info` VALUES (187, '2020-07-27 15:17', NULL, '云购支付宝秘钥', 169, '', 'zhifubao');
INSERT INTO `common_info` VALUES (200, '2020-11-04 10:31:40', NULL, '支付宝证书地址', 200, '', 'zhifubao');
INSERT INTO `common_info` VALUES (201, '2020-11-04 10:31:40', NULL, '支付宝方式 1证书 2秘钥 3云购os', 201, '2', 'zhifubao');
INSERT INTO `common_info` VALUES (202, '2024-04-16 14:23:37', NULL, '客服配置  1  二维码客服 2 微信公众号客服  3电话客服 4微信公众号', 202, '1', 'xitong');
INSERT INTO `common_info` VALUES (203, '2020-11-04 10:31:40', NULL, '微信客服appid  ', 203, '2', 'xitong');
INSERT INTO `common_info` VALUES (204, '2020-11-04 10:31:40', NULL, '微信客服链接', 204, '2', 'xitong');
INSERT INTO `common_info` VALUES (205, '2023-03-08 17:25:27', NULL, '客服二维码', 205, 'https://jiaoyu.xianmxkj.com/file/uploadPath/2023/03/08/4ab3dbaca00b1c778aab84b09246be9b.jpg', 'image');
INSERT INTO `common_info` VALUES (206, '2020-11-04 10:31:40', NULL, '客服电话', 206, '2', 'xitong');
INSERT INTO `common_info` VALUES (207, '2023-03-08 17:24:55', NULL, '客服微信号', 207, 'maxdlln', 'xitong');
INSERT INTO `common_info` VALUES (234, '2023-12-13 10:39:52', NULL, '上传方式 1阿里云oss 2腾讯云oss 3亚马逊Amazon S3 4本地', 234, '4', 'oss');
INSERT INTO `common_info` VALUES (235, '2023-03-13 10:50:29', NULL, '小程序是否上架1', 235, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (236, '2023-03-13 10:50:29', NULL, '小程序是否上架2', 236, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (237, '2024-04-19 11:36:12', NULL, '小程序是否获取手机号', 237, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (238, '2020-07-27 15:17', NULL, '短信宝用户名', 238, '', 'duanxin');
INSERT INTO `common_info` VALUES (239, '2020-07-27 15:17', NULL, '短信宝密码', 239, '', 'duanxin');
INSERT INTO `common_info` VALUES (240, '2020-11-04 10:31:40', NULL, '腾讯云短信SecretId', 240, '', 'duanxins');
INSERT INTO `common_info` VALUES (241, '2020-11-04 10:31:40', NULL, '腾讯云短信SecretKey', 241, '', 'duanxins');
INSERT INTO `common_info` VALUES (242, '2020-11-04 10:31:40', NULL, '腾讯云短信登录或注册模板id', 242, '1747645', 'duanxin');
INSERT INTO `common_info` VALUES (243, '2020-11-04 10:31:40', NULL, '腾讯云短信找回密码模板id', 243, '1746427', 'duanxin');
INSERT INTO `common_info` VALUES (244, '2020-11-04 10:31:40', NULL, '腾讯云短信绑定手机号模板id', 244, '1746428', 'duanxin');
INSERT INTO `common_info` VALUES (245, '2023-09-01 13:15:35', NULL, '充值列表', 245, '0.01,50,100,200', 'fuwufei');
INSERT INTO `common_info` VALUES (246, '2024-04-26 10:13:33', NULL, '用户注销协议', 246, '', 'xieyi');
INSERT INTO `common_info` VALUES (247, '2024-03-18 15:26:55', NULL, '是否开启同步资源', 247, '否', 'kaiguan');
INSERT INTO `common_info` VALUES (248, '2023-09-06 18:55:17', NULL, '是否开启会员', 248, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (249, '2023-09-06 18:55:17', NULL, '热搜词', 249, '回到古代,都市,极品,总裁', 'xitong');
INSERT INTO `common_info` VALUES (250, '2023-09-18 10:27:40', NULL, '短剧视频采集地址', 250, 'http://zqwjc1.aog.cc/api.php', 'xitong');
INSERT INTO `common_info` VALUES (251, '2023-10-12 15:14:18', NULL, '是否开启看广告', 251, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (252, '2023-10-12 10:50:17', NULL, '激励广告id', 252, 'adunit-af2cac875c7bb5ab', 'xitong');
INSERT INTO `common_info` VALUES (253, '2023-12-08 16:25:14', NULL, '首页关注公众号文案', 253, '关注公众号看剧不迷路', 'xitong');
INSERT INTO `common_info` VALUES (450, '2024-02-02 18:12:27', NULL, '剧导入模板', 450, 'https://duanju.xianmxkj.com/剧导入模板.xlsx', 'xitong');
INSERT INTO `common_info` VALUES (451, '2024-02-02 18:12:49', NULL, '集导入模板', 451, 'https://duanju.xianmxkj.com/集导入模板.xlsx', 'xitong');
INSERT INTO `common_info` VALUES (452, '2024-02-02 18:12:27', NULL, '梵会员直推短剧收益比例', 452, '0.5', 'daren');
INSERT INTO `common_info` VALUES (453, '2024-02-02 18:12:27', NULL, '剧达人直推短剧收益比例', 453, '0.7', 'daren');
INSERT INTO `common_info` VALUES (454, '2024-04-23 09:47:34', NULL, '剧达人直推会员收益比例', 454, '0.5', 'daren');
INSERT INTO `common_info` VALUES (455, '2024-02-02 18:12:27', NULL, '剧荐官联盟短剧收益比例', 455, '0.03', 'daren');
INSERT INTO `common_info` VALUES (456, '2024-04-16 14:55:50', NULL, '剧达人每月分平台利润', 456, '0.1', 'daren');
INSERT INTO `common_info` VALUES (500, '2020-02-25 20:44:15', NULL, '付费须知说明', 500, '金豆一经购买不予退款，未满18岁需在监护人的指导、同意下进行付费操作', 'xieyi');
INSERT INTO `common_info` VALUES (800, '2022-11-01 11:18:00', NULL, '腾讯云oss AccessKey', 800, '', 'oss');
INSERT INTO `common_info` VALUES (801, '2022-11-01 11:18:00', NULL, '腾讯云oss SecretKey', 801, '', 'oss');
INSERT INTO `common_info` VALUES (802, '2022-11-01 11:18:00', NULL, '腾讯云oss bucket地域', 802, 'ap-beijing', 'oss');
INSERT INTO `common_info` VALUES (803, '2022-11-01 11:18:00', NULL, '腾讯云oss bucketName', 803, 'sqxsqx-1300415966', 'oss');
INSERT INTO `common_info` VALUES (804, '2022-11-01 11:18:00', NULL, '腾讯云oss Bucket域名', 804, 'https://sqxsqx-1300415966.cos.ap-beijing.myqcloud.com', 'oss');
INSERT INTO `common_info` VALUES (805, '2022-11-01 11:18:00', NULL, '抖音AppID', 805, '', 'xitong');
INSERT INTO `common_info` VALUES (806, '2022-11-01 11:18:00', NULL, '抖音AppSecret', 806, '', 'xitong');
INSERT INTO `common_info` VALUES (807, '2022-11-01 11:18:00', NULL, '亚马逊oss AccessKey', 807, '', 'oss');
INSERT INTO `common_info` VALUES (808, '2022-11-01 11:18:00', NULL, '亚马逊oss SecretKey', 808, '', 'oss');
INSERT INTO `common_info` VALUES (809, '2022-11-01 11:18:00', NULL, '亚马逊oss bucket地域', 809, 'ap-southeast-1', 'oss');
INSERT INTO `common_info` VALUES (810, '2022-11-01 11:18:00', NULL, '亚马逊oss bucketName', 810, 'sqxsqx', 'oss');
INSERT INTO `common_info` VALUES (811, '2022-11-01 11:18:00', NULL, '亚马逊oss Bucket域名', 811, 'https://sqxsqx.s3.ap-southeast-1.amazonaws.com', 'oss');
INSERT INTO `common_info` VALUES (812, '2023-12-14 10:56:31', NULL, '助力背景图', 812, 'https://duanju.xianmxkj.com/file/uploadPath/2023/12/14/de8c4b94d6b36048d67ccdf333371632.png', 'image');
INSERT INTO `common_info` VALUES (813, '2024-03-05 20:13:51', NULL, '是否开启助力活动', 813, '否', 'kaiguan');
INSERT INTO `common_info` VALUES (814, '2024-04-19 11:36:03', NULL, '抖音是否获取手机号', 814, '否', 'kaiguan');
INSERT INTO `common_info` VALUES (815, '2022-11-01 11:18:00', NULL, '抖音SALT', 815, 'oOVzPdjll2dNmEgpvDPIkAIHCmSPmGr1QtCMngMy', 'xitong');
INSERT INTO `common_info` VALUES (817, '2023-12-27 18:06:19', NULL, '是否开启购买整部视频', 817, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (818, '2023-12-27 13:24:31', NULL, '是否开启购买单集视频', 818, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (819, '2023-12-27 13:24:31', NULL, '抖音应用公钥版本', 819, '2', 'xitong');
INSERT INTO `common_info` VALUES (820, '2024-01-06 11:40:25', NULL, '抖音回调域名', 820, 'https://duanju.xianmxkj.com', 'xitong');
INSERT INTO `common_info` VALUES (821, '2024-01-06 11:40:25', NULL, '抖音原视频地址 1抖音云 2第三方', 821, '2', 'xitong');
INSERT INTO `common_info` VALUES (822, '2024-04-18 14:25:19', NULL, '小程序AppKey', 822, '', 'xitong');
INSERT INTO `common_info` VALUES (823, '2024-04-18 14:24:48', NULL, '微信虚拟支付OfferID', 823, '', 'xitong');
INSERT INTO `common_info` VALUES (824, '2024-04-18 14:24:58', NULL, '微信虚拟支付环境 0正式环境 1沙盒环境', 824, '0', 'xitong');
INSERT INTO `common_info` VALUES (825, '2024-01-06 11:40:25', NULL, '充值提示', 825, '点券一经购买不予退款，未满18岁需在监护人的指导、同意下进行付费操作', 'xitong');
INSERT INTO `common_info` VALUES (826, '2024-04-18 14:29:01', NULL, '小程序Token', 826, 'fanhua', 'weixin');
INSERT INTO `common_info` VALUES (827, '2024-04-18 14:28:57', NULL, '小程序EncodingAESKey	', 827, '7E2ykUdowyFVJ0CGFHlmnBSNnFImO1kvwlUqudVSW15', 'weixin');
INSERT INTO `common_info` VALUES (828, '2020-02-25 20:43:59', NULL, '快手AppId', 828, 'ks699465318929951101', 'kuaishou');
INSERT INTO `common_info` VALUES (829, '2020-02-25 20:44:15', NULL, '快手AppSecret', 829, '', 'kuaishou');
INSERT INTO `common_info` VALUES (830, '2024-04-19 11:18:42', NULL, '快手是否获取手机号', 830, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (831, '2023-09-01 13:49:55', NULL, '快手商品支付类目', 831, '1299', 'kuaishou');
INSERT INTO `common_info` VALUES (832, '2023-09-01 13:49:55', NULL, '苹果支付环境 1正式 2沙盒', 832, '2', 'xitong');
INSERT INTO `common_info` VALUES (833, '2024-02-21 18:11:35', NULL, '是否开启苹果登录', 833, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (834, '2024-02-21 18:15:59', NULL, '是否开启苹果支付', 834, '否', 'kaiguan');
INSERT INTO `common_info` VALUES (835, '2023-09-01 13:49:55', NULL, '是否开启微信登录', 835, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (836, '2023-09-01 13:49:55', NULL, '是否开启手机号一键登录', 836, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (837, '2024-03-01 17:20:37', NULL, '新用户红包', 837, '', 'fuwufei');
INSERT INTO `common_info` VALUES (849, '2024-03-29 16:24:52', NULL, '是否开启分类', 849, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (850, '2024-03-05 14:05:48', NULL, '是否开启pc端使用', 850, '否', 'kaiguan');
INSERT INTO `common_info` VALUES (853, '2021-08-05 15:30:22', NULL, 'pc展示用户端文字', 853, '', 'xieyi');
INSERT INTO `common_info` VALUES (854, '2024-02-23 16:08:34', NULL, 'pc展示用户端二维码', 854, 'https://taobao.xianmxkj.com/custom.jpg', 'image');
INSERT INTO `common_info` VALUES (855, '2024-03-01 18:42:05', NULL, '是否开启卡密充值', 855, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (856, '2024-03-05 14:05:48', NULL, '是否开启收益充值余额', 856, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (857, '2024-03-05 14:05:48', NULL, '抖音im客服', 857, '', 'xitong');
INSERT INTO `common_info` VALUES (858, '2024-03-05 14:05:48', NULL, '是否开启公众号充值', 858, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (860, '2024-04-02 10:48:27', NULL, '抖音广告id', 860, '', 'xitong');
INSERT INTO `common_info` VALUES (861, '2024-02-02 18:12:27', NULL, '接口请求凭证apiKey', 861, '', 'xitong');
INSERT INTO `common_info` VALUES (862, '2024-02-02 18:12:27', NULL, '接口请求凭证apiSecret', 862, '', 'xitong');
INSERT INTO `common_info` VALUES (863, '2024-02-02 18:12:27', NULL, '平台昵称默认数值', 863, '小剧', 'xitong');
INSERT INTO `common_info` VALUES (864, '2024-02-02 18:12:27', NULL, '每日头像修改次数', 864, '3', 'xitong');
INSERT INTO `common_info` VALUES (865, '2024-02-02 18:12:27', NULL, '每日昵称修改次数', 865, '3', 'xitong');
INSERT INTO `common_info` VALUES (866, '2024-02-02 18:12:27', NULL, '推荐人每月分平台利润', 866, '0.25', 'daren');
INSERT INTO `common_info` VALUES (867, '2024-04-16 14:55:50', NULL, '剧荐官每月分平台利润', 867, '0.25', 'daren');
INSERT INTO `common_info` VALUES (868, '2024-04-09 18:56:26', NULL, '微信提现方式 1官方 2二维码', 868, '2', 'xitong');
INSERT INTO `common_info` VALUES (869, '2024-02-02 18:12:27', NULL, '自动扣费须知说明', 869, '点券一经购买不予退款，未满18岁需在监护人的指导、同意下进行付费操作', 'xieyi');
INSERT INTO `common_info` VALUES (870, '2024-02-02 18:12:27', NULL, '我是推荐人分成说明', 870, '点券一经购买不予退款，未满18岁需在监护人的指导、同意下进行付费操作', 'xieyi');
INSERT INTO `common_info` VALUES (871, '2024-04-25 16:32:08', NULL, '个人信息政策清单', 871, '<p class=\"ql-align-center\"><strong class=\"ql-size-huge\">个人信息收集清单</strong></p><p><br></p><p> &nbsp;&nbsp;我们会收集您在使用服务时主动提供的，以及通过自动化手段收集您在使用服务过程中产生的下述个人信息，我们充分尊重并理解您通过账户设置所作的操作，您有权通过访问“我的-设置”进行访问和管理。</p><p>1.注册登录时，结合您根据平台页面选择的不同注册方式，可能收集的个人信息包括：</p><p>1.1、手机号及验证码信息</p><p>1.2、用户自主绑定的第三方账号（如微信）</p><p>1.3、用户自主绑定的第三方账号的相关信息（昵称、头像以及用户授权的其他信息）</p><p>1.4、用户自主展示的网络身份识别信息[包括头像、昵称、微信号、微信昵称、微信头像（在用户绑定的前提下）]</p><p>2.为了帮助您完成搜索，可能收集的个人信息包括：</p><p>2.1、搜索词条</p><p>2.2、搜索历史</p><p>2.3、剪贴板中读取的文本、链接</p><p>3.为了向您进行内容展示，可能收集的个人信息包括：</p><p>3.1设备信息</p><p>3.2日志信息</p><p>4.发起提现申请时，为了身份核验、帮助提现，可能收集的个人信息包括：</p><p>4.1、收款方式指向的收款账户信息</p><p>4.2、实名认证信息（姓名）</p><p>4.3、手机号及验证码信息</p><p>5.消息通知时，可能收集的个人信息包括：用户主动提供的联系方式（例如：联系电话）</p><p>6.与平台联系及投诉反馈时，为了保障您的账户及系统安全，可能收集的个人信息包括：</p><p>6.1、用户与我们的沟通记录及相关内容[包括账号信息（手机号）]</p><p>6.2、用户为了证明相关事实提供的其他信息</p><p>6.3、用户留下的联系方式信息</p><p>6.4、用户参与问卷调查时向我们发送的问卷答复信息</p><p>7.进行安全保障时，为了提高使用时系统的安全性、保护人身财产安全，可能收集的个人信息包括：</p><p>7.1、账号信息</p><p>7.2、设备信息</p><p>7.3、日志信息</p><p>&nbsp;</p>', 'xieyi');
INSERT INTO `common_info` VALUES (872, '2024-02-02 18:12:27', NULL, '第三方SDK列表', 872, '第三方SDK列表', 'xieyi');
INSERT INTO `common_info` VALUES (873, '2024-02-02 18:12:27', NULL, '权限及使用场景', 873, '权限及使用场景', 'xieyi');
INSERT INTO `common_info` VALUES (874, '2024-02-02 18:12:27', NULL, '系统权限管理', 874, '系统权限管理', 'xieyi');
INSERT INTO `common_info` VALUES (875, '2024-02-02 18:12:27', NULL, 'ICP备案号', 875, 'ICP备案号12345', 'xitong');
INSERT INTO `common_info` VALUES (876, '2024-02-02 18:12:27', NULL, '版本号', 876, '1.0.0', 'xitong');
INSERT INTO `common_info` VALUES (877, '2024-04-25 16:29:02', NULL, '规则中心', 877, '<p>1.严禁在线上及线下渠道以任何方式宣称自身或下辖团队“官方”；</p><p> 2严禁对外发布失实报道或宣传散布不实信息；</p><p> 3.严禁有损平台形象、品牌的行为；</p><p> 4.严禁影响平台正常正规运作的行为；</p><p> 5.严禁以团队内部奖励、培训等名义挖角其他团队人员；</p><p> 6.禁止诋毁其他同类平台；</p><p> 7.禁止恶意刷广告行为；</p><p> 8.严禁虚假夸大宣传平台佣金、多层级、无限级等；</p><p> 9.严禁宣传和搬运违规行为；</p><p> 10.针对以上违规行为官方有权冻结佣金甚至封号处理；</p><p> 11.以上最终解释权归梵花视频平台所有。</p>', 'xieyi');
INSERT INTO `common_info` VALUES (878, '2024-02-02 18:12:27', NULL, '导师微信二维码', 878, 'https://taobao.xianmxkj.com/custom.jpg', 'image');
INSERT INTO `common_info` VALUES (879, '2024-02-02 18:12:27', NULL, '导师微信工作时间', 879, '周一至周五 10:00~12:00 13:00~18:30', 'xieyi');
INSERT INTO `common_info` VALUES (880, '2024-04-26 10:02:44', NULL, '客服微信工作时间', 880, '<p class=\"ql-align-center\">周一至周五 10:00~12:00 13:00~18:30</p><p class=\"ql-align-center\"><img src=\"https://fanhua.xianmxkj.com/file/uploadPath/2024/04/26/06758e8d8e179956fae1979ecaf3259f.png\"></p>', 'xieyi');
INSERT INTO `common_info` VALUES (882, '2024-04-24 10:21:55', NULL, '是否开启梵会员', 882, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (883, '2024-04-23 11:08:13', '24,1', '每月平台利润分配', 883, '0', 'daren');
INSERT INTO `common_info` VALUES (884, '2024-02-02 18:12:27', NULL, '本月利润分红说明', 884, '是', 'xieyi');
INSERT INTO `common_info` VALUES (885, '2024-02-02 18:12:27', NULL, '达人消费分红说明', 885, '0.25', 'xieyi');
INSERT INTO `common_info` VALUES (887, '2024-04-23 09:51:07', '1,2,3,4', '免费看剧角色', 887, '', 'daren');
INSERT INTO `common_info` VALUES (888, '2024-04-24 10:21:55', NULL, '是否开启官方互动', 888, '是', 'kaiguan');
INSERT INTO `common_info` VALUES (889, '2024-04-24 10:21:55', NULL, '是否开启微信支付', 889, '是', 'kaiguan');

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `coupon_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '优惠券id',
  `coupon_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券名称',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '可抵扣金额',
  `coupon_type` int(11) NULL DEFAULT NULL COMMENT '所属类型',
  PRIMARY KEY (`coupon_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠卷' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coupon
-- ----------------------------

-- ----------------------------
-- Table structure for coupon_user
-- ----------------------------
DROP TABLE IF EXISTS `coupon_user`;
CREATE TABLE `coupon_user`  (
  `coupon_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户优惠卷id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `coupon_money` decimal(10, 0) NULL DEFAULT NULL COMMENT '优惠券金额',
  `coupon_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券使用规则',
  PRIMARY KEY (`coupon_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coupon_user
-- ----------------------------

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `course_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `title_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `classify_id` int(11) NULL DEFAULT NULL COMMENT '分类',
  `pay_num` int(11) NULL DEFAULT NULL COMMENT '购买次数',
  `course_label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程标签',
  `course_label_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程标签id',
  `img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容图',
  `details` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '课程介绍',
  `is_delete` int(11) NULL DEFAULT NULL COMMENT '删除标识 0未删除 1已删除',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `msg_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频地址',
  `msg_type` int(1) NULL DEFAULT NULL COMMENT '上传方式0OSS-1本地',
  `is_recommend` int(11) NULL DEFAULT NULL COMMENT '是否是推荐商品',
  `banner_id` int(11) NULL DEFAULT NULL COMMENT '首页金刚区分类',
  `course_type` int(11) NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT 1 COMMENT '状态 1上架 2下架',
  `banner_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '轮播图',
  `over` int(11) NULL DEFAULT NULL COMMENT '是否完结 0否 1是 ',
  `is_price` int(11) NULL DEFAULT NULL COMMENT '是否收费 1是 2免费',
  `view_counts` int(11) NULL DEFAULT NULL COMMENT '播放量',
  `dy_img_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抖音封面图id',
  `dy_course_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抖音短剧id',
  `dy_status` int(11) NULL DEFAULT NULL COMMENT '抖音提审状态 1已提交 2已通过 3已拒绝 4已上线',
  `dy_status_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抖音审核内容',
  `dy_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前版本号',
  `license_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '许可证号',
  `registration_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登记号',
  `ordinary_record_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '普通备案号',
  `key_record_num` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '重点备案号',
  `wx_course_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信短剧id',
  `wx_show` int(11) NULL DEFAULT NULL COMMENT '微信是否显示 1是',
  `dy_show` int(11) NULL DEFAULT NULL COMMENT '抖音是否显示 1是',
  PRIMARY KEY (`course_id`) USING BTREE,
  INDEX `classify_id`(`classify_id`) USING BTREE,
  INDEX `banner_id`(`banner_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1183 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------

-- ----------------------------
-- Table structure for course_classification
-- ----------------------------
DROP TABLE IF EXISTS `course_classification`;
CREATE TABLE `course_classification`  (
  `classification_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程分类id',
  `classification_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程分类名称',
  `is_delete` int(1) NULL DEFAULT NULL COMMENT '假删除0正常1已删除',
  `sort` int(255) NULL DEFAULT NULL,
  `course_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`classification_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_classification
-- ----------------------------
INSERT INTO `course_classification` VALUES (1, '数学', 1, NULL, 65);
INSERT INTO `course_classification` VALUES (2, '都市', 0, 1, 1176);
INSERT INTO `course_classification` VALUES (3, '修真', 0, 2, 1180);
INSERT INTO `course_classification` VALUES (4, '兴趣爱好', 1, NULL, 68);
INSERT INTO `course_classification` VALUES (5, 'H5', 1, NULL, 69);
INSERT INTO `course_classification` VALUES (6, '办公效率', 1, NULL, 72);
INSERT INTO `course_classification` VALUES (7, '个人提升', 1, NULL, 73);
INSERT INTO `course_classification` VALUES (8, '语言教学', 1, NULL, 74);
INSERT INTO `course_classification` VALUES (9, '创意设计', 1, NULL, 75);
INSERT INTO `course_classification` VALUES (10, '体育健康', 1, NULL, 76);
INSERT INTO `course_classification` VALUES (11, '职业教学', 1, NULL, 77);
INSERT INTO `course_classification` VALUES (12, '小电影', 1, NULL, 78);
INSERT INTO `course_classification` VALUES (13, '萌宠合集', 1, NULL, 79);
INSERT INTO `course_classification` VALUES (14, '悬疑', 0, 3, 80);
INSERT INTO `course_classification` VALUES (15, '历史', 0, 4, 81);
INSERT INTO `course_classification` VALUES (16, '苦情戏', 1, 5, 1159);
INSERT INTO `course_classification` VALUES (17, 'abc', 0, 6, 1157);
INSERT INTO `course_classification` VALUES (18, '全部', 0, NULL, 1180);
INSERT INTO `course_classification` VALUES (19, '爱情', 0, 0, 1182);

-- ----------------------------
-- Table structure for course_collect
-- ----------------------------
DROP TABLE IF EXISTS `course_collect`;
CREATE TABLE `course_collect`  (
  `course_collect_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收藏id',
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `course_details_id` int(11) NULL DEFAULT NULL,
  `classify` int(11) NULL DEFAULT NULL,
  `update_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `course_details_sec` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`course_collect_id`) USING BTREE,
  INDEX `course_id`(`course_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12206 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_collect
-- ----------------------------

-- ----------------------------
-- Table structure for course_comment
-- ----------------------------
DROP TABLE IF EXISTS `course_comment`;
CREATE TABLE `course_comment`  (
  `course_comment_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程评论id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程id',
  `goods_num` int(11) NULL DEFAULT 0 COMMENT '点赞数',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论内容',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`course_comment_id`) USING BTREE,
  INDEX `course_id`(`course_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_comment
-- ----------------------------

-- ----------------------------
-- Table structure for course_details
-- ----------------------------
DROP TABLE IF EXISTS `course_details`;
CREATE TABLE `course_details`  (
  `course_details_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '课程目录id',
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程id',
  `course_details_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  `video_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '视频地址',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `title_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '介绍',
  `good_num` int(11) NULL DEFAULT NULL COMMENT '点赞数',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '售价',
  `is_price` int(11) NULL DEFAULT NULL COMMENT '是否收费 1是 2否',
  `good` int(11) NULL DEFAULT NULL COMMENT '是否是推荐 1是',
  `dy_course_details_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抖音视频id',
  `dy_img_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抖音封面图id',
  `dy_episode_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抖音集id',
  `dy_status` int(11) NULL DEFAULT NULL COMMENT '抖音提审状态 1已提交 2已通过 3已拒绝 4已上线',
  `dy_status_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抖音审核内容',
  `dy_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '当前版本号',
  `dy_url_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抖音集审核状态',
  `wx_course_details_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信集id',
  PRIMARY KEY (`course_details_id`) USING BTREE,
  INDEX `course_id`(`course_id`) USING BTREE,
  INDEX `course_id_2`(`course_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 97489 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_details
-- ----------------------------

-- ----------------------------
-- Table structure for course_user
-- ----------------------------
DROP TABLE IF EXISTS `course_user`;
CREATE TABLE `course_user`  (
  `course_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '我的课程id',
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程id',
  `order_id` int(11) NULL DEFAULT NULL COMMENT '订单id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `course_details_id` int(11) NULL DEFAULT NULL COMMENT '集id',
  `classify` int(11) NULL DEFAULT NULL COMMENT '1整集2单集',
  PRIMARY KEY (`course_user_id`) USING BTREE,
  INDEX `course_id`(`course_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 354 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_user
-- ----------------------------

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `goods_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `goods_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品标题',
  `longitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '经度',
  `latitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纬度',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `title_img` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图',
  `img` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详情图',
  `sum_num` int(11) NULL DEFAULT NULL COMMENT '总数量',
  `end_num` int(11) NULL DEFAULT NULL COMMENT '剩余数量',
  `goods_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `member_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '会员满',
  `member_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '会员返',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '普通用户满',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '普通用户返',
  `start_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `classify` int(11) NULL DEFAULT NULL COMMENT '分类 1 饿了么  2美团',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '类型',
  `num_star` int(11) NULL DEFAULT NULL COMMENT '几颗星',
  `num_img` int(11) NULL DEFAULT NULL COMMENT '几张图',
  `num_word` int(11) NULL DEFAULT NULL COMMENT '多少字',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注  富文本',
  `url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '小程序跳转参数',
  `status` int(11) NULL DEFAULT NULL COMMENT '1 上架 2下架',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `district` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区',
  `activity_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '金刚区分类',
  `is_goods` int(11) NULL DEFAULT NULL COMMENT '是否是精选商品  0否 1是',
  `scope` int(11) NULL DEFAULT NULL COMMENT '范围',
  `member_privilege` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员权益',
  `privilege` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '普通用户权益',
  `describes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '门店介绍',
  `shop_describe` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家介绍',
  `phone` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 180 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods
-- ----------------------------

-- ----------------------------
-- Table structure for goods_type
-- ----------------------------
DROP TABLE IF EXISTS `goods_type`;
CREATE TABLE `goods_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品类型id',
  `create_at` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '类型名称',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级类型id',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goods_type
-- ----------------------------

-- ----------------------------
-- Table structure for help_classify
-- ----------------------------
DROP TABLE IF EXISTS `help_classify`;
CREATE TABLE `help_classify`  (
  `help_classify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帮助中心分类',
  `help_classify_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '上级id',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `types` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`help_classify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_classify
-- ----------------------------
INSERT INTO `help_classify` VALUES (7, '登录问题', 0, NULL, '2022-07-06', 1);
INSERT INTO `help_classify` VALUES (11, '注册问题2', 2, NULL, '2023-09-25 11:14:11', 1);
INSERT INTO `help_classify` VALUES (12, '注销账户问题', 0, NULL, '2023-09-25 11:21:42', 1);

-- ----------------------------
-- Table structure for help_word
-- ----------------------------
DROP TABLE IF EXISTS `help_word`;
CREATE TABLE `help_word`  (
  `help_word_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帮助文档id',
  `help_word_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '帮助标题',
  `help_word_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '帮助文档内容',
  `help_classify_id` int(11) NULL DEFAULT NULL COMMENT '帮助分类id',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`help_word_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of help_word
-- ----------------------------
INSERT INTO `help_word` VALUES (1, '为什么提示不支持打开非业务域名', '因为所以科学道理', 2, 1, '2020-12-12 12:12:12');
INSERT INTO `help_word` VALUES (2, '文件类课间如何保存', '不知道', 2, 2, '2020-12-12 12:12:12');
INSERT INTO `help_word` VALUES (3, '已报名的活动可以取消吗', '可以，需要扣除百分之20', 3, 1, '2020-12-12 12:12:12');
INSERT INTO `help_word` VALUES (7, '报名活动呀呀呀呀', '<p>报名活动呀呀呀呀报名活动呀呀呀呀报名活动呀呀呀呀报名活动呀呀呀呀报名活动呀呀呀呀报名活动呀呀呀呀报名活动呀呀呀呀报名活动呀呀呀呀报名活动呀呀呀呀</p>', 6, 0, '2022-07-05');
INSERT INTO `help_word` VALUES (8, '怎么登录呢？', '<p>微信授权即可登录<img src=\"https://jiazheng.xianmxkj.com/file/uploadPath/2022/07/06/0e9d093abb26f9bc85be36e3a206bff5.png\"></p>', 7, 0, '2023-09-25 11:07:52');
INSERT INTO `help_word` VALUES (9, '商户怎么入驻呢？', '<p>登录首页 点击：我要入驻</p>', 8, 1, '2022-10-31 14:46:27');
INSERT INTO `help_word` VALUES (10, '额鹅鹅鹅', '<p>额鹅鹅鹅</p>', 8, 0, '2022-10-31 14:49:57');
INSERT INTO `help_word` VALUES (11, '如何注册?', '<p>使用手机号码注册</p>', 11, 1, '2023-09-25 11:14:46');
INSERT INTO `help_word` VALUES (12, '如何注销账户?', '<p>11111111111111111111111111111111<img src=\"https://duanju.xianmxkj.com/file/uploadPath/2024/01/04/459edfbed6d493b460f7337f9f58a8a4.jpg\"></p>', 12, 3, '2024-01-04 10:33:07');

-- ----------------------------
-- Table structure for invite
-- ----------------------------
DROP TABLE IF EXISTS `invite`;
CREATE TABLE `invite`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '邀请者id',
  `invitee_user_id` int(11) NULL DEFAULT NULL COMMENT '被邀请者id',
  `state` int(11) NULL DEFAULT NULL COMMENT '状态 0非会员 1会员',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '邀请收益',
  `create_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `user_type` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1346 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '邀请信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invite
-- ----------------------------

-- ----------------------------
-- Table structure for invite_award
-- ----------------------------
DROP TABLE IF EXISTS `invite_award`;
CREATE TABLE `invite_award`  (
  `invite_award_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '邀请奖励id',
  `invite_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请图片',
  `invite_count` int(11) NULL DEFAULT NULL COMMENT '邀请人数',
  `invite_month` int(11) NULL DEFAULT NULL COMMENT '奖励月份 0是永久',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`invite_award_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invite_award
-- ----------------------------
INSERT INTO `invite_award` VALUES (1, 'https://duanju.xianmxkj.com/file/uploadPath/2023/12/13/724e51aff7eee1ddb01951295e3fd678.png', 1, 1, '2023-12-12 12:12:12');
INSERT INTO `invite_award` VALUES (3, 'https://duanju.xianmxkj.com/file/uploadPath/2023/12/13/7e8c164a3773f2bd579e27e9e4d1a738.png', 2, 0, '2023-12-12 17:04:42');

-- ----------------------------
-- Table structure for invite_money
-- ----------------------------
DROP TABLE IF EXISTS `invite_money`;
CREATE TABLE `invite_money`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '邀请收益钱包id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `money_sum` decimal(10, 2) NULL DEFAULT NULL COMMENT '总获取收益',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '当前金额',
  `cash_out` decimal(10, 2) NULL DEFAULT NULL COMMENT '累计提现',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 676 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of invite_money
-- ----------------------------

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `member_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员特权id',
  `member_index` int(11) NULL DEFAULT NULL COMMENT '等级',
  `member_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '特权图标',
  `member_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '特权名称',
  `member_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '特权详情',
  `sort` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`member_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------
INSERT INTO `member` VALUES (4, 1, 'https://sac.xianmxkj.com/file/uploadPath/2022/02/16/74ed314cec7ae738cba7efed90d2c656.png', '享会员价', '享会员价享会员价', '1');
INSERT INTO `member` VALUES (6, 1, 'https://sac.xianmxkj.com/file/uploadPath/2022/02/16/88d495a21a15a651748d3d1b2f5c7613.png', '身份标识', '身份标识身份标识', '2');
INSERT INTO `member` VALUES (7, 2, 'https://sac.xianmxkj.com/file/uploadPath/2022/02/16/74ed314cec7ae738cba7efed90d2c656.png', '高级会员', '享会员价享会员价', '1');
INSERT INTO `member` VALUES (8, 2, 'https://sac.xianmxkj.com/file/uploadPath/2022/02/16/88d495a21a15a651748d3d1b2f5c7613.png', '高级身标识', '身份标识身份标识', '2');

-- ----------------------------
-- Table structure for message_info
-- ----------------------------
DROP TABLE IF EXISTS `message_info`;
CREATE TABLE `message_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容',
  `create_at` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '创建时间',
  `image` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '图片',
  `is_see` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `send_state` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `send_time` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `state` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '分类',
  `title` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `url` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '地址',
  `type` varchar(600) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `platform` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34698 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_info
-- ----------------------------

-- ----------------------------
-- Table structure for msg
-- ----------------------------
DROP TABLE IF EXISTS `msg`;
CREATE TABLE `msg`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '短信验证码',
  `phone` varchar(255) CHARACTER SET big5 COLLATE big5_chinese_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_name`(`code`, `phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3604 CHARACTER SET = big5 COLLATE = big5_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of msg
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `orders_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `orders_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝支付单号',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程id',
  `pay_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `pay_way` int(11) NULL DEFAULT NULL COMMENT '支付方式 1微信小程序  2微信公众号 3微信App 4支付宝',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态 0待支付  1已支付  2已退款',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `refund_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退款原因',
  `orders_type` int(11) NULL DEFAULT NULL COMMENT '订单种类 1课程 2会员',
  `vip_name_type` int(1) NULL DEFAULT NULL COMMENT '会员类型0月/1季度/2年',
  `course_details_id` int(11) NULL DEFAULT NULL,
  `qd_money` decimal(10, 2) NULL DEFAULT NULL,
  `sys_user_id` int(11) NULL DEFAULT NULL,
  `one_money` decimal(10, 2) NULL DEFAULT NULL,
  `one_user_id` int(11) NULL DEFAULT NULL,
  `two_money` decimal(10, 2) NULL DEFAULT NULL,
  `two_user_id` int(11) NULL DEFAULT NULL,
  `pay_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `member_user_id` int(11) NULL DEFAULT NULL,
  `member_money` decimal(10, 2) NULL DEFAULT NULL,
  `member_type` int(11) NULL DEFAULT NULL,
  `channel_user_id` int(11) NULL DEFAULT NULL,
  `channel_money` decimal(10, 2) NULL DEFAULT NULL,
  `ping_money` decimal(10, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`orders_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1999 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for pay_classify
-- ----------------------------
DROP TABLE IF EXISTS `pay_classify`;
CREATE TABLE `pay_classify`  (
  `pay_classify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '充值分类id',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '售价',
  `give_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '赠送数量',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '水晶数量',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时间',
  `wx_goods_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信道具id',
  `product_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '苹果道具id',
  PRIMARY KEY (`pay_classify_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_classify
-- ----------------------------
INSERT INTO `pay_classify` VALUES (6, 6.00, 6.00, 60.00, 1, '2024-04-17 14:59:18', 'daoju6', '');
INSERT INTO `pay_classify` VALUES (7, 1.00, 0.00, 10.00, 2, '2024-04-17 14:59:35', 'daoju1', '');

-- ----------------------------
-- Table structure for pay_details
-- ----------------------------
DROP TABLE IF EXISTS `pay_details`;
CREATE TABLE `pay_details`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '充值id',
  `classify` int(4) NULL DEFAULT NULL COMMENT '分类（ 1app微信 2微信公众号 3微信小程序 4支付宝）',
  `trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝支付单号',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '充值金额',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `state` int(4) NULL DEFAULT NULL COMMENT '0待支付 1支付成功 2失败',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `pay_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付时间',
  `type` int(4) NULL DEFAULT NULL COMMENT '支付类型 1 用户  2会员',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `product_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '苹果道具id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2988 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_details
-- ----------------------------

-- ----------------------------
-- Table structure for sdk_info
-- ----------------------------
DROP TABLE IF EXISTS `sdk_info`;
CREATE TABLE `sdk_info`  (
  `sdk_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '卡密id',
  `sdk_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型备注',
  `sdk_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '卡密内容',
  `type_id` int(11) NULL DEFAULT NULL COMMENT '卡密类型',
  `status` int(1) NULL DEFAULT NULL COMMENT '0未使用 1已使用 2已过期',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `overdue_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `give_num` decimal(10, 2) NULL DEFAULT NULL COMMENT '增加次数',
  `use_time` datetime(0) NULL DEFAULT NULL COMMENT '使用时间',
  `sys_user_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`sdk_id`) USING BTREE,
  INDEX `sdk_delete`(`type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2315 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '卡密列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sdk_info
-- ----------------------------

-- ----------------------------
-- Table structure for sdk_type
-- ----------------------------
DROP TABLE IF EXISTS `sdk_type`;
CREATE TABLE `sdk_type`  (
  `type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'sdk类型',
  `give_num` decimal(10, 2) NULL DEFAULT NULL COMMENT '赠送次数',
  `valid_day` int(11) NULL DEFAULT NULL COMMENT '有效天数',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '卡密类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sdk_type
-- ----------------------------
INSERT INTO `sdk_type` VALUES (5, 10.00, 1, '2023-02-24 16:45:05', '赠送1');
INSERT INTO `sdk_type` VALUES (6, 10000.00, 365, '2023-03-25 14:05:47', '赠送1w');
INSERT INTO `sdk_type` VALUES (7, 2.00, 1, '2023-03-29 15:40:40', '卡密1');
INSERT INTO `sdk_type` VALUES (8, 1.00, 1, '2023-12-20 10:49:15', '测试卡001');
INSERT INTO `sdk_type` VALUES (9, 7.00, 1, '2023-12-21 09:04:22', '测试卡密');
INSERT INTO `sdk_type` VALUES (10, 1000.00, 7, '2024-03-14 23:41:48', '17872375876');

-- ----------------------------
-- Table structure for search
-- ----------------------------
DROP TABLE IF EXISTS `search`;
CREATE TABLE `search`  (
  `search_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '搜索id',
  `search_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '搜索名称',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `update_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`search_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of search
-- ----------------------------

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha`  (
  `uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uuid',
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '验证码',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统验证码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_captcha
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `param_key`(`param_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'CLOUD_STORAGE_CONFIG_KEY', '{\"type\":1,\"qiniuDomain\":\"http://7xqbwh.dl1.z0.glb.clouddn.com\",\"qiniuPrefix\":\"upload\",\"qiniuAccessKey\":\"NrgMfABZxWLo5B-YYSjoE8-AZ1EISdi1Z3ubLOeZ\",\"qiniuSecretKey\":\"uIwJHevMRWU0VLxFvgy0tAcOdGqasdtVlJkdy6vV\",\"qiniuBucketName\":\"ios-app\",\"aliyunDomain\":\"\",\"aliyunPrefix\":\"\",\"aliyunEndPoint\":\"\",\"aliyunAccessKeyId\":\"\",\"aliyunAccessKeySecret\":\"\",\"aliyunBucketName\":\"\",\"qcloudDomain\":\"\",\"qcloudPrefix\":\"\",\"qcloudSecretId\":\"\",\"qcloudSecretKey\":\"\",\"qcloudBucketName\":\"\"}', 0, '云存储配置信息');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典码',
  `value` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典值',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除标记  -1：已删除  0：正常',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `type`(`type`, `code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 343 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 167 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', NULL, NULL, 0, 'system', 20);
INSERT INTO `sys_menu` VALUES (2, 1, '管理员列表', 'sys/user', NULL, 1, 'admin', 1);
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', 'sys/role', NULL, 1, 'role', 2);
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', 'sys/menu', NULL, 1, 'menu', 3);
INSERT INTO `sys_menu` VALUES (15, 2, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, 2, '新增', NULL, 'sys:user:save,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, 2, '修改', NULL, 'sys:user:update,sys:role:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, 2, '删除', NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, 3, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, 3, '新增', NULL, 'sys:role:save,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, 3, '修改', NULL, 'sys:role:update,sys:menu:list', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, 3, '删除', NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, 4, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, 4, '新增', NULL, 'sys:menu:save,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (25, 4, '修改', NULL, 'sys:menu:update,sys:menu:select', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (26, 4, '删除', NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (32, 0, '用户中心', 'userList', '', 1, 'yonghul', 1);
INSERT INTO `sys_menu` VALUES (33, 0, '数据中心', 'home', '', 1, 'shuju', 0);
INSERT INTO `sys_menu` VALUES (34, 0, '财务中心', 'financeList', '', 1, 'caiwu', 1);
INSERT INTO `sys_menu` VALUES (35, 34, '查看', '', 'financeList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (36, 34, '转账', '', 'financeList:transfer', 2, '', 0);
INSERT INTO `sys_menu` VALUES (37, 34, '退款', '', 'financeList:refund', 2, '', 0);
INSERT INTO `sys_menu` VALUES (38, 34, '退款', '', 'financeList:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (39, 34, '修改', '', 'financeList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (40, 34, '删除', '', 'financeList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (41, 0, '消息中心', 'message', '', 1, 'xiaoxi', 1);
INSERT INTO `sys_menu` VALUES (42, 41, '查看', '', 'message:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (43, 41, '消息推送', '', 'message:push', 2, '', 0);
INSERT INTO `sys_menu` VALUES (44, 0, '资源中心', 'mission', '', 1, 'renwu', 3);
INSERT INTO `sys_menu` VALUES (45, 44, '查看', '', 'mission:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (46, 44, '添加', '', 'mission:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (47, 44, '修改', '', 'mission:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (48, 44, '删除', '', 'mission:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (49, 44, '下架', '', 'mission:sold', 2, '', 0);
INSERT INTO `sys_menu` VALUES (50, 0, '首页装修', 'bannerList', '', 1, 'shangpin', 2);
INSERT INTO `sys_menu` VALUES (51, 50, '查看', '', 'bannerList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (52, 50, '添加', '', 'bannerList:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (53, 50, '修改', '', 'bannerList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (54, 50, '删除', '', 'bannerList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (57, 0, '系统配置', 'allocationList', '', 1, 'menu', 9);
INSERT INTO `sys_menu` VALUES (58, 57, '查看', '', 'allocationList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (59, 57, '修改', '', 'allocationList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (60, 32, '查看', '', 'userList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (61, 32, '删除', '', 'userList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (62, 0, '订单中心', 'orderCenter', '', 1, 'log', 4);
INSERT INTO `sys_menu` VALUES (63, 62, '查看', '', 'orderCenter:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (64, 62, '删除', '', 'orderCenter:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (66, 0, '会员列表', 'viplist', '', 1, 'fenleilist', 5);
INSERT INTO `sys_menu` VALUES (67, 66, '查看', '', 'viplist:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (69, 41, '添加', '', 'message:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (70, 41, '修改', '', 'message:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (71, 41, '删除', '', 'message:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (72, 66, '添加', '', 'viplist:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (73, 66, '修改', '', 'viplist:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (74, 66, '删除', '', 'viplist:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (75, 32, '修改比例', '', 'userList:updatebl', 2, '', 0);
INSERT INTO `sys_menu` VALUES (76, 32, '修改会员', '', 'userList:updateVip', 2, '', 0);
INSERT INTO `sys_menu` VALUES (77, 0, '邀请排行榜', 'riderTop', '', 1, 'tubiao', 6);
INSERT INTO `sys_menu` VALUES (78, 77, '查看', '', 'riderTop:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (79, 62, '退款', '', 'orderCenter:tuikuan', 2, '', 0);
INSERT INTO `sys_menu` VALUES (80, 32, '修改信息', '', 'userList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (81, 0, '注销信息', 'messageZx', '', 1, 'renwu1', 7);
INSERT INTO `sys_menu` VALUES (82, 81, '查看', '', 'messageZx:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (83, 81, '注销', '', 'messageZx:shenhe', 2, '', 0);
INSERT INTO `sys_menu` VALUES (84, 32, '修改积分', '', 'userList:updateJf', 2, '', 0);
INSERT INTO `sys_menu` VALUES (85, 32, '修改钱包', '', 'userList:updateQb', 2, '', 0);
INSERT INTO `sys_menu` VALUES (86, 1, '升级配置', 'app', '', 1, 'config', 0);
INSERT INTO `sys_menu` VALUES (87, 86, '查看', '', 'app:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (88, 86, '添加', '', 'app:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (89, 86, '修改', '', 'app:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (90, 86, '删除', '', 'app:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (91, 0, '帮助中心', 'materialsList', '', 1, 'order', 8);
INSERT INTO `sys_menu` VALUES (92, 91, '查看', '', 'materialsList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (93, 91, '添加', '', 'materialsList:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (94, 91, '修改', '', 'materialsList:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (95, 91, '删除', '', 'materialsList:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (101, 0, '邀请奖励', 'invite', '', 1, 'shuju', 5);
INSERT INTO `sys_menu` VALUES (103, 101, '添加', '', 'invite:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (104, 101, '添加', '', 'invite:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (105, 101, '修改', '', 'invite:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (106, 101, '删除', '', 'invite:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (107, 0, '发卡管理', 'coupon', '', 1, 'shuju', 6);
INSERT INTO `sys_menu` VALUES (108, 107, '查看', '', 'coupon:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (109, 107, '添加', '', 'coupon:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (110, 107, '修改', '', 'coupon:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (111, 107, '删除', '', 'coupon:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (112, 0, '抖音小程序提审', 'missionDy', '', 1, 'peizhi', 5);
INSERT INTO `sys_menu` VALUES (113, 112, '查看', '', 'missionDy:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (114, 0, '微信小程序提审', 'missionWx', '', 1, 'fenleilist', 5);
INSERT INTO `sys_menu` VALUES (115, 114, '查看', '', 'missionWx:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (116, 114, '设置备案号', '', 'missionWx:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (117, 114, '上传', '', 'missionWx:shangchuan', 2, '', 0);
INSERT INTO `sys_menu` VALUES (118, 114, '送审', '', 'missionWx:songshen', 2, '', 0);
INSERT INTO `sys_menu` VALUES (119, 114, '上线', '', 'missionWx:shangxian', 2, '', 0);
INSERT INTO `sys_menu` VALUES (120, 112, '设置备案号', '', 'missionDy:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (121, 112, '上传', '', 'missionDy:shangchuan', 2, '', 0);
INSERT INTO `sys_menu` VALUES (122, 112, '送审', '', 'missionDy:songshen', 2, '', 0);
INSERT INTO `sys_menu` VALUES (123, 112, '上线', '', 'missionDy:shangxian', 2, '', 0);
INSERT INTO `sys_menu` VALUES (124, 0, '分销代理管理员', 'community', '', 1, 'admin', 9);
INSERT INTO `sys_menu` VALUES (125, 124, '查看', '', 'community:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (126, 124, '添加', '', 'sys:user:save', 2, '', 0);
INSERT INTO `sys_menu` VALUES (127, 124, '修改', '', 'sys:user:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (128, 124, '删除', '', 'sys:user:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (129, 124, '发起提现', '', 'managementStoreincome:draw', 2, '', 0);
INSERT INTO `sys_menu` VALUES (130, 124, '修改信息', '', 'managementStoreincome:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (131, 0, '分享域名管理', 'domainName', '', 1, 'fenleilist', 7);
INSERT INTO `sys_menu` VALUES (132, 131, '查看', '', 'domainName:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (133, 131, '添加', '', 'domainName:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (134, 131, '修改', '', 'domainName:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (135, 131, '删除', '', 'domainName:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (136, 0, '代理详情', 'storeincome', '', 1, 'role', 9);
INSERT INTO `sys_menu` VALUES (137, 136, '查看', '', 'storeincome:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (138, 0, '员工列表', 'communityY', '', 1, 'fenleilist', 9);
INSERT INTO `sys_menu` VALUES (139, 138, '查看', '', 'community:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (140, 138, '添加', '', 'sys:user:save', 2, '', 0);
INSERT INTO `sys_menu` VALUES (141, 138, '修改', '', 'sys:user:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (142, 138, '删除', '', 'sys:user:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (143, 138, '发起提现', '', 'managementStoreincome:draw', 2, '', 0);
INSERT INTO `sys_menu` VALUES (144, 138, '修改信息', '', 'managementStoreincome:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (145, 0, '发卡列表', 'couponQ', '', 1, 'order', 9);
INSERT INTO `sys_menu` VALUES (146, 145, '查看', '', 'couponQ:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (147, 0, '分享管理', 'campus', '', 1, 'tianjia', 9);
INSERT INTO `sys_menu` VALUES (148, 147, '查看', '', 'campus:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (149, 0, '充值管理', '', '', 0, 'caiwu', 5);
INSERT INTO `sys_menu` VALUES (150, 149, '充值配置', 'IntegralGoods', '', 1, 'fenleilist', 0);
INSERT INTO `sys_menu` VALUES (151, 150, '查看', '', 'IntegralGoods:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (152, 150, '添加', '', 'IntegralGoods:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (153, 150, '修改', '', 'IntegralGoods:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (154, 150, '删除', '', 'IntegralGoods:delete', 2, '', 0);
INSERT INTO `sys_menu` VALUES (155, 149, '充值记录', 'exchangeList', '', 1, 'renwu', 0);
INSERT INTO `sys_menu` VALUES (156, 155, '查看', '', 'exchangeList:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (157, 32, '修改密码', '', 'userList:password', 2, '', 0);
INSERT INTO `sys_menu` VALUES (158, 161, '达人配置', 'allocationDr', '', 1, 'pingtai', 0);
INSERT INTO `sys_menu` VALUES (159, 158, '查看', '', 'allocationDr:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (160, 158, '修改', '', 'allocationDr:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (161, 0, '达人管理', '', '', 0, 'leibie', 7);
INSERT INTO `sys_menu` VALUES (162, 161, '达人特权', 'vipPrivilege', '', 1, 'shoucangfill', 1);
INSERT INTO `sys_menu` VALUES (163, 162, '查看', '', 'vipPrivilege:list', 2, '', 0);
INSERT INTO `sys_menu` VALUES (164, 162, '添加', '', 'vipPrivilege:add', 2, '', 0);
INSERT INTO `sys_menu` VALUES (165, 162, '修改', '', 'vipPrivilege:update', 2, '', 0);
INSERT INTO `sys_menu` VALUES (166, 162, '删除', '', 'vipPrivilege:delete', 2, '', 0);

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', '', 11, '2021-06-04 17:38:28');
INSERT INTO `sys_role` VALUES (2, '演示用户', '演示用户', 11, '2021-08-20 10:13:44');
INSERT INTO `sys_role` VALUES (4, '代理', '代理', 11, '2024-01-11 15:52:05');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3653 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2736, 4, 33);
INSERT INTO `sys_role_menu` VALUES (2737, 4, 32);
INSERT INTO `sys_role_menu` VALUES (2738, 4, 60);
INSERT INTO `sys_role_menu` VALUES (2739, 4, 61);
INSERT INTO `sys_role_menu` VALUES (2740, 4, 75);
INSERT INTO `sys_role_menu` VALUES (2741, 4, 76);
INSERT INTO `sys_role_menu` VALUES (2742, 4, 80);
INSERT INTO `sys_role_menu` VALUES (2743, 4, 84);
INSERT INTO `sys_role_menu` VALUES (2744, 4, 85);
INSERT INTO `sys_role_menu` VALUES (2745, 4, 44);
INSERT INTO `sys_role_menu` VALUES (2746, 4, 45);
INSERT INTO `sys_role_menu` VALUES (2747, 4, 46);
INSERT INTO `sys_role_menu` VALUES (2748, 4, 47);
INSERT INTO `sys_role_menu` VALUES (2749, 4, 48);
INSERT INTO `sys_role_menu` VALUES (2750, 4, 49);
INSERT INTO `sys_role_menu` VALUES (2751, 4, 62);
INSERT INTO `sys_role_menu` VALUES (2752, 4, 63);
INSERT INTO `sys_role_menu` VALUES (2753, 4, 64);
INSERT INTO `sys_role_menu` VALUES (2754, 4, 79);
INSERT INTO `sys_role_menu` VALUES (2755, 4, 136);
INSERT INTO `sys_role_menu` VALUES (2756, 4, 137);
INSERT INTO `sys_role_menu` VALUES (2757, 4, 138);
INSERT INTO `sys_role_menu` VALUES (2758, 4, 139);
INSERT INTO `sys_role_menu` VALUES (2759, 4, 140);
INSERT INTO `sys_role_menu` VALUES (2760, 4, 141);
INSERT INTO `sys_role_menu` VALUES (2761, 4, 142);
INSERT INTO `sys_role_menu` VALUES (2762, 4, 143);
INSERT INTO `sys_role_menu` VALUES (2763, 4, 144);
INSERT INTO `sys_role_menu` VALUES (2764, 4, 145);
INSERT INTO `sys_role_menu` VALUES (2765, 4, 146);
INSERT INTO `sys_role_menu` VALUES (2766, 4, 147);
INSERT INTO `sys_role_menu` VALUES (2767, 4, 148);
INSERT INTO `sys_role_menu` VALUES (2768, 4, -666666);
INSERT INTO `sys_role_menu` VALUES (3067, 2, 33);
INSERT INTO `sys_role_menu` VALUES (3068, 2, 32);
INSERT INTO `sys_role_menu` VALUES (3069, 2, 60);
INSERT INTO `sys_role_menu` VALUES (3070, 2, 61);
INSERT INTO `sys_role_menu` VALUES (3071, 2, 75);
INSERT INTO `sys_role_menu` VALUES (3072, 2, 76);
INSERT INTO `sys_role_menu` VALUES (3073, 2, 80);
INSERT INTO `sys_role_menu` VALUES (3074, 2, 84);
INSERT INTO `sys_role_menu` VALUES (3075, 2, 85);
INSERT INTO `sys_role_menu` VALUES (3076, 2, 35);
INSERT INTO `sys_role_menu` VALUES (3077, 2, 36);
INSERT INTO `sys_role_menu` VALUES (3078, 2, 37);
INSERT INTO `sys_role_menu` VALUES (3079, 2, 38);
INSERT INTO `sys_role_menu` VALUES (3080, 2, 39);
INSERT INTO `sys_role_menu` VALUES (3081, 2, 41);
INSERT INTO `sys_role_menu` VALUES (3082, 2, 42);
INSERT INTO `sys_role_menu` VALUES (3083, 2, 43);
INSERT INTO `sys_role_menu` VALUES (3084, 2, 69);
INSERT INTO `sys_role_menu` VALUES (3085, 2, 70);
INSERT INTO `sys_role_menu` VALUES (3086, 2, 71);
INSERT INTO `sys_role_menu` VALUES (3087, 2, 51);
INSERT INTO `sys_role_menu` VALUES (3088, 2, 45);
INSERT INTO `sys_role_menu` VALUES (3089, 2, 46);
INSERT INTO `sys_role_menu` VALUES (3090, 2, 47);
INSERT INTO `sys_role_menu` VALUES (3091, 2, 49);
INSERT INTO `sys_role_menu` VALUES (3092, 2, 63);
INSERT INTO `sys_role_menu` VALUES (3093, 2, 79);
INSERT INTO `sys_role_menu` VALUES (3094, 2, 67);
INSERT INTO `sys_role_menu` VALUES (3095, 2, 72);
INSERT INTO `sys_role_menu` VALUES (3096, 2, 73);
INSERT INTO `sys_role_menu` VALUES (3097, 2, 103);
INSERT INTO `sys_role_menu` VALUES (3098, 2, 104);
INSERT INTO `sys_role_menu` VALUES (3099, 2, 105);
INSERT INTO `sys_role_menu` VALUES (3100, 2, 112);
INSERT INTO `sys_role_menu` VALUES (3101, 2, 113);
INSERT INTO `sys_role_menu` VALUES (3102, 2, 120);
INSERT INTO `sys_role_menu` VALUES (3103, 2, 121);
INSERT INTO `sys_role_menu` VALUES (3104, 2, 122);
INSERT INTO `sys_role_menu` VALUES (3105, 2, 123);
INSERT INTO `sys_role_menu` VALUES (3106, 2, 114);
INSERT INTO `sys_role_menu` VALUES (3107, 2, 115);
INSERT INTO `sys_role_menu` VALUES (3108, 2, 116);
INSERT INTO `sys_role_menu` VALUES (3109, 2, 117);
INSERT INTO `sys_role_menu` VALUES (3110, 2, 118);
INSERT INTO `sys_role_menu` VALUES (3111, 2, 119);
INSERT INTO `sys_role_menu` VALUES (3112, 2, 151);
INSERT INTO `sys_role_menu` VALUES (3113, 2, 155);
INSERT INTO `sys_role_menu` VALUES (3114, 2, 156);
INSERT INTO `sys_role_menu` VALUES (3115, 2, 77);
INSERT INTO `sys_role_menu` VALUES (3116, 2, 78);
INSERT INTO `sys_role_menu` VALUES (3117, 2, 107);
INSERT INTO `sys_role_menu` VALUES (3118, 2, 108);
INSERT INTO `sys_role_menu` VALUES (3119, 2, 109);
INSERT INTO `sys_role_menu` VALUES (3120, 2, 110);
INSERT INTO `sys_role_menu` VALUES (3121, 2, 111);
INSERT INTO `sys_role_menu` VALUES (3122, 2, 81);
INSERT INTO `sys_role_menu` VALUES (3123, 2, 82);
INSERT INTO `sys_role_menu` VALUES (3124, 2, 83);
INSERT INTO `sys_role_menu` VALUES (3125, 2, 132);
INSERT INTO `sys_role_menu` VALUES (3126, 2, 91);
INSERT INTO `sys_role_menu` VALUES (3127, 2, 92);
INSERT INTO `sys_role_menu` VALUES (3128, 2, 93);
INSERT INTO `sys_role_menu` VALUES (3129, 2, 94);
INSERT INTO `sys_role_menu` VALUES (3130, 2, 95);
INSERT INTO `sys_role_menu` VALUES (3131, 2, 58);
INSERT INTO `sys_role_menu` VALUES (3132, 2, 124);
INSERT INTO `sys_role_menu` VALUES (3133, 2, 125);
INSERT INTO `sys_role_menu` VALUES (3134, 2, 126);
INSERT INTO `sys_role_menu` VALUES (3135, 2, 127);
INSERT INTO `sys_role_menu` VALUES (3136, 2, 128);
INSERT INTO `sys_role_menu` VALUES (3137, 2, 129);
INSERT INTO `sys_role_menu` VALUES (3138, 2, 130);
INSERT INTO `sys_role_menu` VALUES (3139, 2, 87);
INSERT INTO `sys_role_menu` VALUES (3140, 2, 15);
INSERT INTO `sys_role_menu` VALUES (3141, 2, 19);
INSERT INTO `sys_role_menu` VALUES (3142, 2, 23);
INSERT INTO `sys_role_menu` VALUES (3143, 2, -666666);
INSERT INTO `sys_role_menu` VALUES (3144, 2, 34);
INSERT INTO `sys_role_menu` VALUES (3145, 2, 50);
INSERT INTO `sys_role_menu` VALUES (3146, 2, 44);
INSERT INTO `sys_role_menu` VALUES (3147, 2, 62);
INSERT INTO `sys_role_menu` VALUES (3148, 2, 66);
INSERT INTO `sys_role_menu` VALUES (3149, 2, 101);
INSERT INTO `sys_role_menu` VALUES (3150, 2, 149);
INSERT INTO `sys_role_menu` VALUES (3151, 2, 150);
INSERT INTO `sys_role_menu` VALUES (3152, 2, 131);
INSERT INTO `sys_role_menu` VALUES (3153, 2, 57);
INSERT INTO `sys_role_menu` VALUES (3154, 2, 1);
INSERT INTO `sys_role_menu` VALUES (3155, 2, 86);
INSERT INTO `sys_role_menu` VALUES (3156, 2, 2);
INSERT INTO `sys_role_menu` VALUES (3157, 2, 3);
INSERT INTO `sys_role_menu` VALUES (3158, 2, 4);
INSERT INTO `sys_role_menu` VALUES (3531, 1, 33);
INSERT INTO `sys_role_menu` VALUES (3532, 1, 32);
INSERT INTO `sys_role_menu` VALUES (3533, 1, 60);
INSERT INTO `sys_role_menu` VALUES (3534, 1, 61);
INSERT INTO `sys_role_menu` VALUES (3535, 1, 75);
INSERT INTO `sys_role_menu` VALUES (3536, 1, 76);
INSERT INTO `sys_role_menu` VALUES (3537, 1, 80);
INSERT INTO `sys_role_menu` VALUES (3538, 1, 84);
INSERT INTO `sys_role_menu` VALUES (3539, 1, 85);
INSERT INTO `sys_role_menu` VALUES (3540, 1, 157);
INSERT INTO `sys_role_menu` VALUES (3541, 1, 34);
INSERT INTO `sys_role_menu` VALUES (3542, 1, 35);
INSERT INTO `sys_role_menu` VALUES (3543, 1, 36);
INSERT INTO `sys_role_menu` VALUES (3544, 1, 37);
INSERT INTO `sys_role_menu` VALUES (3545, 1, 38);
INSERT INTO `sys_role_menu` VALUES (3546, 1, 39);
INSERT INTO `sys_role_menu` VALUES (3547, 1, 40);
INSERT INTO `sys_role_menu` VALUES (3548, 1, 41);
INSERT INTO `sys_role_menu` VALUES (3549, 1, 42);
INSERT INTO `sys_role_menu` VALUES (3550, 1, 43);
INSERT INTO `sys_role_menu` VALUES (3551, 1, 69);
INSERT INTO `sys_role_menu` VALUES (3552, 1, 70);
INSERT INTO `sys_role_menu` VALUES (3553, 1, 71);
INSERT INTO `sys_role_menu` VALUES (3554, 1, 50);
INSERT INTO `sys_role_menu` VALUES (3555, 1, 51);
INSERT INTO `sys_role_menu` VALUES (3556, 1, 52);
INSERT INTO `sys_role_menu` VALUES (3557, 1, 53);
INSERT INTO `sys_role_menu` VALUES (3558, 1, 54);
INSERT INTO `sys_role_menu` VALUES (3559, 1, 44);
INSERT INTO `sys_role_menu` VALUES (3560, 1, 45);
INSERT INTO `sys_role_menu` VALUES (3561, 1, 46);
INSERT INTO `sys_role_menu` VALUES (3562, 1, 47);
INSERT INTO `sys_role_menu` VALUES (3563, 1, 48);
INSERT INTO `sys_role_menu` VALUES (3564, 1, 49);
INSERT INTO `sys_role_menu` VALUES (3565, 1, 62);
INSERT INTO `sys_role_menu` VALUES (3566, 1, 63);
INSERT INTO `sys_role_menu` VALUES (3567, 1, 64);
INSERT INTO `sys_role_menu` VALUES (3568, 1, 79);
INSERT INTO `sys_role_menu` VALUES (3569, 1, 66);
INSERT INTO `sys_role_menu` VALUES (3570, 1, 67);
INSERT INTO `sys_role_menu` VALUES (3571, 1, 72);
INSERT INTO `sys_role_menu` VALUES (3572, 1, 73);
INSERT INTO `sys_role_menu` VALUES (3573, 1, 74);
INSERT INTO `sys_role_menu` VALUES (3574, 1, 101);
INSERT INTO `sys_role_menu` VALUES (3575, 1, 103);
INSERT INTO `sys_role_menu` VALUES (3576, 1, 104);
INSERT INTO `sys_role_menu` VALUES (3577, 1, 105);
INSERT INTO `sys_role_menu` VALUES (3578, 1, 106);
INSERT INTO `sys_role_menu` VALUES (3579, 1, 112);
INSERT INTO `sys_role_menu` VALUES (3580, 1, 113);
INSERT INTO `sys_role_menu` VALUES (3581, 1, 120);
INSERT INTO `sys_role_menu` VALUES (3582, 1, 121);
INSERT INTO `sys_role_menu` VALUES (3583, 1, 122);
INSERT INTO `sys_role_menu` VALUES (3584, 1, 123);
INSERT INTO `sys_role_menu` VALUES (3585, 1, 114);
INSERT INTO `sys_role_menu` VALUES (3586, 1, 115);
INSERT INTO `sys_role_menu` VALUES (3587, 1, 116);
INSERT INTO `sys_role_menu` VALUES (3588, 1, 117);
INSERT INTO `sys_role_menu` VALUES (3589, 1, 118);
INSERT INTO `sys_role_menu` VALUES (3590, 1, 119);
INSERT INTO `sys_role_menu` VALUES (3591, 1, 149);
INSERT INTO `sys_role_menu` VALUES (3592, 1, 150);
INSERT INTO `sys_role_menu` VALUES (3593, 1, 151);
INSERT INTO `sys_role_menu` VALUES (3594, 1, 152);
INSERT INTO `sys_role_menu` VALUES (3595, 1, 153);
INSERT INTO `sys_role_menu` VALUES (3596, 1, 154);
INSERT INTO `sys_role_menu` VALUES (3597, 1, 155);
INSERT INTO `sys_role_menu` VALUES (3598, 1, 156);
INSERT INTO `sys_role_menu` VALUES (3599, 1, 77);
INSERT INTO `sys_role_menu` VALUES (3600, 1, 78);
INSERT INTO `sys_role_menu` VALUES (3601, 1, 107);
INSERT INTO `sys_role_menu` VALUES (3602, 1, 108);
INSERT INTO `sys_role_menu` VALUES (3603, 1, 109);
INSERT INTO `sys_role_menu` VALUES (3604, 1, 110);
INSERT INTO `sys_role_menu` VALUES (3605, 1, 111);
INSERT INTO `sys_role_menu` VALUES (3606, 1, 81);
INSERT INTO `sys_role_menu` VALUES (3607, 1, 82);
INSERT INTO `sys_role_menu` VALUES (3608, 1, 83);
INSERT INTO `sys_role_menu` VALUES (3609, 1, 131);
INSERT INTO `sys_role_menu` VALUES (3610, 1, 132);
INSERT INTO `sys_role_menu` VALUES (3611, 1, 133);
INSERT INTO `sys_role_menu` VALUES (3612, 1, 134);
INSERT INTO `sys_role_menu` VALUES (3613, 1, 135);
INSERT INTO `sys_role_menu` VALUES (3614, 1, 161);
INSERT INTO `sys_role_menu` VALUES (3615, 1, 158);
INSERT INTO `sys_role_menu` VALUES (3616, 1, 159);
INSERT INTO `sys_role_menu` VALUES (3617, 1, 160);
INSERT INTO `sys_role_menu` VALUES (3618, 1, 162);
INSERT INTO `sys_role_menu` VALUES (3619, 1, 163);
INSERT INTO `sys_role_menu` VALUES (3620, 1, 164);
INSERT INTO `sys_role_menu` VALUES (3621, 1, 165);
INSERT INTO `sys_role_menu` VALUES (3622, 1, 166);
INSERT INTO `sys_role_menu` VALUES (3623, 1, 91);
INSERT INTO `sys_role_menu` VALUES (3624, 1, 92);
INSERT INTO `sys_role_menu` VALUES (3625, 1, 93);
INSERT INTO `sys_role_menu` VALUES (3626, 1, 94);
INSERT INTO `sys_role_menu` VALUES (3627, 1, 95);
INSERT INTO `sys_role_menu` VALUES (3628, 1, 57);
INSERT INTO `sys_role_menu` VALUES (3629, 1, 58);
INSERT INTO `sys_role_menu` VALUES (3630, 1, 59);
INSERT INTO `sys_role_menu` VALUES (3631, 1, 1);
INSERT INTO `sys_role_menu` VALUES (3632, 1, 86);
INSERT INTO `sys_role_menu` VALUES (3633, 1, 87);
INSERT INTO `sys_role_menu` VALUES (3634, 1, 88);
INSERT INTO `sys_role_menu` VALUES (3635, 1, 89);
INSERT INTO `sys_role_menu` VALUES (3636, 1, 90);
INSERT INTO `sys_role_menu` VALUES (3637, 1, 2);
INSERT INTO `sys_role_menu` VALUES (3638, 1, 15);
INSERT INTO `sys_role_menu` VALUES (3639, 1, 16);
INSERT INTO `sys_role_menu` VALUES (3640, 1, 17);
INSERT INTO `sys_role_menu` VALUES (3641, 1, 18);
INSERT INTO `sys_role_menu` VALUES (3642, 1, 3);
INSERT INTO `sys_role_menu` VALUES (3643, 1, 19);
INSERT INTO `sys_role_menu` VALUES (3644, 1, 20);
INSERT INTO `sys_role_menu` VALUES (3645, 1, 21);
INSERT INTO `sys_role_menu` VALUES (3646, 1, 22);
INSERT INTO `sys_role_menu` VALUES (3647, 1, 4);
INSERT INTO `sys_role_menu` VALUES (3648, 1, 23);
INSERT INTO `sys_role_menu` VALUES (3649, 1, 24);
INSERT INTO `sys_role_menu` VALUES (3650, 1, 25);
INSERT INTO `sys_role_menu` VALUES (3651, 1, 26);
INSERT INTO `sys_role_menu` VALUES (3652, 1, -666666);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `qd_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道码',
  `qd_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '渠道佣金',
  `is_channel` int(11) NULL DEFAULT NULL COMMENT '是否是渠道 1是',
  `sys_user_id` int(11) NULL DEFAULT NULL COMMENT '员工 上级id',
  `zhi_fu_bao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `zhi_fu_bao_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', 'd03aedc2e175f30331116f673b04056502e53d1821e4966e4857518eee787598', 'U0qnh11wzA2bMdJAbELW', '1389585394@163.com', '13612345678', 1, 10, '2016-11-11 11:11:11', NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与角色对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (2, 4, 1);
INSERT INTO `sys_user_role` VALUES (3, 5, 1);
INSERT INTO `sys_user_role` VALUES (5, 6, 1);
INSERT INTO `sys_user_role` VALUES (6, 7, 1);
INSERT INTO `sys_user_role` VALUES (14, 8, 2);
INSERT INTO `sys_user_role` VALUES (16, 2, 1);
INSERT INTO `sys_user_role` VALUES (17, 9, 1);
INSERT INTO `sys_user_role` VALUES (21, 12, 1);
INSERT INTO `sys_user_role` VALUES (23, 13, 1);
INSERT INTO `sys_user_role` VALUES (25, 11, 1);
INSERT INTO `sys_user_role` VALUES (33, 10, 2);
INSERT INTO `sys_user_role` VALUES (34, 14, 2);
INSERT INTO `sys_user_role` VALUES (36, 16, 10);
INSERT INTO `sys_user_role` VALUES (40, 18, 10);
INSERT INTO `sys_user_role` VALUES (44, 19, 4);
INSERT INTO `sys_user_role` VALUES (45, 20, 4);
INSERT INTO `sys_user_role` VALUES (47, 17, 4);
INSERT INTO `sys_user_role` VALUES (50, 21, 4);
INSERT INTO `sys_user_role` VALUES (57, 15, 4);
INSERT INTO `sys_user_role` VALUES (60, 22, 4);
INSERT INTO `sys_user_role` VALUES (63, 26, 4);
INSERT INTO `sys_user_role` VALUES (65, 25, 4);
INSERT INTO `sys_user_role` VALUES (67, 27, 4);
INSERT INTO `sys_user_role` VALUES (68, 28, 4);
INSERT INTO `sys_user_role` VALUES (69, 30, 4);
INSERT INTO `sys_user_role` VALUES (70, 31, 4);
INSERT INTO `sys_user_role` VALUES (71, 1, 1);
INSERT INTO `sys_user_role` VALUES (72, 32, 4);
INSERT INTO `sys_user_role` VALUES (73, 33, 4);
INSERT INTO `sys_user_role` VALUES (74, 24, 4);
INSERT INTO `sys_user_role` VALUES (77, 34, 4);
INSERT INTO `sys_user_role` VALUES (78, 37, 1);

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `token`(`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户Token' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `sex` int(11) NULL DEFAULT NULL COMMENT '性别 1男 2女',
  `open_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信小程序openId',
  `wx_open_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信App  openId',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `apple_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '苹果id',
  `sys_phone` int(11) NULL DEFAULT NULL COMMENT '手机类型 1安卓 2ios',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态 1正常 2禁用',
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源 APP 小程序  公众号',
  `jifen` int(11) NULL DEFAULT NULL COMMENT '积分',
  `invitation_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请码',
  `inviter_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邀请人邀请码',
  `clientid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app推送设备id',
  `zhi_fu_bao_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝名称',
  `zhi_fu_bao` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付宝账号',
  `wx_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信id',
  `on_line_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后一次在线时间',
  `dy_open_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '抖音id',
  `qd_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道码',
  `ks_open_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快手id',
  `is_new_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否是新用户 1否',
  `qd_user_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道用户标识 1级 2级 3级',
  `agency_index` int(255) NULL DEFAULT NULL COMMENT '达人等级 1初级 2高级',
  `agency_end_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '达人到期时间',
  `channel_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道商渠道码',
  `is_channel` int(255) NULL DEFAULT NULL COMMENT '是否是渠道商 1是',
  `is_recommend` int(255) NULL DEFAULT NULL COMMENT '是否是推荐人 1是',
  `id_number_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `id_number_end_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证到期时间',
  `id_number_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证名称',
  `wx_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_name_num` int(11) NULL DEFAULT NULL,
  `avatar_num` int(11) NULL DEFAULT NULL,
  `agency_start_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `auto_price` int(11) NULL DEFAULT NULL COMMENT '是否开启自动扣费 1是',
  `recommend_user_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `invitation_code`(`invitation_code`) USING BTREE,
  INDEX `inviter_code`(`inviter_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13945 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, '官方', '15212345611', 'https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIudXcMWibhibrMLCJ7TCCVOWVhuyoT0BiboVwhfe7S2Z6C2XQWwicLdZMjQWtG1c3rm3rNricC2LNUXYA/132', 1, '', NULL, '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '2021-08-14 11:07:08', '2024-04-12 09:53:51', NULL, NULL, 1, '小程序', NULL, '666666', '666666', NULL, NULL, NULL, NULL, '2024-04-12 09:53:51', NULL, NULL, NULL, NULL, NULL, 1, '2026-04-11 19:12:24', NULL, NULL, NULL, NULL, NULL, NULL, 'https://fanhua.xianmxkj.com/file/uploadPath/2024/04/11/7f8b9368e04bc6db3c41b1203542b5e3.png', 0, 0, '2024-04-11 19:12:24', NULL, NULL);

-- ----------------------------
-- Table structure for url_address
-- ----------------------------
DROP TABLE IF EXISTS `url_address`;
CREATE TABLE `url_address`  (
  `url_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '域名池id',
  `url_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '域名地址',
  `num` int(11) NULL DEFAULT NULL COMMENT '使用次数',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态 1开启 2关闭',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`url_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of url_address
-- ----------------------------
INSERT INTO `url_address` VALUES (1, 'https://duanju.xianmxkj.com', 176, 1, '2024-01-01 00:00:00');
INSERT INTO `url_address` VALUES (3, 'https://duanju.xianmxkj.com', 176, 1, '2024-01-25 11:27:46');

-- ----------------------------
-- Table structure for user_integral
-- ----------------------------
DROP TABLE IF EXISTS `user_integral`;
CREATE TABLE `user_integral`  (
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `integral_num` int(11) NULL DEFAULT NULL COMMENT '积分数量',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_integral
-- ----------------------------

-- ----------------------------
-- Table structure for user_integral_details
-- ----------------------------
DROP TABLE IF EXISTS `user_integral_details`;
CREATE TABLE `user_integral_details`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '积分详情id',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `classify` int(255) NULL DEFAULT NULL COMMENT '获取类型 1签到2报名',
  `type` int(255) NULL DEFAULT NULL COMMENT '分类 1增加 2减少',
  `num` int(11) NULL DEFAULT NULL COMMENT '数量',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `day` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1213 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_integral_details
-- ----------------------------

-- ----------------------------
-- Table structure for user_money
-- ----------------------------
DROP TABLE IF EXISTS `user_money`;
CREATE TABLE `user_money`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '钱包id',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '钱包金额',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `sys_user_id` int(11) NULL DEFAULT NULL COMMENT '渠道用户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 631 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户钱包' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_money
-- ----------------------------

-- ----------------------------
-- Table structure for user_money_details
-- ----------------------------
DROP TABLE IF EXISTS `user_money_details`;
CREATE TABLE `user_money_details`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '钱包详情id',
  `orders_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `sys_user_id` int(11) NULL DEFAULT NULL COMMENT '渠道用户id',
  `by_user_id` int(11) NULL DEFAULT NULL COMMENT '对应用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `classify` int(11) NULL DEFAULT NULL COMMENT '1注册  2首次购买  3购买 4提现',
  `type` int(11) NULL DEFAULT 1 COMMENT '类别（1充值2支出）',
  `state` int(11) NULL DEFAULT 1 COMMENT '状态 1待支付 2已到账 3取消',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '金额',
  `pay_money` decimal(10, 2) NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `create_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2280 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_money_details
-- ----------------------------

-- ----------------------------
-- Table structure for user_vip
-- ----------------------------
DROP TABLE IF EXISTS `user_vip`;
CREATE TABLE `user_vip`  (
  `vip_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员id',
  `vip_name_type` int(1) NULL DEFAULT NULL COMMENT '会员类型0月/1季度/2年',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `is_vip` int(11) NULL DEFAULT NULL COMMENT '是否是会员  1否 2是',
  `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '购买时间',
  `end_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '到期时间',
  `vip_type` int(11) NULL DEFAULT NULL COMMENT '会员类型 1活动赠送 2充值开通 3卡密 4系统赠送',
  PRIMARY KEY (`vip_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 211 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_vip
-- ----------------------------

-- ----------------------------
-- Table structure for vip_details
-- ----------------------------
DROP TABLE IF EXISTS `vip_details`;
CREATE TABLE `vip_details`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `vip_name_type` int(1) NULL DEFAULT NULL COMMENT '会员类型0月/1季/2年',
  `money` decimal(10, 2) NULL DEFAULT NULL COMMENT '会员价格',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vip_details
-- ----------------------------
INSERT INTO `vip_details` VALUES (1, 0, 1.00);

SET FOREIGN_KEY_CHECKS = 1;
