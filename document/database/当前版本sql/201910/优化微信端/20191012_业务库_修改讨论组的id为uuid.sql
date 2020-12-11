
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for talk_chat_record
-- ----------------------------
DROP TABLE IF EXISTS `talk_chat_record`;

-- ----------------------------
-- Table structure for talk_chat_record_detail
-- ----------------------------
DROP TABLE IF EXISTS `talk_chat_record_detail`;

-- ----------------------------
-- Table structure for talk_group
-- ----------------------------
DROP TABLE IF EXISTS `talk_group`;
DROP TABLE IF EXISTS `talk_group_bulletin`;
DROP TABLE IF EXISTS `talk_member`;
CREATE TABLE `talk_group` (
  `id` varchar(32) NOT NULL,
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
) ENGINE=InnoDB CHARSET=utf8 COMMENT='讨论组';

-- ----------------------------
-- Table structure for talk_group_bulletin
-- ----------------------------
CREATE TABLE `talk_group_bulletin` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL COMMENT '公告内容',
  `fk_group_id` varchar(32) NOT NULL COMMENT '所属组',
  `modify_date` datetime NOT NULL COMMENT '修改日期',
  `modify_by` varchar(50) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `talk_group_bulletin_group_id` (`fk_group_id`),
  CONSTRAINT `talk_group_bulletin_group_id` FOREIGN KEY (`fk_group_id`) REFERENCES `talk_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='讨论组公告';

-- ----------------------------
-- Table structure for talk_member
-- ----------------------------
CREATE TABLE `talk_member` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fk_sso_user_id` varchar(50) NOT NULL COMMENT '关联用户',
  `flag_talk_role_type` varchar(50) NOT NULL COMMENT '角色类型',
  `join_time` datetime NOT NULL COMMENT '加入时间',
  `is_delete` char(1) NOT NULL COMMENT '是否删除',
  `flag_allow_talk` varchar(50) NOT NULL COMMENT '是否允许发言',
  `create_by` varchar(50) NOT NULL COMMENT '创建人',
  `fk_group_id` varchar(32) NOT NULL COMMENT '所属组',
  PRIMARY KEY (`id`),
  KEY `talk_member_group_id` (`fk_group_id`),
  CONSTRAINT `talk_member_group_id` FOREIGN KEY (`fk_group_id`) REFERENCES `talk_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='讨论组成员';
