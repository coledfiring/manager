CREATE TABLE `requirement_info` (
  `id` varchar(50) NOT NULL,
  `serial` varchar(16) NOT NULL DEFAULT '' COMMENT '需求编号',
  `customer` varchar(50) NOT NULL DEFAULT '' COMMENT '客户',
  `area` varchar(255) DEFAULT '' COMMENT '地区',
  `requirement_info` varchar(255) DEFAULT '' COMMENT '需求说明',
  `linkman` varchar(50) NOT NULL DEFAULT '' COMMENT '联系人',
  `link_phone` varchar(255) NOT NULL DEFAULT '' COMMENT '联系电话',
  `flag_training_target` varchar(50) NOT NULL COMMENT '培训对象',
  `flag_info_source` varchar(50) NOT NULL COMMENT '需求来源',
  `fk_create_user_id` varchar(50) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `fk_follow_up_user_id` varchar(50) DEFAULT NULL COMMENT '跟进人',
  `accept_time` datetime DEFAULT NULL COMMENT '接受时间',
  `flag_follow_up_status` varchar(50) DEFAULT NULL COMMENT '跟进状态',
  `flag_requirement_status` varchar(50) DEFAULT NULL COMMENT '需求状态',
  `training_person_number` int(5) DEFAULT NULL COMMENT '培训人数',
  `training_day_number` int(5) DEFAULT NULL COMMENT '培训天数',
  `daily_training_fee` decimal(7,2) DEFAULT NULL COMMENT '日培训费',
  `daily_room_fee` decimal(7,2) DEFAULT NULL COMMENT '日住宿费',
  `daily_transport_fee` decimal(7,2) DEFAULT NULL COMMENT '日交通费',
  `daily_food_fee` decimal(7,2) DEFAULT NULL COMMENT '日餐费',
  `daily_tea_break_fee` decimal(7,2) DEFAULT NULL COMMENT '日茶歇费',
  `other_fee` decimal(7,2) DEFAULT NULL COMMENT '其他费用',
  `note` varchar(255) DEFAULT '' COMMENT '备注',
  `site_code` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `requirement_info_serial_unique` (`serial`) USING BTREE,
  KEY `requirement_info_fk_training_target` (`flag_training_target`),
  KEY `requirement_info_fk_info_source` (`flag_info_source`),
  KEY `requirement_info_fk_create_user_id` (`fk_create_user_id`),
  KEY `requirement_info_fk_follow_up_user_id` (`fk_follow_up_user_id`),
  KEY `requirement_info_fk_requirement_status` (`flag_requirement_status`),
  KEY `requirement_info_fk_follow_up_status` (`flag_follow_up_status`),
  CONSTRAINT `requirement_info_fk_create_user_id` FOREIGN KEY (`fk_create_user_id`) REFERENCES `pe_manager` (`id`),
  CONSTRAINT `requirement_info_fk_follow_up_status` FOREIGN KEY (`flag_follow_up_status`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `requirement_info_fk_follow_up_user_id` FOREIGN KEY (`fk_follow_up_user_id`) REFERENCES `pe_manager` (`id`),
  CONSTRAINT `requirement_info_fk_info_source` FOREIGN KEY (`flag_info_source`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `requirement_info_fk_requirement_status` FOREIGN KEY (`flag_requirement_status`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `requirement_info_fk_training_target` FOREIGN KEY (`flag_training_target`) REFERENCES `enum_const` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `requirement_follow_up_info` (
  `id` varchar(50) NOT NULL,
  `fk_requirement_info_id` varchar(50) DEFAULT NULL COMMENT '需求信息',
  `follow_up_target` varchar(50) DEFAULT '' COMMENT '跟进对象',
  `follow_up_time` datetime DEFAULT NULL COMMENT '跟进时间',
  `follow_up_content` varchar(255) DEFAULT NULL COMMENT '跟进内容',
  `follow_up_way` varchar(50) DEFAULT '' COMMENT '跟进方式',
  `follow_up_result` varchar(255) DEFAULT '' COMMENT '跟进结果',
  `note` varchar(255) DEFAULT '' COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `fk_follow_up_user_id` varchar(50) NOT NULL COMMENT '跟进人',
  PRIMARY KEY (`id`),
  KEY `follow_up_info_fk_requirement_info_id` (`fk_requirement_info_id`),
  KEY `follow_up_info_fk_user_id` (`fk_follow_up_user_id`),
  CONSTRAINT `follow_up_info_fk_requirement_info_id` FOREIGN KEY (`fk_requirement_info_id`) REFERENCES `requirement_info` (`id`),
  CONSTRAINT `follow_up_info_fk_user_id` FOREIGN KEY (`fk_follow_up_user_id`) REFERENCES `pe_manager` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('a80f0598ae7b11e9a734fcaa140ebf84', '待跟进', '1', 'FlagFollowUpStatus', '0', NULL, NULL, NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('a8131627ae7b11e9a734fcaa140ebf84', '跟进中', '2', 'FlagFollowUpStatus', '0', NULL, NULL, NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('a817273eae7b11e9a734fcaa140ebf84', '已签约', '3', 'FlagFollowUpStatus', '0', NULL, NULL, NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('a81b778fae7b11e9a734fcaa140ebf84', '失败', '4', 'FlagFollowUpStatus', '0', NULL, NULL, NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('a804f6f7ae7b11e9a734fcaa140ebf84', '待分派', '1', 'FlagRequirementStatus', '0', NULL, NULL, NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('a80849ebae7b11e9a734fcaa140ebf84', '已分派', '2', 'FlagRequirementStatus', '0', NULL, NULL, NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('a80be697ae7b11e9a734fcaa140ebf84', '已接受', '3', 'FlagRequirementStatus', '0', NULL, NULL, NULL);
