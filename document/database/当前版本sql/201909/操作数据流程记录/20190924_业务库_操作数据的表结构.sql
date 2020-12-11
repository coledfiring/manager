/*
Navicat MySQL Data Transfer

Source Server         : 192.168.46.81测试
Source Server Version : 50622
Source Host           : 192.168.46.81:3306
Source Database       : training_dev

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2019-09-24 10:53:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for operate_status_record
-- ----------------------------
DROP TABLE IF EXISTS `operate_status_record`;
CREATE TABLE `operate_status_record` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `operate_name` varchar(50) NOT NULL COMMENT '操作名称',
  `name_space` varchar(50) NOT NULL COMMENT '命名空间',
  `site_code` varchar(20) NOT NULL COMMENT '站点编号',
  `fk_link_id` varchar(50) NOT NULL COMMENT '关联id',
  `operate_date` varchar(20) NOT NULL COMMENT '操作日期',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  `server_ip` varchar(15) DEFAULT NULL COMMENT '服务端ip',
  `client_ip` varchar(15) DEFAULT NULL COMMENT '客户端ip',
  `fk_operate_user_id` varchar(50) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`),
  KEY `operate_status_record_name_space_index` (`name_space`,`site_code`),
  KEY `operate_status_record_link_index` (`fk_link_id`,`site_code`),
  KEY `operate_status_record_operate_date_index` (`operate_date`,`site_code`),
  KEY `operate_status_record_operate_user_index` (`fk_operate_user_id`,`site_code`),
  KEY `operate_status_record_site_code_index` (`site_code`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='操作数据状态记录';

-- ----------------------------
-- Table structure for operate_status_record_detail
-- ----------------------------
DROP TABLE IF EXISTS `operate_status_record_detail`;
CREATE TABLE `operate_status_record_detail` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fk_operate_record_status_id` int(9) NOT NULL COMMENT '关联记录id',
  `status_data` text NOT NULL COMMENT '状态数据',
  PRIMARY KEY (`id`),
  KEY `operate_status_record_fk` (`fk_operate_record_status_id`),
  CONSTRAINT `operate_status_record_fk` FOREIGN KEY (`fk_operate_record_status_id`) REFERENCES `operate_status_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

alter table operate_status_record_detail add column type varchar(10) NOT NULL COMMENT '更改类型';
alter table operate_status_record_detail add index record_detail_type_index(type);