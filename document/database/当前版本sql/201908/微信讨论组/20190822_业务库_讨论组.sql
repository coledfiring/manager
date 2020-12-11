
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `talk_chat_record`;
CREATE TABLE `talk_chat_record` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT,
  `fk_group_id` int(9) NOT NULL COMMENT '所属组',
  `flag_chat_type` varchar(50) NOT NULL COMMENT '记录类型',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `is_delete` char(1) NOT NULL COMMENT '是否删除',
  `fk_member_id` int(9) NOT NULL COMMENT '所属成员',
  `short_cut_content` varchar(50) NOT NULL COMMENT '缩减内容（用于历史记录与组列表显示的缩减内容）',
  PRIMARY KEY (`id`),
  KEY `talk_chat_record_member_fk` (`fk_member_id`),
  KEY `talk_chat_record_chat_type_fk` (`flag_chat_type`),
  KEY `talk_chat_record_group_fk` (`fk_group_id`,`is_delete`,`send_time`,`short_cut_content`) USING BTREE,
  CONSTRAINT `talk_chat_record_chat_type_fk` FOREIGN KEY (`flag_chat_type`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `talk_chat_record_group_fk` FOREIGN KEY (`fk_group_id`) REFERENCES `talk_group` (`id`),
  CONSTRAINT `talk_chat_record_member_fk` FOREIGN KEY (`fk_member_id`) REFERENCES `talk_member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='聊天记录';

-- ----------------------------
-- Table structure for talk_chat_record_detail
-- ----------------------------
DROP TABLE IF EXISTS `talk_chat_record_detail`;
CREATE TABLE `talk_chat_record_detail` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL COMMENT '内容',
  `fk_group_id` int(9) NOT NULL COMMENT '所属组',
  `fk_chat_record_id` bigint(12) NOT NULL COMMENT '所属聊天记录',
  PRIMARY KEY (`id`),
  KEY `talk_chat_record_detail_record_id` (`fk_chat_record_id`),
  KEY `talk_chat_record_detail_group_id` (`fk_group_id`),
  CONSTRAINT `talk_chat_record_detail_group_id` FOREIGN KEY (`fk_group_id`) REFERENCES `talk_group` (`id`),
  CONSTRAINT `talk_chat_record_detail_record_id` FOREIGN KEY (`fk_chat_record_id`) REFERENCES `talk_chat_record` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='聊天记录详情';

-- ----------------------------
-- Table structure for talk_group
-- ----------------------------
DROP TABLE IF EXISTS `talk_group`;
CREATE TABLE `talk_group` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `flag_allow_talk` varchar(50) NOT NULL COMMENT '是否允许交流',
  `end_time` datetime DEFAULT NULL COMMENT '截止时间',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(50) NOT NULL COMMENT '创建人',
  `is_delete` char(1) NOT NULL COMMENT '是否删除',
  `item_id` varchar(50) DEFAULT NULL COMMENT '关联元素（根据type的不同关联的表不同）',
  `flag_talk_group_type` varchar(50) NOT NULL COMMENT '讨论类型',
  `site_code` varchar(50) NOT NULL COMMENT '战点编号',
  `profile_picture` varchar(255) DEFAULT NULL COMMENT '讨论组头像',
  `last_content` varchar(50) DEFAULT NULL COMMENT '最后一条内容',
  `last_send_time` datetime DEFAULT NULL COMMENT '最后一条内容的发送时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='讨论组';

-- ----------------------------
-- Table structure for talk_group_bulletin
-- ----------------------------
DROP TABLE IF EXISTS `talk_group_bulletin`;
CREATE TABLE `talk_group_bulletin` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL COMMENT '公告内容',
  `fk_group_id` int(9) NOT NULL COMMENT '所属组',
  `modify_date` datetime NOT NULL COMMENT '修改日期',
  `modify_by` varchar(50) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `talk_group_bulletin_group_id` (`fk_group_id`),
  CONSTRAINT `talk_group_bulletin_group_id` FOREIGN KEY (`fk_group_id`) REFERENCES `talk_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='讨论组公告';

-- ----------------------------
-- Table structure for talk_member
-- ----------------------------
DROP TABLE IF EXISTS `talk_member`;
CREATE TABLE `talk_member` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fk_sso_user_id` varchar(50) NOT NULL COMMENT '关联用户',
  `flag_talk_role_type` varchar(50) NOT NULL COMMENT '角色类型',
  `join_time` datetime NOT NULL COMMENT '加入时间',
  `is_delete` char(1) NOT NULL COMMENT '是否删除',
  `flag_allow_talk` varchar(50) NOT NULL COMMENT '是否允许发言',
  `create_by` varchar(50) NOT NULL COMMENT '创建人',
  `fk_group_id` int(9) NOT NULL COMMENT '所属组',
  PRIMARY KEY (`id`),
  KEY `talk_member_group_id` (`fk_group_id`),
  CONSTRAINT `talk_member_group_id` FOREIGN KEY (`fk_group_id`) REFERENCES `talk_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=197 DEFAULT CHARSET=utf8 COMMENT='讨论组成员';

INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('e2e055bdc25c11e9a734fcaa140ebf84', '文本', '1', 'flagChatType', '0', '2019-08-19 16:39:41', '讨论组聊天记录类型', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('e2e6e6bec25c11e9a734fcaa140ebf84', '图片', '2', 'flagChatType', '0', '2019-08-19 16:39:41', '讨论组聊天记录类型', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('e2ec7dc1c25c11e9a734fcaa140ebf84', '文件', '3', 'flagChatType', '0', '2019-08-19 16:39:41', '讨论组聊天记录类型', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('97865201c19f11e9a734fcaa140ebf84', '拥有者', '1', 'flagTalkRoleType', '0', '2019-08-18 18:04:39', '讨论组角色', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('978bcdf0c19f11e9a734fcaa140ebf84', '普通成员', '2', 'flagTalkRoleType', '0', '2019-08-18 18:04:39', '讨论组角色', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('40a1f2c1c18211e9a734fcaa140ebf84', '是', '1', 'FlagAllowTalk', '0', '2019-08-18 14:34:38', '讨论组是否允许发言', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('40aeaa9dc18211e9a734fcaa140ebf84', '否', '0', 'FlagAllowTalk', '0', '2019-08-18 14:34:38', '讨论组是否允许发言', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('287fa6d4c16911e9a734fcaa140ebf84', '班级讨论', '1', 'flagTalkGroupType', '0', '2019-08-18 11:35:00', '讨论组类型', NULL);
