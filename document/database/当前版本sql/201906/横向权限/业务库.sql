/*
Navicat MySQL Data Transfer

Source Server         : 192.168.46.81测试
Source Server Version : 50622
Source Host           : 192.168.46.81:3306
Source Database       : training_dev

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2019-06-14 09:10:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pr_pri_manager_unit
-- ----------------------------
DROP TABLE IF EXISTS `pr_pri_manager_unit`;
CREATE TABLE `pr_pri_manager_unit` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fk_sso_user_id` varchar(50) NOT NULL COMMENT '关联用户',
  `fk_item_id` varchar(50) NOT NULL COMMENT '关联项目',
  PRIMARY KEY (`id`),
  UNIQUE KEY `manager_unit_unique_index` (`fk_sso_user_id`,`fk_item_id`) USING BTREE,
  CONSTRAINT `manager_unit_user_fk` FOREIGN KEY (`fk_sso_user_id`) REFERENCES `sso_user` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
