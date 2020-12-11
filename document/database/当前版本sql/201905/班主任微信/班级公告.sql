/*
Navicat MySQL Data Transfer

Source Server         : 192.168.46.81测试
Source Server Version : 50622
Source Host           : 192.168.46.81:3306
Source Database       : training_dev

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2019-05-29 15:32:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for class_notice
-- ----------------------------
DROP TABLE IF EXISTS `class_notice`;
CREATE TABLE `class_notice` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL COMMENT '标题',
  `flag_message_type` varchar(50) NOT NULL COMMENT '消息类型',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `fk_teacher_id` varchar(32) NOT NULL COMMENT '发送人',
  `fk_class_id` varchar(32) NOT NULL COMMENT '接受班级',
  PRIMARY KEY (`id`),
  KEY `class_notice_message_type_fk` (`flag_message_type`),
  KEY `class_notice_teacher_fk` (`fk_teacher_id`),
  KEY `class_notice_class_fk` (`fk_class_id`),
  CONSTRAINT `class_notice_class_fk` FOREIGN KEY (`fk_class_id`) REFERENCES `pe_class` (`id`),
  CONSTRAINT `class_notice_message_type_fk` FOREIGN KEY (`flag_message_type`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `class_notice_teacher_fk` FOREIGN KEY (`fk_teacher_id`) REFERENCES `pe_teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('4028ae31697b6cc901697b6e5ecf000a', '微信模板消息', '1', 'FlagMessageType', '1', NULL, '发送消息类型', '');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('4028ae31697b6cc901697b6eacd1000a', '站内信消息', '2', 'FlagMessageType', '0', NULL, '发送消息类型', '');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('4028ae3169903ce0016993ae74d0000a', '短信', '4', 'FlagMessageType', '0', NULL, '发送消息类型', '');
