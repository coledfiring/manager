/*
Navicat MySQL Data Transfer

Source Server         : 192.168.46.81测试
Source Server Version : 50622
Source Host           : 192.168.46.81:3306
Source Database       : training_dev

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2019-06-03 14:28:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for course_time
-- ----------------------------
DROP TABLE IF EXISTS `course_time`;
CREATE TABLE `course_time` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `start_time` varchar(5) NOT NULL COMMENT '开始时间',
  `end_time` varchar(5) NOT NULL COMMENT '结束时间',
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  `create_by` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `course_time_unique_index` (`site_code`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course_time
-- ----------------------------
INSERT INTO `course_time` VALUES ('4028ae316b075f0d016b076102fa0000', '上午', '08:00', '11:30', 'training', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-05-30 14:15:27', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-05-30 14:15:49');
INSERT INTO `course_time` VALUES ('4028ae316b075f0d016b076143c60001', '下午', '14:30', '17:00', 'training', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-05-30 14:15:43', null, null);
INSERT INTO `course_time` VALUES ('4028ae316b075f0d016b0761b7da0002', '晚上', '19:30', '21:30', 'training', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-05-30 14:16:13', null, null);


ALTER TABLE class_course_timetable DROP COLUMN start_time;
ALTER TABLE class_course_timetable DROP COLUMN end_time;
ALTER TABLE class_course_timetable ADD COLUMN fk_course_time_id varchar(32) NOT NULL COMMENT '时间段';
ALTER TABLE class_course_timetable ADD CONSTRAINT class_course_timetable_time_fk FOREIGN KEY(fk_course_time_id) REFERENCES course_time(id);
ALTER TABLE class_course_timetable ADD COLUMN training_date date DEFAULT NULL COMMENT '培训日期';
ALTER TABLE class_course_timetable ADD COLUMN color varchar(10) DEFAULT NULL COMMENT '地点在一览表中颜色';