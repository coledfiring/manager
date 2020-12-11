/*
Navicat MySQL Data Transfer

Source Server         : 192.168.46.81测试
Source Server Version : 50622
Source Host           : 192.168.46.81:3306
Source Database       : training_dev

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2019-06-03 14:26:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for temporary_use_place
-- ----------------------------
DROP TABLE IF EXISTS `temporary_use_place`;
CREATE TABLE `temporary_use_place` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `use_date` date NOT NULL COMMENT '使用日期',
  `fk_course_time_id` varchar(32) NOT NULL COMMENT '时间段',
  `application` varchar(50) NOT NULL COMMENT '用途',
  `fk_place_id` varchar(32) NOT NULL COMMENT '地点',
  `color` varchar(10) NOT NULL COMMENT '颜色',
  `create_by` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `temporary_use_place_course_time_fk` (`fk_course_time_id`),
  KEY `temporary_use_place_id` (`fk_place_id`),
  CONSTRAINT `temporary_use_place_course_time_fk` FOREIGN KEY (`fk_course_time_id`) REFERENCES `course_time` (`id`),
  CONSTRAINT `temporary_use_place_id` FOREIGN KEY (`fk_place_id`) REFERENCES `pe_place` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
