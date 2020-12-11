/*
Navicat MySQL Data Transfer

Source Server         : 192.168.46.81@成教测试库
Source Server Version : 50622
Source Host           : 192.168.46.81:3306
Source Database       : training_dev

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2019-04-17 16:46:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for custom_analyse
-- ----------------------------
DROP TABLE IF EXISTS `custom_analyse`;
CREATE TABLE `custom_analyse` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `custom_analyse_config` text COMMENT '自定义图表配置',
  `fk_chart_def_id` varchar(32) NOT NULL COMMENT '包含的图表id',
  `fk_grid_id` varchar(50) NOT NULL COMMENT '包含的gridid',
  `flag_active` varchar(50) NOT NULL COMMENT '是否有效',
  `fk_custom_analyse_group_id` int(5) NOT NULL COMMENT '关联的自定义统计组',
  `flag_analyse_view_level` varchar(50) NOT NULL COMMENT '显示级别，如公开、私有',
  `can_view_role_code` varchar(300) DEFAULT '' COMMENT '显示级别为可选角色时，可看的角色code拼接',
  `fk_create_user_id` varchar(50) NOT NULL COMMENT '创建人id',
  PRIMARY KEY (`id`),
  KEY `fk_flag_active` (`flag_active`) USING BTREE,
  KEY `fk_custom_analyse_group` (`fk_custom_analyse_group_id`) USING BTREE,
  CONSTRAINT `custom_analyse_ibfk_1` FOREIGN KEY (`fk_custom_analyse_group_id`) REFERENCES `custom_analyse_group` (`id`),
  CONSTRAINT `custom_analyse_ibfk_2` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义统计配置';

-- ----------------------------
-- Records of custom_analyse
-- ----------------------------

-- ----------------------------
-- Table structure for custom_analyse_condition_config
-- ----------------------------
DROP TABLE IF EXISTS `custom_analyse_condition_config`;
CREATE TABLE `custom_analyse_condition_config` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '条件名称',
  `key` varchar(50) NOT NULL COMMENT '编号',
  `type` varchar(20) NOT NULL COMMENT '条件类型',
  `option_sql` varchar(255) DEFAULT NULL COMMENT '查询项的sql',
  `helper` varchar(50) DEFAULT NULL COMMENT '帮助文档',
  `fk_custom_analyse_group_id` int(5) NOT NULL COMMENT '所属分析组',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义统计条件配置';

-- ----------------------------
-- Records of custom_analyse_condition_config
-- ----------------------------

-- ----------------------------
-- Table structure for custom_analyse_group
-- ----------------------------
DROP TABLE IF EXISTS `custom_analyse_group`;
CREATE TABLE `custom_analyse_group` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `flag_active` varchar(50) NOT NULL COMMENT '是否有效',
  `grid_sql` text COMMENT 'grid查询sql',
  `charts_sql` text COMMENT '统计查询sql',
  `column_sql` text COMMENT '查询横轴项sql',
  `code` varchar(50) NOT NULL COMMENT '编号',
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `analyse_group_unique_index` (`code`,`site_code`),
  KEY `fk_enum_const_active` (`flag_active`) USING BTREE,
  CONSTRAINT `custom_analyse_group_ibfk_1` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义统计组';

-- ----------------------------
-- Records of custom_analyse_group
-- ----------------------------

-- ----------------------------
-- Table structure for custom_analyse_item_config
-- ----------------------------
DROP TABLE IF EXISTS `custom_analyse_item_config`;
CREATE TABLE `custom_analyse_item_config` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `data_index` varchar(50) DEFAULT NULL,
  `code` varchar(50) NOT NULL COMMENT '统计项值',
  `fk_custom_analyse_group_id` int(5) NOT NULL COMMENT '所属统计组',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义统计统计维度配置';

-- ----------------------------
-- Records of custom_analyse_item_config
-- ----------------------------

-- ----------------------------
-- Table structure for custom_analyse_user_collection
-- ----------------------------
DROP TABLE IF EXISTS `custom_analyse_user_collection`;
CREATE TABLE `custom_analyse_user_collection` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `fk_sso_user_id` varchar(50) NOT NULL,
  `fk_custom_analyse_id` int(5) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_analyse_user` (`fk_sso_user_id`,`fk_custom_analyse_id`) USING BTREE,
  KEY `fk_custom_analyse` (`fk_custom_analyse_id`) USING BTREE,
  CONSTRAINT `custom_analyse_user_collection_ibfk_1` FOREIGN KEY (`fk_custom_analyse_id`) REFERENCES `custom_analyse` (`id`),
  CONSTRAINT `custom_analyse_user_collection_ibfk_2` FOREIGN KEY (`fk_sso_user_id`) REFERENCES `sso_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义统计用户收藏夹';

-- ----------------------------
-- Records of custom_analyse_user_collection
-- ----------------------------

-- ----------------------------
-- Table structure for custom_analyse_x_group_config
-- ----------------------------
DROP TABLE IF EXISTS `custom_analyse_x_group_config`;
CREATE TABLE `custom_analyse_x_group_config` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `value` varchar(50) NOT NULL COMMENT '维度值',
  `data_index` varchar(50) DEFAULT NULL COMMENT '维度的查询索引',
  `fk_custom_analyse_group_id` int(5) NOT NULL COMMENT '关联的自定义统计组',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自定义统计统计项配置';

-- ----------------------------
-- Records of custom_analyse_x_group_config
-- ----------------------------

-- ----------------------------
-- Table structure for data_board
-- ----------------------------
DROP TABLE IF EXISTS `data_board`;
CREATE TABLE `data_board` (
  `id` varchar(50) NOT NULL,
  `module_type` varchar(50) NOT NULL COMMENT '模块类型',
  `module_name` varchar(255) NOT NULL COMMENT '模块名称',
  `module_sql` text COMMENT '模块显示内容查询sql',
  `module_data_type` varchar(50) DEFAULT NULL COMMENT '模块条目类型',
  `module_data_name` varchar(50) DEFAULT NULL COMMENT '模块条目名称',
  `module_data_sql` text COMMENT '模块条目显示内容查询sql',
  `module_data_content` varchar(50) DEFAULT NULL COMMENT '模块条目显示补充内容',
  `url` varchar(255) DEFAULT NULL COMMENT '条目链接',
  `pe_priority_action_Id` varchar(50) DEFAULT NULL COMMENT '条目权限对应的actionID',
  `more_sql` varchar(255) DEFAULT NULL COMMENT '显示更多的sql',
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`id`),
  KEY `data_board_site_code_index` (`site_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据看板';

-- ----------------------------
-- Records of data_board
-- ----------------------------

-- ----------------------------
-- Table structure for enum_const
-- ----------------------------
DROP TABLE IF EXISTS `enum_const`;
CREATE TABLE `enum_const` (
  `ID` varchar(50) NOT NULL COMMENT '主键',
  `NAME` varchar(50) NOT NULL COMMENT '名称',
  `CODE` varchar(50) DEFAULT NULL COMMENT '编号',
  `NAMESPACE` varchar(50) NOT NULL COMMENT '命名空间',
  `IS_DEFAULT` varchar(1) DEFAULT NULL COMMENT '是否默认',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `NOTE` varchar(50) DEFAULT NULL COMMENT '项目编号',
  `TEAM` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `enum_const_unique_index` (`NAMESPACE`,`CODE`,`TEAM`) USING BTREE,
  KEY `ID` (`ID`),
  KEY `ID_2` (`ID`),
  KEY `ID_3` (`ID`),
  KEY `ID_4` (`ID`),
  KEY `ID_5` (`ID`),
  KEY `ID_6` (`ID`),
  KEY `ID_7` (`ID`),
  KEY `ID_8` (`ID`),
  KEY `ID_9` (`ID`),
  KEY `ID_10` (`ID`),
  KEY `ID_11` (`ID`),
  KEY `ID_12` (`ID`),
  KEY `ID_13` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='常量表';

-- ----------------------------
-- Records of enum_const
-- ----------------------------
INSERT INTO `enum_const` VALUES ('00a59243dbc63ab3d57f3fd69760ees9', '是', '1', 'FlagIsStar', '1', '2018-01-11 20:41:13', '通知推送是否星标', null);
INSERT INTO `enum_const` VALUES ('2', '是', '1', 'FlagIsvalid', '0', '2008-11-02 14:04:53', '是否有效', null);
INSERT INTO `enum_const` VALUES ('3', '否', '0', 'FlagIsvalid', '1', '2008-11-02 14:04:53', '是否有效', null);
INSERT INTO `enum_const` VALUES ('377561066d3c11e89e1dfcaa140ebf84', '公开', '1', 'FlagAnalyseViewLevel', '1', '2018-06-11 13:56:43', '显示级别，如公开、私有和选择角色可看', null);
INSERT INTO `enum_const` VALUES ('377868296d3c11e89e1dfcaa140ebf84', '私有', '2', 'FlagAnalyseViewLevel', '1', '2018-06-11 13:56:43', '显示级别，如公开、私有和选择角色可看', null);
INSERT INTO `enum_const` VALUES ('3791408f6d3c11e89e1dfcaa140ebf84', '选择角色可看', '3', 'FlagAnalyseViewLevel', '1', '2018-06-11 13:56:43', '显示级别，如公开、私有和选择角色可看', null);
INSERT INTO `enum_const` VALUES ('4028808c1de7580f011de75f999990001', '否', '0', 'FlagIsClassmaster', '0', '2013-07-23 11:31:01', null, null);
INSERT INTO `enum_const` VALUES ('4028808c1de7580f011de75f999990002', '是', '1', 'FlagIsClassmaster', '0', '2013-07-23 11:31:25', null, null);
INSERT INTO `enum_const` VALUES ('402880951dad22bd011dad5d21fa0005', '教师', '1', 'FlagRoleType', '0', '2008-11-18 00:00:00', '角色类型', null);
INSERT INTO `enum_const` VALUES ('402880951df7720e011df78e5bbb0111', '否', '0', 'FlagBak', '0', '2014-01-24 00:00:00', '是否强制更新', ',adult,');
INSERT INTO `enum_const` VALUES ('402880951df7720e011df78e5bbb0112', '是', '1', 'FlagBak', '1', '2014-01-24 00:00:00', '是否强制更新', ',adult,');
INSERT INTO `enum_const` VALUES ('4028809c1d625bcf011d66fd0dda0006', '是', '1', 'FlagActive', '0', '2008-11-04 00:00:00', '是否有效', null);
INSERT INTO `enum_const` VALUES ('4028809c1d625bcf011d66fdb39f0007', '否', '0', 'FlagActive', '0', '2008-11-04 00:00:00', '是否有效', null);
INSERT INTO `enum_const` VALUES ('402880a923db866c0123db8d377a4324', '基本信息', '0', 'FlagInfoType', '0', '2011-08-17 00:00:00', '学生基本信息', null);
INSERT INTO `enum_const` VALUES ('4028826a1db7bb01011db7e52d130001', '学生', '0', 'FlagRoleType', '0', '2008-11-20 00:00:00', '角色类型', null);
INSERT INTO `enum_const` VALUES ('4028826a1db7bb01011db7e623d70003', '管理员', '3', 'FlagRoleType', '0', '2008-11-20 00:00:00', '角色类型', null);
INSERT INTO `enum_const` VALUES ('4028ae31698f801c01698f8bc69a0002', '未发送', '0', 'FlagSendStatus', '1', null, '发送信息发送状态', '');
INSERT INTO `enum_const` VALUES ('4028ae31698f801c01698f8c062c0003', '发送成功', '1', 'FlagSendStatus', '0', null, '发送信息发送状态', '');
INSERT INTO `enum_const` VALUES ('4028ae31698f801c01698f8c3e500004', '发送失败', '2', 'FlagSendStatus', '0', null, '发送信息发送状态', '');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8d5ce80000', '继教处', '1', 'flagUnitType', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8dbe3f0001', '学院', '2', 'flagUnitType', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8dcebe0002', '其他', '3', 'flagUnitType', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8dfb020003', '校本部', '1', 'flagSchoolZone', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8e100d0004', '全国--市级单位', '1', 'flagTrainingArea', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8e41790006', '干部培训', '1', 'flagTrainingType', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8e51550007', '专业技术培训', '2', 'flagTrainingType', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8e60450008', '职业技能培训', '3', 'flagTrainingType', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8e71060009', '师资培训', '4', 'flagTrainingType', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8e85a3000a', '岗前培训', '5', 'flagTrainingType', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8e9438000b', '其他', '6', 'flagTrainingType', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8eb733000c', '职业人员', '1', 'flagTrainingTarget', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8ec737000d', '学生', '2', 'flagTrainingTarget', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8ed3a1000e', '会计', '3', 'flagTrainingTarget', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8ee23a000f', '教师', '4', 'flagTrainingTarget', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8ef3b40010', '干部', '5', 'flagTrainingTarget', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8f033e0011', '专业技术人员', '6', 'flagTrainingTarget', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8f15a00012', '工人', '7', 'flagTrainingTarget', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8f2b7a0013', '其他', '8', 'flagTrainingTarget', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8f526a0014', '实践课', '1', 'flagCourseType', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8f627a0015', '理论课', '2', 'flagCourseType', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8f76640016', '干部', '1', 'flagSuitableTarget', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8f883e0017', '社会', '2', 'flagSuitableTarget', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8f96c60018', '专业技术', '3', 'flagSuitableTarget', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8fa42e0019', '上岗证', '4', 'flagSuitableTarget', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8fba56001a', '讲师', '1', 'flagPositionalTitle', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8fcb1a001b', '教授', '2', 'flagPositionalTitle', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8fdbb7001c', '副教授', '3', 'flagPositionalTitle', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8ff1e0001d', '专科', '1', 'flagEducationalBackground', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b8ffe72001e', '本科', '2', 'flagEducationalBackground', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b900f6b001f', '硕士研究生', '3', 'flagEducationalBackground', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b9023100020', '博士研究生', '4', 'flagEducationalBackground', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0b8c30016a0b9030e70021', '其他', '5', 'flagEducationalBackground', null, null, null, ',training,');
INSERT INTO `enum_const` VALUES ('4028ae316a0bceb0016a0bd8316d0000', '身份证', '1', 'flagCardType', '1', null, '证件类型', '');
INSERT INTO `enum_const` VALUES ('4028ae316a0bceb0016a0bd87dcb0001', '港澳台证件', '2', 'flagCardType', '0', null, '证件类型', '');
INSERT INTO `enum_const` VALUES ('4028ae316a0bceb0016a0bd94fae0002', '军官证', '3', 'flagCardType', '0', null, '证件类型', '');
INSERT INTO `enum_const` VALUES ('4028ae316a0bceb0016a0bd9821d0003', '其他证件', '4', 'flagCardType', '0', null, '证件类型', '');
INSERT INTO `enum_const` VALUES ('46a6f8d95f2611e89e1dfcaa140ebf84', '是', '1', 'FlagSiteSuperAdmin', '0', '2018-05-24 15:44:23', '是否是站点超管', null);
INSERT INTO `enum_const` VALUES ('46aa648f5f2611e89e1dfcaa140ebf84', '否', '0', 'FlagSiteSuperAdmin', '0', '2018-05-24 15:44:23', '是否是站点超管', null);
INSERT INTO `enum_const` VALUES ('4974dbda-c2fe-11e8-a2c3-fcaa140ebf84', '平台及信息操作', '1', 'FlagModuleName', '0', '2018-09-28 15:41:39', '操作日志记录，模块名称', null);
INSERT INTO `enum_const` VALUES ('63a2135f-c3ac-11e8-a2c3-fcaa140ebf84', '支付接口', '2', 'FlagModuleName', '0', '2018-09-28 15:41:39', '操作日志记录，模块名称', null);
INSERT INTO `enum_const` VALUES ('72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '管理员用户管理', '3', 'FlagModuleName', '0', '2018-09-28 15:41:39', '操作日志记录，模块名称', null);
INSERT INTO `enum_const` VALUES ('761d60c6da2411e7acea94de80c4da50', '是', '1', 'FlagCourseTeacher', '0', '2017-12-06 09:26:19', null, null);
INSERT INTO `enum_const` VALUES ('7f190d82da2411e7acea94de80c4da50', '否', '0', 'FlagCourseTeacher', '1', '2017-12-06 09:26:34', null, null);
INSERT INTO `enum_const` VALUES ('84a5ac89-6883-11e8-9e1d-fcaa140ebf84', '站点超管管理员', '9998', 'FlagRoleType', '0', '2018-06-05 10:43:07', '角色类型', null);
INSERT INTO `enum_const` VALUES ('85559724-c3d3-11e8-a2c3-fcaa140ebf84', '打印管理', '4', 'FlagModuleName', '0', '2018-09-28 15:41:39', '操作日志记录，模块名称', null);
INSERT INTO `enum_const` VALUES ('8b6c7789da2411e7acea94de80c4da50', '否', '0', 'FlagTutorTeacher', '1', '2017-12-06 09:26:55', null, null);
INSERT INTO `enum_const` VALUES ('90a5bdd6da2411e7acea94de80c4da50', '是', '1', 'FlagTutorTeacher', '0', '2017-12-06 09:27:03', null, null);
INSERT INTO `enum_const` VALUES ('acf44775c4ec11e7bb1db083fedeaab1', '广播', '0', 'flagScopeType', '1', '2017-11-09 09:24:05', '发送类型', null);
INSERT INTO `enum_const` VALUES ('acf44ffdc4ec11e7bb1db083fedeaab1', '单播', '1', 'flagScopeType', '1', '2017-11-09 09:24:05', '发送类型', null);
INSERT INTO `enum_const` VALUES ('acf45747c4ec11e7bb1db083fedeaab1', '用户操作', '0', 'flagNoticeType', '1', '2017-11-09 09:24:05', '通知类型', null);
INSERT INTO `enum_const` VALUES ('acf45e67c4ec11e7bb1db083fedeaab1', '未读', '0', 'flagReaded', '1', '2017-11-09 09:24:05', '是否已读', null);
INSERT INTO `enum_const` VALUES ('acf4655ec4ec11e7bb1db083fedeaab1', '已读', '1', 'flagReaded', '1', '2017-11-09 09:24:05', '是否已读', null);
INSERT INTO `enum_const` VALUES ('e444c346abc23e26ghs7e5fa90ddbbse', '否', '0', 'FlagIsStar', '0', '2018-01-11 20:41:13', '通知推送是否星标', null);
INSERT INTO `enum_const` VALUES ('e6b70a305d0211e9a2c3fcaa140ebf84', '女', '0', 'FlagGender', '0', '2010-07-20 00:00:00', '性别', null);
INSERT INTO `enum_const` VALUES ('e6bcac195d0211e9a2c3fcaa140ebf84', '男', '1', 'FlagGender', '1', '2010-07-20 00:00:00', '性别', null);
INSERT INTO `enum_const` VALUES ('fe04dfef-c30b-11e8-a2c3-fcaa140ebf84', '消息管理', '5', 'FlagModuleName', '0', '2018-09-28 15:41:39', '操作日志记录，模块名称', null);
INSERT INTO `enum_const` VALUES ('ff808081673e54dd016745479d6c000d', '开放接口授权角色', '9000', 'FlagRoleType', '0', null, '角色类型', null);

-- ----------------------------
-- Table structure for operate_record
-- ----------------------------
DROP TABLE IF EXISTS `operate_record`;
CREATE TABLE `operate_record` (
  `id` char(32) NOT NULL,
  `flag_module_id` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL,
  `url` varchar(100) NOT NULL,
  `request_ip` char(15) NOT NULL,
  `fk_user_id` varchar(50) NOT NULL,
  `operate_time` datetime NOT NULL,
  `operate_date` char(10) NOT NULL,
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`id`),
  KEY `fk_module_id` (`flag_module_id`) USING BTREE,
  KEY `index_name` (`name`) USING BTREE,
  KEY `index_url` (`url`) USING BTREE,
  KEY `index_user_id` (`fk_user_id`) USING BTREE,
  KEY `index_operate_date` (`operate_date`) USING BTREE,
  KEY `index_operate_record_site_code` (`site_code`),
  CONSTRAINT `operate_record_ibfk_1` FOREIGN KEY (`flag_module_id`) REFERENCES `enum_const` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作记录';

-- ----------------------------
-- Records of operate_record
-- ----------------------------
INSERT INTO `operate_record` VALUES ('02ceb9501b934538aa5d294722eff768', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '用户角色管理添加', '/npapi/entity/siteManager/siteRoleManage/abstractAdd', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 16:54:25', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('161a4f5d50624ba5877abc6d43abdb0c', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点', '/npapi/entity/siteManager/siteRoleMenuManage/getWebSiteId', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 18:22:43', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('2ad5c47503a04f4e818c7ba606b493ac', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前角色菜单权限', '/npapi/entity/siteManager/siteRoleMenuManage/getRoleGridMenuIds', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 16:59:20', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('3b8b25ab5c374bfe88087a7f664dfb23', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点所有角色', '/npapi/entity/siteManager/siteRoleMenuManage/getAllRoles', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-15 17:34:25', '2019-04-15', 'training');
INSERT INTO `operate_record` VALUES ('3bbe9a9fb98742f09fbaea9c18b1d5dc', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '用户角色管理添加', '/npapi/entity/siteManager/siteRoleManage/abstractAdd', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 16:50:07', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('57c4a3fed1324f5eb0e1540ba3ec3924', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '保存当前角色菜单', '/npapi/entity/siteManager/siteRoleMenuManage/saveRoleMenuIds', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 16:59:27', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('6e42d8c96fd94887b36ff6219fc0c0a0', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点所有角色', '/npapi/entity/siteManager/siteRoleMenuManage/getAllRoles', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 17:09:34', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('74576c392a2844a5ade320e537aa4d41', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点', '/npapi/entity/siteManager/siteRoleMenuManage/getWebSiteId', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-16 11:33:33', '2019-04-16', 'training');
INSERT INTO `operate_record` VALUES ('796e0d984870438d867aca6052dbeddf', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点所有角色', '/npapi/entity/siteManager/siteRoleMenuManage/getAllRoles', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 18:22:43', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('834c65eede584a8ebd8ae67174ff1b79', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点所有角色', '/npapi/entity/siteManager/siteRoleMenuManage/getAllRoles', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 16:59:17', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('8b5994dc9bab40b6adf53c15b62d7501', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前角色菜单', '/npapi/entity/siteManager/siteRoleMenuManage/getRoleMenuIds', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 16:59:20', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('8dda99e1e0884dab9e1ad4a0591b4f9b', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '用户角色管理添加', '/npapi/entity/siteManager/siteRoleManage/abstractAdd', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 16:51:28', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('927e9c38c1c1488ab3315fa03e061f26', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点菜单列表', '/npapi/entity/siteManager/siteRoleMenuManage/treeWithSubMenu', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 16:59:19', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('992ee6f9b4b745e98648576e98a76142', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点所有角色', '/npapi/entity/siteManager/siteRoleMenuManage/getAllRoles', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-15 15:27:44', '2019-04-15', 'training');
INSERT INTO `operate_record` VALUES ('9cef70bcae154e4693b2ac85d2d176e1', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '用户角色管理添加', '/npapi/entity/siteManager/siteRoleManage/abstractAdd', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 16:53:02', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('ae7c98471217431196422dc7c4fb7df5', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '管理员用户管理添加', '/npapi/entity/siteManager/siteManagerManage/abstractAdd', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 18:31:56', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('b37f1c7675fa4599a96f902d1fed7b50', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点所有角色', '/npapi/entity/siteManager/siteRoleMenuManage/getAllRoles', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-16 11:33:33', '2019-04-16', 'training');
INSERT INTO `operate_record` VALUES ('b4d6b3a092b945309346002a46c8c4f3', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '用户角色管理添加', '/npapi/entity/siteManager/siteRoleManage/abstractAdd', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 16:50:27', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('c65533b6c76749cc9c6c5a57ebe96333', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点所有角色', '/npapi/entity/siteManager/siteRoleMenuManage/getAllRoles', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-15 15:57:49', '2019-04-15', 'training');
INSERT INTO `operate_record` VALUES ('cdcf66738bcd4dc58aead921d71a8d7b', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '保存当前角色菜单', '/npapi/entity/siteManager/siteRoleMenuManage/saveRoleMenuIds', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 17:04:30', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('dc8054bb5b524ab3a85188a7e6dc73cf', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点', '/npapi/entity/siteManager/siteRoleMenuManage/getWebSiteId', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-15 17:34:25', '2019-04-15', 'training');
INSERT INTO `operate_record` VALUES ('e26375ec4c3d48f08f663e4f4836ac8e', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点', '/npapi/entity/siteManager/siteRoleMenuManage/getWebSiteId', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 17:09:34', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('e4d493b1ac17454ba8f70a310f01e08a', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点', '/npapi/entity/siteManager/siteRoleMenuManage/getWebSiteId', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-15 15:27:44', '2019-04-15', 'training');
INSERT INTO `operate_record` VALUES ('e886fa33ebd14c94a97096df2c504c3d', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '管理员用户管理添加', '/npapi/entity/siteManager/siteManagerManage/abstractAdd', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 17:11:42', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('eb454db2d5944e2ab9628e27c937b972', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '管理员用户管理添加', '/npapi/entity/siteManager/siteManagerManage/abstractAdd', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 18:34:03', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('f6e4f4d6160f4fb8acd442794c1f862e', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点', '/npapi/entity/siteManager/siteRoleMenuManage/getWebSiteId', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-15 15:57:49', '2019-04-15', 'training');
INSERT INTO `operate_record` VALUES ('fa25def6b69146d190bcb52a16298b11', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '用户角色管理添加', '/npapi/entity/siteManager/siteRoleManage/abstractAdd', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 16:54:55', '2019-04-11', 'training');
INSERT INTO `operate_record` VALUES ('fbec996d3f904cafb6fb72c085e41741', '72dd2c69-c3b3-11e8-a2c3-fcaa140ebf84', '获取当前站点', '/npapi/entity/siteManager/siteRoleMenuManage/getWebSiteId', '127.0.0.1', 'b4e130c9e93b477b8d7acb43dd428f89', '2019-04-11 16:59:17', '2019-04-11', 'training');

-- ----------------------------
-- Table structure for pe_chart_column_def
-- ----------------------------
DROP TABLE IF EXISTS `pe_chart_column_def`;
CREATE TABLE `pe_chart_column_def` (
  `ID` varchar(32) NOT NULL,
  `FK_CHART_DEF_ID` varchar(50) NOT NULL DEFAULT '' COMMENT '所属图标引用',
  `COLUMN_INDEX` int(11) unsigned NOT NULL COMMENT '对应SQL显示引用',
  `TYPE` varchar(20) DEFAULT NULL COMMENT '数据列类型',
  `SERIES_NAME` varchar(50) DEFAULT NULL COMMENT '图表系列类型名称',
  `GROUP_NAME` varchar(50) DEFAULT NULL COMMENT '分组名称',
  `AXIS_INDEX` int(11) DEFAULT NULL,
  `INPUT_DATE` datetime DEFAULT NULL,
  `COLUMN_NAME` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK3C00FBC725E8B040` (`FK_CHART_DEF_ID`) USING BTREE,
  CONSTRAINT `pe_chart_column_def_ibfk_1` FOREIGN KEY (`FK_CHART_DEF_ID`) REFERENCES `pe_chart_def` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='weCharts统计图字段';

-- ----------------------------
-- Records of pe_chart_column_def
-- ----------------------------

-- ----------------------------
-- Table structure for pe_chart_def
-- ----------------------------
DROP TABLE IF EXISTS `pe_chart_def`;
CREATE TABLE `pe_chart_def` (
  `ID` varchar(32) NOT NULL,
  `CHART` varchar(100) NOT NULL COMMENT '图表名称',
  `CODE` varchar(32) NOT NULL COMMENT '图表编码',
  `DATA_DIRECTION` varchar(20) DEFAULT NULL COMMENT '数据方向',
  `HAS_TIME_LINE` tinyint(1) unsigned DEFAULT NULL COMMENT '是否含有时间轴',
  `HAS_DATA_ZOOM` tinyint(1) unsigned DEFAULT NULL COMMENT '是否含有数据域',
  `DATA_ZOOM_START` int(11) DEFAULT NULL COMMENT '数据域开始点',
  `DATA_ZOOM_END` int(11) DEFAULT NULL COMMENT '数据域结束点',
  `ZOOM_LOCK` tinyint(1) unsigned DEFAULT NULL COMMENT '数据域是否锁定',
  `TYPE` varchar(20) DEFAULT NULL COMMENT '图表类型',
  `DATA_INDEX_COLUMN` varchar(100) DEFAULT NULL COMMENT '图例列',
  `VALUE_COLUMNS_STR` varchar(9999) DEFAULT NULL COMMENT '数据列',
  `INPUT_DATE` datetime DEFAULT NULL,
  `chart_sql` longtext,
  `IS_DEL` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否被删除',
  `is_cache` int(1) DEFAULT NULL COMMENT '是否需要缓存',
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `chart_code_unique_index` (`CODE`,`site_code`) USING BTREE,
  KEY `IN_DEL` (`IS_DEL`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='weCharts统计图';

-- ----------------------------
-- Records of pe_chart_def
-- ----------------------------

-- ----------------------------
-- Table structure for pe_course
-- ----------------------------
DROP TABLE IF EXISTS `pe_course`;
CREATE TABLE `pe_course` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `flag_training_target` varchar(50) DEFAULT NULL,
  `flag_teach_type` varchar(50) DEFAULT NULL,
  `course_intro` varchar(255) DEFAULT NULL,
  `course_url` varchar(255) DEFAULT NULL,
  `site_code` varchar(20) NOT NULL,
  `code` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `course_target_fk` (`flag_training_target`) USING BTREE,
  KEY `course_teach_type_fk` (`flag_teach_type`) USING BTREE,
  KEY `course_name` (`name`) USING BTREE,
  KEY `course_site_code` (`site_code`) USING BTREE,
  CONSTRAINT `course_teach_type` FOREIGN KEY (`flag_teach_type`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `course_training_target_fk` FOREIGN KEY (`flag_training_target`) REFERENCES `enum_const` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pe_course
-- ----------------------------
INSERT INTO `pe_course` VALUES ('4028aefc6a2a6082016a2a6c98cb0001', '123', '4028ae316a0b8c30016a0b8eb733000c', null, '123', 'incoming/training/temp/20190417/1555489919833-2.jpg', 'training', '123');

-- ----------------------------
-- Table structure for pe_hotel
-- ----------------------------
DROP TABLE IF EXISTS `pe_hotel`;
CREATE TABLE `pe_hotel` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `contact` varchar(50) DEFAULT NULL,
  `agree_price` decimal(10,0) DEFAULT NULL,
  `agreement` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `site_code` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `hotel_site_code` (`site_code`) USING BTREE,
  KEY `hotel_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pe_hotel
-- ----------------------------

-- ----------------------------
-- Table structure for pe_manager
-- ----------------------------
DROP TABLE IF EXISTS `pe_manager`;
CREATE TABLE `pe_manager` (
  `id` varchar(50) NOT NULL DEFAULT '',
  `login_id` varchar(20) NOT NULL,
  `true_name` varchar(50) NOT NULL,
  `fk_sso_user_id` varchar(50) NOT NULL,
  `flag_active` varchar(50) NOT NULL,
  `fk_unit_id` varchar(50) DEFAULT NULL,
  `flag_card_type` varchar(50) DEFAULT NULL,
  `card_no` varchar(20) DEFAULT NULL,
  `flag_gender` varchar(50) DEFAULT NULL,
  `birthday` varchar(20) DEFAULT NULL,
  `work_unit` varchar(50) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL,
  `home_phone` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `zip_code` varchar(10) DEFAULT NULL,
  `qq` varchar(20) DEFAULT NULL,
  `site_code` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `manager_unique_index` (`site_code`,`login_id`) USING BTREE,
  KEY `manager_active_fk` (`flag_active`),
  KEY `manager_unit_fk` (`fk_unit_id`),
  KEY `manager_user_fk` (`fk_sso_user_id`),
  KEY `manager_gender_fk` (`flag_gender`),
  KEY `manager_card_type_fk` (`flag_card_type`),
  CONSTRAINT `manager_active_fk` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `manager_card_type_fk` FOREIGN KEY (`flag_card_type`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `manager_gender_fk` FOREIGN KEY (`flag_gender`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `manager_unit_fk` FOREIGN KEY (`fk_unit_id`) REFERENCES `pe_unit` (`id`),
  CONSTRAINT `manager_user_fk` FOREIGN KEY (`fk_sso_user_id`) REFERENCES `sso_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户：管理员';

-- ----------------------------
-- Records of pe_manager
-- ----------------------------
INSERT INTO `pe_manager` VALUES ('4028ae316a0bf543016a0bf6289d0001', 'weipengsenAdmin', '牛福兴', '4028ae316a0bf543016a0bf628820000', '4028809c1d625bcf011d66fd0dda0006', '4028ae316a0b8c30016a0b90b23a0022', '4028ae316a0bceb0016a0bd8316d0000', '', null, null, '', '', '', '', '', '', '', '', 'training');
INSERT INTO `pe_manager` VALUES ('8343cd28eaca498b8ae30c94f2fb302f', 'weipengsen', '牛福兴', 'b4e130c9e93b477b8d7acb43dd428f89', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, null, null, null, null, 'training');

-- ----------------------------
-- Table structure for pe_notice
-- ----------------------------
DROP TABLE IF EXISTS `pe_notice`;
CREATE TABLE `pe_notice` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `CONTENT` text NOT NULL COMMENT '通知内容',
  `CREATE_TIME` timestamp NOT NULL COMMENT '发送时间',
  `OPERATE_IP` varchar(39) NOT NULL COMMENT '操作者ip',
  `REQUEST_URL` text NOT NULL COMMENT '请求路径',
  `FLAG_SCOPE_TYPE` varchar(50) NOT NULL COMMENT '发送类型',
  `FLAG_NOTICE_TYPE` varchar(50) NOT NULL COMMENT '通知类型',
  `FK_SEND_USER` varchar(50) NOT NULL COMMENT '创建用户',
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`ID`),
  KEY `CREATE_TIME_INDEX` (`CREATE_TIME`) USING BTREE,
  KEY `enum_const_scope_type_fk` (`FLAG_SCOPE_TYPE`) USING BTREE,
  KEY `enum_const_notice_type_fk` (`FLAG_NOTICE_TYPE`) USING BTREE,
  KEY `send_user_fk` (`FK_SEND_USER`) USING BTREE,
  KEY `notice_site_code_index` (`site_code`),
  CONSTRAINT `pe_notice_ibfk_1` FOREIGN KEY (`FLAG_NOTICE_TYPE`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `pe_notice_ibfk_2` FOREIGN KEY (`FLAG_SCOPE_TYPE`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `pe_notice_ibfk_3` FOREIGN KEY (`FK_SEND_USER`) REFERENCES `sso_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='推送通知表';

-- ----------------------------
-- Records of pe_notice
-- ----------------------------

-- ----------------------------
-- Table structure for pe_place
-- ----------------------------
DROP TABLE IF EXISTS `pe_place`;
CREATE TABLE `pe_place` (
  `id` varchar(50) NOT NULL,
  `flag_school_zone` varchar(50) DEFAULT NULL,
  `fk_place_unit` varchar(50) DEFAULT NULL,
  `capacity` varchar(50) DEFAULT NULL,
  `charges` varchar(50) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `site_code` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_place_unit` (`fk_place_unit`),
  KEY `place_school_zone` (`flag_school_zone`) USING BTREE,
  KEY `place_site_code` (`site_code`) USING BTREE,
  CONSTRAINT `fk_place_unit` FOREIGN KEY (`fk_place_unit`) REFERENCES `pe_unit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pe_place
-- ----------------------------

-- ----------------------------
-- Table structure for pe_print_template
-- ----------------------------
DROP TABLE IF EXISTS `pe_print_template`;
CREATE TABLE `pe_print_template` (
  `ID` varchar(50) NOT NULL DEFAULT '',
  `TITLE` varchar(50) NOT NULL COMMENT '标题',
  `CODE` varchar(50) DEFAULT NULL COMMENT '标记',
  `PATH` varchar(100) DEFAULT NULL COMMENT '路径',
  `THUMB` varchar(100) DEFAULT NULL COMMENT '样本',
  `TEAM` varchar(50) DEFAULT NULL COMMENT '学校代码',
  `NUM` varchar(5) DEFAULT NULL COMMENT '学生人数',
  `VARIABLE` varchar(1000) DEFAULT NULL COMMENT '自定义变量',
  `MODIFY_DATE` datetime DEFAULT NULL,
  `DEFAULT_PATH` varchar(200) DEFAULT NULL,
  `SEARCH_SQL` text COMMENT '查询数据的sql',
  `SEARCH_TYPE` varchar(10) NOT NULL COMMENT '模板查询数据的方式',
  `print_page_size` int(5) DEFAULT NULL,
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`ID`),
  KEY `print_template_site_code_index` (`site_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板打印组';

-- ----------------------------
-- Records of pe_print_template
-- ----------------------------

-- ----------------------------
-- Table structure for pe_print_template_group
-- ----------------------------
DROP TABLE IF EXISTS `pe_print_template_group`;
CREATE TABLE `pe_print_template_group` (
  `id` varchar(40) NOT NULL,
  `name` varchar(20) NOT NULL,
  `fk_print_template_id` varchar(50) NOT NULL,
  `serial_number` int(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_print_template` (`fk_print_template_id`) USING BTREE,
  KEY `serial_number_index` (`serial_number`) USING BTREE,
  CONSTRAINT `pe_print_template_group_ibfk_1` FOREIGN KEY (`fk_print_template_id`) REFERENCES `pe_print_template` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板打印占位符分类';

-- ----------------------------
-- Records of pe_print_template_group
-- ----------------------------

-- ----------------------------
-- Table structure for pe_print_template_sign
-- ----------------------------
DROP TABLE IF EXISTS `pe_print_template_sign`;
CREATE TABLE `pe_print_template_sign` (
  `id` varchar(40) NOT NULL,
  `FK_PRINT_TEMPLATE_GROUP_ID` varchar(40) NOT NULL,
  `name` varchar(50) NOT NULL,
  `sign` varchar(100) NOT NULL,
  `SERIAL_NUMBER` int(2) DEFAULT NULL COMMENT '排序顺序',
  `FLAG_ACTIVE` varchar(50) NOT NULL COMMENT '是否有效',
  `is_show` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `template_group_fk` (`FK_PRINT_TEMPLATE_GROUP_ID`) USING BTREE,
  CONSTRAINT `pe_print_template_sign_ibfk_1` FOREIGN KEY (`FK_PRINT_TEMPLATE_GROUP_ID`) REFERENCES `pe_print_template_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板打印占位符';

-- ----------------------------
-- Records of pe_print_template_sign
-- ----------------------------

-- ----------------------------
-- Table structure for pe_pri_role
-- ----------------------------
DROP TABLE IF EXISTS `pe_pri_role`;
CREATE TABLE `pe_pri_role` (
  `ID` varchar(50) NOT NULL DEFAULT '',
  `NAME` varchar(50) DEFAULT NULL COMMENT '角色名',
  `CODE` varchar(50) DEFAULT '' COMMENT '角色code',
  `FLAG_ROLE_TYPE` varchar(50) DEFAULT NULL COMMENT '角色类型',
  `FLAG_BAK` varchar(50) DEFAULT NULL COMMENT '是否强制更新',
  `flag_site_super_admin` varchar(50) DEFAULT NULL COMMENT '是否是站点超级管理员',
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `role_code_unique_index` (`CODE`,`site_code`) USING BTREE,
  KEY `FLAG_ROLE_TYPE` (`FLAG_ROLE_TYPE`) USING BTREE,
  KEY `FLAG_BAK` (`FLAG_BAK`) USING BTREE,
  CONSTRAINT `pe_pri_role_ibfk_1` FOREIGN KEY (`FLAG_ROLE_TYPE`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `pe_pri_role_ibfk_2` FOREIGN KEY (`FLAG_BAK`) REFERENCES `enum_const` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息';

-- ----------------------------
-- Records of pe_pri_role
-- ----------------------------
INSERT INTO `pe_pri_role` VALUES ('0', '学生', '0', '4028826a1db7bb01011db7e52d130001', null, null, 'training');
INSERT INTO `pe_pri_role` VALUES ('05199dca0ac54a66bed0422b7dd7a642', '总站管理员', '3', '4028826a1db7bb01011db7e623d70003', null, '46aa648f5f2611e89e1dfcaa140ebf84', 'training');
INSERT INTO `pe_pri_role` VALUES ('1', '教师', '1', '402880951dad22bd011dad5d21fa0005', null, null, 'training');
INSERT INTO `pe_pri_role` VALUES ('9ac92a164a984b40b09717597d7362eb', '站点管理员', '2', '84a5ac89-6883-11e8-9e1d-fcaa140ebf84', null, '46a6f8d95f2611e89e1dfcaa140ebf84', 'training');

-- ----------------------------
-- Table structure for pe_site_config
-- ----------------------------
DROP TABLE IF EXISTS `pe_site_config`;
CREATE TABLE `pe_site_config` (
  `id` bigint(20) NOT NULL,
  `code` varchar(50) NOT NULL COMMENT '配置项code',
  `name` varchar(50) NOT NULL COMMENT '配置项名称',
  `note` varchar(300) DEFAULT NULL COMMENT '配置项说明',
  `json_config` varchar(5000) DEFAULT NULL COMMENT 'json或jsonArray格式的配置',
  `site_code` varchar(20) NOT NULL,
  `service_script_url` varchar(255) DEFAULT NULL COMMENT '客服js链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作室配置';

-- ----------------------------
-- Records of pe_site_config
-- ----------------------------

-- ----------------------------
-- Table structure for pe_teacher
-- ----------------------------
DROP TABLE IF EXISTS `pe_teacher`;
CREATE TABLE `pe_teacher` (
  `id` varchar(50) NOT NULL,
  `login_id` varchar(50) NOT NULL COMMENT '用户名',
  `true_name` varchar(50) NOT NULL,
  `flag_positional_title` varchar(50) NOT NULL COMMENT '职称',
  `flag_educational_background` varchar(50) DEFAULT NULL COMMENT '最高学历',
  `flag_course_teacher` varchar(50) NOT NULL COMMENT '是否授课教师',
  `flag_tutor_teacher` varchar(50) NOT NULL COMMENT '是否资源教师',
  `flag_isclassmaster` varchar(50) NOT NULL COMMENT '是否班主任',
  `fk_unit_id` varchar(50) NOT NULL COMMENT '所属单位',
  `flag_active` varchar(50) NOT NULL COMMENT '是否有效',
  `flag_card_type` varchar(50) DEFAULT NULL COMMENT '证件类型',
  `card_no` varchar(20) DEFAULT NULL COMMENT '证件号码',
  `flag_gender` varchar(50) DEFAULT NULL COMMENT '性别',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `work_unit` varchar(50) DEFAULT NULL COMMENT '工作单位',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机',
  `work_phone` varchar(20) DEFAULT NULL COMMENT '工作电话',
  `home_phone` varchar(20) DEFAULT NULL COMMENT '家庭电话',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  `address` varchar(255) DEFAULT NULL COMMENT '联系地址',
  `qq` varchar(20) DEFAULT NULL COMMENT 'QQ号码',
  `introduction` text COMMENT '简介',
  `site_code` varchar(20) NOT NULL,
  `fk_sso_user_id` varchar(50) NOT NULL COMMENT '用户',
  `zip_code` varchar(10) DEFAULT NULL COMMENT '邮政编码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `teacher_unique_index` (`site_code`,`login_id`),
  KEY `teacher_positional_title_fk` (`flag_positional_title`),
  KEY `teacher_educational_background_fk` (`flag_educational_background`),
  KEY `teacher_course_teacher_fk` (`flag_course_teacher`),
  KEY `teacher_tutor_teacher_fk` (`flag_tutor_teacher`),
  KEY `teacher_isclassmaster_fk` (`flag_isclassmaster`),
  KEY `teacher_unit_fk` (`fk_unit_id`),
  KEY `teacher_active_fk` (`flag_active`),
  KEY `teacher_card_type_fk` (`flag_card_type`),
  KEY `teacher_gender_fk` (`flag_gender`),
  CONSTRAINT `teacher_active_fk` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `teacher_card_type_fk` FOREIGN KEY (`flag_card_type`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `teacher_course_teacher_fk` FOREIGN KEY (`flag_course_teacher`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `teacher_educational_background_fk` FOREIGN KEY (`flag_educational_background`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `teacher_gender_fk` FOREIGN KEY (`flag_gender`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `teacher_isclassmaster_fk` FOREIGN KEY (`flag_isclassmaster`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `teacher_positional_title_fk` FOREIGN KEY (`flag_positional_title`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `teacher_tutor_teacher_fk` FOREIGN KEY (`flag_tutor_teacher`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `teacher_unit_fk` FOREIGN KEY (`fk_unit_id`) REFERENCES `pe_unit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pe_teacher
-- ----------------------------
INSERT INTO `pe_teacher` VALUES ('4028ae316a115998016a115ae0b60001', 'weipengsentea', '牛福兴', '4028ae316a0b8c30016a0b8fba56001a', null, '761d60c6da2411e7acea94de80c4da50', '90a5bdd6da2411e7acea94de80c4da50', '4028808c1de7580f011de75f999990002', '4028ae316a0b8c30016a0b90b23a0022', '4028809c1d625bcf011d66fd0dda0006', '4028ae316a0bceb0016a0bd8316d0000', '', null, null, '', '', '', '', '', '', '', '', 'training', '4028ae316a115998016a115ae0a10000', '');

-- ----------------------------
-- Table structure for pe_unit
-- ----------------------------
DROP TABLE IF EXISTS `pe_unit`;
CREATE TABLE `pe_unit` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `head_name` varchar(20) DEFAULT NULL COMMENT '负责人姓名',
  `head_mobile` varchar(11) DEFAULT NULL COMMENT '负责人手机号',
  `head_telephone` varchar(20) DEFAULT NULL COMMENT '负责人办公电话',
  `head_email` varchar(30) DEFAULT NULL COMMENT '负责人邮箱',
  `flag_unit_type` varchar(50) NOT NULL COMMENT '单位类型',
  `note` text,
  `site_code` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unit_name_site_unique_index` (`site_code`,`name`),
  KEY `unit_type_index` (`flag_unit_type`) USING BTREE,
  CONSTRAINT `unit_type_fk` FOREIGN KEY (`flag_unit_type`) REFERENCES `enum_const` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='单位表';

-- ----------------------------
-- Records of pe_unit
-- ----------------------------
INSERT INTO `pe_unit` VALUES ('4028ae316a0b8c30016a0b90b23a0022', '校本部', '牛福兴', '17600545979', '0378-6995975', 'weipengsen@whaty.com', '4028ae316a0b8c30016a0b8d5ce80000', '', 'training');

-- ----------------------------
-- Table structure for pr_columns_config
-- ----------------------------
DROP TABLE IF EXISTS `pr_columns_config`;
CREATE TABLE `pr_columns_config` (
  `ID` varchar(50) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `COLUMN_NAME` varchar(50) DEFAULT NULL COMMENT '名称',
  `DISPLAY` varchar(1) DEFAULT NULL,
  `FLAG_INFO_TYPE` varchar(50) DEFAULT NULL,
  `MY_ORDER` varchar(50) DEFAULT NULL,
  `EDIT` varchar(1) DEFAULT NULL,
  `HTML_TYPE` varchar(50) DEFAULT NULL,
  `NOT_NULL` varchar(1) DEFAULT NULL,
  `TEXT_LENGTH` decimal(3,0) DEFAULT NULL,
  `DATE_FORMAT` varchar(50) DEFAULT NULL,
  `REG` text,
  `OPTIONS` text,
  `SELECTED` varchar(50) DEFAULT NULL,
  `HELP_DISPLAY` varchar(1) DEFAULT NULL,
  `HELP` text,
  `ERROR` text,
  `HELP_URL` text,
  `HELP_URL_NAME` varchar(50) DEFAULT NULL,
  `TABLE_NAME` varchar(50) DEFAULT NULL,
  `USER_EDIT` varchar(1) DEFAULT NULL,
  `CTRL_DIV` varchar(50) DEFAULT NULL,
  `CTRL_VALUE` text,
  `TEAM` varchar(50) DEFAULT NULL,
  `LAST_NAME` varchar(50) DEFAULT NULL,
  `DB_COLUMN_LENGTH` varchar(50) DEFAULT NULL COMMENT '查询sql',
  `listSql` varchar(500) DEFAULT NULL,
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`ID`),
  KEY `columns_config_site_code_index` (`site_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生信息配置';

-- ----------------------------
-- Records of pr_columns_config
-- ----------------------------

-- ----------------------------
-- Table structure for pr_user_notice
-- ----------------------------
DROP TABLE IF EXISTS `pr_user_notice`;
CREATE TABLE `pr_user_notice` (
  `ID` int(12) NOT NULL AUTO_INCREMENT,
  `FK_NOTICE_ID` int(10) NOT NULL COMMENT '通知表外键',
  `FK_SSO_USER_ID` varchar(50) NOT NULL COMMENT '用户表外键',
  `FLAG_READED` varchar(50) NOT NULL COMMENT '是否已读',
  `FLAG_IS_STAR` varchar(50) DEFAULT NULL COMMENT '是否星标信息',
  PRIMARY KEY (`ID`),
  KEY `notice_fk` (`FK_NOTICE_ID`) USING BTREE,
  KEY `sso_user_fk` (`FK_SSO_USER_ID`) USING BTREE,
  KEY `enum_const_readed_fk` (`FLAG_READED`) USING BTREE,
  KEY `flag_is_star_fk` (`FLAG_IS_STAR`) USING BTREE,
  CONSTRAINT `pr_user_notice_ibfk_1` FOREIGN KEY (`FLAG_READED`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `pr_user_notice_ibfk_2` FOREIGN KEY (`FK_NOTICE_ID`) REFERENCES `pe_notice` (`ID`),
  CONSTRAINT `pr_user_notice_ibfk_3` FOREIGN KEY (`FLAG_IS_STAR`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `pr_user_notice_ibfk_4` FOREIGN KEY (`FK_SSO_USER_ID`) REFERENCES `sso_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通知用户关联表';

-- ----------------------------
-- Records of pr_user_notice
-- ----------------------------

-- ----------------------------
-- Table structure for send_message_analyse
-- ----------------------------
DROP TABLE IF EXISTS `send_message_analyse`;
CREATE TABLE `send_message_analyse` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `group_code` varchar(50) NOT NULL COMMENT '组编号',
  `group_name` varchar(50) NOT NULL COMMENT '组名称',
  `last_type_name` varchar(50) NOT NULL COMMENT '最后发送类型',
  `last_send_time` datetime NOT NULL COMMENT '最后发送时间',
  `send_number` int(9) NOT NULL DEFAULT '0' COMMENT '发送次数',
  `receive_id` varchar(50) NOT NULL,
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `send_message_analyse_unique` (`group_code`,`receive_id`,`site_code`) USING BTREE,
  KEY `send_message_analyse_receive_index` (`receive_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发送消息信息统计';

-- ----------------------------
-- Records of send_message_analyse
-- ----------------------------

-- ----------------------------
-- Table structure for send_message_history
-- ----------------------------
DROP TABLE IF EXISTS `send_message_history`;
CREATE TABLE `send_message_history` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `receive_id` varchar(50) NOT NULL COMMENT '接受者id',
  `group_code` varchar(50) NOT NULL COMMENT '组编号',
  `group_name` varchar(50) NOT NULL COMMENT '组名称',
  `type_name` varchar(50) NOT NULL COMMENT '类型名称',
  `message_content` text NOT NULL COMMENT '消息内容配置',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `send_user` varchar(50) NOT NULL COMMENT '发送人',
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`id`),
  KEY `send_message_history_site_code_index` (`site_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发送消息历史记录';

-- ----------------------------
-- Records of send_message_history
-- ----------------------------

-- ----------------------------
-- Table structure for send_message_time_config
-- ----------------------------
DROP TABLE IF EXISTS `send_message_time_config`;
CREATE TABLE `send_message_time_config` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) NOT NULL COMMENT '消息组名称',
  `message_type` varchar(50) NOT NULL COMMENT '消息类型',
  `message_type_code` varchar(5) NOT NULL COMMENT '消息类型编号',
  `time` datetime NOT NULL COMMENT '发送时间',
  `schedule_job_id` varchar(50) DEFAULT NULL COMMENT '调度任务id',
  `receive_number` int(7) NOT NULL COMMENT '接受人数量',
  `data` text NOT NULL COMMENT '数据',
  `create_by` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `flag_send_status` varchar(50) NOT NULL COMMENT '发送状态',
  `failure_message` varchar(255) DEFAULT NULL COMMENT '错误提示（用户）',
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`id`),
  KEY `send_message_time_config_site_code_index` (`site_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发送消息定时配置';

-- ----------------------------
-- Records of send_message_time_config
-- ----------------------------

-- ----------------------------
-- Table structure for shiro_permission
-- ----------------------------
DROP TABLE IF EXISTS `shiro_permission`;
CREATE TABLE `shiro_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `http_method` varchar(10) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`id`),
  KEY `shiro_permission_site_code` (`site_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='shiro权限表';

-- ----------------------------
-- Records of shiro_permission
-- ----------------------------

-- ----------------------------
-- Table structure for shiro_role
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role`;
CREATE TABLE `shiro_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`id`),
  KEY `shiro_role_site_code_index` (`site_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='shiro角色';

-- ----------------------------
-- Records of shiro_role
-- ----------------------------

-- ----------------------------
-- Table structure for shiro_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role_permission`;
CREATE TABLE `shiro_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fk_shiro_permission_id` bigint(20) DEFAULT NULL,
  `fk_shiro_role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmelfrsbswxmeakn68c91cc476` (`fk_shiro_role_id`) USING BTREE,
  KEY `FKreyteyg5n0ulag8jwvmnen3dv` (`fk_shiro_permission_id`) USING BTREE,
  CONSTRAINT `shiro_role_permission_ibfk_1` FOREIGN KEY (`fk_shiro_role_id`) REFERENCES `shiro_role` (`id`),
  CONSTRAINT `shiro_role_permission_ibfk_2` FOREIGN KEY (`fk_shiro_permission_id`) REFERENCES `shiro_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='shiro角色权限';

-- ----------------------------
-- Records of shiro_role_permission
-- ----------------------------

-- ----------------------------
-- Table structure for shiro_user_role
-- ----------------------------
DROP TABLE IF EXISTS `shiro_user_role`;
CREATE TABLE `shiro_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fk_shiro_role_id` bigint(20) DEFAULT NULL,
  `fk_sso_user_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh4p8twfkqav7t5slxyx6lj2o` (`fk_shiro_role_id`) USING BTREE,
  KEY `FKqvu8uiujla9fs0whyg13hgr5w` (`fk_sso_user_id`) USING BTREE,
  CONSTRAINT `shiro_user_role_ibfk_1` FOREIGN KEY (`fk_shiro_role_id`) REFERENCES `shiro_role` (`id`),
  CONSTRAINT `shiro_user_role_ibfk_2` FOREIGN KEY (`fk_sso_user_id`) REFERENCES `sso_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='shiro用户角色';

-- ----------------------------
-- Records of shiro_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for sns_user_dynamic
-- ----------------------------
DROP TABLE IF EXISTS `sns_user_dynamic`;
CREATE TABLE `sns_user_dynamic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `author_name` varchar(20) NOT NULL COMMENT '发布人姓名',
  `fk_sso_user_id` varchar(50) NOT NULL,
  `publish_date` datetime NOT NULL,
  `flag_valid` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否有效',
  `content` text,
  `comment_num` int(11) NOT NULL DEFAULT '0' COMMENT '评论数',
  `like_num` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `like_text` text,
  PRIMARY KEY (`id`),
  KEY `sns_user_dynamic_ibfk_1` (`fk_sso_user_id`) USING BTREE,
  CONSTRAINT `sns_user_dynamic_ibfk_1` FOREIGN KEY (`fk_sso_user_id`) REFERENCES `sso_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sns_user_dynamic
-- ----------------------------

-- ----------------------------
-- Table structure for sns_user_dynamic_comment
-- ----------------------------
DROP TABLE IF EXISTS `sns_user_dynamic_comment`;
CREATE TABLE `sns_user_dynamic_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `author_name` varchar(20) NOT NULL COMMENT '发布人姓名',
  `fk_dynamic_id` bigint(20) NOT NULL,
  `publish_date` datetime NOT NULL,
  `fk_sso_user_id` varchar(50) NOT NULL,
  `receive_user_id` varchar(50) DEFAULT NULL,
  `receive_user_true_name` varchar(20) DEFAULT NULL,
  `flag_valid` tinyint(4) DEFAULT '0' COMMENT '是否有效',
  `content` text,
  PRIMARY KEY (`id`),
  KEY `sns_user_dynamic_comment_ibfk_1` (`fk_dynamic_id`) USING BTREE,
  KEY `sns_user_dynamic_comment_ibfk_2` (`fk_sso_user_id`) USING BTREE,
  CONSTRAINT `sns_user_dynamic_comment_ibfk_1` FOREIGN KEY (`fk_dynamic_id`) REFERENCES `sns_user_dynamic` (`id`),
  CONSTRAINT `sns_user_dynamic_comment_ibfk_2` FOREIGN KEY (`fk_sso_user_id`) REFERENCES `sso_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sns_user_dynamic_comment
-- ----------------------------

-- ----------------------------
-- Table structure for sns_user_dynamic_img
-- ----------------------------
DROP TABLE IF EXISTS `sns_user_dynamic_img`;
CREATE TABLE `sns_user_dynamic_img` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fk_dynamic_id` bigint(20) NOT NULL,
  `url` varchar(300) DEFAULT NULL COMMENT '图片地址',
  `thumb_url` varchar(300) DEFAULT NULL COMMENT '图片缩略图地址',
  PRIMARY KEY (`id`),
  KEY `sns_user_dynamic_img_ibfk_1` (`fk_dynamic_id`) USING BTREE,
  CONSTRAINT `sns_user_dynamic_img_ibfk_1` FOREIGN KEY (`fk_dynamic_id`) REFERENCES `sns_user_dynamic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sns_user_dynamic_img
-- ----------------------------

-- ----------------------------
-- Table structure for sso_user
-- ----------------------------
DROP TABLE IF EXISTS `sso_user`;
CREATE TABLE `sso_user` (
  `ID` varchar(50) NOT NULL,
  `LOGIN_ID` varchar(50) NOT NULL,
  `PASSWORD` varchar(50) NOT NULL,
  `FK_ROLE_ID` varchar(50) DEFAULT NULL,
  `FLAG_ISVALID` varchar(50) DEFAULT NULL,
  `FLAG_BAK` varchar(50) DEFAULT NULL,
  `LOGIN_NUM` bigint(20) DEFAULT NULL,
  `LAST_LOGIN_DATE` datetime DEFAULT NULL,
  `LAST_LOGIN_IP` varchar(100) DEFAULT NULL,
  `MOBILE_ALIAS` varchar(100) DEFAULT NULL,
  `TOKEN` varchar(200) DEFAULT NULL,
  `LEARNSPACE_SITE_CODE` varchar(20) DEFAULT NULL,
  `profile_picture` varchar(300) DEFAULT NULL COMMENT '头像地址',
  `wechat_union_id` varchar(100) DEFAULT NULL COMMENT '微信扫码登录关联id',
  `wechat_nick_name` varchar(255) DEFAULT NULL COMMENT '绑定微信昵称',
  `flag_wechat_check` varchar(50) DEFAULT NULL COMMENT '是否需要微信扫码验证',
  `EMAIL` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `NICK_NAME` varchar(50) DEFAULT NULL COMMENT '昵称',
  `MOBILEPHONE` varchar(32) DEFAULT NULL,
  `FLAG_GENDER` varchar(50) DEFAULT NULL COMMENT '性别',
  `photo` varchar(255) DEFAULT NULL COMMENT '照片',
  `true_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `site_code` varchar(50) DEFAULT NULL COMMENT '站点code',
  `fk_web_site_id` varchar(50) DEFAULT NULL COMMENT 'site_manage字段',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `LOGIN_ID` (`LOGIN_ID`) USING BTREE,
  KEY `FLAG_BAK` (`FLAG_BAK`) USING BTREE,
  KEY `FLAG_ISVALID` (`FLAG_ISVALID`) USING BTREE,
  KEY `FK_ROLE_ID` (`FK_ROLE_ID`) USING BTREE,
  KEY `ID` (`ID`),
  CONSTRAINT `sso_user_ibfk_1` FOREIGN KEY (`FLAG_BAK`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `sso_user_ibfk_2` FOREIGN KEY (`FLAG_ISVALID`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `sso_user_ibfk_3` FOREIGN KEY (`FK_ROLE_ID`) REFERENCES `pe_pri_role` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登陆信息表';

-- ----------------------------
-- Records of sso_user
-- ----------------------------
INSERT INTO `sso_user` VALUES ('4028ae316a0bf543016a0bf628820000', 'weipengsenAdmin', '4a951f283dfb830b0d35600c8ce6bba9', '05199dca0ac54a66bed0422b7dd7a642', '2', null, null, null, null, null, null, 'cep', null, null, null, null, null, null, null, null, null, null, 'training', null);
INSERT INTO `sso_user` VALUES ('4028ae316a115998016a115ae0a10000', 'weipengsentea', '653619f38fd18c81503eef53a9925540', '1', null, null, null, null, null, null, null, 'cep', null, null, null, null, null, null, null, null, null, null, 'training', null);
INSERT INTO `sso_user` VALUES ('b4e130c9e93b477b8d7acb43dd428f89', 'weipengsen', 'c9e6e729163d22e290a572a5bc21635c', '9ac92a164a984b40b09717597d7362eb', '2', null, null, null, null, null, null, 'cep', null, null, null, null, null, null, null, null, null, '牛福兴', 'training', null);

-- ----------------------------
-- Table structure for station_message
-- ----------------------------
DROP TABLE IF EXISTS `station_message`;
CREATE TABLE `station_message` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `title` varchar(30) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `foot` varchar(30) NOT NULL COMMENT '脚注',
  `fk_sso_user_id` varchar(50) NOT NULL COMMENT '用户外键关联',
  `flag_is_star` varchar(50) NOT NULL COMMENT '是否星标',
  `flag_readed` varchar(50) NOT NULL COMMENT '是否已读',
  `send_date` varchar(30) DEFAULT NULL,
  `fk_send_user_id` varchar(50) NOT NULL COMMENT '发送者',
  PRIMARY KEY (`id`),
  KEY `station_message_user_id` (`fk_sso_user_id`) USING BTREE,
  KEY `station_message_is_star` (`flag_is_star`) USING BTREE,
  KEY `station_message_readed` (`flag_readed`) USING BTREE,
  KEY `station_message_send_user` (`fk_send_user_id`) USING BTREE,
  CONSTRAINT `station_message_ibfk_1` FOREIGN KEY (`flag_is_star`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `station_message_ibfk_2` FOREIGN KEY (`flag_readed`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `station_message_ibfk_3` FOREIGN KEY (`fk_send_user_id`) REFERENCES `sso_user` (`ID`),
  CONSTRAINT `station_message_ibfk_4` FOREIGN KEY (`fk_sso_user_id`) REFERENCES `sso_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of station_message
-- ----------------------------

-- ----------------------------
-- Table structure for system_mylist_sql
-- ----------------------------
DROP TABLE IF EXISTS `system_mylist_sql`;
CREATE TABLE `system_mylist_sql` (
  `ID` varchar(50) NOT NULL,
  `SQLSTR` varchar(500) DEFAULT NULL,
  `NOTE` varchar(500) DEFAULT NULL,
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`ID`),
  KEY `list_sql_site_code_index` (`site_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_mylist_sql
-- ----------------------------

-- ----------------------------
-- Table structure for system_variables
-- ----------------------------
DROP TABLE IF EXISTS `system_variables`;
CREATE TABLE `system_variables` (
  `ID` varchar(50) NOT NULL DEFAULT '',
  `NAME` varchar(50) DEFAULT NULL,
  `VALUE` text,
  `PATTERN` text,
  `FLAG_PLATFORM_SECTION` varchar(50) DEFAULT NULL,
  `FLAG_BAK` varchar(50) DEFAULT NULL,
  `NOTE` text,
  `TEAM` text,
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `system_variables_name_unique_index` (`site_code`,`NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='平台配置信息';

-- ----------------------------
-- Records of system_variables
-- ----------------------------

-- ----------------------------
-- Table structure for third_pay_data
-- ----------------------------
DROP TABLE IF EXISTS `third_pay_data`;
CREATE TABLE `third_pay_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) NOT NULL,
  `response_data` text NOT NULL,
  `order_type` varchar(50) NOT NULL,
  `pay_type` varchar(50) NOT NULL,
  `return_code` varchar(10) NOT NULL,
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`id`),
  KEY `third_pay_data_site_code_index` (`site_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of third_pay_data
-- ----------------------------

-- ----------------------------
-- Table structure for upload_check_config
-- ----------------------------
DROP TABLE IF EXISTS `upload_check_config`;
CREATE TABLE `upload_check_config` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `check_sql` varchar(1000) NOT NULL,
  `serial_number` int(3) NOT NULL,
  `fk_upload_config_id` varchar(50) NOT NULL,
  `can_interrupt` varchar(1) NOT NULL COMMENT '是否可以中断程序，可中断时，如果checkSql查出数据不为空则中断',
  `error_tip` varchar(255) NOT NULL COMMENT '错误提示',
  PRIMARY KEY (`id`),
  KEY `upload_check_config_upload_config_fk` (`fk_upload_config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of upload_check_config
-- ----------------------------

-- ----------------------------
-- Table structure for upload_check_item_config
-- ----------------------------
DROP TABLE IF EXISTS `upload_check_item_config`;
CREATE TABLE `upload_check_item_config` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(50) NOT NULL COMMENT '编号',
  `fk_upload_config_id` varchar(50) NOT NULL,
  `not_null` varchar(1) NOT NULL COMMENT '是否必填',
  PRIMARY KEY (`id`),
  KEY `upload_check_item_config_upload_config_fk` (`fk_upload_config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of upload_check_item_config
-- ----------------------------

-- ----------------------------
-- Table structure for upload_config
-- ----------------------------
DROP TABLE IF EXISTS `upload_config`;
CREATE TABLE `upload_config` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(50) NOT NULL COMMENT '编号',
  `execute_sql` text NOT NULL COMMENT '执行sql，用“;”分割',
  `type` varchar(20) NOT NULL COMMENT '文件类型，dbf，excel等',
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `upload_config_unique_index` (`site_code`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of upload_config
-- ----------------------------

-- ----------------------------
-- Table structure for util_excel_table
-- ----------------------------
DROP TABLE IF EXISTS `util_excel_table`;
CREATE TABLE `util_excel_table` (
  `ID` varchar(50) NOT NULL,
  `row` int(11) DEFAULT NULL,
  `NAMESPACE` varchar(50) DEFAULT NULL,
  `COLUMN1` varchar(100) DEFAULT NULL,
  `COLUMN2` varchar(100) DEFAULT NULL,
  `COLUMN3` varchar(100) DEFAULT NULL,
  `COLUMN4` varchar(100) DEFAULT NULL,
  `COLUMN5` varchar(100) DEFAULT NULL,
  `COLUMN6` varchar(100) DEFAULT NULL,
  `COLUMN7` varchar(100) DEFAULT NULL,
  `COLUMN8` varchar(100) DEFAULT NULL,
  `COLUMN9` varchar(100) DEFAULT NULL,
  `COLUMN10` varchar(100) DEFAULT NULL,
  `COLUMN11` varchar(100) DEFAULT NULL,
  `COLUMN12` varchar(100) DEFAULT NULL,
  `COLUMN13` varchar(100) DEFAULT NULL,
  `COLUMN14` varchar(100) DEFAULT NULL,
  `COLUMN15` varchar(100) DEFAULT NULL,
  `COLUMN16` varchar(100) DEFAULT NULL,
  `COLUMN17` varchar(100) DEFAULT NULL,
  `COLUMN18` varchar(100) DEFAULT NULL,
  `COLUMN19` varchar(100) DEFAULT NULL,
  `COLUMN20` varchar(100) DEFAULT NULL,
  `COLUMN21` varchar(50) DEFAULT NULL,
  `COLUMN22` varchar(50) DEFAULT NULL,
  `COLUMN23` varchar(50) DEFAULT NULL,
  `COLUMN24` varchar(50) DEFAULT NULL,
  `COLUMN25` varchar(50) DEFAULT NULL,
  `COLUMN26` varchar(50) DEFAULT NULL,
  `COLUMN27` varchar(50) DEFAULT NULL,
  `COLUMN28` varchar(50) DEFAULT NULL,
  `COLUMN29` varchar(50) DEFAULT NULL,
  `COLUMN30` varchar(50) DEFAULT NULL,
  `COLUMN31` varchar(50) DEFAULT NULL,
  `COLUMN32` varchar(50) DEFAULT NULL,
  `COLUMN33` varchar(50) DEFAULT NULL,
  `COLUMN34` varchar(50) DEFAULT NULL,
  `COLUMN35` varchar(50) DEFAULT NULL,
  `COLUMN36` varchar(50) DEFAULT NULL,
  `valid` varchar(2) DEFAULT NULL,
  `error_info` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `column1` (`COLUMN1`) USING BTREE,
  KEY `column2` (`COLUMN2`) USING BTREE,
  KEY `column3` (`COLUMN3`) USING BTREE,
  KEY `column4` (`COLUMN4`) USING BTREE,
  KEY `column5` (`COLUMN5`) USING BTREE,
  KEY `column6` (`COLUMN6`) USING BTREE,
  KEY `column7` (`COLUMN7`) USING BTREE,
  KEY `column8` (`COLUMN8`) USING BTREE,
  KEY `column9` (`COLUMN9`) USING BTREE,
  KEY `column10` (`COLUMN10`) USING BTREE,
  KEY `column11` (`COLUMN11`) USING BTREE,
  KEY `column12` (`COLUMN12`) USING BTREE,
  KEY `column13` (`COLUMN13`) USING BTREE,
  KEY `column14` (`COLUMN14`) USING BTREE,
  KEY `column15` (`COLUMN15`) USING BTREE,
  KEY `column16` (`COLUMN16`) USING BTREE,
  KEY `column17` (`COLUMN17`) USING BTREE,
  KEY `column18` (`COLUMN18`) USING BTREE,
  KEY `column19` (`COLUMN19`) USING BTREE,
  KEY `column20` (`COLUMN20`) USING BTREE,
  KEY `column21` (`COLUMN21`) USING BTREE,
  KEY `column22` (`COLUMN22`) USING BTREE,
  KEY `column23` (`COLUMN23`) USING BTREE,
  KEY `column24` (`COLUMN24`) USING BTREE,
  KEY `column25` (`COLUMN25`) USING BTREE,
  KEY `column26` (`COLUMN26`) USING BTREE,
  KEY `column27` (`COLUMN27`) USING BTREE,
  KEY `column28` (`COLUMN28`) USING BTREE,
  KEY `column29` (`COLUMN29`) USING BTREE,
  KEY `column30` (`COLUMN30`) USING BTREE,
  KEY `column31` (`COLUMN31`) USING BTREE,
  KEY `column32` (`COLUMN32`) USING BTREE,
  KEY `column33` (`COLUMN33`) USING BTREE,
  KEY `column34` (`COLUMN34`) USING BTREE,
  KEY `column35` (`COLUMN35`) USING BTREE,
  KEY `column36` (`COLUMN36`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sql操作辅助表|Excel导入辅助表';

-- ----------------------------
-- Records of util_excel_table
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_access_token
-- ----------------------------
DROP TABLE IF EXISTS `wechat_access_token`;
CREATE TABLE `wechat_access_token` (
  `id` varchar(32) NOT NULL,
  `fk_wechat_site_id` varchar(32) DEFAULT NULL,
  `access_token` varchar(512) DEFAULT NULL,
  `expires_in` bigint(10) DEFAULT NULL,
  `input_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_access_token
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_app
-- ----------------------------
DROP TABLE IF EXISTS `wechat_app`;
CREATE TABLE `wechat_app` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(50) NOT NULL COMMENT 'appId',
  `app_secret` varchar(50) NOT NULL COMMENT 'appSecret',
  `code` varchar(50) NOT NULL COMMENT '编号',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `pay_mch_id` varchar(50) DEFAULT NULL COMMENT '支付商户号',
  `pay_key` varchar(50) DEFAULT NULL COMMENT '支付密钥',
  `pay_notify_url` text COMMENT '通知回调地址',
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `wechat_app_code_unique` (`site_code`,`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_app
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_basic
-- ----------------------------
DROP TABLE IF EXISTS `wechat_basic`;
CREATE TABLE `wechat_basic` (
  `id` varchar(32) NOT NULL,
  `fk_wechat_site_id` varchar(32) NOT NULL,
  `appId` varchar(50) DEFAULT NULL COMMENT '配置名称',
  `appSecret` varchar(50) DEFAULT NULL,
  `token` varchar(50) DEFAULT NULL,
  `menu` varchar(5000) DEFAULT NULL,
  `input_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_basic
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_jsapi_ticket
-- ----------------------------
DROP TABLE IF EXISTS `wechat_jsapi_ticket`;
CREATE TABLE `wechat_jsapi_ticket` (
  `id` varchar(50) NOT NULL DEFAULT '',
  `fk_wechat_site_id` varchar(50) DEFAULT NULL,
  `jsapi_ticket` varchar(255) DEFAULT NULL,
  `expires_in` bigint(10) DEFAULT NULL,
  `input_time` bigint(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_jsapi_ticket
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_receive_message
-- ----------------------------
DROP TABLE IF EXISTS `wechat_receive_message`;
CREATE TABLE `wechat_receive_message` (
  `id` varchar(32) NOT NULL,
  `toUserName` varchar(50) DEFAULT NULL COMMENT '开发者微信号',
  `fromUserName` varchar(50) DEFAULT NULL COMMENT '发送方openid',
  `createTime` bigint(20) DEFAULT NULL COMMENT '消息创建时间',
  `msgType` varchar(15) DEFAULT NULL COMMENT '消息类型',
  `msgId` varchar(64) DEFAULT NULL COMMENT '消息id',
  `content` varchar(500) DEFAULT NULL COMMENT '文本消息内容',
  `picUrl` varchar(256) DEFAULT NULL COMMENT '图片链接',
  `location_X` varchar(50) DEFAULT NULL COMMENT '地理位置纬度',
  `location_Y` varchar(50) DEFAULT NULL COMMENT '地理位置经度',
  `scale` varchar(10) DEFAULT NULL COMMENT '地图缩放大小',
  `label` varchar(10) DEFAULT NULL COMMENT '地理位置信息',
  `title` varchar(50) DEFAULT NULL COMMENT '消息标题',
  `description` varchar(50) DEFAULT NULL COMMENT '消息描述',
  `url` varchar(256) DEFAULT NULL COMMENT '消息链接',
  `event` varchar(100) DEFAULT NULL COMMENT '事件类型',
  `eventKey` varchar(256) DEFAULT NULL COMMENT '事件KEY值',
  `input_date` datetime DEFAULT NULL COMMENT '保存时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_receive_message
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_site
-- ----------------------------
DROP TABLE IF EXISTS `wechat_site`;
CREATE TABLE `wechat_site` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) NOT NULL COMMENT '站点名称',
  `domain` varchar(30) NOT NULL COMMENT '站点域名',
  `path` varchar(30) DEFAULT NULL COMMENT '访问路径',
  `public_account` varchar(30) DEFAULT NULL COMMENT '公众号账号',
  `public_id` varchar(20) DEFAULT NULL COMMENT '公众号原始id',
  `fk_union_id` varchar(32) DEFAULT NULL COMMENT '所属认证服务号,用于网页获取用户信息',
  `input_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`id`),
  KEY `fk_site_ref_site` (`fk_union_id`) USING BTREE,
  KEY `wechat_site_code_index` (`site_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_site
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_user
-- ----------------------------
DROP TABLE IF EXISTS `wechat_user`;
CREATE TABLE `wechat_user` (
  `id` varchar(32) NOT NULL COMMENT '微信openid',
  `source` varchar(20) DEFAULT NULL COMMENT '用户来源 关注:subscribe  页面:page',
  `fk_wechat_site_id` varchar(50) DEFAULT NULL,
  `openid` varchar(30) DEFAULT NULL COMMENT '用户的标识，对当前公众号唯一',
  `fk_sso_user_id` varchar(50) DEFAULT NULL COMMENT '平台用户id',
  `subscribe` int(11) DEFAULT NULL COMMENT '用户是否订阅该公众号标识',
  `nickname` varchar(50) DEFAULT NULL COMMENT '用户的昵称',
  `sex` int(11) DEFAULT NULL COMMENT '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
  `city` varchar(50) DEFAULT NULL COMMENT '用户所在城市',
  `country` varchar(50) DEFAULT NULL COMMENT '用户所在国家',
  `province` varchar(50) DEFAULT NULL COMMENT '用户所在省份',
  `language` varchar(50) DEFAULT NULL COMMENT '用户的语言，简体中文为zh_CN',
  `headimgurl` varchar(255) DEFAULT NULL COMMENT '用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。',
  `subscribe_time` bigint(15) DEFAULT NULL COMMENT '用户关注时间，为时间戳',
  `unionid` varchar(50) DEFAULT NULL,
  `complete` int(255) DEFAULT '0' COMMENT '资料是否完整：0：不完整，1：完整',
  `input_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_ref_wechat_site` (`fk_wechat_site_id`) USING BTREE,
  KEY `wechat_user_fk_sso_user` (`fk_sso_user_id`),
  CONSTRAINT `wechat_user_ibfk_1` FOREIGN KEY (`fk_sso_user_id`) REFERENCES `sso_user` (`ID`),
  CONSTRAINT `wechat_user_ibfk_2` FOREIGN KEY (`fk_wechat_site_id`) REFERENCES `wechat_site` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_user
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_user_token
-- ----------------------------
DROP TABLE IF EXISTS `wechat_user_token`;
CREATE TABLE `wechat_user_token` (
  `id` varchar(32) NOT NULL,
  `openid` varchar(32) DEFAULT NULL,
  `token` varchar(50) DEFAULT NULL,
  `input_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_user_token
-- ----------------------------
