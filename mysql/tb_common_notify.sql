/*
Navicat MySQL Data Transfer

Source Server         : 192.168.30.3_test
Source Server Version : 50634
Source Host           : 192.168.30.3:3306
Source Database       : ddd

Target Server Type    : MYSQL
Target Server Version : 50634
File Encoding         : 65001

Date: 2017-04-13 10:12:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_common_notify_record
-- ----------------------------
DROP TABLE IF EXISTS `tb_common_notify_record`;
CREATE TABLE `tb_common_notify_record` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `CONCURRENCY_VERSION` int(11) NOT NULL DEFAULT '0' COMMENT '版本事情',
  `CREATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `EDIT_TIME` timestamp NOT NULL COMMENT '最后修改时间',
  `NOTIFY_RULE` varchar(255) NOT NULL COMMENT '通知规则（单位:分钟）',
  `NOTIFY_TIMES` int(11) NOT NULL DEFAULT '0' COMMENT '已通知次数',
  `LIMIT_NOTIFY_TIMES` int(11) NOT NULL DEFAULT '0' COMMENT '最大通知次数限制',
  `URL` varchar(2000) NOT NULL DEFAULT '' COMMENT '通知请求链接（包含通知内容）',
  `STATUS` varchar(50) NOT NULL DEFAULT '' COMMENT '通知状态（对应枚举值）',
  `NOTIFY_TYPE` varchar(30) NOT NULL COMMENT '通知类型',
  `NOTIFY_ID` varchar(32) NOT NULL,
  `NOTIFY_BODY` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='通知记录表 RP_NOTIFY_RECORD';

-- ----------------------------
-- Table structure for tb_common_notify_record_log
-- ----------------------------
DROP TABLE IF EXISTS `tb_common_notify_record_log`;
CREATE TABLE `tb_common_notify_record_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `CONCURRENCY_VERSION` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  `EDIT_TIME` timestamp NOT NULL COMMENT '最后修改时间',
  `CREATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `NOTIFY_ID` varchar(32) NOT NULL DEFAULT '' COMMENT '通知记录ID',
  `REQUEST` varchar(500) NOT NULL DEFAULT '' COMMENT '请求内容',
  `RESPONSE` varchar(1000) NOT NULL DEFAULT '' COMMENT '响应内容',
  `NOTIFY_BODY` varchar(255) NOT NULL DEFAULT '' COMMENT '通知消息体',
  `HTTP_STATUS` varchar(50) NOT NULL COMMENT 'HTTP状态',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='通知记录日志表 RP_NOTIFY_RECORD_LOG';
