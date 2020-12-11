INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('e1e2cf4e02b711eabd12fcaa140ebf84', '培训指南', '5', 'FlagItemExtendType', '0', '2019-05-30 16:16:58', '班级信息类型', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('e1f1e04a02b711eabd12fcaa140ebf84', '培训要求', '6', 'FlagItemExtendType', '0', '2019-05-30 16:16:58', '班级信息类型', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('e1f73e3602b711eabd12fcaa140ebf84', '学生手册', '7', 'FlagItemExtendType', '0', '2019-05-30 16:16:58', '班级信息类型', NULL);

ALTER TABLE `training_item_extend` DROP FOREIGN KEY `training_item_extend_fk_item_id`;
ALTER TABLE `training_item_extend`
ADD COLUMN `namespace`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展内容业务类型';
UPDATE training_item_extend SET namespace='trainingItem';
ALTER TABLE `training_item_extend`
DROP INDEX `training_item_extend_unique` ,
ADD UNIQUE INDEX `training_item_extend_unique` (`flag_item_extend_type`, `fk_item_id`, `namespace`) USING BTREE ;
