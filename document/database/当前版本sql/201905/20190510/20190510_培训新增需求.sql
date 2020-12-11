CREATE TABLE `cooperate_unit` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `area` varchar(255) DEFAULT NULL COMMENT '地区',
  `linkman` varchar(50) DEFAULT NULL COMMENT '联系人',
  `linkman_position` varchar(255) DEFAULT NULL COMMENT '联系人职位',
  `telephone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `tax_number` varchar(100) DEFAULT NULL COMMENT '纳税人识别号（税号）',
  `cooperate_date` datetime DEFAULT NULL COMMENT '合作日期',
  `flag_cooperate_unit_type` varchar(50) DEFAULT NULL COMMENT '合作单位类型',
  `fk_unit_id` varchar(50) DEFAULT NULL COMMENT '合作单位',
  `division_proportion` varchar(50) DEFAULT NULL COMMENT '分成比例',
  `cooperate_agreement_path` varchar(255) DEFAULT NULL COMMENT '合作协议（附件）',
  `site_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cooperate_unit_fk_unit_id` (`fk_unit_id`),
  KEY `cooperate_unit_fk_cooperate_unit_type` (`flag_cooperate_unit_type`),
  CONSTRAINT `cooperate_unit_fk_cooperate_unit_type` FOREIGN KEY (`flag_cooperate_unit_type`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `cooperate_unit_fk_unit_id` FOREIGN KEY (`fk_unit_id`) REFERENCES `pe_unit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `entrusted_unit` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `flag_entrusted_unit_type` varchar(50) DEFAULT NULL COMMENT '机构类型',
  `area` varchar(255) DEFAULT NULL COMMENT '地区',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `tax_number` varchar(100) DEFAULT NULL COMMENT '纳税人识别号（税号）',
  `site_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `entrusted_unit_fk_entrusted_unit_type` (`flag_entrusted_unit_type`),
  CONSTRAINT `entrusted_unit_fk_entrusted_unit_type` FOREIGN KEY (`flag_entrusted_unit_type`) REFERENCES `enum_const` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `entrusted_unit_linkman` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `telephone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `mobile_number` varchar(50) DEFAULT NULL COMMENT '手机号',
  `job` varchar(255) DEFAULT NULL COMMENT '职务',
  `contacter` varchar(255) DEFAULT NULL COMMENT '接洽人',
  `fk_unit_id` varchar(50) DEFAULT NULL COMMENT '所属单位',
  `site_code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `entrusted_unit_linkman_fk_unit_id` (`fk_unit_id`),
  CONSTRAINT `entrusted_unit_linkman_fk_unit_id` FOREIGN KEY (`fk_unit_id`) REFERENCES `entrusted_unit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `training_certificate` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `flag_training_type` varchar(50) DEFAULT NULL,
  `attach_file_path` varchar(255) DEFAULT NULL COMMENT '证书附件地址',
  `site_code` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `training_certificate_fk_training_type` (`flag_training_type`),
  CONSTRAINT `training_certificate_fk_training_type` FOREIGN KEY (`flag_training_type`) REFERENCES `enum_const` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('6430ec25730011e9a4b4fcaa140ebf84', '机关', '0', 'flagEntrustedUnitType', NULL, NULL, '委托单位类型', ',training,');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('643c982d730011e9a4b4fcaa140ebf84', '事业', '1', 'flagEntrustedUnitType', NULL, NULL, '委托单位类型', ',training,');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('644777df730011e9a4b4fcaa140ebf84', '企业', '2', 'flagEntrustedUnitType', NULL, NULL, '委托单位类型', ',training,');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('64507960730011e9a4b4fcaa140ebf84', '其他', '3', 'flagEntrustedUnitType', NULL, NULL, '委托单位类型', ',training,');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('9b94c2b1730011e9a4b4fcaa140ebf84', '培训机构', '0', 'FlagCooperateUnitType', NULL, NULL, '合作单位类型', ',training,');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('9b9a6e99730011e9a4b4fcaa140ebf84', '高校', '1', 'FlagCooperateUnitType', NULL, NULL, '合作单位类型', ',training,');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('9ba347ad730011e9a4b4fcaa140ebf84', '其他', '2', 'FlagCooperateUnitType', NULL, NULL, '合作单位类型', ',training,');

ALTER TABLE `pe_manager`
ADD COLUMN `flag_department_type`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '部门';

ALTER TABLE `pe_manager` ADD CONSTRAINT `manager_department_type_fk` FOREIGN KEY (`flag_department_type`) REFERENCES `enum_const` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `training_item`
ADD COLUMN `fk_cooperate_unit_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '合作单位';

ALTER TABLE `training_item` ADD CONSTRAINT `training_item_cooperate_unit_fk` FOREIGN KEY (`fk_cooperate_unit_id`) REFERENCES `cooperate_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `training_item`
ADD COLUMN `fk_entrusted_unit_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '委托单位';

ALTER TABLE `training_item` ADD CONSTRAINT `training_item_entrusted_unit_fk` FOREIGN KEY (`fk_entrusted_unit_id`) REFERENCES `entrusted_unit` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

