SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for prepare_item
-- ----------------------------
DROP TABLE IF EXISTS `prepare_item`;
CREATE TABLE `prepare_item` (
  `id` varchar(32) NOT NULL,
  `name` varchar(20) NOT NULL COMMENT '名称',
  `flag_prepare_type` varchar(50) NOT NULL COMMENT '准备类型',
  `flag_training_type` varchar(50) NOT NULL COMMENT '培训种类',
  `create_date` datetime NOT NULL,
  `create_by` varchar(50) NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `site_code` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `prepare_item_unique_index` (`site_code`,`name`),
  KEY `prepare_item_prepare_type_fk` (`flag_prepare_type`),
  KEY `prepare_item_training_type_fk` (`flag_training_type`),
  CONSTRAINT `prepare_item_prepare_type_fk` FOREIGN KEY (`flag_prepare_type`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `prepare_item_training_type_fk` FOREIGN KEY (`flag_training_type`) REFERENCES `enum_const` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教师需要准备的物品';

-- ----------------------------
-- Records of prepare_item
-- ----------------------------

-- ----------------------------
-- Table structure for teacher_prepare_item
-- ----------------------------
DROP TABLE IF EXISTS `teacher_prepare_item`;
CREATE TABLE `teacher_prepare_item` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fk_prepare_item_id` varchar(32) NOT NULL COMMENT '准备工作外键',
  `fk_teacher_id` varchar(50) NOT NULL COMMENT '教师',
  `is_prepare` char(1) NOT NULL COMMENT '是否准备',
  `operate_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `prepare_item_prepare_item_fk` (`fk_prepare_item_id`,`fk_teacher_id`) USING BTREE,
  KEY `prepare_item_teacher_fk` (`fk_teacher_id`),
  CONSTRAINT `prepare_item_prepare_item_fk` FOREIGN KEY (`fk_prepare_item_id`) REFERENCES `prepare_item` (`id`),
  CONSTRAINT `prepare_item_teacher_fk` FOREIGN KEY (`fk_teacher_id`) REFERENCES `pe_teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='教师准备物品状态';

INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('d6402a52806c11e9a4b4fcaa140ebf84', '训前', '1', 'flagPrepareType', '1', '2019-05-27 18:47:35', '教师准备物品的阶段类型', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('d643c9c3806c11e9a4b4fcaa140ebf84', '训中', '2', 'flagPrepareType', '1', '2019-05-27 18:47:35', '教师准备物品的阶段类型', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('d6477d59806c11e9a4b4fcaa140ebf84', '训后', '3', 'flagPrepareType', '1', '2019-05-27 18:47:35', '教师准备物品的阶段类型', NULL);
