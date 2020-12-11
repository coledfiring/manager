/*
Navicat MySQL Data Transfer

Source Server         : 192.168.46.81测试
Source Server Version : 50622
Source Host           : 192.168.46.81:3306
Source Database       : training_dev

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2019-06-18 15:14:14
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for live_course
-- ----------------------------
DROP TABLE IF EXISTS `live_course`;
CREATE TABLE `live_course` (
  `id` varchar(32) NOT NULL,
  `fk_class_id` varchar(32) NOT NULL COMMENT '所属班级',
  `course_name` varchar(50) DEFAULT NULL COMMENT '课程',
  `exam_paper_code` varchar(50) DEFAULT NULL COMMENT '试卷编号',
  `flag_active` varchar(50) NOT NULL COMMENT '是否有效',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `fk_teacher_id` varchar(32) DEFAULT NULL COMMENT '直播教师',
  `live_url` varchar(255) NOT NULL COMMENT '直播地址',
  `create_by` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `live_course_class_fk` (`fk_class_id`),
  KEY `live_course_active_fk` (`flag_active`),
  CONSTRAINT `live_course_active_fk` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `live_course_class_fk` FOREIGN KEY (`fk_class_id`) REFERENCES `pe_class` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='直播课';

ALTER TABLE live_course DROP COLUMN course_name;
ALTER TABLE live_course ADD COLUMN fk_course_id varchar(32) DEFAULT NULL COMMENT '对应课程';
ALTER TABLE live_course ADD CONSTRAINT live_course_course_fk FOREIGN KEY (fk_course_id) REFERENCES pe_course(id);