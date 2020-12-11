
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for class_sign
-- ----------------------------
DROP TABLE IF EXISTS `class_sign`;
CREATE TABLE `class_sign` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fk_class_id` varchar(32) NOT NULL COMMENT '签到单对应班级',
  `sign_date` char(10) NOT NULL COMMENT '签到日期',
  `start_time` char(8) NOT NULL COMMENT '签到开始时间',
  `end_time` char(8) NOT NULL COMMENT '签到结束时间',
  `create_by` varchar(50) NOT NULL COMMENT '创建人',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `class_sign_class_fk` (`fk_class_id`),
  CONSTRAINT `class_sign_class_fk` FOREIGN KEY (`fk_class_id`) REFERENCES `pe_class` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for class_sign_student
-- ----------------------------
DROP TABLE IF EXISTS `class_sign_student`;
CREATE TABLE `class_sign_student` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fk_class_sign_id` int(9) NOT NULL,
  `fk_student_id` varchar(32) NOT NULL,
  `sign_time` datetime DEFAULT NULL,
  `flag_is_late` varchar(50) NOT NULL COMMENT '迟到',
  `flag_leave_early` varchar(50) NOT NULL COMMENT '早退',
  `flag_is_cribbing` varchar(50) NOT NULL COMMENT '作弊',
  PRIMARY KEY (`id`),
  UNIQUE KEY `class_sign_student_unque_index` (`fk_student_id`,`fk_class_sign_id`),
  KEY `class_sign_student_sign_fk` (`fk_class_sign_id`),
  CONSTRAINT `class_sign_student_sign_fk` FOREIGN KEY (`fk_class_sign_id`) REFERENCES `class_sign` (`id`),
  CONSTRAINT `class_sign_student_student_fk` FOREIGN KEY (`fk_student_id`) REFERENCES `pe_student` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('f14553d48c1f11e9a4b4fcaa140ebf84', '否', '0', 'flagIsCribbing', '0', '2019-06-11 16:07:23', '签到单是否作弊', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('f13ec0478c1f11e9a4b4fcaa140ebf84', '是', '1', 'flagIsCribbing', '0', '2019-06-11 16:07:23', '签到单是否作弊', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('f12c26938c1f11e9a4b4fcaa140ebf84', '否', '0', 'flagIsLate', '0', '2019-06-11 16:07:22', '签到单是否迟到', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('f123d50d8c1f11e9a4b4fcaa140ebf84', '是', '1', 'flagIsLate', '0', '2019-06-11 16:07:22', '签到单是否迟到', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('f13846478c1f11e9a4b4fcaa140ebf84', '否', '0', 'flagLeaveEarly', '0', '2019-06-11 16:07:23', '签到单是否早退', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('f132557a8c1f11e9a4b4fcaa140ebf84', '是', '1', 'flagLeaveEarly', '0', '2019-06-11 16:07:22', '签到单是否早退', NULL);
