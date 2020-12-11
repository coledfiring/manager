/*
Navicat MySQL Data Transfer

Source Server         : 192.168.46.81测试
Source Server Version : 50622
Source Host           : 192.168.46.81:3306
Source Database       : risen_dev

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2019-05-30 10:54:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pe_bulletin
-- ----------------------------
DROP TABLE IF EXISTS `pe_bulletin`;
CREATE TABLE `pe_bulletin` (
  `ID` varchar(50) NOT NULL DEFAULT '',
  `FK_MANAGER_ID` varchar(50) DEFAULT NULL,
  `FLAG_ISVALID` varchar(50) DEFAULT NULL,
  `FLAG_ISTOP` varchar(50) DEFAULT NULL,
  `TITLE` text,
  `PUBLISH_DATE` datetime DEFAULT NULL,
  `UPDATE_DATE` datetime DEFAULT NULL,
  `SCOPE_STRING` text,
  `NOTE` longtext,
  `FK_SSO_USER_ID` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公告：公告信息表';

-- ----------------------------
-- Table structure for pe_bulletin_class
-- ----------------------------
DROP TABLE IF EXISTS `pe_bulletin_class`;
CREATE TABLE `pe_bulletin_class` (
  `ID` varchar(50) NOT NULL,
  `FK_MANAGER_ID` varchar(50) DEFAULT NULL,
  `FLAG_ISVALID` varchar(50) DEFAULT NULL,
  `FLAG_ISTOP` varchar(50) DEFAULT NULL,
  `TITLE` varchar(500) NOT NULL,
  `PUBLISH_DATE` date DEFAULT NULL,
  `UPDATE_DATE` date DEFAULT NULL,
  `SCOPE_STRING` varchar(4000) DEFAULT NULL,
  `NOTE` text,
  `FK_SITEMANAGER_ID` varchar(50) DEFAULT NULL,
  `FK_CLASS_ID` varchar(50) DEFAULT NULL,
  `FK_TEACHER_ID` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pr_bulletin_user
-- ----------------------------
DROP TABLE IF EXISTS `pr_bulletin_user`;
CREATE TABLE `pr_bulletin_user` (
  `ID` varchar(50) NOT NULL,
  `FK_SSO_USER_ID` varchar(50) DEFAULT NULL,
  `FK_BULLETIN_ID` varchar(50) DEFAULT NULL,
  `READ_TYPE` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
