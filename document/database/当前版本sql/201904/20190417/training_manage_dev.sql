/*
Navicat MySQL Data Transfer

Source Server         : 192.168.46.81@成教测试库
Source Server Version : 50622
Source Host           : 192.168.46.81:3306
Source Database       : training_manage_dev

Target Server Type    : MYSQL
Target Server Version : 50622
File Encoding         : 65001

Date: 2019-04-17 16:47:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for email_message_group
-- ----------------------------
DROP TABLE IF EXISTS `email_message_group`;
CREATE TABLE `email_message_group` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(50) NOT NULL COMMENT '编号',
  `data_sql` text NOT NULL COMMENT '数据查询sql',
  `flag_active` varchar(50) NOT NULL COMMENT '是否有效',
  `filter_sql` text COMMENT '过滤sql',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_message_group_code_index` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of email_message_group
-- ----------------------------

-- ----------------------------
-- Table structure for email_message_site
-- ----------------------------
DROP TABLE IF EXISTS `email_message_site`;
CREATE TABLE `email_message_site` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fk_web_site_id` varchar(50) NOT NULL COMMENT '所属站点',
  `fk_email_message_group_id` int(9) NOT NULL COMMENT '关联消息组',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_message_site_site_fk` (`fk_web_site_id`,`fk_email_message_group_id`) USING BTREE,
  KEY `email_message_site_group_fk` (`fk_email_message_group_id`),
  CONSTRAINT `email_message_site_ibfk_1` FOREIGN KEY (`fk_email_message_group_id`) REFERENCES `email_message_group` (`id`),
  CONSTRAINT `email_message_site_ibfk_2` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of email_message_site
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
  `TEAM` text COMMENT '备注',
  PRIMARY KEY (`ID`),
  KEY `ID` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='编码表';

-- ----------------------------
-- Records of enum_const
-- ----------------------------
INSERT INTO `enum_const` VALUES ('00a59243dbc63ab3d57f3fd69760ees9', '是', '1', 'FlagIsStar', '1', '2018-01-10 17:55:47', '通知推送是否星标', null);
INSERT INTO `enum_const` VALUES ('030ebfea479b11e89697fcaa140ebf84', '删除', 'delete', 'FlagIntegratedMenuType', '1', '2018-04-24 16:39:33', '3', null);
INSERT INTO `enum_const` VALUES ('058cfaf74df211e8aac8fcaa140ebf84', '是', '1', 'FlagIsSystem', '0', '2018-05-02 18:17:30', '是否系统属性', null);
INSERT INTO `enum_const` VALUES ('0bc108f54df211e8aac8fcaa140ebf84', '否', '0', 'FlagIsSystem', '0', '2018-05-02 18:17:40', '是否系统属性', null);
INSERT INTO `enum_const` VALUES ('193ED9439C83154BE050007F01003EBF', '有效', '1', 'FlagJobValid', '1', '2017-09-25 10:20:45', '定时任务是否有效', null);
INSERT INTO `enum_const` VALUES ('193ED9439C84154BE050007F01003EBF', '无效', '0', 'FlagJobValid', '0', '2017-09-25 10:20:45', '定时任务是否有效', null);
INSERT INTO `enum_const` VALUES ('19BF5489AAF08C19E050007F01005C28', '执行中', '1', 'FlagJobStatus', '1', '2017-09-25 10:20:45', '任务状态', null);
INSERT INTO `enum_const` VALUES ('19BF5489AAF18C19E050007F01005C28', '成功', '2', 'FlagJobStatus', '0', '2017-09-25 10:20:45', '任务状态', null);
INSERT INTO `enum_const` VALUES ('19BF5489AAF28C19E050007F01005C28', '失败', '3', 'FlagJobStatus', '0', '2017-09-25 10:20:45', '任务状态', null);
INSERT INTO `enum_const` VALUES ('2', '是', '1', 'FlagIsvalid', '0', '2008-11-02 14:04:53', '是否有效', null);
INSERT INTO `enum_const` VALUES ('2bcba6af51d211e9a2c3fcaa140ebf84', '用户中心配置', '7', 'FlagConfigType', '1', '2019-03-29 11:24:33', '站点配置', null);
INSERT INTO `enum_const` VALUES ('3', '否', '0', 'FlagIsvalid', '1', '2008-11-02 14:04:53', '是否有效', null);
INSERT INTO `enum_const` VALUES ('402880951df7720e011df78e5bbb0111', '否', '0', 'FlagBak', '0', '2014-01-24 00:00:00', '是否强制更新', ',adult,');
INSERT INTO `enum_const` VALUES ('402880951df7720e011df78e5bbb0112', '是', '1', 'FlagBak', '1', '2014-01-24 00:00:00', '是否强制更新', ',adult,');
INSERT INTO `enum_const` VALUES ('4028809c1d625bcf011d66fd0dda0006', '是', '1', 'FlagActive', '0', '2008-11-04 00:00:00', '是否有效', null);
INSERT INTO `enum_const` VALUES ('4028809c1d625bcf011d66fdb39f0007', '否', '0', 'FlagActive', '0', '2008-11-04 00:00:00', '是否有效', null);
INSERT INTO `enum_const` VALUES ('4028826a1db7bb01011db7e623d70003', '管理员', '3', 'FlagRoleType', '0', '2008-11-20 00:00:00', '角色类型', null);
INSERT INTO `enum_const` VALUES ('46548fb4276511e8baa4fcaa140ebf84', '女', '0', 'FlagGender', '0', '2010-07-20 00:00:00', '性别', null);
INSERT INTO `enum_const` VALUES ('46a6f8d95f2611e89e1dfcaa140ebf84', '是', '1', 'FlagSiteSuperAdmin', '0', '2018-05-24 15:44:23', '是否是站点超管', null);
INSERT INTO `enum_const` VALUES ('46aa648f5f2611e89e1dfcaa140ebf84', '否', '0', 'FlagSiteSuperAdmin', '0', '2018-05-24 15:44:23', '是否是站点超管', null);
INSERT INTO `enum_const` VALUES ('495137d5276511e8baa4fcaa140ebf84', '男', '1', 'FlagGender', '1', '2010-07-20 00:00:00', '性别', null);
INSERT INTO `enum_const` VALUES ('4b808c1f071c11e89847fcaa140ebf84', 'updateColumnMenu', 'updateColumnMenu', 'FlagGridMenuType', '1', '2018-01-22 10:20:49', 'updateColumnMenu', null);
INSERT INTO `enum_const` VALUES ('4f6d91b94edd11e9a2c3fcaa140ebf84', '是', '1', 'FlagIsSingleton', '1', '2019-03-25 17:06:43', '是否是分布式单例', null);
INSERT INTO `enum_const` VALUES ('4f723aee4edd11e9a2c3fcaa140ebf84', '否', '0', 'FlagIsSingleton', '0', '2019-03-25 17:06:43', '是否是分布式单例', null);
INSERT INTO `enum_const` VALUES ('57d4184a3e2611e9a2c3fcaa140ebf84', '数据源配置', '1', 'FlagConfigType', '1', '2019-03-04 10:36:41', '站点配置', null);
INSERT INTO `enum_const` VALUES ('57d731663e2611e9a2c3fcaa140ebf84', '课程空间配置', '2', 'FlagConfigType', '1', '2019-03-04 10:36:41', '站点配置', null);
INSERT INTO `enum_const` VALUES ('57e354cf3e2611e9a2c3fcaa140ebf84', '平台业务配置', '3', 'FlagConfigType', '1', '2019-03-04 10:36:41', '站点配置', null);
INSERT INTO `enum_const` VALUES ('760665d5460911e9a2c3fcaa140ebf84', '是', '1', 'FlagIsShow', '1', '2019-03-14 11:30:06', '是否显示', null);
INSERT INTO `enum_const` VALUES ('760c8d8c460911e9a2c3fcaa140ebf84', '否', '0', 'FlagIsShow', '1', '2019-03-14 11:30:06', '是否显示', null);
INSERT INTO `enum_const` VALUES ('84a5ac89-6883-11e8-9e1d-fcaa140ebf84', '站点超管管理员', '9998', 'FlagRoleType', '0', '2018-06-05 10:43:07', '角色类型', null);
INSERT INTO `enum_const` VALUES ('9ef0d1ee3ff211e9a2c3fcaa140ebf84', '考试系统配置', '5', 'FlagConfigType', '1', '2019-03-06 17:31:29', '站点配置', null);
INSERT INTO `enum_const` VALUES ('9ef57b0e3ff211e9a2c3fcaa140ebf84', '工作室配置', '6', 'FlagConfigType', '1', '2019-03-06 17:31:29', '站点配置', null);
INSERT INTO `enum_const` VALUES ('a92817f1361611e8a37efcaa140ebf84', 'changeRouterMenu', 'changeRouterMenu', 'FlagGridMenuType', '1', '2018-04-02 09:39:18', 'changeRouterMenu', null);
INSERT INTO `enum_const` VALUES ('acf44775c4ec11e7bb1db083fedeaab1', '广播', '0', 'flagScopeType', '1', '2017-11-09 09:24:05', '发送类型', null);
INSERT INTO `enum_const` VALUES ('acf44ffdc4ec11e7bb1db083fedeaab1', '单播', '1', 'flagScopeType', '1', '2017-11-09 09:24:05', '发送类型', null);
INSERT INTO `enum_const` VALUES ('acf45747c4ec11e7bb1db083fedeaab1', '用户操作', '0', 'flagNoticeType', '1', '2017-11-09 09:24:05', '通知类型', null);
INSERT INTO `enum_const` VALUES ('acf45e67c4ec11e7bb1db083fedeaab1', '未读', '0', 'flagReaded', '1', '2017-11-09 09:24:05', '是否已读', null);
INSERT INTO `enum_const` VALUES ('acf4655ec4ec11e7bb1db083fedeaab1', '已读', '1', 'flagReaded', '1', '2017-11-09 09:24:05', '是否已读', null);
INSERT INTO `enum_const` VALUES ('cb245573ff1a11e7ab1d001e671d0be8', 'ajaxMenu', 'ajaxMenu', 'FlagGridMenuType', '1', '2018-01-22 10:20:20', 'ajaxMenu', null);
INSERT INTO `enum_const` VALUES ('ccefbc343e6111e9a2c3fcaa140ebf84', '异动与学籍对应配置', '4', 'FlagConfigType', '1', '2019-03-04 10:36:41', '站点配置', null);
INSERT INTO `enum_const` VALUES ('d2470b3cf02d11e7ab1d001e671d0be8', '超级管理员', '9999', 'FlagRoleType', '0', '2018-01-03 10:28:45', '角色类型', null);
INSERT INTO `enum_const` VALUES ('d314862aff1a11e7ab1d001e671d0be8', 'toUrlMenu', 'toUrlMenu', 'FlagGridMenuType', '1', '2018-01-22 10:20:33', 'toUrlMenu', null);
INSERT INTO `enum_const` VALUES ('d54b3e85479a11e89697fcaa140ebf84', '添加', 'add', 'FlagIntegratedMenuType', '1', '2018-04-24 16:38:16', '1', null);
INSERT INTO `enum_const` VALUES ('d6228e78ff1a11e7ab1d001e671d0be8', 'backMenu', 'backMenu', 'FlagGridMenuType', '1', '2018-01-22 10:20:38', 'backMenu', null);
INSERT INTO `enum_const` VALUES ('d956a336ff1a11e7ab1d001e671d0be8', 'messageMenu', 'messageMenu', 'FlagGridMenuType', '1', '2018-01-22 10:20:43', 'messageMenu', null);
INSERT INTO `enum_const` VALUES ('dc8475e8ff1a11e7ab1d001e671d0be8', 'formMenu', 'formMenu', 'FlagGridMenuType', '1', '2018-01-22 10:20:49', 'formMenu', null);
INSERT INTO `enum_const` VALUES ('e444c346abc23e26ghs7e5fa90ddbbse', '否', '0', 'FlagIsStar', '0', '2018-01-10 17:55:47', '通知推送是否星标', null);
INSERT INTO `enum_const` VALUES ('f02ce777a51711e89e1dfcaa140ebf84', '开放接口授权角色', '9000', 'FlagRoleType', '0', '2018-08-21 22:23:40', '角色类型', null);
INSERT INTO `enum_const` VALUES ('f16c536730a511e8a37efcaa140ebf84', 'simpleImportMenu', 'simpleImportMenu', 'FlagGridMenuType', '1', '2018-03-26 11:29:51', 'simpleImportMenu', null);
INSERT INTO `enum_const` VALUES ('f82ae75b479a11e89697fcaa140ebf84', '批量导入', 'batchAdd', 'FlagIntegratedMenuType', '1', '2018-04-24 16:39:14', '4', null);
INSERT INTO `enum_const` VALUES ('fc43396e479a11e89697fcaa140ebf84', '修改', 'update', 'FlagIntegratedMenuType', '1', '2018-04-24 16:39:21', '2', null);
INSERT INTO `enum_const` VALUES ('ff4e10d833f811e9a2c3fcaa140ebf84', '微信', '1', 'FlagPayType', '1', '2019-02-19 11:46:53', '线上支付方式', null);
INSERT INTO `enum_const` VALUES ('ff50ead333f811e9a2c3fcaa140ebf84', '支付宝', '2', 'FlagPayType', '0', '2019-02-19 11:46:53', '线上支付方式', null);

-- ----------------------------
-- Table structure for grid_basic_config
-- ----------------------------
DROP TABLE IF EXISTS `grid_basic_config`;
CREATE TABLE `grid_basic_config` (
  `id` varchar(50) NOT NULL DEFAULT '',
  `title` varchar(500) DEFAULT NULL COMMENT '标题',
  `can_search` varchar(1) DEFAULT NULL COMMENT '是否可以搜索',
  `can_add` varchar(1) DEFAULT NULL COMMENT '是否有添加功能',
  `can_delete` varchar(1) DEFAULT NULL COMMENT '是否有删除功能',
  `can_update` varchar(1) DEFAULT NULL COMMENT '是否有修改功能',
  `can_batch_add` varchar(1) DEFAULT NULL COMMENT '是否可以excel导入',
  `can_projections` varchar(1) DEFAULT NULL COMMENT '是否增加投影',
  `first_search` varchar(1) DEFAULT NULL COMMENT '是否有与搜索功能',
  `entity_class` varchar(100) DEFAULT NULL COMMENT '对应的实体类',
  `note` varchar(2000) DEFAULT NULL COMMENT '备注',
  `list_type` varchar(1) DEFAULT NULL COMMENT 'list方法的类型（0：dc；1：sql）',
  `dc` varchar(4000) DEFAULT NULL COMMENT '对应的dc查询语句',
  `flag_active` varchar(50) DEFAULT NULL COMMENT '是否可用',
  `flag_bak` varchar(50) DEFAULT NULL COMMENT '是否强制更新',
  `dateFormat` varchar(50) DEFAULT NULL COMMENT '日期格式',
  `sqlstr` text COMMENT '对应的sql查询语句',
  `statistics` varchar(1) DEFAULT NULL COMMENT '是否有统计功能',
  `s_note` varchar(500) DEFAULT NULL,
  `update_note` varchar(500) DEFAULT NULL,
  `is_enum` varchar(50) DEFAULT NULL COMMENT '用户角色类型',
  `countsql` text COMMENT '统计记录数sql',
  `isGroupSql` varchar(2) DEFAULT '0' COMMENT '最外层的sql是否使用了group by,0 否,1 是',
  `LIST_FUNCTION` varchar(1) DEFAULT NULL COMMENT '列表加载方式，list1，list3',
  `FLAG_INTE_ACTIVE` varchar(50) DEFAULT NULL COMMENT '继承按钮是否激活',
  `fk_web_site_id` varchar(50) NOT NULL COMMENT '所属站点',
  `can_checked_all` varchar(1) DEFAULT NULL COMMENT '是否开启选择全部数据',
  `add_mode` varchar(10) DEFAULT NULL COMMENT 'add模式dialog/drawer',
  `add_cmp_name` varchar(50) DEFAULT NULL COMMENT '添加时的自定义组件的name',
  `fk_chart_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `flag_bak` (`flag_bak`) USING BTREE,
  KEY `flag_active` (`flag_active`) USING BTREE,
  KEY `grid_basic_config_ibfk_3` (`fk_web_site_id`),
  CONSTRAINT `grid_basic_config_ibfk_1` FOREIGN KEY (`flag_bak`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `grid_basic_config_ibfk_2` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `grid_basic_config_ibfk_3` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='grid配置表';

-- ----------------------------
-- Records of grid_basic_config
-- ----------------------------
INSERT INTO `grid_basic_config` VALUES ('allSiteRoleManage', '角色管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.CorePePriRole', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	role.id id,\n	role. CODE code,\n	role. NAME name,\n	site. NAME siteName,\n	roleType. NAME type,\n  ifnull(em2.name,\'否\') isAdmin\nFROM\n	pe_pri_role role\nINNER JOIN enum_const roleType ON roleType.id = role.FLAG_ROLE_TYPE\nINNER JOIN pe_web_site site ON site.id = role.fk_web_site_id\nleft JOIN enum_const em2 ON em2.id=role.flag_site_super_admin', null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('apiSiteConfig', '外接项目站点管理', '1', null, null, null, null, '1', '0', 'com.whaty.api.grant.domain.bean.ApiSiteConfig', null, '0', 'ApiSiteConfig;enumConstByFlagActive;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('baseCategoryInterfaceManage', '基础功能接口管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.PrBaseCategoryInterface', null, '0', 'PrBaseCategoryInterface;\npeBaseCategory;\npeInterface,[enumConstByFlagIsvalid];', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('baseCategoryManage', '基础功能管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.PeBaseCategory', null, '0', 'PeBaseCategory;\nenumConstByFlagIsvalid;\nenumConstByFlagIsSystem;\npeBaseCategoryGroup;', '4028809c1d625bcf011d66fd0dda0006', null, null, '', null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('courseResourceManage', '课程库', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.PeCourse', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n       cou.id as id,\n       cou.name as name,\n       cou.code as code,\n       trt.id as  trainingTargetId,\n       trt.name as trainingTargetName,\n       cou.course_intro as courseIntro,\n       cou.course_url as courseUrl\nFROM pe_course cou\nINNER JOIN enum_const  trt ON trt.id = cou.flag_training_target\nWHERE\n       cou.site_code = \'${siteCode}\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('courseTypeManage', '课程类型管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.EnumConst', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	id AS id,\n	NAME AS name\nFROM\n	enum_const\nWHERE\n	namespace = \'flagCourseType\'\nAND team = \',${siteCode},\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('educationalBackgroundManage', '最高学历管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.EnumConst', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	id AS id,\n	NAME AS name\nFROM\n	enum_const\nWHERE\n	namespace = \'flagEducationalBackground\'\nAND team = \',${siteCode},\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('emailMessageGroupManage', '邮件消息组管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.EmailMessageGroup', null, '0', 'EmailMessageGroup;enumConstByFlagActive;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('emailMessageSiteManage', '邮件消息与站点管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.EmailMessageSite', null, '0', 'EmailMessageSite;emailMessageGroup;peWebSite;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('enumConstManage', 'EnumConst管理', '1', null, null, null, null, '0', '0', 'com.whaty.core.framework.bean.EnumConst', null, '0', 'EnumConst;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('gridManage', 'grid管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.GridBasicConfig', null, '0', 'GridBasicConfig;\nenumConstByFlagBak:left;\nenumConstByFlagActive:left;\npeWebSite;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('hotelResourceManage', '合作宾馆', '0', null, null, null, null, '0', '0', 'com.whaty.domain.bean', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	hotel.id AS id,\n	hotel. NAME AS name,\n	hotel.address AS address,\n	hotel.contact AS contact,\n	hotel.agree_price AS agreePrice,\n	hotel.agreement AS agreement,\n	hotel.remark AS remark\nFROM\n	pe_hotel hotel\nWHERE\n	hotel.site_code = \'${siteCode}\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '0', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('interfaceManage', '接口管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.PeInterface', null, '0', 'PeInterface;\nenumConstByFlagIsvalid;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('operateGuideManage', '操作指导管理', '1', '1', '1', '1', '0', '1', '0', 'com.whaty.domain.bean.OperateGuideDescription', null, '0', 'OperateGuideDescription;operateGuide;enumConstByFlagActive;peWebSite;', '4028809c1d625bcf011d66fd0dda0006', null, 'yyyy-MM-dd', null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('peWebSiteConfigManage', '站点基础配置管理', '1', null, null, null, null, '0', '0', 'com.whaty.domain.bean.PeWebSiteConfig', null, '0', 'PeWebSiteConfig;enumConstByFlagConfigType;peWebSite;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('placeResourceManage', '培训地点', '0', null, null, null, null, '0', '0', 'com.whaty.domain.bean.PePlace', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	place.id AS id,\n	place. NAME AS name,\n	unit.id AS unitId,\n	unit.`name` AS unitName,\n	place.capacity AS capacity,\n	place.charges AS charges,\n	fsz.id AS schoolZoneId,\n	fsz. NAME AS schoolZoneName\nFROM\n	pe_place place\nINNER JOIN pe_unit unit ON unit.id = place.fk_place_unit\nINNER JOIN enum_const fsz ON fsz.id = place.flag_school_zone\nWHERE\n	place.site_code = \'${siteCode}\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '0', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('positionalTitleManage', '讲师职称管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.EnumConst', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	id AS id,\n	NAME AS name\nFROM\n	enum_const\nWHERE\n	namespace = \'flagPositionalTitle\'\nAND team = \',${siteCode},\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('printTemplate', '打印模板管理', '1', '1', '1', '1', '0', '1', '0', 'com.whaty.domain.bean.PePrintTemplate', null, '0', 'PePrintTemplate;{code:=word or pdf or excel};{siteCode:=${siteCode}};', '4028809c1d625bcf011d66fd0dda0006', null, 'yyyy-MM-dd', null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('printTemplateGroup', '打印模板分组管理', '1', '1', '1', '1', '0', '1', '0', 'com.whaty.domain.bean.PePrintTemplateGroup', null, '0', 'PePrintTemplateGroup;pePrintTemplate,{code:= pdf or word or excel},{siteCode:=${siteCode}};', '4028809c1d625bcf011d66fd0dda0006', null, 'yyyy-MM-dd', null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('printTemplateSign', '打印模板占位符管理', '1', '1', '1', '1', '0', '1', '0', 'com.whaty.domain.bean.PePrintTemplateSign', null, '0', 'PePrintTemplateSign;enumConstByFlagActive;pePrintTemplateGroup:pePrintTemplate,{code:=word or pdf or excel},{siteCode:=${siteCode}};', '4028809c1d625bcf011d66fd0dda0006', null, 'yyyy-MM-dd', null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('saRoleManage', '超管角色管理', '1', '1', '1', '1', '1', '0', '0', 'com.whaty.core.framework.bean.CorePePriRole', null, '0', 'CorePePriRole;\nenumConstByFlagRoleType:left;\npeWebSite;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('saSiteManage', '站点管理', '1', '1', '1', '1', '0', '0', '0', 'com.whaty.core.framework.bean.PeWebSite', null, '0', 'PeWebSite;peWebSiteDetail:left;', '4028809c1d625bcf011d66fd0dda0006', null, null, '', null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('saUserManage', '超管用户管理', '1', '1', '1', '1', '1', '0', '0', 'com.whaty.core.framework.bean.CoreSsoUser', null, '0', 'CoreSsoUser;\ncorePePriRole:left,[enumConstByFlagRoleType:left];enumConstByFlagIsvalid:left;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('saVueRouterManage', '超管路由管理', '1', '1', '1', '1', '0', '0', '0', 'com.whaty.core.framework.bean.PeVueRouter', null, '0', 'PeVueRouter;\nenumConstByFlagRouterType:left;\npeWebSite;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('scheduleJobManage', '调度任务管理', '1', null, null, null, null, '0', '0', 'com.whaty.schedule.bean.PeScheduleJob', null, '0', 'PeScheduleJob;enumConstByFlagJobValid;enumConstByFlagIsShow,{code:=1};enumConstByFlagIsSingleton;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('scheduleJobRecordShow', '调度记录查询', '1', null, null, null, null, '0', '0', 'com.whaty.schedule.bean.PeScheduleJobRecord', null, '0', 'PeScheduleJobRecord;enumConstByFlagJobStatus;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('scheduleJobTriggerManage', '调度时间管理', '1', null, null, null, null, '0', '0', 'com.whaty.schedule.bean.PeScheduleTrigger', null, '0', 'PeScheduleTrigger;enumConstByFlagJobValid;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('schoolZoneManage', '校区管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.EnumConst', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	id AS id,\n	NAME AS name\nFROM\n	enum_const\nWHERE\n	namespace = \'flagSchoolZone\'\nAND team = \',${siteCode},\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('scoreComposeItemManage', '成绩项管理', '1', null, null, null, null, '1', '0', 'com.whaty.bean.PeScoreComposeItem', null, '0', 'PeScoreComposeItem;enumConstByFlagYesNo;enumConstByFlagIsExport;enumConstByFlagTeacherCanEnter;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('sendMessageGroupManage', '发送消息组管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.SendMessageGroup', null, '0', 'SendMessageGroup;enumConstByFlagActive;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('sendMessageSiteManage', '发送消息站点管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.SendMessageSite', null, '0', 'SendMessageSite;sendMessageType,[sendMessageGroup],[enumConstByFlagMessageType];peWebSite;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('sendMessageTypeManage', '发送消息类型管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.SendMessageType', null, '0', 'SendMessageType;sendMessageGroup;enumConstByFlagMessageType;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('siteManagerManage', '管理员用户管理', '1', '0', '0', '1', '0', '1', '0', 'com.whaty.bean.PeManager', null, '1', '', '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	m.id as id,\n	m.login_id as loginId,\n	m.true_name as trueName,\n  pr.id as roleId,\n	pr.name as roleName,\n  un.id as unitId,\n	un.name as unitName,\n  ac.id as activeId,\n	ac.name as active,\n  ct.id as cardTypeId,\n	ct.name as cardType,\n	m.card_no as cardNo,\n  ge.id as genderId,\n	ge.name as gender,\n	m.birthday as birthday,\n	m.work_unit as workUnit,\n	m.mobile as mobile,\n	m.telephone as telephone,\n	m.home_phone as homePhone,\n	m.email as email,\n	m.address as address,\n	m.zip_code as zipCode,\n	m.qq as qq,\n	if(wu.openid is null, \'是\', \'否\') as hasWeChat\nFROM\n	pe_manager m\nINNER JOIN sso_user ss ON ss.id = m.fk_sso_user_id\nINNER JOIN pe_unit un on un.id = m.fk_unit_id AND [peUnit|un.id]\nINNER JOIN pe_pri_role pr ON pr.id = ss.FK_ROLE_ID\nINNER JOIN enum_const ty ON ty.id = pr.FLAG_ROLE_TYPE\nINNER JOIN enum_const ac ON ac.id = m.flag_active\nLEFT JOIN enum_const ge ON ge.id = m.flag_gender\nLEFT JOIN enum_const ct on ct.id = m.flag_card_type\r\nLEFT JOIN wechat_user wu on wu.fk_sso_user_id = ss.id\nWHERE\n	ty.code = \'3\'\r\nAND m.site_code = \'${siteCode}\'', null, null, null, null, null, null, null, null, '4028ae316a09ee85016a09f4d7640000', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('siteRoleManage', '角色管理', '1', '1', '1', '1', '0', '1', '0', 'com.whaty.bean.PePriRole', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	role.id id,\n	role.NAME name,\n	roleType .name roleName\nFROM\n	pe_pri_role role\nINNER JOIN enum_const roleType ON roleType.id = role.FLAG_ROLE_TYPE\nleft JOIN enum_const em2 ON em2.id=role.flag_site_super_admin\nWHERE \r\n	roleType.code = \'3\' \r\nand (\r\n		role.flag_site_super_admin is null \r\n	or \r\n		em2.code=\'0\'\r\n)\r\nAND role.site_code = \'${siteCode}\'', null, null, null, null, null, null, null, null, '4028ae316a09ee85016a09f4d7640000', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('siteSuperMangerManage', '站点超管管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.SsoUser', null, '0', 'SsoUser;\npePriRole,enumConstByFlagRoleType,{code:=9998};\nenumConstByFlagBak:left;\nenumConstByFlagIsvalid:left;\nenumConstByFlagGender:left;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('smsMessageGroupManage', '短信消息组管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.SmsMessageGroup', null, '0', 'SmsMessageGroup;enumConstByFlagActive;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('smsMessageSiteManage', '短信消息与站点管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.SmsMessageSite', null, '0', 'SmsMessageSite;peWebSite;smsMessageGroup;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('stationMessageGroupManage', '站内信组管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.StationMessageGroup', null, '0', 'StationMessageGroup;enumConstByFlagActive;', '4028809c1d625bcf011d66fd0dda0006', null, 'yyyy-MM-dd', null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('stationMessageSignManage', '站内信占位符管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.StationMessageGroupSign', null, '0', 'StationMessageGroupSign;stationMessageGroup;enumConstByFlagActive;', '4028809c1d625bcf011d66fd0dda0006', null, 'yyyy-MM-dd', null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('stationMessageSiteManage', '站内信站点管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.StationMessageGroupSite', null, '0', 'StationMessageGroupSite;stationMessageGroup;peWebSite;enumConstByFlagActive;', '4028809c1d625bcf011d66fd0dda0006', null, 'yyyy-MM-dd', null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('stuEnrollInfoManage', '学生信息配置', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.PrColumnsConfig', null, '0', 'PrColumnsConfig;{siteCode:=${siteCode}};enumConstByFlagInfoType:left;', '4028809c1d625bcf011d66fd0dda0006', null, 'yyyy-MM-dd', '', null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('suitableTargetManage', '适合对象管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.EnumConst', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	id AS id,\n	NAME AS name\nFROM\n	enum_const\nWHERE\n	namespace = \'flagSuitableTarget\'\nAND team = \',${siteCode},\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('systemCustomConfigManage', '定制配置管理', '1', null, null, null, null, '1', '0', 'com.whaty.custom.bean.SystemCustomConfig', null, '0', 'SystemCustomConfig;peBaseCategory;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('systemCustomConfigUrlManage', '定制配置Url管理', '1', null, null, null, null, '1', '0', 'com.whaty.custom.bean.SystemCustomConfigUrl', null, '0', 'SystemCustomConfigUrl;systemCustomConfig;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('systemSiteCustomConfigManage', '定制配置站点关联', '1', null, null, null, null, '1', '0', 'com.whaty.custom.bean.SystemSiteCustomConfig', null, '0', 'SystemSiteCustomConfig;peWebSite;systemCustomConfig;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('systemVariablesManage', 'systemVariables管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.SystemVariables', null, '0', 'SystemVariables;{siteCode:=${siteCode}}\nenumConstByFlagBak:left;\nenumConstByFlagPlatformSection:left;', '4028809c1d625bcf011d66fd0dda0006', null, 'yyyy-MM-dd', '', null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);
INSERT INTO `grid_basic_config` VALUES ('teacherResourceManage', '师资库', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.PeTeacher', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\r\n	tea.id as id,\r\n	tea.login_id as loginId,\r\n	tea.true_name as trueName,\r\n	pt.id as positionalTitleId,\r\n	pt.name as positionalTitle,\r\n	eb.id as eduBackgroundId,\r\n	eb.name as eduBackground,\r\n	tt.id as tutorTeacherId,\r\n	tt.name as tutorTeacher,\r\n	ct.id as courseTeacherId,\r\n	ct.name as courseTeacher,\r\n	ic.id as classMasterId,\r\n	ic.name as classMaster,\r\n	pu.id as unitId,\r\n	pu.name as unitName,\r\n	ac.id as activeId,\r\n	ac.name as activeName,\r\n	cat.id as cardTypeId,\r\n	cat.name as cardType,\r\n	tea.card_no as cardNo,\r\n	gen.id as genderId,\r\n	gen.name as gender,\r\n	tea.birthday as birthday,\r\n	tea.work_unit as workUnit,\r\n	tea.mobile as mobile,\r\n	tea.work_phone as workPhone,\r\n	tea.home_phone as homePhone,\r\n	tea.email as email,\r\n	tea.address as address,\r\n	tea.zip_code as zipCode,\r\n	tea.qq as qq,\r\n	if(wu.openid is null, \'否\', \'是\') as hasWeChat,\r\n	tea.introduction as introduction\r\nFROM\r\n	pe_teacher tea\r\nINNER JOIN enum_const pt ON pt.id = tea.flag_positional_title\r\nLEFT JOIN enum_const eb ON eb.id = tea.flag_educational_background\r\nINNER JOIN enum_const tt ON tt.id = tea.flag_tutor_teacher\r\nINNER JOIN enum_const ct ON ct.id = tea.flag_course_teacher\r\nINNER JOIN enum_const ic ON ic.id = tea.flag_isclassmaster\r\nINNER JOIN pe_unit pu ON pu.id = tea.fk_unit_id and [peUnit|pu.id]\r\nINNER JOIN enum_const ac ON ac.id = tea.flag_active\r\nLEFT JOIN enum_const cat ON cat.id = tea.flag_card_type\r\nLEFT JOIN enum_const gen ON gen.id = tea.flag_gender\r\nLEFT JOIN wechat_user wu ON wu.fk_sso_user_id = tea.fk_sso_user_id\r\nWHERE\r\n	tea.site_code = \'${siteCode}\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('trainingAreaManage', '培训地区管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.EnumConst', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	id AS id,\n	NAME AS name\nFROM\n	enum_const\nWHERE\n	namespace = \'flagTrainingArea\'\nAND team = \',${siteCode},\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('trainingTargetManage', '培训对象管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.EnumConst', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	id AS id,\n	NAME AS name\nFROM\n	enum_const\nWHERE\n	namespace = \'flagTrainingTarget\'\nAND team = \',${siteCode},\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('trainingTypeManage', '培训类型管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.EnumConst', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	id AS id,\n	NAME AS name\nFROM\n	enum_const\nWHERE\n	namespace = \'flagTrainingType\'\nAND team = \',${siteCode},\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('unitManage', '单位管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.PeUnit', null, '0', 'PeUnit;{siteCode:=${siteCode}};enumConstByFlagUnitType;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('unitTypeManage', '机构类型管理', '1', null, null, null, null, '1', '0', 'com.whaty.core.framework.bean.EnumConst', null, '1', null, '4028809c1d625bcf011d66fd0dda0006', null, null, 'SELECT\n	id AS id,\n	NAME AS name\nFROM\n	enum_const\nWHERE\n	namespace = \'flagUnitType\'\nAND team = \',${siteCode},\'', null, null, null, null, null, null, '0', null, '4028ae316a09ee85016a09f4d7640000', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('weChatTemplateColumnManage', '微信模板消息字段管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.WeChatTemplateMessageColumn', null, '0', 'WeChatTemplateMessageColumn;enumConstByFlagActive;weChatTemplateMessageGroup;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('weChatTemplateGroupManage', '微信模板组管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.WeChatTemplateMessageGroup', null, '0', 'WeChatTemplateMessageGroup;enumConstByFlagActive;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('weChatTemplateSiteManage', '微信模板消息站点管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.message.WeChatTemplateMessageSite', null, '0', 'WeChatTemplateMessageSite;weChatTemplateMessageGroup;peWebSite;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, '0', null, '83356b76275211e8baa4fcaa140ebf84', '1', 'dialog', null, null);
INSERT INTO `grid_basic_config` VALUES ('workSpaceConfigManage', '工作室配置管理', '1', null, null, null, null, '1', '0', 'com.whaty.domain.bean.PeSiteConfig', null, '0', 'PeSiteConfig;', '4028809c1d625bcf011d66fd0dda0006', null, null, null, null, null, null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84', '1', null, null, null);

-- ----------------------------
-- Table structure for grid_column_config
-- ----------------------------
DROP TABLE IF EXISTS `grid_column_config`;
CREATE TABLE `grid_column_config` (
  `ID` varchar(50) NOT NULL DEFAULT '' COMMENT '主键',
  `fk_action_grid_config_id` varchar(50) NOT NULL COMMENT 'actionId',
  `name` varchar(50) NOT NULL COMMENT '列名',
  `dataindex` text COMMENT '索引（sql查询的字段名或dc查询对象属性名）',
  `data_column` varchar(100) DEFAULT NULL COMMENT 'bean对象的属性字段',
  `search` varchar(1) DEFAULT NULL COMMENT '是否能搜索',
  `to_add` varchar(1) DEFAULT NULL COMMENT '是否能添加',
  `column_can_update` varchar(1) DEFAULT NULL COMMENT '列是否允许修改',
  `list` varchar(1) DEFAULT NULL COMMENT '是否显示在列表中',
  `report` varchar(1) DEFAULT NULL COMMENT '是否可以导出',
  `type` varchar(50) DEFAULT NULL COMMENT '类型（文本框、下拉框、、）',
  `dateFormat` varchar(50) DEFAULT NULL COMMENT '日期格式',
  `allow_blank` varchar(1) DEFAULT NULL COMMENT '是否允许为空',
  `max_length` decimal(8,0) DEFAULT NULL COMMENT '最大长度',
  `check_message` varchar(50) DEFAULT NULL COMMENT '校验提醒信息',
  `check_regular` varchar(200) DEFAULT NULL,
  `text_field_parameters` text,
  `combo_sql` text COMMENT '自定义sql',
  `serial_number` decimal(4,0) DEFAULT NULL COMMENT '序列',
  `flag_active` varchar(50) DEFAULT NULL COMMENT '是否有效',
  `flag_bak` varchar(50) DEFAULT NULL COMMENT '是否强制更新',
  `user_type` varchar(50) DEFAULT NULL COMMENT '用户类型',
  `team` text,
  `s_note` varchar(50) DEFAULT NULL COMMENT '备注',
  `sql_result` varchar(255) DEFAULT NULL COMMENT '对应sql结果集，list3使用',
  `is_html` tinyint(1) DEFAULT NULL COMMENT '字段是否是html',
  `default_text` varchar(30) DEFAULT NULL COMMENT '列的值为空时, 列表中默认显示的值',
  `cascade_columns` varchar(255) DEFAULT NULL COMMENT '级联的列的id组成的ids',
  PRIMARY KEY (`ID`),
  KEY `FLAG_BAK` (`flag_bak`) USING BTREE,
  KEY `FLAG_ACTIVE` (`flag_active`) USING BTREE,
  CONSTRAINT `grid_column_config_ibfk_1` FOREIGN KEY (`flag_bak`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `grid_column_config_ibfk_2` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='grid配置：column配置信息';

-- ----------------------------
-- Records of grid_column_config
-- ----------------------------
INSERT INTO `grid_column_config` VALUES ('4028acc86200026b01620017278d0002', 'saRoleManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', null, null, '', '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028acc86200026b0162001801a70003', 'saRoleManage', '角色名称', 'name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028acc86200026b01620018614f0004', 'saRoleManage', '角色类型', 'enumConstByFlagRoleType.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028acc86200026b01620018c6260005', 'saRoleManage', '角色类型id', 'enumConstByFlagRoleType.id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', null, null, '', '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ad1c64e9e08e0164e9e711c50000', 'gridManage', 'id', 'id', '', '0', '0', '1', '0', '0', 'TextField', null, '1', '50', '字母和数字，长度20', '^[A-Za-z0-9]{20}$', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ad1c64e9e08e0164ea1408c00002', 'gridManage', '站点id', 'peWebSite.id', '', '0', '0', '1', '0', '0', 'TextField', null, '1', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ad1c64e9e08e0164ea14b9520003', 'gridManage', '站点name', 'peWebSite.name', '', '0', '0', '1', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62227d740162228574da0000', 'saRoleManage', '站点', 'peWebSite.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62227d740162228c8d940001', 'saRoleManage', '站点id', 'peWebSite.id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', null, null, '', '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62704dc70162705b62250000', 'saSiteManage', '名称', 'name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62704dc70162705baad10001', 'saSiteManage', 'code', 'code', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62704dc70162705bfeb90002', 'saSiteManage', '域名', 'domain', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62704dc70162705c80bd0003', 'saSiteManage', '数据源', 'datasourceCode', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62704dc70162705d00d60004', 'saSiteManage', '激活状态', 'activeStatus', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62704dc70162705d6b8e0005', 'saSiteManage', 'appId', 'appId', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62704dc7016270614b000006', 'saVueRouterManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', null, null, '', '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62704dc701627061864a0007', 'saVueRouterManage', '名称', 'name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62704dc701627061e2100008', 'saVueRouterManage', '类型', 'enumConstByFlagRouterType.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62704dc701627062322a0009', 'saVueRouterManage', '类型id', 'enumConstByFlagRouterType.id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', null, null, '', '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62704dc7016270628824000a', 'saVueRouterManage', '配置', 'routeRconfig', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '1000', null, null, '', '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62704dc701627062f824000b', 'saSiteManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', null, null, '', '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef627081d5016270a6397a0000', 'saVueRouterManage', '站点', 'peWebSite.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef627081d5016270a67aeb0001', 'saVueRouterManage', '站点id', 'peWebSite.id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', null, null, '', '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef62d736780162d74bc27f0000', 'saRoleManage', '角色code', 'code', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef63205de50163206b791d0000', 'baseCategoryManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef63205de50163206bdc6d0001', 'baseCategoryManage', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '20', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef63205de50163206c2da40002', 'baseCategoryManage', '编号', 'code', '', '1', '1', '0', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef63205de50163206c6a630003', 'baseCategoryManage', 'url', 'url', '', '1', '1', '0', '1', '1', 'TextField', null, '1', '100', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef63205de50163206ce5a80004', 'baseCategoryManage', '是否有效', 'enumConstByFlagIsvalid.name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef63205de50163206d34130005', 'baseCategoryManage', '是否有效id', 'enumConstByFlagIsvalid.id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef63205de50163206da2a40006', 'baseCategoryManage', '是否系统功能', 'enumConstByFlagIsSystem.name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef63205de50163206de7900007', 'baseCategoryManage', '是否系统功能id', 'enumConstByFlagIsSystem.id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef63205de50163206e41e00008', 'baseCategoryManage', '类别', 'peBaseCategoryGroup.name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '8', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef63207c730163207e6fbf0000', 'baseCategoryManage', 'gridId', 'gridBasicConfig.id', '', '1', '1', '0', '1', '1', 'TextField', null, '1', '50', '', '', null, 'select id, concat(id, \'-\', title) as name from grid_basic_config', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef632509050163251a957d0000', 'interfaceManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef632509050163251ae5c70001', 'interfaceManage', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '20', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef632509050163251b3a7e0002', 'interfaceManage', 'url', 'url', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '100', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef632509050163251b99ea0003', 'interfaceManage', 'http方法', 'method', '', '1', '1', '0', '1', '1', 'TextField', null, '1', '20', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef63251ebc016325248ad50000', 'interfaceManage', '是否有效', 'enumConstByFlagIsvalid.name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef63251ebc01632524cd990001', 'interfaceManage', '是否有效id', 'enumConstByFlagIsvalid.id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef632546a70163254a57660000', 'baseCategoryInterfaceManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef632546a70163254b866d0001', 'baseCategoryInterfaceManage', '接口名称', 'peInterface.name', '', '1', '0', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef632546a70163254bded70002', 'baseCategoryInterfaceManage', '接口url', 'peInterface.url', '', '1', '0', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef632546a70163254c47970003', 'baseCategoryInterfaceManage', '接口方法', 'peInterface.method', '', '1', '0', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adef632546a70163254ce4990004', 'baseCategoryInterfaceManage', '是否有效', 'peInterface.enumConstByFlagIsvalid.name', '', '1', '0', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adf86325052f01632525c46f0000', 'saSiteManage', 'peWebSiteDetailId', 'peWebSiteDetail.id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adf86325052f0163252714bc0001', 'saSiteManage', '标题', 'peWebSiteDetail.title', '', '1', '1', '1', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adf86325052f0163252b80c40002', 'saSiteManage', 'pcLogo', 'peWebSiteDetail.pcLogo', '', '0', '1', '1', '1', '1', 'File', null, '1', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028adf86325052f0163252c05670003', 'saSiteManage', 'mobileLogo', 'peWebSiteDetail.mobileLogo', '', '0', '1', '1', '1', '1', 'File', null, '1', '50', '', '', null, '', '8', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3162fbab500162fbd6fae70004', 'printTemplate', 'id', 'id', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3162fbab500162fbd75bd80005', 'printTemplate', '名称', 'title', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3162fbab500162fbd8fcb00006', 'printTemplate', '类型', 'code', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3162fbab500162fbd95cd70007', 'printTemplate', '自定义模板路径', 'path', '', '1', '0', '0', '1', '1', 'TextField', null, '1', '100', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3162fbab500162fbd9f3490008', 'printTemplate', '系统默认模板路径', 'defaultPath', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '100', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3162fbab500162fbdacee30009', 'printTemplate', '查询sql', 'searchSql', '', '0', '1', '0', '1', '1', 'TextArea', null, '0', '5000', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3163007a1c0163007de7fc0003', 'printTemplate', '查询类型', 'searchType', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '2000', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3163011d9e0163012844850000', 'printTemplateGroup', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3163011d9e0163012892790001', 'printTemplateGroup', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3163011d9e01630128de630002', 'printTemplateGroup', '序号', 'serialNumber', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3163011d9e0163012990960003', 'printTemplateGroup', '所属模板', 'pePrintTemplate.title', '', '1', '0', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3163012a710163012f15a30000', 'printTemplateGroup', '所属模板id', 'pePrintTemplate.id', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3163015b850163016c4d8e0000', 'printTemplateSign', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3163015b850163016cfe7d0001', 'printTemplateSign', '占位符名', 'name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3163015b850163016d4b780002', 'printTemplateSign', '占位符', 'sign', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3163015b850163016dc91c0003', 'printTemplateSign', '序号', 'serialNumber', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3163015b850163016f44c30004', 'printTemplateSign', '是否在用户界面列出', 'isShow', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3163015b850163016faa210005', 'printTemplateSign', '是否有效', 'enumConstByFlagActive.name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae31635d9fae01635da7e03e0002', 'operateGuideManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae31635d9fae01635da8c86a0003', 'operateGuideManage', '名称', 'name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae316382328f0163823a8e310000', 'operateGuideManage', '顺序', 'serialNumber', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '错误只能为1-2位整数', '^[0-9]{1,2}$', null, '', '8', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165ae6c7f0165b19f4fe40000', 'stationMessageGroupManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '1', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165ae6c7f0165b1a17e320001', 'stationMessageGroupManage', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165ae6c7f0165b1a1b79e0002', 'stationMessageGroupManage', '查询sql', 'dataSql', '', '1', '1', '0', '1', '1', 'TextArea', null, '0', '5000', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165ae6c7f0165b1a2087d0003', 'stationMessageGroupManage', '是否有效', 'enumConstByFlagActive.name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165ae6c7f0165b1b844ec0004', 'stationMessageGroupManage', '编号', 'code', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165b204530165b2060c190000', 'stationMessageSignManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165b204530165b2066ceb0001', 'stationMessageSignManage', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165b204530165b2069c160002', 'stationMessageSignManage', '占位符', 'sign', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165b204530165b206e86a0003', 'stationMessageSignManage', '是否有效', 'enumConstByFlagActive.name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165b204530165b2076e8f0004', 'stationMessageSignManage', '是否在前台显示', 'isShow', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '必须为0或1', '^(0|1)$', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165b204530165b207afc50005', 'stationMessageSignManage', '实例', 'example', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165b213780165b21960d80000', 'stationMessageSiteManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165b213780165b219c0180001', 'stationMessageSiteManage', '站内信组', 'stationMessageGroup.name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165b213780165b21c81e90002', 'stationMessageSiteManage', '站点', 'peWebSite.name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae3165b213780165b21d05c90003', 'stationMessageSiteManage', '是否有效', 'enumConstByFlagActive.name', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028ae316904a6d8016904a760160000', 'scheduleJobManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316904a6d8016904a7cd560001', 'scheduleJobManage', '组', 'group', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316904a6d8016904a807100002', 'scheduleJobManage', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316904a6d8016904a8981b0003', 'scheduleJobManage', '执行类全限定名', 'beanClass', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316904a6d8016904a91a580004', 'scheduleJobManage', 'IOC容器中名称', 'springId', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316904a6d8016904a975900005', 'scheduleJobManage', '执行方法', 'methodName', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316904a6d8016904a9c10b0006', 'scheduleJobManage', '任务描述', 'description', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316904a6d8016904aa45740007', 'scheduleJobManage', '是否有效', 'enumConstByFlagJobValid.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316904a6d8016904aaa4ff0008', 'scheduleJobManage', 'enumConstByFlagJobValid.id', 'enumConstByFlagJobValid.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '8', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316909c14c016909cf59380002', 'scheduleJobTriggerManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316909c14c016909cfa35e0003', 'scheduleJobTriggerManage', '组', 'group', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316909c14c016909cfd2540004', 'scheduleJobTriggerManage', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316909c14c016909ed23bb0005', 'scheduleJobTriggerManage', '是否有效', 'enumConstByFlagJobValid.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316909c14c016909ed5d960006', 'scheduleJobTriggerManage', 'enumConstByFlagJobValid.id', 'enumConstByFlagJobValid.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316909f753016909fb2b8f0002', 'scheduleJobTriggerManage', 'cron表达式', 'cronExpression', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690db61d01690dba022a0003', 'scheduleJobTriggerManage', '传递参数', 'data', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '500', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690dd82901690ddddde70003', 'scheduleJobRecordShow', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690dd82901690dde269b0004', 'scheduleJobRecordShow', '组', 'jobGroup', '', '1', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690dd82901690dde68e40005', 'scheduleJobRecordShow', '名称', 'jobName', '', '1', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690dd82901690ddeb8890006', 'scheduleJobRecordShow', '触发器组', 'triggerGroup', '', '1', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690dd82901690ddf018f0007', 'scheduleJobRecordShow', '触发器名称', 'triggerName', '', '1', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690dd82901690ddf6c020008', 'scheduleJobRecordShow', '状态', 'enumConstByFlagJobStatus.name', '', '1', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690dd82901690ddfaef30009', 'scheduleJobRecordShow', '计划执行时间', 'planTime', '', '1', '0', '0', '1', '1', 'datetime', 'yyyy-MM-dd HH:mm:ss', '1', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690dd82901690de083a3000a', 'scheduleJobRecordShow', '开始时间', 'startTime', '', '1', '0', '0', '1', '1', 'datetime', 'yyyy-MM-dd HH:mm:ss', '1', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690dd82901690de10a30000b', 'scheduleJobRecordShow', '结束时间', 'endTime', '', '1', '0', '0', '1', '1', 'date', 'yyyy-MM-dd HH:mm:ss', '1', '50', '', '', null, '', '8', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690dd82901690de176ca000c', 'scheduleJobRecordShow', '下次执行时间', 'nextTime', '', '1', '0', '0', '1', '1', 'datetime', 'yyyy-MM-dd HH:mm:ss', '1', '50', '', '', null, '', '9', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690dd82901690de1be38000d', 'scheduleJobRecordShow', '执行参数', 'initData', '', '1', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '10', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31690dd82901690de214a3000e', 'scheduleJobRecordShow', '异常信息', 'exceptionMessage', '', '1', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '11', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316913114a0169131471270003', 'scheduleJobRecordShow', '执行机', 'executeMachine', '', '1', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '12', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31694c6ac401694c6dcabb0000', 'peWebSiteConfigManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31694c6ac401694c6e26df0001', 'peWebSiteConfigManage', '配置类型', 'enumConstByFlagConfigType.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31694c6ac401694c6e75e20002', 'peWebSiteConfigManage', '所属站点', 'peWebSite.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31694c6ac401694c6eb5e20003', 'peWebSiteConfigManage', 'enumConstByFlagConfigType.id', 'enumConstByFlagConfigType.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31694c6ac401694c6f047e0004', 'peWebSiteConfigManage', 'peWebSite.id', 'peWebSite.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31694c6ac401694c7000d80005', 'peWebSiteConfigManage', '是否样例配置(0否，1是)', 'isExample', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '^(0|1)$', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31694c6ac401694c78468e0007', 'peWebSiteConfigManage', '配置', 'config', '', '1', '1', '0', '1', '1', 'textArea', 'yyyy-MM-dd', '0', '10000000', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b01697663044f0000', 'weChatTemplateGroupManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b016976633d1c0001', 'weChatTemplateGroupManage', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b01697663712a0002', 'weChatTemplateGroupManage', '编号', 'code', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b01697663b94f0003', 'weChatTemplateGroupManage', '是否有效', 'enumConstByFlagActive.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b01697664137c0004', 'weChatTemplateGroupManage', '查询sql', 'dataSql', '', '1', '1', '0', '1', '1', 'textArea', 'yyyy-MM-dd', '0', '500', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b016976646e6a0005', 'weChatTemplateGroupManage', 'enumConstByFlagActive.id', 'enumConstByFlagActive.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b01697669813d0008', 'weChatTemplateColumnManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b01697669f74e0009', 'weChatTemplateColumnManage', '所属模板组', 'weChatTemplateMessageGroup.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b0169766a3c16000a', 'weChatTemplateColumnManage', 'weChatTemplateMessageGroup.id', 'weChatTemplateMessageGroup.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b0169766a99a4000b', 'weChatTemplateColumnManage', '字段名', 'label', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b0169766ada8d000c', 'weChatTemplateColumnManage', '字段占位符', 'sign', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b0169766b4bd2000d', 'weChatTemplateColumnManage', '是否动态字段', 'isDynamic', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '^(0|1)$', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b0169766bc0f2000e', 'weChatTemplateColumnManage', '实例文本', 'templateText', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b0169766c126f000f', 'weChatTemplateColumnManage', '是否有效', 'enumConstByFlagActive.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b0169766c6b460010', 'weChatTemplateColumnManage', 'enumConstByFlagActive.id', 'enumConstByFlagActive.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '8', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b0169766fab920012', 'weChatTemplateSiteManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b0169767012d70013', 'weChatTemplateSiteManage', '所属模板组', 'weChatTemplateMessageGroup.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b016976705de40014', 'weChatTemplateSiteManage', 'weChatTemplateMessageGroup.id', 'weChatTemplateMessageGroup.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b01697670a95d0015', 'weChatTemplateSiteManage', '所属站点', 'peWebSite.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b01697670e7500016', 'weChatTemplateSiteManage', 'peWebSite.id', 'peWebSite.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316976612b0169767131fc0017', 'weChatTemplateSiteManage', '微信模板id', 'templateId', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b531ae30000', 'sendMessageGroupManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b5348230001', 'sendMessageGroupManage', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b5372600002', 'sendMessageGroupManage', '编号', 'code', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b53add60003', 'sendMessageGroupManage', '是否有效', 'enumConstByFlagActive.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b53d4510004', 'sendMessageGroupManage', 'enumConstByFlagActive.id', 'enumConstByFlagActive.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b5570470005', 'sendMessageTypeManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b55cb6b0006', 'sendMessageTypeManage', '发送消息组', 'sendMessageGroup.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b55fb510007', 'sendMessageTypeManage', 'sendMessageGroup.id', 'sendMessageGroup.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b5664790008', 'sendMessageTypeManage', '消息类型', 'enumConstByFlagMessageType.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b568ba20009', 'sendMessageTypeManage', 'enumConstByFlagMessageType.id', 'enumConstByFlagMessageType.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b56f5e6000a', 'sendMessageTypeManage', '关联消息编号', 'messageCode', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b580ca7000b', 'sendMessageSiteManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b5aa1e3000c', 'sendMessageSiteManage', '所属发送消息组', 'sendMessageType.sendMessageGroup.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b5af0d8000d', 'sendMessageSiteManage', '所属消息类型', 'sendMessageType.enumConstByFlagMessageType.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b5b228f000e', 'sendMessageSiteManage', 'sendMessageType.sendMessageGroup.id', 'sendMessageType.sendMessageGroup.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b51b301697b5b4fa7000f', 'sendMessageSiteManage', 'sendMessageType.enumConstByFlagMessageType.id', 'sendMessageType.enumConstByFlagMessageType.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b790301697b7b77ff0000', 'sendMessageSiteManage', '所属站点', 'peWebSite.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31697b790301697b7ba29c0001', 'sendMessageSiteManage', 'peWebSite.id', 'peWebSite.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca90169808f987c0000', 'systemCustomConfigManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca90169808fd3eb0001', 'systemCustomConfigManage', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca90169808fff540002', 'systemCustomConfigManage', '编号', 'code', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca901698091208d0003', 'systemCustomConfigManage', '默认值', 'defaultValue', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca901698091c14f0004', 'systemCustomConfigManage', '配置类型(text/select，此配置暂时无用)', 'type', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca90169809236320005', 'systemCustomConfigManage', '所属基础功能', 'peBaseCategory.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca9016980928d560006', 'systemCustomConfigManage', '备注', 'note', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca9016980956af20007', 'systemCustomConfigUrlManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca901698095f3a30008', 'systemCustomConfigUrlManage', 'url', 'url', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca9016980967dbe0009', 'systemCustomConfigManage', 'peBaseCategory.id', 'peBaseCategory.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca9016980979896000a', 'systemSiteCustomConfigManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca901698097d40d000b', 'systemSiteCustomConfigManage', '所属站点', 'peWebSite.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca901698097fbb0000c', 'systemSiteCustomConfigManage', 'peWebSite.id', 'peWebSite.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169808ca9016980986163000d', 'systemSiteCustomConfigManage', '定制值', 'value', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae31698e4ecc01698ebba9780000', 'scheduleJobTriggerManage', '调度时间（只执行一次）', 'triggerTime', '', '1', '1', '0', '1', '1', 'datetime', 'yyyy-MM-dd HH:mm:ss', '1', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169954ce401699553f6af0000', 'emailMessageGroupManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169954ce40169955422a70001', 'emailMessageGroupManage', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169954ce40169955461220002', 'emailMessageGroupManage', '编号', 'code', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169954ce401699554b2e20003', 'emailMessageGroupManage', '查询邮件的sql', 'dataSql', '', '1', '1', '0', '1', '1', 'textArea', 'yyyy-MM-dd', '0', '500', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169954ce401699554fc140004', 'emailMessageGroupManage', '是否有效', 'enumConstByFlagActive.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169954ce40169955539070005', 'emailMessageGroupManage', 'enumConstByFlagActive.id', 'enumConstByFlagActive.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169954ce4016995578d720006', 'emailMessageSiteManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169954ce401699557f8430007', 'emailMessageSiteManage', '模板组', 'emailMessageGroup.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169954ce401699558370d0008', 'emailMessageSiteManage', '所属站点', 'peWebSite.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169954ce4016995588e9b0009', 'emailMessageSiteManage', '模板组编号', 'emailMessageGroup.code', '', '1', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169954ce401699558c6e4000a', 'emailMessageSiteManage', 'emailMessageGroup.id', 'emailMessageGroup.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169954ce401699558f4cb000b', 'emailMessageSiteManage', 'peWebSite.id', 'peWebSite.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998c277016998c3d43d0000', 'smsMessageGroupManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998c277016998c3fdc10001', 'smsMessageGroupManage', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998c277016998c4245d0002', 'smsMessageGroupManage', '编号', 'code', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998c277016998c46c8b0003', 'smsMessageGroupManage', '查询手机号的sql', 'dataSql', '', '1', '1', '0', '1', '1', 'textArea', 'yyyy-MM-dd', '0', '500', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998c277016998c4bfa40004', 'smsMessageGroupManage', '是否有效', 'enumConstByFlagActive.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998c277016998c4eac80005', 'smsMessageGroupManage', 'enumConstByFlagActive.id', 'enumConstByFlagActive.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998c277016998c6401a0006', 'smsMessageSiteManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998c277016998c67b7d0007', 'smsMessageSiteManage', '所属站点', 'peWebSite.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998c277016998c6a2b20008', 'smsMessageSiteManage', 'peWebSite.id', 'peWebSite.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998c277016998c6ec7b0009', 'smsMessageSiteManage', '关联模板', 'smsMessageGroup.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998c277016998c73b16000a', 'smsMessageSiteManage', 'smsMessageGroup.id', 'smsMessageGroup.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998c277016998c79171000b', 'smsMessageSiteManage', '关联模板编号', 'smsMessageGroup.code', '', '1', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998e6bc01699a6a5a050003', 'emailMessageGroupManage', '过滤sql', 'filterSql', '', '1', '1', '0', '1', '1', 'textArea', 'yyyy-MM-dd', '0', '500', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998e6bc01699a6aac3e0004', 'smsMessageGroupManage', '过滤sql', 'filterSql', '', '1', '1', '0', '1', '1', 'textArea', 'yyyy-MM-dd', '0', '500', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998e6bc01699a6b15050005', 'stationMessageGroupManage', '过滤sql', 'filterSql', '', '1', '1', '0', '1', '1', 'textArea', 'yyyy-MM-dd', '0', '500', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316998e6bc01699a6b6fbf0006', 'weChatTemplateGroupManage', '过滤sql', 'filterSql', '', '1', '1', '0', '1', '1', 'textArea', 'yyyy-MM-dd', '0', '500', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169b42e240169b42f70810000', 'scheduleJobManage', '是否是分布式单例', 'enumConstByFlagIsSingleton.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '9', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae3169b42e240169b42fa2110001', 'scheduleJobManage', 'enumConstByFlagIsSingleton.id', 'enumConstByFlagIsSingleton.id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '10', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a05eb5c016a060102780000', 'saSiteManage', 'ssoAppId', 'ssoAppId', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '9', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a05eb5c016a06014cd40001', 'saSiteManage', 'ssoAppSecret', 'ssoAppSecret', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '10', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a05eb5c016a060193f50002', 'saSiteManage', 'ssoBasePath', 'ssoBasePath', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '11', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0a7d6d016a0a85327f0000', 'unitTypeManage', 'id', 'id', 'id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0a7d6d016a0a8587000001', 'unitTypeManage', '名称', 'name', 'name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b0fff016a0b12783c0001', 'schoolZoneManage', 'id', 'id', 'id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b0fff016a0b12ccdc0002', 'schoolZoneManage', '名称', 'name', 'name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b2cc5250000', 'trainingAreaManage', 'id', 'id', 'id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b2cfd740001', 'trainingAreaManage', '名称', 'name', 'name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b35153f0003', 'trainingTypeManage', 'id', 'id', 'id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b354e910004', 'trainingTypeManage', '名称', 'name', 'name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b3f70bb0005', 'trainingTargetManage', 'id', 'id', 'id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b3fa1230006', 'trainingTargetManage', '名称', 'name', 'name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b45bb740009', 'courseTypeManage', 'id', 'id', 'id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b45e7c9000a', 'courseTypeManage', '名称', 'name', 'name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b48e39f000c', 'suitableTargetManage', 'id', 'id', 'id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b49119b000d', 'suitableTargetManage', '名称', 'name', 'name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b4be5d2000f', 'positionalTitleManage', 'id', 'id', 'id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b4c13360010', 'positionalTitleManage', '名称', 'name', 'name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b4ebfe50012', 'educationalBackgroundManage', 'id', 'id', 'id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b2a2a016a0b4ef21e0013', 'educationalBackgroundManage', '名称', 'name', 'name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b55f9016a0b739d83001d', 'unitManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b55f9016a0b73cb25001e', 'unitManage', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b55f9016a0b741c6a001f', 'unitManage', '单位类型', 'enumConstByFlagUnitType.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b55f9016a0b7482e30020', 'unitManage', '负责人姓名', 'headName', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b55f9016a0b74d5b60021', 'unitManage', '负责人手机', 'headMobile', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '11', '', '^1[0-9]{10}$', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b55f9016a0b752bb30022', 'unitManage', '负责人办公电话', 'headTelephone', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '^[0-9|-]+$', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b55f9016a0b7563c00023', 'unitManage', '负责人邮箱', 'headEmail', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '^[0-9|a-z|A-Z]+@[0-9|a-z|A-Z]+\\.[0-9|a-z|A-Z]+$', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0b55f9016a0b75af800024', 'unitManage', '备注', 'note', '', '1', '1', '1', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be112d40004', 'siteManagerManage', 'cardTypeId', 'cardTypeId', 'enumConstByFlagCardType.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '8', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be17a4c0005', 'siteManagerManage', '证件号码', 'cardNo', 'cardNo', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '20', '', '^[0-9]+$', null, '', '8', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be209dc0006', 'siteManagerManage', '性别', 'combobox_enumConstByFlagGender.gender', 'enumConstByFlagGender.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '9', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be283900007', 'siteManagerManage', 'genderId', 'genderId', 'enumConstByFlagGender.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '10', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be2d6310008', 'siteManagerManage', '出生日期', 'birthday', 'birthday', '1', '1', '0', '1', '1', 'date', 'yyyy-MM-dd', '1', '50', '', '', null, '', '11', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be31f6c0009', 'siteManagerManage', '工作单位', 'workUnit', 'workUnit', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '12', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be35480000a', 'siteManagerManage', '手机', 'mobile', 'mobile', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '11', '', '^1[0-9]{10}$', null, '', '13', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be39210000b', 'siteManagerManage', '工作电话', 'telephone', 'telephone', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '^[0-9|-]+$', null, '', '14', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be3d935000c', 'siteManagerManage', '家庭电话', 'homePhone', 'homePhone', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '^[0-9|-]+$', null, '', '15', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be41f2c000d', 'siteManagerManage', '电子邮箱', 'email', 'email', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '^[0-9|a-z|A-Z]+@[0-9|a-z|A-Z]+\\.[0-9|a-z|A-Z]+$', null, '', '16', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be454ff000e', 'siteManagerManage', '联系地址', 'address', 'address', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '17', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be49f2b000f', 'siteManagerManage', '邮政编号', 'zipCode', 'zipCode', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '10', '', '^[0-9]+$', null, '', '18', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be4d8bc0010', 'siteManagerManage', 'QQ号', 'qq', 'qq', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '20', '', '^[0-9]+$', null, '', '19', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0bceb0016a0be5273d0011', 'siteManagerManage', '是否绑定微信', 'hasWeChat', '', '1', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '20', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0be7c6016a0beaef840000', 'siteManagerManage', 'activeId', 'activeId', 'enumConstByFlagActive.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '21', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0be7c6016a0beb41b20001', 'siteManagerManage', '所属单位', 'combobox_peUnit.unitName', 'peUnit.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a0be7c6016a0beb7d520002', 'siteManagerManage', 'unitId', 'unitId', 'peUnit.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '22', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103999130000', 'teacherResourceManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a1039c9f80001', 'teacherResourceManage', '用户名', 'loginId', 'loginId', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103a03ab0002', 'teacherResourceManage', '姓名', 'trueName', 'trueName', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103a54050003', 'teacherResourceManage', '讲师职称', 'combobox_enumConstByFlagPositionalTitle.positionalTitle', 'enumConstByFlagPositionalTitle.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103aa1fe0004', 'teacherResourceManage', '最高学历', 'combobox_enumConstByFlagEducationalBackground.eduBackground', 'enumConstByFlagEducationalBackground.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103af5b80005', 'teacherResourceManage', '是否授课教师', 'combobox_enumConstByFlagCourseTeacher.courseTeacher', 'enumConstByFlagCourseTeacher.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103b52dc0006', 'teacherResourceManage', '是否资源教师', 'combobox_enumConstByFlagTutorTeacher.tutorTeacher', 'enumConstByFlagTutorTeacher.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103baf1f0007', 'teacherResourceManage', '是否班主任', 'combobox_enumConstByFlagIsclassmaster.classMaster', 'enumConstByFlagIsclassmaster.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103bebc90008', 'teacherResourceManage', '所属单位', 'combobox_peUnit.unitName', 'peUnit.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '8', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103c3de80009', 'teacherResourceManage', '是否有效', 'combobox_enumConstByFlagActive.activeName', 'enumConstByFlagActive.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '9', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103c7d02000a', 'teacherResourceManage', '证件类型', 'combobox_enumConstByFlagCardType.cardType', 'enumConstByFlagCardType.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '10', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103cb87f000b', 'teacherResourceManage', '证件号码', 'cardNo', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '20', '', '^[0-9a-zA-Z]+$', null, '', '11', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103d13e5000c', 'teacherResourceManage', '性别', 'combobox_enumConstByFlagGender.gender', 'enumConstByFlagGender.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '12', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103d4841000d', 'teacherResourceManage', '出生日期', 'birthday', 'birthday', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '13', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103d7b20000e', 'teacherResourceManage', '工作单位', 'workUnit', 'workUnit', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '14', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103dc445000f', 'teacherResourceManage', '手机', 'mobile', 'mobile', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '11', '', '^1[0-9]{10}$', null, '', '15', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103e05360010', 'teacherResourceManage', '工作电话', 'workPhone', 'workPhone', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '20', '', '^[0-9|-]+$', null, '', '16', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103e44910011', 'teacherResourceManage', '家庭电话', 'homePhone', 'homePhone', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '20', '', '^[0-9|-]+$', null, '', '17', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103e7d510012', 'teacherResourceManage', '电子邮箱', 'email', 'email', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '^[0-9|a-z|A-Z]+@[0-9|a-z|A-Z]+\\.[0-9|a-z|A-Z]+$', null, '', '18', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a103eb3300013', 'teacherResourceManage', '联系地址', 'address', 'address', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '19', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a104073b90014', 'teacherResourceManage', '邮政编码', 'zipCode', 'zipCode', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '20', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a1040aa3c0015', 'teacherResourceManage', 'QQ号码', 'qq', 'qq', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '21', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a104106e50016', 'teacherResourceManage', '是否绑定微信', 'hasWeChat', '', '1', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '22', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a1035ae016a1041924e0017', 'teacherResourceManage', '教师简介', 'introduction', 'introduction', '0', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '23', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a10bdef016a10c101540000', 'teacherResourceManage', 'positionalTitleId', 'positionalTitleId', 'enumConstByFlagPositionalTitle.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '24', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a10bdef016a10c1756d0001', 'teacherResourceManage', 'eduBackgroundId', 'eduBackgroundId', 'enumConstByFlagEducationalBackground.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '25', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a10bdef016a10c1cdac0002', 'teacherResourceManage', 'tutorTeacherId', 'tutorTeacherId', 'enumConstByFlagTutorTeacher.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '26', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a10bdef016a10c222890003', 'teacherResourceManage', 'courseTeacherId', 'courseTeacherId', 'enumConstByFlagCourseTeacher.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '27', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a10bdef016a10c281c00004', 'teacherResourceManage', 'classMasterId', 'classMasterId', 'enumConstByFlagIsclassmaster.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '28', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a10bdef016a10c2c2140005', 'teacherResourceManage', 'unitId', 'unitId', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '29', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', 'peUnit.id', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a10bdef016a10c3132d0006', 'teacherResourceManage', 'activeId', 'activeId', 'enumConstByFlagActive.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '30', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a10bdef016a10c355cd0007', 'teacherResourceManage', 'cardTypeId', 'cardTypeId', 'enumConstByFlagCardType.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '31', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028ae316a10bdef016a10c395fb0008', 'teacherResourceManage', 'genderId', 'genderId', 'enumConstByFlagGender.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '32', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aec76347dea9016347edf00d0000', 'siteManagerManage', '姓名', 'trueName', 'trueName', '1', '1', '1', '1', '0', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec76347dea9016347f0406e0001', 'siteManagerManage', '用户角色', 'combobox_pePriRole.roleName', 'ssoUser.pePriRole.name', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, 'SELECT\n	ro.id,\n	ro.name\nFROM\n	pe_pri_role ro\nINNER JOIN enum_const ty ON ty.id = ro.FLAG_ROLE_TYPE\nLEFT JOIN enum_const su ON su.id = ro.flag_site_super_admin\nWHERE\n	ty.code = \'3\'\nAND su.code <> \'1\'', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec76347dea9016347f32dc30002', 'siteManagerManage', '用户名', 'loginId', 'loginId', '1', '1', '1', '1', '0', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec76348296d0163482e2bd70000', 'siteManagerManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec76362b85a016362e419030001', 'siteRoleManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec76362b85a016362e48ef50002', 'siteRoleManage', '角色名称', 'name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7636318210163633595b90000', 'siteManagerManage', '是否有效', 'combobox_enumConstByFlagActive.active', 'enumConstByFlagActive.name', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7636c1cd601636c37dd830001', 'siteSuperMangerManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7636c1cd601636c38c6ff0002', 'siteSuperMangerManage', '用户名', 'loginId', 'loginId', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '只允许为4位以上20位以下字母或数字！', '^[A-Za-z0-9]{4,21}$', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7636c1cd601636c42533f0006', 'siteSuperMangerManage', '是否有效', 'enumConstByFlagIsvalid.name', 'enumConstByFlagIsvalid.name', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, 'select id,name from enum_const where namespace=\'FlagIsvalid\'', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7638b143401638c09d6ca0000', 'allSiteRoleManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7638b143401638c0ac3430001', 'allSiteRoleManage', '角色名称', 'name', 'name', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7638b143401638c0b71860002', 'allSiteRoleManage', '编码', 'code', 'code', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7638b143401638c0c1cc20003', 'allSiteRoleManage', '站点', 'combobox_PeWebSite.siteName', 'peWebSite.name', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec76394d607016394e3be7e0001', 'allSiteRoleManage', '角色类型', 'combobox_EnumConst.type', 'enumConstByFlagRoleType.name', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, 'select id ,name from enum_const where namespace=\'FlagRoleType\' and code in (\'3\',\'9998\',\'9000\')', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec76394d607016394e41f760002', 'allSiteRoleManage', 'enumConstByFlagRoleType.id', 'enumConstByFlagRoleType.id', '', '0', '0', '0', '0', '1', 'TextField', null, '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fdb39f0007', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec763a9caa30163a9e7ea870001', 'allSiteRoleManage', '是否站点超管', 'isAdmin', '', '1', '0', '0', '1', '0', 'TextField', null, '1', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec763ca112b0163ca1a43c20000', 'siteManagerManage', 'roleId', 'roleId', 'ssoUser.pePriRole.id', '0', '0', '0', '0', '0', 'TextField', null, '1', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b563cb0164b5aa8d550006', 'enumConstManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b563cb0164b5ab19000007', 'enumConstManage', '名称', 'name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b563cb0164b5ab751e0008', 'enumConstManage', 'code', 'code', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b563cb0164b5abeb7b0009', 'enumConstManage', '命名空间', 'namespace', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b563cb0164b5accedd000a', 'enumConstManage', '是否默认值', 'isDefault', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b563cb0164b5adae7b000b', 'enumConstManage', '备注', 'note', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b563cb0164b5ae30c0000c', 'enumConstManage', '创建时间', 'createDate', '', '1', '0', '1', '1', '1', 'Date', null, '0', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b563cb0164b5aea564000d', 'enumConstManage', 'team', 'team', '', '1', '1', '1', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b6eb680164b70633320000', 'systemVariablesManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b6eb680164b706ac5b0001', 'systemVariablesManage', '名称', 'name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b6eb680164b70714850002', 'systemVariablesManage', '内容', 'value', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '1500', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b6eb680164b707f85d0003', 'systemVariablesManage', 'pattern', 'pattern', '', '1', '1', '1', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b6eb680164b7086a160004', 'systemVariablesManage', '备注', 'note', '', '1', '1', '1', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764b6eb680164b70d1d940005', 'systemVariablesManage', '是否强制更新', 'enumConstByFlagBak.name', '', '1', '1', '1', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fdb39f0007', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf93abbf0000', 'stuEnrollInfoManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf9443430001', 'stuEnrollInfoManage', '名称', 'name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf94a2e80002', 'stuEnrollInfoManage', '属性名', 'columnName', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf952ac00003', 'stuEnrollInfoManage', '是否显示', 'display', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf9584a40004', 'stuEnrollInfoManage', '显示顺序', 'myOrder', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf9642f00005', 'stuEnrollInfoManage', '是否必填', 'notNull', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf96d9e90006', 'stuEnrollInfoManage', '正则', 'reg', '', '1', '1', '1', '1', '1', 'TextField', null, '1', '100', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf975c910007', 'stuEnrollInfoManage', '错误提示', 'error', '', '1', '1', '1', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf97dd150008', 'stuEnrollInfoManage', '下拉选项', 'options', '', '1', '1', '1', '1', '1', 'TextArea', null, '1', '50', '', '', null, '', '8', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf986ccc0009', 'stuEnrollInfoManage', '帮助信息', 'help', '', '1', '1', '1', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '9', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf9906b8000a', 'stuEnrollInfoManage', '下拉项sql', 'listSql', '', '1', '1', '1', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '10', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf9a06dd000b', 'stuEnrollInfoManage', '学生信息类别', 'enumConstByFlagInfoType.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec764cf62c30164cf9a66bd000c', 'stuEnrollInfoManage', 'enumConstByFlagInfoType.id', 'enumConstByFlagInfoType.code', '', '0', '0', '0', '0', '0', 'TextField', null, '1', '50', '', '', null, '', '11', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7658568760165856ca8fa0000', 'workSpaceConfigManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7658568760165856d5c140001', 'workSpaceConfigManage', '配置项编码', 'code', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7658568760165856dbed80002', 'workSpaceConfigManage', '配置项名称', 'name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7658568760165856e209b0003', 'workSpaceConfigManage', '配置项备注', 'note', '', '1', '1', '1', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7658568760165856e85c70004', 'workSpaceConfigManage', 'json配置', 'jsonConfig', '', '1', '1', '1', '1', '1', 'TextArea', null, '1', '9999', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7658568760165856ef58f0005', 'workSpaceConfigManage', '站点编号', 'siteCode', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec7658568760165856f4a4a0006', 'workSpaceConfigManage', '客服js链接', 'serviceScriptUrl', '', '1', '1', '1', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aec765fa1a610165fb8b3b290000', 'stuEnrollInfoManage', '内容类型', 'htmlType', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aed0635dba0501635dbb24150000', 'operateGuideManage', '描述', 'description', '', '1', '1', '1', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aed0635dba0501635dbb95df0001', 'operateGuideManage', '关联操作流程图', 'operateGuide.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aed0635dba0501635dbbfb2d0002', 'operateGuideManage', '是否有效', 'enumConstByFlagActive.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aed0635dba0501635dbcb7690003', 'operateGuideManage', '站点', 'peWebSite.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aed0635dba0501635dbe443e0004', 'operateGuideManage', '图标', 'icon', '', '1', '0', '1', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a20bbad240001', 'courseResourceManage', '名称', 'name', 'name', '1', '1', '1', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a20bc1bd50002', 'courseResourceManage', '适合对象', 'combobox_enumConstByFlagTrainingTarget.trainingTargetName', 'enumConstByFlagTrainingTarget.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a20be7fef0003', 'courseResourceManage', '授课方式', 'combobox_enumConstByFlagTeachType.teachTypeName', 'enumConstByFlagTeachType.name', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fdb39f0007', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a20bede6b0004', 'courseResourceManage', '课程图片', 'courseUrl', 'courseUrl', '0', '1', '1', '0', '0', 'file', 'yyyy-MM-dd', '1', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a20c149040006', 'courseResourceManage', '课程简介', 'courseIntro', 'courseIntro', '1', '1', '1', '1', '1', 'textArea', 'yyyy-MM-dd', '1', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a23c1299d0008', 'hotelResourceManage', 'id', 'id', 'id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a23c1838c0009', 'hotelResourceManage', '名称', 'name', 'name', '1', '1', '1', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a23c1d301000a', 'hotelResourceManage', '地址', 'address', 'address', '1', '1', '1', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a23c22747000b', 'hotelResourceManage', '联系人', 'contact', 'contact', '1', '1', '1', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a23c2a181000c', 'hotelResourceManage', '协议价格', 'agreePrice', 'agreePrice', '1', '1', '1', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a23da060d000d', 'hotelResourceManage', '合作协议', 'agreement', 'agreement', '1', '1', '1', '1', '0', 'file', 'yyyy-MM-dd', '1', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a23dabba2000e', 'hotelResourceManage', '备注', 'remark', 'remark', '1', '1', '1', '1', '1', 'textArea', 'yyyy-MM-dd', '0', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a23eb5ac1000f', 'placeResourceManage', 'id', 'id', 'id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a23f790890010', 'placeResourceManage', '场地名称', 'name', 'name', '1', '1', '1', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a23f961220011', 'placeResourceManage', '所属单位', 'combobox_peUnit.unitName', 'peUnit.name', '1', '1', '1', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a23fa4f880013', 'placeResourceManage', '容量', 'capacity', 'capacity', '1', '1', '1', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a23fb06530014', 'placeResourceManage', '收费标准', 'charges', 'charges', '1', '1', '1', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a1fe358016a2470ec190016', 'courseResourceManage', 'id', 'id', 'id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a24f81a016a2513f8540000', 'placeResourceManage', '校区', 'combobox_enumConstByFlagSchoolZone.schoolZoneName', 'enumConstByFlagSchoolZone.name', '1', '1', '1', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a290d9e016a295fa5900002', 'courseResourceManage', '课程代码', 'code', 'code', '1', '1', '1', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a2a4864016a2a563c850003', 'courseResourceManage', 'enumIfd', 'combobox_enumConstByFlagTrainingTarget.trainingTargetId', 'enumConstByFlagTrainingTarget.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('4028aefc6a2a4864016a2a599d030005', 'placeResourceManage', 'combobox_enumConstByFlagSchoolZone.schoolZoneName', 'combobox_enumConstByFlagSchoolZone.schoolZoneId', 'enumConstByFlagSchoolZone.id', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('49d4fe4721f411e8ab1d001e671d0be8', 'saUserManage', '用户名', 'loginId', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4c7d4c7021f411e8ab1d001e671d0be8', 'saUserManage', '姓名', 'trueName', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('4e70757d21f411e8ab1d001e671d0be8', 'saUserManage', '密码', 'password', '', '1', '0', '0', '0', '0', 'TextField', null, '0', '50', null, null, '', '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('5079914921f411e8ab1d001e671d0be8', 'saUserManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', null, null, '', '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('52ba1b3021f411e8ab1d001e671d0be8', 'saUserManage', '角色', 'corePePriRole.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('54d9e73121f411e8ab1d001e671d0be8', 'saUserManage', '是否有效', 'enumConstByFlagIsvalid.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('56b3f19f21f411e8ab1d001e671d0be8', 'saUserManage', '最后登录时间', 'lastLoginDate', '', '1', '0', '0', '1', '1', 'TextField', null, '0', '50', null, null, '', '', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('5980336c21f411e8ab1d001e671d0be8', 'saUserManage', '角色id', 'corePePriRole.id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', null, null, '', '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('5b5a007c21f411e8ab1d001e671d0be8', 'saUserManage', '是否有效id', 'enumConstByFlagIsvalid.id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '50', null, null, '', '', '8', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('b2c0e498785611e89e1dfcaa140ebf84', 'baseCategoryManage', '路由name', 'routerName', '', '1', '1', '0', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('d05cd9d58e1a11e89e1dfcaa140ebf84', 'scoreComposeItemManage', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', null, '0', '25', null, null, null, null, '1', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', null, null, null, null, null);
INSERT INTO `grid_column_config` VALUES ('d0624fa48e1a11e89e1dfcaa140ebf84', 'scoreComposeItemManage', '名称', 'name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '25', null, null, null, null, '2', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', null, null, null, null, null);
INSERT INTO `grid_column_config` VALUES ('d07135cf8e1a11e89e1dfcaa140ebf84', 'scoreComposeItemManage', '导出打印顺序', 'orderNum', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '25', '打印顺序只能是1-10的整数', '^([1-9]|(10))$', 'regex:new RegExp(/^([1-9]|(10))$/),regexText:\'打印顺序只能是1-10的整数\',', null, '3', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', null, null, null, null, null);
INSERT INTO `grid_column_config` VALUES ('d078a2918e1a11e89e1dfcaa140ebf84', 'scoreComposeItemManage', '是否参与合成', 'enumConstByFlagYesNo.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '25', null, null, null, null, '4', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', null, null, null, null, null);
INSERT INTO `grid_column_config` VALUES ('d09aad8a8e1a11e89e1dfcaa140ebf84', 'scoreComposeItemManage', '是否可以导出', 'enumConstByFlagIsExport.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '25', null, null, null, null, '5', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', null, null, null, null, null);
INSERT INTO `grid_column_config` VALUES ('d0a1b1798e1a11e89e1dfcaa140ebf84', 'scoreComposeItemManage', '序号', 'serial', null, '1', '1', '0', '1', '1', 'TextField', null, '0', '25', null, null, null, null, '7', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', null, null, null, null, null);
INSERT INTO `grid_column_config` VALUES ('d0a735048e1a11e89e1dfcaa140ebf84', 'scoreComposeItemManage', '教师是否可录入', 'enumConstByFlagTeacherCanEnter.name', '', '1', '1', '1', '1', '1', 'TextField', null, '0', '25', null, null, null, null, '6', '4028809c1d625bcf011d66fd0dda0006', null, null, 'adult', null, '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('ff80808163cf81160163cf97e5650004', 'siteSuperMangerManage', '姓名', 'trueName', 'trueName', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('ff80808165238c63016530d356ee0000', 'siteManagerManage', 'unitId', 'unitId', 'peUnit.id', '0', '0', '0', '0', '0', 'TextField', null, '1', '50', '', '', null, 'select id,name from enum_const where namespace = \'FlagWechatCheck\'', '6', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('ff80808165238c63016530d3da7a0001', 'siteManagerManage', '证件类型', 'combobox_enumConstByFlagCardType.cardType', 'enumConstByFlagCardType.name', '1', '1', '0', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('ff80808165238c630165310189800002', 'siteSuperMangerManage', '扫码验证', 'enumConstFlagWechatCheck.name', '', '1', '1', '0', '1', '1', 'TextField', null, '1', '50', '', '', null, '', '6', '4028809c1d625bcf011d66fdb39f0007', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('ff80808165d357140165d6c8fe880009', 'siteSuperMangerManage', '角色', 'pePriRole.name', '', '1', '0', '0', '1', '0', 'TextField', null, '1', '50', '', '', null, '', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('ff808081670cface01670feede030000', 'printTemplateSign', '模板组', 'pePrintTemplateGroup.id', '', '1', '1', '0', '1', '1', 'TextField', null, '0', '50', '', '', null, 'SELECT\n	ptg.id as id,\n	CONCAT(pt.title,\'-\',ptg.name) as name\nFROM\n	pe_print_template_group ptg \ninner join pe_print_template pt on pt.id = ptg.fk_print_template_id', '7', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, null, null);
INSERT INTO `grid_column_config` VALUES ('ff8080816832aa5501683656107a0000', 'apiSiteConfig', 'id', 'id', '', '0', '0', '0', '0', '0', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '0', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('ff8080816832aa55016836565bc00001', 'apiSiteConfig', '名称', 'name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '1', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('ff8080816832aa5501683656913b0002', 'apiSiteConfig', '编号', 'code', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '2', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('ff8080816832aa5501683656ebca0003', 'apiSiteConfig', '密钥', 'secretKey', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '3', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('ff8080816832aa5501683657a5260004', 'apiSiteConfig', '域名', 'domain', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', null, '', '4', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);
INSERT INTO `grid_column_config` VALUES ('ff8080816832aa55016836580ed90005', 'apiSiteConfig', '是否有效', 'enumConstByFlagActive.name', '', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', null, '', '5', '4028809c1d625bcf011d66fd0dda0006', null, null, '', '', '', null, '', null);

-- ----------------------------
-- Table structure for grid_menu_config
-- ----------------------------
DROP TABLE IF EXISTS `grid_menu_config`;
CREATE TABLE `grid_menu_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fk_grid_id` varchar(50) NOT NULL,
  `flag_menu_type` varchar(50) DEFAULT NULL COMMENT '菜单类型',
  `flag_integrated_menu_type` varchar(50) DEFAULT NULL COMMENT '系统菜单类型',
  `text` varchar(20) NOT NULL COMMENT '菜单显示的文字',
  `show_type` varchar(10) NOT NULL COMMENT '菜单显示位置',
  `column_type` varchar(10) NOT NULL COMMENT '行内菜单位置',
  `must_select_row` tinyint(1) NOT NULL COMMENT '是否必须选择记录',
  `select_limit` int(11) NOT NULL DEFAULT '0' COMMENT '单次允许操作的最大记录数',
  `data_index` varchar(50) NOT NULL COMMENT 'value获取的数据列索引,默认为"id"',
  `null_text` varchar(50) DEFAULT NULL COMMENT 'showType为column时, column值为空时显示的信息',
  `show_confirm` tinyint(1) DEFAULT NULL COMMENT '是否显示确认框',
  `confirm_text` varchar(100) DEFAULT NULL COMMENT '确认框提示信息',
  `url` varchar(300) DEFAULT NULL,
  `extra_parameters` varchar(200) DEFAULT NULL,
  `waiting_msg` varchar(100) DEFAULT NULL,
  `success_msg` varchar(100) DEFAULT NULL,
  `error_msg` varchar(100) DEFAULT NULL,
  `open_mode` varchar(10) DEFAULT NULL,
  `form_config` varchar(2000) DEFAULT NULL,
  `user_type` varchar(100) DEFAULT NULL COMMENT '拥有此菜单的用户类型',
  `flag_is_valid` varchar(50) NOT NULL,
  `serial_number` int(11) NOT NULL COMMENT '序号',
  `script` varchar(2000) DEFAULT NULL COMMENT 'javascript模板',
  `column_data_index` varchar(50) DEFAULT NULL,
  `value` varchar(50) DEFAULT NULL,
  `tips` varchar(500) DEFAULT NULL COMMENT '提示信息',
  `extend_menu_config` varchar(2000) DEFAULT NULL COMMENT '菜单配置,菜单的额外配置,不同类型菜单的格式可能不一致',
  PRIMARY KEY (`id`),
  KEY `grid_menu_config_fk_1` (`fk_grid_id`) USING BTREE,
  KEY `grid_menu_config_fk_2` (`flag_menu_type`) USING BTREE,
  KEY `grid_menu_config_fk_3` (`flag_is_valid`) USING BTREE,
  KEY `grid_menu_config_ibfk_4` (`flag_integrated_menu_type`) USING BTREE,
  CONSTRAINT `grid_menu_config_ibfk_1` FOREIGN KEY (`fk_grid_id`) REFERENCES `grid_basic_config` (`id`),
  CONSTRAINT `grid_menu_config_ibfk_2` FOREIGN KEY (`flag_menu_type`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `grid_menu_config_ibfk_3` FOREIGN KEY (`flag_is_valid`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `grid_menu_config_ibfk_4` FOREIGN KEY (`flag_integrated_menu_type`) REFERENCES `enum_const` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29963 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of grid_menu_config
-- ----------------------------
INSERT INTO `grid_menu_config` VALUES ('1056', 'printTemplate', 'dc8475e8ff1a11e7ab1d001e671d0be8', null, '上传默认模板', 'top', 'left', '1', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', '{     formTitle: \'上传默认模板\',     requestConfig: {         url: \'/print/printTemplate/uploadDefaultTemplate\',         waitingMsg: \'处理中，请稍候...\'     },     formFieldList: [{         name: \'namespace\',         type: \'text\',         labelText: \'模板命名空间\', 		checkRegular: \'^([a-z])([a-z|A-Z]+)$\', 		checkMessage: \'输入格式：驼峰词\'     },{         name: \'upload\',         type: \'file\',         labelText: \'默认模板\'     }] }', null, '2', '1', null, null, null, '1、模板文件格式根据模板类型上传，命名空间为一个驼峰词 <br/>2、上传模板默认在/templatefile/命名空间/模板类型/模板id.模板文件格式', '');
INSERT INTO `grid_menu_config` VALUES ('1059', 'printTemplateGroup', 'dc8475e8ff1a11e7ab1d001e671d0be8', null, '复制组到指定模板', 'top', 'left', '1', '0', 'id', null, '1', null, '', null, null, null, null, '_blank', '{ 	formTitle:\'复制分组到指定的模板\', 	requestConfig:{ 		url:\'/print/printTemplateSign/copyGroupToTemplate\', 		waitingMsg:\'处理中，请稍候...\' 	}, 	formFieldList:[ 		{ 			name:\'printId\', 			type:\'select\', 			labelText:\'模板\', 			selectDataMode:\'local\', 			selectDataUrl:\'/print/printTemplateSign/listTemplate\' 		} 	] }', null, '2', '1', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('1075', 'baseCategoryManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1076', 'baseCategoryManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1077', 'baseCategoryManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1078', 'baseCategoryManage', 'a92817f1361611e8a37efcaa140ebf84', null, '接口管理', 'column', 'left', '0', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', null, null, '2', '1', null, null, null, '', '{\"routerName\":\"baseCategoryInterfaceManage\", \"fieldName\": \"bcId\"}');
INSERT INTO `grid_menu_config` VALUES ('1079', 'baseCategoryInterfaceManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1080', 'baseCategoryInterfaceManage', 'dc8475e8ff1a11e7ab1d001e671d0be8', null, '添加接口', 'top', 'left', '0', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', '{   \"formTitle\": \"添加接口\",   \"requestConfig\": {     \"url\": \"/superAdmin/baseCategoryInterfaceManage/addInterfaceForCategory\",     \"waitingMsg\": \"处理中，请稍候...\"   },   \"formFieldList\": [     {       \"name\": \"interfaceId\",       \"type\": \"select\",       \"labelText\": \"接口\",       \"selectDataMode\": \"local\",       \"selectDataUrl\": \"/superAdmin/interfaceManage/getIdNameList\"     }   ] }', null, '2', '1', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('1081', 'interfaceManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1082', 'interfaceManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1083', 'interfaceManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1123', 'printTemplate', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1124', 'printTemplateGroup', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1125', 'printTemplateSign', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1126', 'saRoleManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1127', 'saSiteManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '3', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1128', 'saUserManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1129', 'saVueRouterManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1190', 'printTemplate', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1191', 'printTemplateGroup', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1192', 'printTemplateSign', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1194', 'saRoleManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1195', 'saSiteManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '3', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1196', 'saUserManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1197', 'saVueRouterManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1258', 'printTemplate', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1259', 'printTemplateGroup', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1260', 'printTemplateSign', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1265', 'saRoleManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1266', 'saSiteManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1267', 'saUserManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1268', 'saVueRouterManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1290', 'saRoleManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1291', 'saUserManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1308', 'baseCategoryInterfaceManage', 'd6228e78ff1a11e7ab1d001e671d0be8', null, '返回', 'top', 'left', '0', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', null, null, '2', '1', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('1309', 'siteManagerManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1310', 'siteManagerManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1311', 'siteManagerManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1312', 'siteRoleManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1313', 'siteRoleManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1314', 'siteRoleManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1315', 'siteSuperMangerManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1316', 'siteSuperMangerManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1317', 'siteSuperMangerManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1319', 'operateGuideManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1320', 'operateGuideManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1321', 'operateGuideManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1322', 'operateGuideManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1323', 'saSiteManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', 'left', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '3', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1331', 'allSiteRoleManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1332', 'allSiteRoleManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('1333', 'allSiteRoleManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9232', 'allSiteRoleManage', 'dc8475e8ff1a11e7ab1d001e671d0be8', null, '修改角色类型', 'top', 'left', '1', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', '{	formTitle:\'修改角色属性\',	requestConfig:{		url:\'/superAdmin/siteSuperManager/superAdminSiteRoleManage/updateRoleProperty\',		waitingMsg:\'处理中，请稍候...\'	},	formFieldList:[		{			name:\'flagRoleType\',			type:\'select\',			labelText:\'角色类型\',			selectDataMode:\'local\',			selectDataUrl:\'/superAdmin/siteSuperManager/superAdminSiteRoleManage/getManagerRoleType\'		},,{\r\n        name: \'code\',\r\n        type: \'text\',\r\n        labelText: \'编码\'\r\n    }	]}', null, '3', '1', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('9233', 'allSiteRoleManage', 'cb245573ff1a11e7ab1d001e671d0be8', null, '设为站点管理员', 'top', 'left', '1', '1', 'id', null, '1', null, '/superAdmin/siteSuperManager/superAdminSiteRoleManage/setSiteSuperAdmin', null, null, null, null, '_blank', null, null, '2', '1', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('9234', 'siteSuperMangerManage', 'cb245573ff1a11e7ab1d001e671d0be8', null, '重置密码', 'top', 'left', '1', '0', 'id', null, '1', null, '/superAdmin/siteSuperManager/siteSuperManagerManage/resetUserPwd', null, null, null, null, '_blank', null, null, '2', '1', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('9235', 'allSiteRoleManage', 'cb245573ff1a11e7ab1d001e671d0be8', null, '设为普通管理员', 'top', 'left', '1', '1', 'id', null, '1', null, '/superAdmin/siteSuperManager/superAdminSiteRoleManage/setOrdinaryAdmin', null, null, null, null, '_blank', null, null, '2', '1', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('9249', 'saSiteManage', 'a92817f1361611e8a37efcaa140ebf84', null, '添加', 'top', 'left', '0', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', null, null, '2', '1', null, null, null, '', '{\"routerName\": \"addSiteConfig\"}');
INSERT INTO `grid_menu_config` VALUES ('9250', 'saSiteManage', 'a92817f1361611e8a37efcaa140ebf84', null, '修改', 'column', 'left', '0', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', null, null, '2', '2', null, null, null, '', '{\"routerName\":\"editSiteConfig\",\"fieldName\":\"id\"}');
INSERT INTO `grid_menu_config` VALUES ('9251', 'saSiteManage', 'a92817f1361611e8a37efcaa140ebf84', null, '站点数据初始化', 'column', 'left', '1', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', null, null, '2', '3', null, null, null, '', '{\"routerName\":\"siteInit\",\"fieldName\":\"id\"}');
INSERT INTO `grid_menu_config` VALUES ('9252', 'saSiteManage', 'a92817f1361611e8a37efcaa140ebf84', null, '站点超管管理', 'column', 'left', '0', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', null, null, '2', '4', null, null, null, '', '{\"routerName\":\"siteSuperAdminManage\",\"fieldName\":\"siteId\"} ');
INSERT INTO `grid_menu_config` VALUES ('9274', 'saSiteManage', 'f16c536730a511e8a37efcaa140ebf84', null, '一键开设站点', 'top', 'left', '0', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', null, null, '2', '6', null, null, null, '', '{\"importConfig\":{\"title\":\"一键开设站点\",\"excelTemplateDownloadUrl\":\"/common/downloadFile/downloadExistentFile\",\"excelUploadUrl\":\"/entity/superAdmin/superAdminCreateSite/createSites\",\"downloadExtraParams\":{\"fileKey\":\"createSite.xls\"}},\"gridImportConfig\":{\"title\":\"一键开设站点\",\"downloadImportTemplateUrl\":\"/common/downloadFile/downloadExistentFile\",\"downloadImportTemplateExtraParams\":{\"fileKey\":\"createSite.xls\"},\"downloadImportTemplateName\":\"导入模板.xls\",\"startImportUrl\":\"/entity/superAdmin/superAdminCreateSite/createSites\",\"uploadExtraParams\":{\"startNum\":0}},\"oldImportMode\":true}');
INSERT INTO `grid_menu_config` VALUES ('9275', 'saSiteManage', 'dc8475e8ff1a11e7ab1d001e671d0be8', null, '上传站点logo', 'top', 'left', '0', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', '{ 	formTitle: \'上传站点logo\', 	requestConfig: { 		url: \'/entity/superAdmin/superAdminCreateSite/uploadSiteLogo\', 		waitingMsg: \'处理中，请稍候...\' 	}, 	formFieldList: [ 		{ 			name: \'upload\', 			type: \'file\', 			labelText: \'logo压缩包\' 		} 	] }', null, '2', '5', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('9510', 'saSiteManage', 'a92817f1361611e8a37efcaa140ebf84', null, '站点配置管理', 'column', 'left', '0', '1', 'code', null, '1', null, null, null, null, null, null, '_blank', null, null, '2', '7', null, null, null, '', '{\"routerName\":\"siteConfigEdit\",\"fieldName\":\"siteCode\"} ');
INSERT INTO `grid_menu_config` VALUES ('9511', 'enumConstManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9512', 'enumConstManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9513', 'enumConstManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9514', 'systemVariablesManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9515', 'systemVariablesManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9516', 'systemVariablesManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9517', 'scoreComposeItemManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9518', 'scoreComposeItemManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9519', 'scoreComposeItemManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9520', 'stuEnrollInfoManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9521', 'stuEnrollInfoManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9522', 'stuEnrollInfoManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9538', 'gridManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('9539', 'gridManage', 'a92817f1361611e8a37efcaa140ebf84', null, '添加', 'top', 'left', '0', '0', 'id', null, '1', null, '/sa/grid/addGridConfig', null, null, null, null, '_self', null, null, '2', '1', null, null, null, '', '{\"routerName\":\"addGridConfig\",\"fieldName\":\"\"}');
INSERT INTO `grid_menu_config` VALUES ('9540', 'gridManage', 'a92817f1361611e8a37efcaa140ebf84', null, '修改', 'column', 'left', '0', '1', 'id', null, '0', null, null, null, null, null, null, '_blank', null, null, '2', '2', null, null, null, '', '{\"routerName\":\"editGridConfig\",\"fieldName\":\"id\"}');
INSERT INTO `grid_menu_config` VALUES ('9541', 'gridManage', 'cb245573ff1a11e7ab1d001e671d0be8', null, '清除缓存', 'top', 'left', '1', '0', 'id', null, '1', null, '/superAdmin/gridManage/clearGridCache', null, null, null, null, '_blank', null, null, '2', '3', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('9542', 'gridManage', 'dc8475e8ff1a11e7ab1d001e671d0be8', null, '复制Grid', 'top', 'left', '1', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', '{\r\nformTitle: \'复制Grid\',     \r\n requestConfig: {        \r\n url: \'/superAdmin/gridManage/copyGrid\',\r\n waitingMsg: \'处理中，请稍等...\'   \r\n }, \r\n formFieldList:  		\r\n [{id:\'id\',\r\n name: \'id\', \r\n type: \'text\',         		\r\n labelText: \'gridId\',\r\n nullable: false,\r\n checkRegular: \'^[a-zA-Z0-9]{6,50}$\',\r\n checkMessage: \'字母和数字，长度50\'\r\n },{\r\n id: \'peWebSite_name\',\r\n name: \'peWebSite.name\',\r\n type: \'select\',\r\n labelText: \'站点(仅备注)\',\r\n nullable: false\r\n }]\r\n}', null, '2', '4', null, null, null, '', '{\"routerName\":\"editGridConfig\",\"fieldName\":\"id\"}');
INSERT INTO `grid_menu_config` VALUES ('19444', 'stationMessageGroupManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19445', 'stationMessageGroupManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19446', 'stationMessageGroupManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19447', 'stationMessageGroupManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19448', 'stationMessageSignManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19449', 'stationMessageSignManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19450', 'stationMessageSignManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19451', 'stationMessageSignManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19452', 'stationMessageGroupManage', 'a92817f1361611e8a37efcaa140ebf84', null, '占位符管理', 'column', 'left', '0', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', null, null, '2', '1', null, null, null, '', '{\"routerName\":\"stationMessageSignManage\",\"fieldName\":\"parentId\"}');
INSERT INTO `grid_menu_config` VALUES ('19453', 'stationMessageSignManage', 'd6228e78ff1a11e7ab1d001e671d0be8', null, '返回', 'top', 'left', '0', '1', 'id', null, '1', null, null, null, null, null, null, '_blank', null, null, '2', '1', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('19454', 'stationMessageSiteManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19455', 'stationMessageSiteManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19456', 'stationMessageSiteManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19457', 'stationMessageSiteManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19458', 'workSpaceConfigManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19459', 'workSpaceConfigManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('19460', 'workSpaceConfigManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29685', 'apiSiteConfig', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29686', 'apiSiteConfig', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29687', 'apiSiteConfig', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29688', 'apiSiteConfig', 'cb245573ff1a11e7ab1d001e671d0be8', null, '全站点生成自对接配置', 'top', 'left', '0', '1', 'id', '', '1', '', '/entity/superAdmin/apiSiteConfig/generateAllSiteConfig', null, '', null, null, '_blank', null, null, '2', '1', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('29781', 'scheduleJobManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29782', 'scheduleJobManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29783', 'scheduleJobManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29784', 'scheduleJobManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29785', 'scheduleJobManage', 'a92817f1361611e8a37efcaa140ebf84', null, '触发器管理', 'column', 'right', '0', '1', 'id', '', '1', '', null, null, '', null, null, '_blank', null, null, '2', '1', null, null, null, '', '{\"routerName\": \"scheduleJobTriggerManage\", \"fieldName\": \"parentId\"}');
INSERT INTO `grid_menu_config` VALUES ('29787', 'scheduleJobTriggerManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29788', 'scheduleJobTriggerManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29789', 'scheduleJobTriggerManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '3', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29790', 'scheduleJobTriggerManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29791', 'scheduleJobTriggerManage', 'd6228e78ff1a11e7ab1d001e671d0be8', null, '返回', 'top', 'left', '0', '1', 'id', '', '1', '', null, null, '', null, null, '_blank', null, null, '2', '1', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('29823', 'peWebSiteConfigManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29824', 'peWebSiteConfigManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29825', 'peWebSiteConfigManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29826', 'peWebSiteConfigManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29828', 'saSiteManage', 'cb245573ff1a11e7ab1d001e671d0be8', null, '通知项目更新站点配置', 'top', 'left', '0', '1', 'id', '', '1', '', '/entity/superAdmin/superAdminCreateSite/updateSiteConfig', null, '', null, null, '_blank', null, null, '2', '8', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('29831', 'weChatTemplateGroupManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29832', 'weChatTemplateGroupManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29833', 'weChatTemplateGroupManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29834', 'weChatTemplateGroupManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29835', 'weChatTemplateColumnManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29836', 'weChatTemplateColumnManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29837', 'weChatTemplateColumnManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29838', 'weChatTemplateColumnManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29839', 'weChatTemplateSiteManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29840', 'weChatTemplateSiteManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29841', 'weChatTemplateSiteManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29842', 'weChatTemplateSiteManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29845', 'sendMessageGroupManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29846', 'sendMessageGroupManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29847', 'sendMessageGroupManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29848', 'sendMessageGroupManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29849', 'sendMessageTypeManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29850', 'sendMessageTypeManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29851', 'sendMessageTypeManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29852', 'sendMessageTypeManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29853', 'sendMessageSiteManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29854', 'sendMessageSiteManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29855', 'sendMessageSiteManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29856', 'sendMessageSiteManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29857', 'systemCustomConfigManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29858', 'systemCustomConfigManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29859', 'systemCustomConfigManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29860', 'systemCustomConfigManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29861', 'systemCustomConfigManage', 'a92817f1361611e8a37efcaa140ebf84', null, 'URL管理', 'column', 'right', '0', '1', 'id', '', '1', '', null, null, '', null, null, '_blank', null, null, '2', '1', null, null, null, '', '{\"routerName\": \"systemCustomConfigUrlManage\", \"fieldName\": \"parentId\"}');
INSERT INTO `grid_menu_config` VALUES ('29862', 'systemCustomConfigManage', 'a92817f1361611e8a37efcaa140ebf84', null, '站点定制管理', 'column', 'right', '0', '1', 'id', '', '1', '', null, null, '', null, null, '_blank', null, null, '2', '2', null, null, null, '', '{\"routerName\": \"systemSiteCustomConfigManage\", \"fieldName\": \"parentId\"}');
INSERT INTO `grid_menu_config` VALUES ('29863', 'systemCustomConfigUrlManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29864', 'systemCustomConfigUrlManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '3', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29865', 'systemCustomConfigUrlManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29866', 'systemCustomConfigUrlManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29867', 'systemSiteCustomConfigManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29868', 'systemSiteCustomConfigManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29869', 'systemSiteCustomConfigManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29870', 'systemSiteCustomConfigManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29871', 'systemCustomConfigUrlManage', 'd6228e78ff1a11e7ab1d001e671d0be8', null, '返回', 'top', 'left', '0', '1', 'id', '', '1', '', null, null, '', null, null, '_blank', null, null, '2', '1', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('29872', 'systemSiteCustomConfigManage', 'd6228e78ff1a11e7ab1d001e671d0be8', null, '返回', 'top', 'left', '0', '1', 'id', '', '1', '', null, null, '', null, null, '_blank', null, null, '2', '1', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('29874', 'emailMessageGroupManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29875', 'emailMessageGroupManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29876', 'emailMessageGroupManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29877', 'emailMessageGroupManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29878', 'emailMessageSiteManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29879', 'emailMessageSiteManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29880', 'emailMessageSiteManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29881', 'emailMessageSiteManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29882', 'smsMessageGroupManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29883', 'smsMessageGroupManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29884', 'smsMessageGroupManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29885', 'smsMessageGroupManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29886', 'smsMessageSiteManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29887', 'smsMessageSiteManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29888', 'smsMessageSiteManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29889', 'smsMessageSiteManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29900', 'unitTypeManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29901', 'unitTypeManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29902', 'unitTypeManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29903', 'unitTypeManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29904', 'schoolZoneManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29905', 'schoolZoneManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29906', 'schoolZoneManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29907', 'schoolZoneManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29908', 'trainingAreaManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29909', 'trainingAreaManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29910', 'trainingAreaManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29911', 'trainingAreaManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29912', 'trainingTypeManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29913', 'trainingTypeManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29914', 'trainingTypeManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29915', 'trainingTypeManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29916', 'trainingTargetManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29917', 'trainingTargetManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29918', 'trainingTargetManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29919', 'trainingTargetManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29920', 'courseTypeManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29921', 'courseTypeManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29922', 'courseTypeManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29923', 'courseTypeManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29924', 'suitableTargetManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29925', 'suitableTargetManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29926', 'suitableTargetManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29927', 'suitableTargetManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29928', 'positionalTitleManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29929', 'positionalTitleManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29930', 'positionalTitleManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29931', 'positionalTitleManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29932', 'educationalBackgroundManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29933', 'educationalBackgroundManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29934', 'educationalBackgroundManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29935', 'educationalBackgroundManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29936', 'unitManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29937', 'unitManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29938', 'unitManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29939', 'unitManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29940', 'siteManagerManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '3', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29941', 'teacherResourceManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29942', 'teacherResourceManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29943', 'teacherResourceManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29944', 'teacherResourceManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29946', 'courseResourceManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29947', 'courseResourceManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29948', 'courseResourceManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29949', 'courseResourceManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29950', 'placeResourceManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29951', 'placeResourceManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29952', 'placeResourceManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29953', 'placeResourceManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29954', 'hotelResourceManage', null, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '1', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29955', 'hotelResourceManage', null, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '2', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29956', 'hotelResourceManage', null, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '3', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29957', 'hotelResourceManage', null, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', '0', '0', '', null, null, null, null, null, null, null, null, null, null, null, '2', '4', null, null, null, null, null);
INSERT INTO `grid_menu_config` VALUES ('29961', 'courseResourceManage', 'd314862aff1a11e7ab1d001e671d0be8', null, '课程设计', 'column', 'left', '0', '1', 'id', '', '1', '', null, null, '', null, null, '_blank', null, null, '2', '6', null, null, null, '', '');
INSERT INTO `grid_menu_config` VALUES ('29962', 'courseResourceManage', 'd314862aff1a11e7ab1d001e671d0be8', null, '课程预览', 'column', 'left', '0', '1', 'id', '', '1', '', null, null, '', null, null, '_blank', null, null, '2', '7', null, null, null, '', '');

-- ----------------------------
-- Table structure for grid_menu_permission
-- ----------------------------
DROP TABLE IF EXISTS `grid_menu_permission`;
CREATE TABLE `grid_menu_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` varchar(50) NOT NULL COMMENT '站点id',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单id',
  `role_code` varchar(300) DEFAULT NULL COMMENT '角色code，逗号分隔',
  `isActive` varchar(1) DEFAULT NULL COMMENT '1：代表有效的；0：代表无效的',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_gridmenupermission_siteid_menuid` (`site_id`,`menu_id`) USING BTREE,
  KEY `grid_menu_permission_ibfk_02` (`menu_id`) USING BTREE,
  CONSTRAINT `grid_menu_permission_ibfk_1` FOREIGN KEY (`site_id`) REFERENCES `pe_web_site` (`id`),
  CONSTRAINT `grid_menu_permission_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `grid_menu_config` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6298 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of grid_menu_permission
-- ----------------------------
INSERT INTO `grid_menu_permission` VALUES ('1', '83356b76275211e8baa4fcaa140ebf84', '1075', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('2', '83356b76275211e8baa4fcaa140ebf84', '1076', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3', '83356b76275211e8baa4fcaa140ebf84', '1077', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4', '83356b76275211e8baa4fcaa140ebf84', '1078', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('5', '83356b76275211e8baa4fcaa140ebf84', '1081', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6', '83356b76275211e8baa4fcaa140ebf84', '1082', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('7', '83356b76275211e8baa4fcaa140ebf84', '1083', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('582', '83356b76275211e8baa4fcaa140ebf84', '1056', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('583', '83356b76275211e8baa4fcaa140ebf84', '1059', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('584', '83356b76275211e8baa4fcaa140ebf84', '1123', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('585', '83356b76275211e8baa4fcaa140ebf84', '1124', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('586', '83356b76275211e8baa4fcaa140ebf84', '1126', ',', '1');
INSERT INTO `grid_menu_permission` VALUES ('587', '83356b76275211e8baa4fcaa140ebf84', '1127', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('588', '83356b76275211e8baa4fcaa140ebf84', '1128', ',', '1');
INSERT INTO `grid_menu_permission` VALUES ('589', '83356b76275211e8baa4fcaa140ebf84', '1129', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('590', '83356b76275211e8baa4fcaa140ebf84', '1190', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('591', '83356b76275211e8baa4fcaa140ebf84', '1191', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('592', '83356b76275211e8baa4fcaa140ebf84', '1194', ',', '1');
INSERT INTO `grid_menu_permission` VALUES ('593', '83356b76275211e8baa4fcaa140ebf84', '1195', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('594', '83356b76275211e8baa4fcaa140ebf84', '1196', ',', '1');
INSERT INTO `grid_menu_permission` VALUES ('595', '83356b76275211e8baa4fcaa140ebf84', '1197', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('596', '83356b76275211e8baa4fcaa140ebf84', '1258', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('597', '83356b76275211e8baa4fcaa140ebf84', '1259', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('598', '83356b76275211e8baa4fcaa140ebf84', '1265', ',', '1');
INSERT INTO `grid_menu_permission` VALUES ('599', '83356b76275211e8baa4fcaa140ebf84', '1266', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('600', '83356b76275211e8baa4fcaa140ebf84', '1267', ',', '1');
INSERT INTO `grid_menu_permission` VALUES ('601', '83356b76275211e8baa4fcaa140ebf84', '1268', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('602', '83356b76275211e8baa4fcaa140ebf84', '1290', ',', '1');
INSERT INTO `grid_menu_permission` VALUES ('603', '83356b76275211e8baa4fcaa140ebf84', '1291', ',', '1');
INSERT INTO `grid_menu_permission` VALUES ('614', '83356b76275211e8baa4fcaa140ebf84', '1125', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('615', '83356b76275211e8baa4fcaa140ebf84', '1192', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('616', '83356b76275211e8baa4fcaa140ebf84', '1260', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1143', '83356b76275211e8baa4fcaa140ebf84', '1315', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1144', '83356b76275211e8baa4fcaa140ebf84', '1316', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1145', '83356b76275211e8baa4fcaa140ebf84', '1317', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1146', '83356b76275211e8baa4fcaa140ebf84', '9234', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1147', '83356b76275211e8baa4fcaa140ebf84', '1331', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1148', '83356b76275211e8baa4fcaa140ebf84', '1332', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1149', '83356b76275211e8baa4fcaa140ebf84', '1333', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1150', '83356b76275211e8baa4fcaa140ebf84', '9233', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1151', '83356b76275211e8baa4fcaa140ebf84', '9235', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1500', '83356b76275211e8baa4fcaa140ebf84', '1079', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1501', '83356b76275211e8baa4fcaa140ebf84', '1080', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1502', '83356b76275211e8baa4fcaa140ebf84', '1308', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1503', '83356b76275211e8baa4fcaa140ebf84', '1319', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1504', '83356b76275211e8baa4fcaa140ebf84', '1320', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1505', '83356b76275211e8baa4fcaa140ebf84', '1321', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1506', '83356b76275211e8baa4fcaa140ebf84', '1322', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1507', '83356b76275211e8baa4fcaa140ebf84', '9249', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1508', '83356b76275211e8baa4fcaa140ebf84', '9250', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1509', '83356b76275211e8baa4fcaa140ebf84', '9251', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('1510', '83356b76275211e8baa4fcaa140ebf84', '9252', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('2911', '83356b76275211e8baa4fcaa140ebf84', '9275', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('2912', '83356b76275211e8baa4fcaa140ebf84', '9274', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3176', '83356b76275211e8baa4fcaa140ebf84', '9510', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3177', '83356b76275211e8baa4fcaa140ebf84', '9511', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3178', '83356b76275211e8baa4fcaa140ebf84', '9512', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3179', '83356b76275211e8baa4fcaa140ebf84', '9513', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3180', '83356b76275211e8baa4fcaa140ebf84', '9516', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3181', '83356b76275211e8baa4fcaa140ebf84', '9514', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3182', '83356b76275211e8baa4fcaa140ebf84', '9515', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3183', '83356b76275211e8baa4fcaa140ebf84', '9517', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3184', '83356b76275211e8baa4fcaa140ebf84', '9519', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3185', '83356b76275211e8baa4fcaa140ebf84', '9518', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3186', '83356b76275211e8baa4fcaa140ebf84', '9520', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3187', '83356b76275211e8baa4fcaa140ebf84', '9521', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3188', '83356b76275211e8baa4fcaa140ebf84', '9522', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3733', '83356b76275211e8baa4fcaa140ebf84', '9538', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3734', '83356b76275211e8baa4fcaa140ebf84', '9539', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3735', '83356b76275211e8baa4fcaa140ebf84', '9540', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3736', '83356b76275211e8baa4fcaa140ebf84', '9541', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('3737', '83356b76275211e8baa4fcaa140ebf84', '9542', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4134', '83356b76275211e8baa4fcaa140ebf84', '19458', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4135', '83356b76275211e8baa4fcaa140ebf84', '19459', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4136', '83356b76275211e8baa4fcaa140ebf84', '19460', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4138', '83356b76275211e8baa4fcaa140ebf84', '19456', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4139', '83356b76275211e8baa4fcaa140ebf84', '19457', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4140', '83356b76275211e8baa4fcaa140ebf84', '19444', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4141', '83356b76275211e8baa4fcaa140ebf84', '19445', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4142', '83356b76275211e8baa4fcaa140ebf84', '19446', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4143', '83356b76275211e8baa4fcaa140ebf84', '19447', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4144', '83356b76275211e8baa4fcaa140ebf84', '19448', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4145', '83356b76275211e8baa4fcaa140ebf84', '19449', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4146', '83356b76275211e8baa4fcaa140ebf84', '19450', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4147', '83356b76275211e8baa4fcaa140ebf84', '19451', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4148', '83356b76275211e8baa4fcaa140ebf84', '19452', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4149', '83356b76275211e8baa4fcaa140ebf84', '19453', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4150', '83356b76275211e8baa4fcaa140ebf84', '19454', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('4151', '83356b76275211e8baa4fcaa140ebf84', '19455', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('5979', '83356b76275211e8baa4fcaa140ebf84', '29685', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('5980', '83356b76275211e8baa4fcaa140ebf84', '29686', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('5981', '83356b76275211e8baa4fcaa140ebf84', '29687', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('5982', '83356b76275211e8baa4fcaa140ebf84', '29688', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6116', '83356b76275211e8baa4fcaa140ebf84', '29781', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6117', '83356b76275211e8baa4fcaa140ebf84', '29782', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6118', '83356b76275211e8baa4fcaa140ebf84', '29783', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6119', '83356b76275211e8baa4fcaa140ebf84', '29784', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6120', '83356b76275211e8baa4fcaa140ebf84', '29785', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6121', '83356b76275211e8baa4fcaa140ebf84', '29787', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6122', '83356b76275211e8baa4fcaa140ebf84', '29788', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6123', '83356b76275211e8baa4fcaa140ebf84', '29790', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6124', '83356b76275211e8baa4fcaa140ebf84', '29791', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6152', '83356b76275211e8baa4fcaa140ebf84', '29823', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6153', '83356b76275211e8baa4fcaa140ebf84', '29824', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6154', '83356b76275211e8baa4fcaa140ebf84', '29825', ',1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6155', '83356b76275211e8baa4fcaa140ebf84', '29826', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6159', '83356b76275211e8baa4fcaa140ebf84', '29828', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6163', '83356b76275211e8baa4fcaa140ebf84', '29831', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6164', '83356b76275211e8baa4fcaa140ebf84', '29832', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6165', '83356b76275211e8baa4fcaa140ebf84', '29833', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6166', '83356b76275211e8baa4fcaa140ebf84', '29834', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6167', '83356b76275211e8baa4fcaa140ebf84', '29835', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6168', '83356b76275211e8baa4fcaa140ebf84', '29836', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6169', '83356b76275211e8baa4fcaa140ebf84', '29837', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6170', '83356b76275211e8baa4fcaa140ebf84', '29838', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6171', '83356b76275211e8baa4fcaa140ebf84', '29839', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6172', '83356b76275211e8baa4fcaa140ebf84', '29840', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6173', '83356b76275211e8baa4fcaa140ebf84', '29841', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6174', '83356b76275211e8baa4fcaa140ebf84', '29842', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6177', '83356b76275211e8baa4fcaa140ebf84', '29845', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6178', '83356b76275211e8baa4fcaa140ebf84', '29846', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6179', '83356b76275211e8baa4fcaa140ebf84', '29847', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6180', '83356b76275211e8baa4fcaa140ebf84', '29848', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6181', '83356b76275211e8baa4fcaa140ebf84', '29849', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6182', '83356b76275211e8baa4fcaa140ebf84', '29850', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6183', '83356b76275211e8baa4fcaa140ebf84', '29851', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6184', '83356b76275211e8baa4fcaa140ebf84', '29852', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6185', '83356b76275211e8baa4fcaa140ebf84', '29853', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6186', '83356b76275211e8baa4fcaa140ebf84', '29854', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6187', '83356b76275211e8baa4fcaa140ebf84', '29855', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6188', '83356b76275211e8baa4fcaa140ebf84', '29856', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6189', '83356b76275211e8baa4fcaa140ebf84', '29857', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6190', '83356b76275211e8baa4fcaa140ebf84', '29858', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6191', '83356b76275211e8baa4fcaa140ebf84', '29859', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6192', '83356b76275211e8baa4fcaa140ebf84', '29860', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6193', '83356b76275211e8baa4fcaa140ebf84', '29861', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6194', '83356b76275211e8baa4fcaa140ebf84', '29862', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6195', '83356b76275211e8baa4fcaa140ebf84', '29863', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6196', '83356b76275211e8baa4fcaa140ebf84', '29864', ',9,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6197', '83356b76275211e8baa4fcaa140ebf84', '29865', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6198', '83356b76275211e8baa4fcaa140ebf84', '29866', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6199', '83356b76275211e8baa4fcaa140ebf84', '29867', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6200', '83356b76275211e8baa4fcaa140ebf84', '29868', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6201', '83356b76275211e8baa4fcaa140ebf84', '29869', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6202', '83356b76275211e8baa4fcaa140ebf84', '29870', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6203', '83356b76275211e8baa4fcaa140ebf84', '29871', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6204', '83356b76275211e8baa4fcaa140ebf84', '29872', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6206', '83356b76275211e8baa4fcaa140ebf84', '29874', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6207', '83356b76275211e8baa4fcaa140ebf84', '29875', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6208', '83356b76275211e8baa4fcaa140ebf84', '29876', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6209', '83356b76275211e8baa4fcaa140ebf84', '29877', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6210', '83356b76275211e8baa4fcaa140ebf84', '29878', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6211', '83356b76275211e8baa4fcaa140ebf84', '29879', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6212', '83356b76275211e8baa4fcaa140ebf84', '29880', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6213', '83356b76275211e8baa4fcaa140ebf84', '29881', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6214', '83356b76275211e8baa4fcaa140ebf84', '29882', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6215', '83356b76275211e8baa4fcaa140ebf84', '29883', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6216', '83356b76275211e8baa4fcaa140ebf84', '29884', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6217', '83356b76275211e8baa4fcaa140ebf84', '29885', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6218', '83356b76275211e8baa4fcaa140ebf84', '29886', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6219', '83356b76275211e8baa4fcaa140ebf84', '29887', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6220', '83356b76275211e8baa4fcaa140ebf84', '29888', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6221', '83356b76275211e8baa4fcaa140ebf84', '29889', ',9,1,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6232', '4028ae316a09ee85016a09f4d7640000', '29900', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6233', '4028ae316a09ee85016a09f4d7640000', '29901', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6234', '4028ae316a09ee85016a09f4d7640000', '29902', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6235', '4028ae316a09ee85016a09f4d7640000', '29903', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6236', '4028ae316a09ee85016a09f4d7640000', '29904', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6237', '4028ae316a09ee85016a09f4d7640000', '29905', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6238', '4028ae316a09ee85016a09f4d7640000', '29906', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6239', '4028ae316a09ee85016a09f4d7640000', '29907', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6240', '4028ae316a09ee85016a09f4d7640000', '29908', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6241', '4028ae316a09ee85016a09f4d7640000', '29909', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6242', '4028ae316a09ee85016a09f4d7640000', '29910', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6243', '4028ae316a09ee85016a09f4d7640000', '29911', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6244', '4028ae316a09ee85016a09f4d7640000', '29912', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6245', '4028ae316a09ee85016a09f4d7640000', '29913', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6246', '4028ae316a09ee85016a09f4d7640000', '29914', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6247', '4028ae316a09ee85016a09f4d7640000', '29915', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6248', '4028ae316a09ee85016a09f4d7640000', '29916', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6249', '4028ae316a09ee85016a09f4d7640000', '29917', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6250', '4028ae316a09ee85016a09f4d7640000', '29918', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6251', '4028ae316a09ee85016a09f4d7640000', '29919', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6252', '4028ae316a09ee85016a09f4d7640000', '29920', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6253', '4028ae316a09ee85016a09f4d7640000', '29921', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6254', '4028ae316a09ee85016a09f4d7640000', '29922', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6255', '4028ae316a09ee85016a09f4d7640000', '29923', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6256', '4028ae316a09ee85016a09f4d7640000', '29924', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6257', '4028ae316a09ee85016a09f4d7640000', '29925', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6258', '4028ae316a09ee85016a09f4d7640000', '29926', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6259', '4028ae316a09ee85016a09f4d7640000', '29927', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6260', '4028ae316a09ee85016a09f4d7640000', '29928', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6261', '4028ae316a09ee85016a09f4d7640000', '29929', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6262', '4028ae316a09ee85016a09f4d7640000', '29930', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6263', '4028ae316a09ee85016a09f4d7640000', '29931', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6264', '4028ae316a09ee85016a09f4d7640000', '29932', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6265', '4028ae316a09ee85016a09f4d7640000', '29933', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6266', '4028ae316a09ee85016a09f4d7640000', '29934', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6267', '4028ae316a09ee85016a09f4d7640000', '29935', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6268', '4028ae316a09ee85016a09f4d7640000', '29936', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6269', '4028ae316a09ee85016a09f4d7640000', '29937', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6270', '4028ae316a09ee85016a09f4d7640000', '29938', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6271', '4028ae316a09ee85016a09f4d7640000', '29939', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6272', '4028ae316a09ee85016a09f4d7640000', '1312', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6273', '4028ae316a09ee85016a09f4d7640000', '1313', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6274', '4028ae316a09ee85016a09f4d7640000', '1314', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6276', '4028ae316a09ee85016a09f4d7640000', '1309', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6277', '4028ae316a09ee85016a09f4d7640000', '1310', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6278', '4028ae316a09ee85016a09f4d7640000', '1311', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6279', '4028ae316a09ee85016a09f4d7640000', '29941', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6280', '4028ae316a09ee85016a09f4d7640000', '29942', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6281', '4028ae316a09ee85016a09f4d7640000', '29943', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6282', '4028ae316a09ee85016a09f4d7640000', '29944', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6283', '4028ae316a09ee85016a09f4d7640000', '29946', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6284', '4028ae316a09ee85016a09f4d7640000', '29947', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6285', '4028ae316a09ee85016a09f4d7640000', '29948', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6286', '4028ae316a09ee85016a09f4d7640000', '29949', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6287', '4028ae316a09ee85016a09f4d7640000', '29954', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6288', '4028ae316a09ee85016a09f4d7640000', '29955', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6289', '4028ae316a09ee85016a09f4d7640000', '29956', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6290', '4028ae316a09ee85016a09f4d7640000', '29957', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6291', '4028ae316a09ee85016a09f4d7640000', '29950', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6292', '4028ae316a09ee85016a09f4d7640000', '29951', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6293', '4028ae316a09ee85016a09f4d7640000', '29952', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6294', '4028ae316a09ee85016a09f4d7640000', '29953', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6296', '4028ae316a09ee85016a09f4d7640000', '29961', ',2,3,', '1');
INSERT INTO `grid_menu_permission` VALUES ('6297', '4028ae316a09ee85016a09f4d7640000', '29962', ',2,3,', '1');

-- ----------------------------
-- Table structure for operate_guide
-- ----------------------------
DROP TABLE IF EXISTS `operate_guide`;
CREATE TABLE `operate_guide` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `flow_config` text,
  PRIMARY KEY (`id`),
  KEY `name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of operate_guide
-- ----------------------------

-- ----------------------------
-- Table structure for operate_guide_description
-- ----------------------------
DROP TABLE IF EXISTS `operate_guide_description`;
CREATE TABLE `operate_guide_description` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `icon` varchar(100) DEFAULT NULL,
  `description` varchar(300) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `fk_operate_guide_id` int(11) NOT NULL,
  `flag_active` varchar(50) NOT NULL,
  `fk_web_site_id` varchar(50) NOT NULL,
  `serial_number` int(2) DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`id`),
  KEY `fk_operate_guide` (`fk_operate_guide_id`),
  KEY `fk_active` (`flag_active`),
  KEY `fk_web_site` (`fk_web_site_id`),
  CONSTRAINT `operate_guide_description_ibfk_1` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `operate_guide_description_ibfk_2` FOREIGN KEY (`fk_operate_guide_id`) REFERENCES `operate_guide` (`id`),
  CONSTRAINT `operate_guide_description_ibfk_3` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of operate_guide_description
-- ----------------------------

-- ----------------------------
-- Table structure for pe_base_category
-- ----------------------------
DROP TABLE IF EXISTS `pe_base_category`;
CREATE TABLE `pe_base_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `code` varchar(40) DEFAULT NULL,
  `router_name` varchar(50) DEFAULT NULL,
  `url` varchar(500) DEFAULT NULL,
  `fk_grid_id` varchar(50) DEFAULT NULL,
  `flag_isvalid` varchar(50) NOT NULL,
  `flag_is_system` varchar(50) NOT NULL COMMENT '系统功能不能删除',
  `fk_base_category_group` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pebasecategory_fkgridid` (`fk_grid_id`) USING BTREE,
  KEY `fk_pebasecategory_fkbasecategorygroup` (`fk_base_category_group`) USING BTREE,
  KEY `fk_pebasecategory_fkisvalid` (`flag_isvalid`) USING BTREE,
  KEY `fk_pebasecategory_fkissystem` (`flag_is_system`) USING BTREE,
  CONSTRAINT `pe_base_category_ibfk_1` FOREIGN KEY (`fk_base_category_group`) REFERENCES `pe_base_category_group` (`id`),
  CONSTRAINT `pe_base_category_ibfk_2` FOREIGN KEY (`fk_grid_id`) REFERENCES `grid_basic_config` (`id`),
  CONSTRAINT `pe_base_category_ibfk_3` FOREIGN KEY (`flag_is_system`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `pe_base_category_ibfk_4` FOREIGN KEY (`flag_isvalid`) REFERENCES `enum_const` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1010 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of pe_base_category
-- ----------------------------
INSERT INTO `pe_base_category` VALUES ('2', '接口管理', null, null, '/superAdmin/interfaceManage/abstractGrid', 'interfaceManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('258', '站点管理', 'site-config-manage', null, '/superAdmin/siteManage/abstractGrid', 'saSiteManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('259', 'grid路由管理', null, null, '/superAdmin/vueRouterManage/abstractGrid', 'saVueRouterManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('260', '打印模板管理', 'super-business', null, '/print/printTemplate/abstractGrid', 'printTemplate', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('261', '打印模板分组管理', 'super-business', null, '/print/printTemplateGroup/abstractGrid', 'printTemplateGroup', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('262', '操作指导流程图设计', 'operate-guide-manage', null, '', null, '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('264', '角色菜单管理', 'role_menu_manage', null, '/role/menu/manage', null, '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('265', '平台菜单管理', 'menu-manage-tree', null, '/superAdmin/gridManage/abstractGrid', null, '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('266', '用户管理', null, null, '/superAdmin/userManage/abstractGrid', 'saUserManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('267', '角色管理', null, null, '/superAdmin/roleManage/abstractGrid', 'saRoleManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('456', 'grid管理(新)', '', '', '/superAdmin/gridManage/abstractGrid', 'gridManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('467', '基础功能管理', null, null, '/superAdmin/baseCategoryManage/abstractGrid', 'baseCategoryManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('479', '站点超管管理', 'siteSuperAdminManage', 'siteSuperAdminManage', '/superAdmin/siteSuperManager/siteSuperManagerManage/abstractGrid', 'siteSuperMangerManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('483', '打印模板占位符管理', 'super-business', null, '/print/printTemplateSign/abstractGrid', 'printTemplateSign', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('486', '操作指导管理', 'operate-guide-description-manage', null, '/superAdmin/operateGuide/operateGuideManage/abstractGrid', 'operateGuideManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('491', '缓存清理', 'cache-clear', null, '', null, '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('495', '各站点角色管理', 'super-site-role-manage', null, '/superAdmin/siteSuperManager/superAdminSiteRoleManage/abstractGrid', 'allSiteRoleManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('505', '菜单是否有效设置', 'menu_is_active_manage', null, '', null, '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('528', '基础功能接口管理', null, 'baseCategoryInterfaceManage', '/superAdmin/baseCategoryInterfaceManage/abstractGrid?actionId=baseCategoryInterfaceManage', 'baseCategoryInterfaceManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('620', '工作室配置管理', '', 'workSpaceConfigManage', '/superAdmin/siteConfig/workSpaceConfigManage/abstractGrid', 'workSpaceConfigManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('628', '站内信组管理', '', '', '/superAdmin/message/superAdminStationMessageGroupManage/abstractGrid', 'stationMessageGroupManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('629', '站内信占位符管理', '', 'stationMessageSignManage', '/superAdmin/message/superAdminStationMessageSignManage/abstractGrid', 'stationMessageSignManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('630', '站内信站点管理', '', '', '/superAdmin/message/superAdminStationMessageSiteManage/abstractGrid', 'stationMessageSiteManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('673', '站点配置管理', 'site-config-edit', 'siteConfigEdit', '', null, '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('674', 'EnumConst管理', '', 'enumConstManage', '/superAdmin/siteConfig/enumConstManage/abstractGrid', 'enumConstManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('675', 'SystemVariables管理', '', 'systemVariablesManage', '/superAdmin/siteConfig/systemVariablesManage/abstractGrid', 'systemVariablesManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('676', '成绩项管理', '', 'scoreComposeItemManage', '/superAdmin/siteConfig/peScoreComposeItemManage/abstractGrid', 'scoreComposeItemManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('678', '自定义学号生成规则', 'reg-number-regular-manage', 'regNumRegularManage', '', null, '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('692', '自定义班号生成规则', 'class-number-regular-manage', 'classNumRegularManage', '', null, '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('693', '自定义班级名称生成规则', 'class-name-regular-manage', 'classNameRegularManage', '', null, '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('694', '学生信息配置(新)', '', 'stuEnrollInfoConfig', '/superAdmin/siteConfig/stuEnrollInfoManage/abstractGrid', 'stuEnrollInfoManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('713', '平台数据处理', 'handle-data-page', '', '', null, '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('923', '外接项目站点管理', 'super-business', '', '/entity/superAdmin/apiSiteConfig/abstractGrid', 'apiSiteConfig', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('924', '操作日志管理', 'operate-record-manage', '', '', null, '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('951', '调度任务管理', '', '', '/superAdmin/schedule/scheduleJobManage/abstractGrid', 'scheduleJobManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('952', '调度时间管理', '', 'scheduleJobTriggerManage', '/superAdmin/schedule/scheduleJobTriggerManage/abstractGrid', 'scheduleJobTriggerManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('953', '调度记录查询', '', '', '/superAdmin/schedule/scheduleJobRecord/abstractGrid', 'scheduleJobRecordShow', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('967', '站点基础配置管理', '', '', '/superAdmin/webSiteConfigManage/abstractGrid', 'peWebSiteConfigManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('968', '微信模板消息组管理', '', '', '/superAdmin/message/weChatTemplateGroupManage/abstractGrid', 'weChatTemplateGroupManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('969', '微信模板消息字段管理', '', '', '/superAdmin/message/weChatTemplateColumnManage/abstractGrid', 'weChatTemplateColumnManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('970', '微信模板消息站点管理', '', '', '/superAdmin/message/weChatTemplateSiteManage/abstractGrid', 'weChatTemplateSiteManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('971', '发送消息组管理', '', '', '/superAdmin/message/sendMessageGroupManage/abstractGrid', 'sendMessageGroupManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('972', '发送消息类型管理', '', '', '/superAdmin/message/sendMessageTypeManage/abstractGrid', 'sendMessageTypeManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('973', '发送消息站点管理', '', '', '/superAdmin/message/sendMessageSiteManage/abstractGrid', 'sendMessageSiteManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('974', '定制配置管理', '', '', '/superAdmin/custom/systemCustomConfigManage/abstractGrid', 'systemCustomConfigManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('975', '定制配置与Url关联管理', '', 'systemCustomConfigUrlManage', '/superAdmin/custom/systemCustomConfigUrlManage/abstractGrid', 'systemCustomConfigUrlManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('976', '定制配置与站点关联管理', '', 'systemSiteCustomConfigManage', '/superAdmin/custom/systemSiteCustomConfigManage/abstractGrid', 'systemSiteCustomConfigManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('978', '邮件消息组管理', '', '', '/entity/message/emailMessageGroupManage/abstractGrid', 'emailMessageGroupManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('979', '邮件消息与站点管理', '', '', '/entity/message/emailMessageSiteManage/abstractGrid', 'emailMessageSiteManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('980', '短信消息组管理', '', '', '/entity/message/smsMessageGroupManage/abstractGrid', 'smsMessageGroupManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('981', '短信消息与站点关联管理', '', '', '/entity/message/smsMessageSiteManage/abstractGrid', 'smsMessageSiteManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('992', '机构类型管理', '', '', '/entity/basic/unitTypeManage/abstractGrid', 'unitTypeManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('993', '校区管理', '', '', '/entity/basic/schoolZoneManage/abstractGrid', 'schoolZoneManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('994', '培训地区管理', '', '', '/entity/basic/trainingAreaManage/abstractGrid', 'trainingAreaManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('995', '培训类型管理', '', '', '/entity/basic/trainingTypeManage/abstractGrid', 'trainingTypeManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('996', '培训对象管理', '', '', '/entity/basic/trainingTargetManage/abstractGrid', 'trainingTargetManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('997', '课程类型管理', '', '', '/entity/basic/courseTypeManage/abstractGrid', 'courseTypeManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('998', '适合对象管理', '', '', '/entity/basic/suitableTargetManage/abstractGrid', 'suitableTargetManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('999', '讲师职称管理', '', '', '/entity/basic/positionalTitleManage/abstractGrid', 'positionalTitleManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('1000', '最高学历管理', '', '', '/entity/basic/educationalBackgroundManage/abstractGrid', 'educationalBackgroundManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('1001', '单位管理', '', '', '/entity/basic/unitManage/abstractGrid', 'unitManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('1002', '站点角色管理', 'site_role_manage', null, '/entity/siteManager/siteRoleManage/abstractGrid', 'siteRoleManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('1003', '站点角色菜单管理', 'site-role-menu-manage', null, null, null, '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('1005', '管理员用户管理', 'site-manager-manage', 'siteManagerManage', '/entity/siteManager/siteManagerManage/abstractGrid', 'siteManagerManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '1');
INSERT INTO `pe_base_category` VALUES ('1006', '师资库', '', '', '/entity/resource/teacherResourceManage/abstractGrid', 'teacherResourceManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '2');
INSERT INTO `pe_base_category` VALUES ('1007', '课程库', 'training-course-photo', '', '/entity/resource/courseResourceManage/abstractGrid', 'courseResourceManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '2');
INSERT INTO `pe_base_category` VALUES ('1008', '合作宾馆', '', '', '/entity/resource/hotelResourceManage/abstractGrid', 'hotelResourceManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '2');
INSERT INTO `pe_base_category` VALUES ('1009', '培训地点', '', '', '/entity/resource/placeResourceManage/abstractGrid', 'placeResourceManage', '2', '0bc108f54df211e8aac8fcaa140ebf84', '2');

-- ----------------------------
-- Table structure for pe_base_category_group
-- ----------------------------
DROP TABLE IF EXISTS `pe_base_category_group`;
CREATE TABLE `pe_base_category_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `code` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of pe_base_category_group
-- ----------------------------
INSERT INTO `pe_base_category_group` VALUES ('1', '系统高级设置', '1');
INSERT INTO `pe_base_category_group` VALUES ('2', '资源管理', '2');

-- ----------------------------
-- Table structure for pe_chart
-- ----------------------------
DROP TABLE IF EXISTS `pe_chart`;
CREATE TABLE `pe_chart` (
  `id` varchar(50) NOT NULL,
  `code` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `basic_config` varchar(1000) DEFAULT NULL,
  `chart_config` varchar(5000) DEFAULT NULL,
  `data_url` varchar(300) DEFAULT NULL,
  `data_params` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pe_chart
-- ----------------------------

-- ----------------------------
-- Table structure for pe_interface
-- ----------------------------
DROP TABLE IF EXISTS `pe_interface`;
CREATE TABLE `pe_interface` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `url` varchar(100) NOT NULL COMMENT 'spring securit的url规则，支持通配符',
  `method` varchar(20) DEFAULT NULL,
  `flag_isvalid` varchar(50) NOT NULL COMMENT '常量。是否有效',
  PRIMARY KEY (`id`),
  KEY `fk_peinterface_flagisvalid` (`flag_isvalid`) USING BTREE,
  CONSTRAINT `pe_interface_ibfk_1` FOREIGN KEY (`flag_isvalid`) REFERENCES `enum_const` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=679 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of pe_interface
-- ----------------------------
INSERT INTO `pe_interface` VALUES ('97', 'grid管理', '/superAdmin/gridManage/**', null, '2');
INSERT INTO `pe_interface` VALUES ('156', '打印模板管理', '/print/printTemplate/**', '', '2');
INSERT INTO `pe_interface` VALUES ('157', '打印模板分组管理', '/print/printTemplateGroup/**', '', '2');
INSERT INTO `pe_interface` VALUES ('158', '打印模板占位符管理', '/print/printTemplateSign/**', '', '2');
INSERT INTO `pe_interface` VALUES ('164', '各站点角色管理', '/superAdmin/siteSuperManager/superAdminSiteRoleManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('257', '工作室配置管理', '/superAdmin/siteConfig/workSpaceConfigManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('264', '超管站内信组管理', '/superAdmin/message/superAdminStationMessageGroupManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('265', '站内信占位符管理', '/superAdmin/message/superAdminStationMessageSignManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('266', '站内信站点管理', '/superAdmin/message/superAdminStationMessageSiteManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('317', 'EnumConst管理', '/superAdmin/siteConfig/enumConstManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('318', '站点配置信息管理', '/superAdmin/siteConfig/siteConfigInfo/**', '', '2');
INSERT INTO `pe_interface` VALUES ('319', 'SystemVariables管理', '/superAdmin/siteConfig/systemVariablesManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('320', '成绩项管理', '/superAdmin/siteConfig/peScoreComposeItemManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('336', '学生信息配置', '/superAdmin/siteConfig/stuEnrollInfoManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('337', '站点开设上传', '/entity/superAdmin/superAdminCreateSite/**', '', '2');
INSERT INTO `pe_interface` VALUES ('351', '平台数据处理', '/entity/superAdmin/handleData/**', '', '2');
INSERT INTO `pe_interface` VALUES ('611', '外接项目站点管理', '/entity/superAdmin/apiSiteConfig/**', '', '2');
INSERT INTO `pe_interface` VALUES ('612', '操作日志管理', '/entity/superAdmin/operateRecordManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('634', '调度任务管理', '/superAdmin/schedule/scheduleJobManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('635', '调度时间管理', '/superAdmin/schedule/scheduleJobTriggerManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('636', '调度记录查询', '/superAdmin/schedule/scheduleJobRecord/**', '', '2');
INSERT INTO `pe_interface` VALUES ('646', '站点基础配置管理', '/superAdmin/webSiteConfigManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('647', '微信模板消息组管理', '/superAdmin/message/weChatTemplateGroupManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('648', '微信模板消息字段管理', '/superAdmin/message/weChatTemplateColumnManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('649', '微信模板消息站点管理', '/superAdmin/message/weChatTemplateSiteManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('650', '发送消息组管理', '/superAdmin/message/sendMessageGroupManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('651', '发送消息类型管理', '/superAdmin/message/sendMessageTypeManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('652', '发送消息站点管理', '/superAdmin/message/sendMessageSiteManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('653', '定制配置管理', '/superAdmin/custom/systemCustomConfigManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('654', '定制配置与Url关联', '/superAdmin/custom/systemCustomConfigUrlManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('655', '定制配置与站点关联', '/superAdmin/custom/systemSiteCustomConfigManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('657', '邮件消息组管理', '/entity/message/emailMessageGroupManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('658', '邮件消息与站点管理', '/entity/message/emailMessageSiteManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('659', '短信消息组管理', '/entity/message/smsMessageGroupManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('660', '短信消息与站点管理', '/entity/message/smsMessageSiteManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('662', '机构类型管理', '/entity/basic/unitTypeManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('663', '校区管理', '/entity/basic/schoolZoneManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('664', '培训地区管理', '/entity/basic/trainingAreaManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('665', '培训类型管理', '/entity/basic/trainingTypeManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('666', '培训对象管理', '/entity/basic/trainingTargetManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('667', '课程类型管理', '/entity/basic/courseTypeManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('668', '适合对象管理', '/entity/basic/suitableTargetManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('669', '讲师职称管理', '/entity/basic/positionalTitleManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('670', '最高学历管理', '/entity/basic/educationalBackgroundManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('671', '单位管理', '/entity/basic/unitManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('672', '站点角色管理', '/entity/siteManager/siteRoleManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('673', '站点角色菜单管理', '/entity/siteManager/siteRoleMenuManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('674', '管理员用户管理', '/entity/siteManager/siteManagerManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('675', '师资库', '/entity/resource/teacherResourceManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('676', '课程库', '/entity/resource/courseResourceManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('677', '合作宾馆', '/entity/resource/hotelResourceManage/**', '', '2');
INSERT INTO `pe_interface` VALUES ('678', '培训地点', '/entity/resource/placeResourceManage/**', '', '2');

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
  PRIMARY KEY (`ID`),
  KEY `CREATE_TIME_INDEX` (`CREATE_TIME`),
  KEY `enum_const_scope_type_fk` (`FLAG_SCOPE_TYPE`),
  KEY `enum_const_notice_type_fk` (`FLAG_NOTICE_TYPE`),
  KEY `send_user_fk` (`FK_SEND_USER`),
  CONSTRAINT `pe_notice_ibfk_1` FOREIGN KEY (`FLAG_NOTICE_TYPE`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `pe_notice_ibfk_2` FOREIGN KEY (`FLAG_SCOPE_TYPE`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `pe_notice_ibfk_3` FOREIGN KEY (`FK_SEND_USER`) REFERENCES `sso_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通知表';

-- ----------------------------
-- Records of pe_notice
-- ----------------------------

-- ----------------------------
-- Table structure for pe_pri_category
-- ----------------------------
DROP TABLE IF EXISTS `pe_pri_category`;
CREATE TABLE `pe_pri_category` (
  `ID` varchar(50) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL COMMENT '菜单名',
  `FK_PARENT_ID` varchar(50) DEFAULT NULL COMMENT '父菜单',
  `CODE` varchar(50) DEFAULT NULL COMMENT '代码',
  `PATH` text COMMENT '访问路径',
  `FLAG_LEFT_MENU` varchar(1) DEFAULT NULL COMMENT '0：包含下级菜单；1：带有url的菜单',
  `serial_Number` int(11) DEFAULT NULL COMMENT '序号',
  `isActive` varchar(1) DEFAULT NULL COMMENT '1：代表有效的；0：代表无效的',
  `LEVEL` int(2) DEFAULT NULL COMMENT '菜单级别',
  `fk_grid_id` varchar(50) DEFAULT NULL,
  `fk_web_site_id` varchar(50) NOT NULL,
  `icon` varchar(50) DEFAULT NULL,
  `fk_base_category_id` bigint(20) DEFAULT NULL,
  `base_id` varchar(50) DEFAULT NULL COMMENT '菜单身份id,用于确定不同站点菜单的对应关系',
  `show_in_left_menu` char(2) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_pepricategory_baseid_fkwebsiteid` (`base_id`,`fk_web_site_id`) USING BTREE,
  KEY `pe_pri_category_ibfk_01` (`fk_web_site_id`),
  CONSTRAINT `pe_pri_category_ibfk_1` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单信息';

-- ----------------------------
-- Records of pe_pri_category
-- ----------------------------
INSERT INTO `pe_pri_category` VALUES ('4028adef626fc93801626fe2477a0000', '站点管理', 'ff80808163cf81160163cf85d2750001', null, '', '1', '0', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '258', 'cecd63b0-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028adef626fc93801626fe29c2b0001', '路由管理', 'ff80808163cf81160163cf86961a0002', null, null, '0', '4', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, null, 'cecd67a5-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028adef626fc93801626fe2b7df0002', 'grid路由管理', '4028adef626fc93801626fe29c2b0001', null, null, '1', '0', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '259', 'cecd699b-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028adef633e990601633ea87ce30000', '基础功能管理', 'ff80808163cf81160163cf86961a0002', null, null, '1', '2', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '467', 'cecd6b53-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028adef633e990601633ea8aa950001', '接口管理', 'ff80808163cf81160163cf86961a0002', null, null, '1', '1', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '2', 'cecd6cf7-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028adef63d8fd5a0163d93ad3a40000', '菜单是否有效设置', 'e5252b1826ac11e8ab1d001e671d0be8', null, null, '1', '10', '1', '2', null, '83356b76275211e8baa4fcaa140ebf84', null, '505', 'a980d7c54c6b48bda80f07218942e40b', '1');
INSERT INTO `pe_pri_category` VALUES ('4028adef66d249b40166e2aa93f70007', '常量管理', 'e5252b1826ac11e8ab1d001e671d0be8', null, null, '1', '10', '1', '2', null, '83356b76275211e8baa4fcaa140ebf84', null, '109', '425902aa7ecb4eb3bc977aa04aca7d46', '1');
INSERT INTO `pe_pri_category` VALUES ('4028adef670ae13b01670ae9f1be0000', '图表管理', 'e5252b1826ac11e8ab1d001e671d0be8', null, null, '1', '10', '1', '2', null, '83356b76275211e8baa4fcaa140ebf84', null, '116', '1d28e8d8f6914a9c82c290950055dbcf', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae3162fbab500162fbcbf1b90001', '打印模板管理', 'e5252b1826ac11e8ab1d001e671d0be8', null, null, '0', '3', '1', '2', null, '83356b76275211e8baa4fcaa140ebf84', null, null, 'cecd6e86-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae3162fbab500162fbcc8a780002', '打印模板管理', '4028ae3162fbab500162fbcbf1b90001', null, null, '1', '0', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '260', 'cecd700e-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae3162fbab500162fbcda9240003', '打印模板分组管理', '4028ae3162fbab500162fbcbf1b90001', null, null, '1', '1', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '261', 'cecd719a-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316304d87a01630507533a0000', '操作指导流程图设计', '4028ae31635d9fae01635da224a20000', 'operate-guide-manage', '', '1', '0', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '262', 'cecd731a-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae31634809110163481f11310001', '打印模板占位符管理', '4028ae3162fbab500162fbcbf1b90001', null, null, '1', '2', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '483', 'cecd74cc-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae31635d9fae01635da224a20000', '操作指导管理', 'e5252b1826ac11e8ab1d001e671d0be8', '操作指导管理', null, '0', '2', '1', '2', null, '83356b76275211e8baa4fcaa140ebf84', null, null, 'cecd7668-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae31635d9fae01635da3c8920001', '操作指导管理', '4028ae31635d9fae01635da224a20000', 'operate-guide-description-manage', '/superAdmin/operateGuide/operateGuideManage/abstractGrid?actionId=operateGuideManage', '1', '1', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '486', 'cecd7aa8-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316380afbc016380b1a1270000', '缓存清理', 'ff80808163cf81160163cf86961a0002', null, null, '1', '3', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '491', 'cecd7c40-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316560bb54016560e335580000', '平台数据处理', 'ff80808163cf81160163cf86961a0002', null, null, '1', '6', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '713', '10c9ea5dd956405ea4bdc982f2d333f1', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316854b75201685538a53c0000', '操作日志管理', 'ff80808163cf81160163cf86961a0002', null, null, '1', '7', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '924', '5dd9214abb9a4655934b6f7c0042181b', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316904a6d8016904ae25a50009', '定时任务调度管理', 'e5252b1826ac11e8ab1d001e671d0be8', null, null, '0', '9', '1', '2', null, '83356b76275211e8baa4fcaa140ebf84', null, null, '9fe9461c729d4b6fb6db3fedab00a030', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316904a6d8016904ae4eb0000a', '调度任务管理', '4028ae316904a6d8016904ae25a50009', null, null, '1', '0', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '951', '05d23231046e4029aa9d18c3c3f0d042', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316909c14c016909f07d7d0007', '调度时间管理', '4028ae316904a6d8016904ae4eb0000a', null, null, '1', '0', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '952', '72ee7a99877c4933b291fefc24ec9742', '0');
INSERT INTO `pe_pri_category` VALUES ('4028ae31690dd82901690de4c727000f', '调度记录查询', '4028ae316904a6d8016904ae25a50009', null, null, '1', '1', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '953', '25951d9c7f21469ea6357cbb68723d51', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae31694c6ac401694c7260f30006', '站点基础配置管理', 'ff80808163cf81160163cf85d2750001', null, null, '1', '4', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '967', '22a8882a3d084354a0e850ba944acd27', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316976612b0169766685150006', '微信模板消息管理', '4028ae3169954ce40169955d0ce2000c', null, null, '0', '1', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, null, 'c28b54263ec14e1cbba07e31a7621186', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316976612b01697666bd390007', '微信模板消息组管理', '4028ae316976612b0169766685150006', null, null, '1', '0', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '968', '7eb2fe3246cf40ba8bd6eeabedcb5db4', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316976612b0169766df2960011', '微信模板消息字段管理', '4028ae316976612b0169766685150006', null, null, '1', '1', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '969', '332807e26d084f9fb12094ff426ba344', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316976612b0169767324420018', '微信模板消息站点管理', '4028ae316976612b0169766685150006', null, null, '1', '2', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '970', '1794763d69f4427ab199db95554005bb', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae31697b51b301697b5f8aa10010', '发送消息管理', '4028ae3169954ce40169955d0ce2000c', null, null, '0', '0', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, null, '71e7f1f7cc0b4bbd98c9e5a681c49503', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae31697b51b301697b5fbcdb0011', '发送消息组管理', '4028ae31697b51b301697b5f8aa10010', null, null, '1', '0', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '971', '1cc2353c418245f9a26d7e15398d868d', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae31697b51b301697b5ffaea0012', '发送消息类型管理', '4028ae31697b51b301697b5f8aa10010', null, null, '1', '1', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '972', '435309e4a95f4f5299ad6f5457c1691b', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae31697b51b301697b60277a0013', '发送消息站点管理', '4028ae31697b51b301697b5f8aa10010', null, null, '1', '2', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '973', 'cfe935b1e50045f1a8a47bac0f075fcd', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae3169808ca90169809bc100000e', '定制配置管理', 'ff80808163cf81160163cf86961a0002', null, null, '1', '8', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '974', '43f526c2db1c4a54aaf0d7e07c8aa642', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae3169808ca90169809bf8a9000f', '定制配置与Url关联管理', '4028ae3169808ca90169809bc100000e', null, null, '1', '0', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '975', 'a330d59d11874f58a5edcf2844a03bc2', '0');
INSERT INTO `pe_pri_category` VALUES ('4028ae3169808ca90169809c42660010', '定制配置与站点关联管理', '4028ae3169808ca90169809bc100000e', null, null, '1', '1', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '976', '5aa95140ba144eea8c8033805eba1872', '0');
INSERT INTO `pe_pri_category` VALUES ('4028ae3169954ce40169955d0ce2000c', '消息管理', 'e5252b1826ac11e8ab1d001e671d0be8', null, null, '0', '10', '1', '2', null, '83356b76275211e8baa4fcaa140ebf84', null, null, '562a5974178f49b3b44abd091a59d6ff', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae3169954ce40169955d413e000d', '邮件消息', '4028ae3169954ce40169955d0ce2000c', null, null, '0', '3', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, null, '98383090a8cc4209bf3f6cbd1c0f68b5', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae3169954ce40169955d6f59000e', '邮件消息组管理', '4028ae3169954ce40169955d413e000d', null, null, '1', '0', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '978', '9a07039139e14aecbb3e60f2ea5f08e8', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae3169954ce40169955da3da000f', '邮件消息与站点管理', '4028ae3169954ce40169955d413e000d', null, null, '1', '1', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '979', 'de78cdb4e42944c090704faf74ee6213', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316998c277016998cb031a000c', '短信消息管理', '4028ae3169954ce40169955d0ce2000c', null, null, '0', '4', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, null, 'a44994bf69bc42ab8bbe3bf95342b556', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316998c277016998cb434d000d', '短信消息组管理', '4028ae316998c277016998cb031a000c', null, null, '1', '0', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '980', 'e5fc8c4de28b4c4ab15ff54b19ed0f6f', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316998c277016998cbaf07000e', '短信消息与站点管理', '4028ae316998c277016998cb031a000c', null, null, '1', '1', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '981', 'b0d8636e5f144311a0eb429f0c3326ea', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0a7d6d016a0a882af70002', '数据字典管理', '4028ae316a0b55f9016a0b60fd6f001c', null, null, '0', '0', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, null, 'e4004972a44a404a96335e239815ff24', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0a7d6d016a0a885ff90003', '机构类型管理', '4028ae316a0a7d6d016a0a882af70002', null, null, '1', '0', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '992', '53c26289f6c34d39af2bebc27bae8f11', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b0fff016a0b1632e70003', '校区管理', '4028ae316a0a7d6d016a0a882af70002', null, null, '1', '1', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '993', '263fdb74c83145998d6e326bc0973d81', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b2a2a016a0b2e7e730002', '培训地区管理', '4028ae316a0a7d6d016a0a882af70002', null, null, '1', '2', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '994', 'f48bc2d34a40403481bcb12b8c1f32cb', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b2a2a016a0b44c1d40007', '培训类型管理', '4028ae316a0a7d6d016a0a882af70002', null, null, '1', '3', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '995', '6002688f1daf4555a00e1461206952e6', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b2a2a016a0b44f0910008', '培训对象管理', '4028ae316a0a7d6d016a0a882af70002', null, null, '1', '4', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '996', '67b65f6d307c4ef3a54e9c390cd3d42e', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b2a2a016a0b47ce01000b', '课程类型管理', '4028ae316a0a7d6d016a0a882af70002', null, null, '1', '5', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '997', '3160627359fe42cc830e01c3d60cee07', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b2a2a016a0b4b010a000e', '适合对象管理', '4028ae316a0a7d6d016a0a882af70002', null, null, '1', '6', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '998', '2aad49e3143e4ff3972d3effa30f9f21', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b2a2a016a0b4dc11e0011', '讲师职称管理', '4028ae316a0a7d6d016a0a882af70002', null, null, '1', '7', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '999', '9054ece3706549c39cc4f5f6cc5d457b', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b2a2a016a0b50750c0014', '最高学历管理', '4028ae316a0a7d6d016a0a882af70002', null, null, '1', '8', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '1000', '67f8c93a88bb42f68e883c2aa6450418', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b55f9016a0b60fd6f001c', '系统高级设置', null, null, null, '0', '0', '1', '1', null, '4028ae316a09ee85016a09f4d7640000', null, null, 'e05ac772280240ab81fcc756918ad6c6', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b55f9016a0b80846d0025', '单位管理', '4028ae316a0b55f9016a0b60fd6f001c', null, null, '1', '1', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '1001', 'f260c31524c2454aa5051e0d7cc4586f', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b8c30016a0b9570ad0023', '角色管理', '4028ae316a0b55f9016a0b60fd6f001c', null, null, '1', '2', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '1002', 'de1fa1160fa24acb9b926f4ad0900e8b', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b8c30016a0b9e58af0024', '角色菜单管理', '4028ae316a0b55f9016a0b60fd6f001c', null, '', '1', '3', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '1003', 'ff50cbce70104d09be088fecc3ce8046', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a0b8c30016a0ba830e40025', '用户管理', '4028ae316a0b55f9016a0b60fd6f001c', null, null, '1', '4', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '1005', '63f862cd30a24260bf93b0615c6f82a7', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a10bdef016a10c62ac60009', '资源管理', null, null, null, '0', '1', '1', '1', null, '4028ae316a09ee85016a09f4d7640000', null, null, 'ced7a206da43491090b4d1d08f07bcf6', '1');
INSERT INTO `pe_pri_category` VALUES ('4028ae316a10bdef016a10c64e53000a', '师资库', '4028ae316a10bdef016a10c62ac60009', null, null, '1', '0', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '1006', '46c432a79d644f58b0cbe57d97285ac3', '1');
INSERT INTO `pe_pri_category` VALUES ('4028aec7636cb08e01636cba20e80001', '站点超管管理', '4028adef626fc93801626fe2477a0000', null, '', '1', '0', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '479', 'cecd7e49-78e3-11e8-9e1d-fcaa140ebf84', '0');
INSERT INTO `pe_pri_category` VALUES ('4028aefc6a1fe358016a2442e24c0015', '课程库', '4028ae316a10bdef016a10c62ac60009', null, null, '1', '1', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '1007', 'b44b01a19ffb470d8478827392a4f393', '1');
INSERT INTO `pe_pri_category` VALUES ('4028aefc6a24c4fa016a24eb1e780000', '合作宾馆', '4028ae316a10bdef016a10c62ac60009', null, null, '1', '2', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '1008', '6f90eab151624628b68e1c167166c19f', '1');
INSERT INTO `pe_pri_category` VALUES ('4028aefc6a24ecc7016a24f671970000', '培训地点', '4028ae316a10bdef016a10c62ac60009', null, null, '1', '3', '1', '2', null, '4028ae316a09ee85016a09f4d7640000', null, '1009', 'c4f8eb470b2a47ffa1609eb1db874ec0', '1');
INSERT INTO `pe_pri_category` VALUES ('59a349be0d9440ed811a6e3df2d866d2', '基础功能接口管理', '4028adef633e990601633ea87ce30000', null, null, '1', '0', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '528', '528', '0');
INSERT INTO `pe_pri_category` VALUES ('848131b0e7004573a8e4a21cc97e53ab', '二级页面集合', null, null, null, '0', '2', '0', '1', null, '83356b76275211e8baa4fcaa140ebf84', null, null, 'dfdfe16c2d1145d3958bd58362f5a5fb', '0');
INSERT INTO `pe_pri_category` VALUES ('e5252b1826ac11e8ab1d001e671d0be8', '超管', null, null, null, '0', '2', '1', '1', null, '83356b76275211e8baa4fcaa140ebf84', null, null, 'cecd7fc7-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('e532421026ac11e8ab1d001e671d0be8', 'grid管理', 'ff80808163cf81160163cf86961a0002', null, '', '1', '0', '1', '2', null, '83356b76275211e8baa4fcaa140ebf84', null, '456', 'cecd8154-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('e54a51f526ac11e8ab1d001e671d0be8', '角色菜单管理', 'ff80808163cf81160163cf85d2750001', null, null, '1', '3', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '264', 'cecd82d6-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('e55fdd6f26ac11e8ab1d001e671d0be8', '平台菜单管理', 'ff80808163cf81160163cf85d2750001', null, null, '1', '1', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '265', 'cecd8453-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('e57264de26ac11e8ab1d001e671d0be8', '用户管理', 'e5252b1826ac11e8ab1d001e671d0be8', null, null, '1', '4', '1', '2', null, '83356b76275211e8baa4fcaa140ebf84', null, '266', 'ced34adc-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('e57f056726ac11e8ab1d001e671d0be8', '角色管理', 'e5252b1826ac11e8ab1d001e671d0be8', null, null, '1', '5', '1', '2', null, '83356b76275211e8baa4fcaa140ebf84', null, '267', 'ced34d5c-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('ff80808163cf81160163cf857e340000', '站点角色管理', 'ff80808163cf81160163cf85d2750001', null, '', '1', '2', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '495', 'ced34f30-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('ff80808163cf81160163cf85d2750001', '站点管理', 'e5252b1826ac11e8ab1d001e671d0be8', null, null, '0', '-1', '1', '2', null, '83356b76275211e8baa4fcaa140ebf84', null, null, 'ced350f4-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('ff80808163cf81160163cf86961a0002', '开发者选项', 'e5252b1826ac11e8ab1d001e671d0be8', null, null, '0', '0', '1', '2', null, '83356b76275211e8baa4fcaa140ebf84', null, null, 'ced352ae-78e3-11e8-9e1d-fcaa140ebf84', '1');
INSERT INTO `pe_pri_category` VALUES ('ff8080816557dcf501655b25880d0000', '站点配置管理', '4028adef626fc93801626fe2477a0000', null, null, '1', '1', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '673', '0ec73700319d4188bb5d9ce9d3cd0561', '0');
INSERT INTO `pe_pri_category` VALUES ('ff8080816557dcf501655b264ba20001', '自定义学号生成规则', '4028adef626fc93801626fe2477a0000', null, null, '1', '2', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '678', '3e05060da87c4ce690582ea3cc39eb96', '0');
INSERT INTO `pe_pri_category` VALUES ('ff8080816557dcf501655b2757ce0002', '自定义班号生成规则', '4028adef626fc93801626fe2477a0000', null, null, '1', '3', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '692', '2ac6396b874943e2b819b8e58f616d6b', '0');
INSERT INTO `pe_pri_category` VALUES ('ff8080816557dcf501655b2782360003', 'EnumConst管理', '4028adef626fc93801626fe2477a0000', null, null, '1', '4', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '674', 'c7e08ed6c83d46d58ce14cca8e02b516', '0');
INSERT INTO `pe_pri_category` VALUES ('ff8080816557dcf501655b27c2da0004', 'SystemVariables管理', '4028adef626fc93801626fe2477a0000', null, null, '1', '7', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '675', '5a9dafe4f762496f91dae1627cbbcd1e', '0');
INSERT INTO `pe_pri_category` VALUES ('ff8080816557dcf501655b27f63b0005', '成绩项管理', '4028adef626fc93801626fe2477a0000', null, null, '1', '5', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '676', 'bc07ff3ffdb24cea89850412f934f900', '0');
INSERT INTO `pe_pri_category` VALUES ('ff8080816557dcf501655b2850a90006', '自定义班级名称生成规则', '4028adef626fc93801626fe2477a0000', null, null, '1', '6', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '693', '92e9a1a5be464acf9c9026a25f2b5977', '0');
INSERT INTO `pe_pri_category` VALUES ('ff8080816557dcf501655b2884410007', '学生信息配置', '4028adef626fc93801626fe2477a0000', null, null, '1', '8', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '694', '6b7ed3d004ae4b8cbb336f7f2682de2c', '0');
INSERT INTO `pe_pri_category` VALUES ('ff80808165d779fe0165d79264550003', '站内信管理', '4028ae3169954ce40169955d0ce2000c', null, null, '0', '2', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, null, 'b47891f3c2124801a970944d0e890120', '1');
INSERT INTO `pe_pri_category` VALUES ('ff80808165d779fe0165d7929abd0004', '站内信组管理', 'ff80808165d779fe0165d79264550003', null, null, '1', '0', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '628', '0344cc07c57b40f1bf584fc423b2d474', '1');
INSERT INTO `pe_pri_category` VALUES ('ff80808165d779fe0165d792e51f0005', '站内信站点管理', 'ff80808165d779fe0165d79264550003', null, null, '1', '1', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '630', 'e62a59a4cfa04887be0cc6b5f7310ad4', '1');
INSERT INTO `pe_pri_category` VALUES ('ff80808165d779fe0165d79315f80006', '站内信占位符管理', 'ff80808165d779fe0165d7929abd0004', null, null, '1', '0', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '629', 'f48207fa217440288ec78795508e5040', '0');
INSERT INTO `pe_pri_category` VALUES ('ff80808165d783e80165d78adb460000', '工作室配置管理', '4028adef626fc93801626fe2477a0000', null, '', '1', '9', '1', '4', null, '83356b76275211e8baa4fcaa140ebf84', null, '620', '9da703e6e4414c3da775697833aec2de', '0');
INSERT INTO `pe_pri_category` VALUES ('ff8080816832aa550168365aa78d0006', '外接项目站点管理', 'ff80808163cf81160163cf86961a0002', null, '', '1', '6', '1', '3', null, '83356b76275211e8baa4fcaa140ebf84', null, '923', '35e9f63a1536447ea048753f3f895af9', '1');

-- ----------------------------
-- Table structure for pe_pri_role
-- ----------------------------
DROP TABLE IF EXISTS `pe_pri_role`;
CREATE TABLE `pe_pri_role` (
  `ID` varchar(50) NOT NULL DEFAULT '',
  `NAME` varchar(50) DEFAULT NULL COMMENT '角色名',
  `CODE` varchar(10) NOT NULL,
  `FLAG_ROLE_TYPE` varchar(50) DEFAULT NULL COMMENT '角色类型',
  `FLAG_BAK` varchar(50) DEFAULT NULL COMMENT '是否强制更新',
  `fk_web_site_id` varchar(50) NOT NULL,
  `flag_site_super_admin` varchar(50) DEFAULT NULL COMMENT '是否是站点超级管理员',
  `site_code` varchar(50) NOT NULL COMMENT '站点编号',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `uk_peprirole_code_fkwebsite_id` (`CODE`,`fk_web_site_id`) USING BTREE,
  KEY `FLAG_ROLE_TYPE` (`FLAG_ROLE_TYPE`) USING BTREE,
  KEY `FLAG_BAK` (`FLAG_BAK`) USING BTREE,
  KEY `pe_pri_role_ibfk_3` (`fk_web_site_id`),
  CONSTRAINT `pe_pri_role_ibfk_1` FOREIGN KEY (`FLAG_ROLE_TYPE`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `pe_pri_role_ibfk_2` FOREIGN KEY (`FLAG_BAK`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `pe_pri_role_ibfk_3` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色信息';

-- ----------------------------
-- Records of pe_pri_role
-- ----------------------------
INSERT INTO `pe_pri_role` VALUES ('05199dca0ac54a66bed0422b7dd7a642', '总站管理员', '3', '4028826a1db7bb01011db7e623d70003', null, '4028ae316a09ee85016a09f4d7640000', '46aa648f5f2611e89e1dfcaa140ebf84', 'training');
INSERT INTO `pe_pri_role` VALUES ('9ac92a164a984b40b09717597d7362eb', '站点管理员', '2', '84a5ac89-6883-11e8-9e1d-fcaa140ebf84', null, '4028ae316a09ee85016a09f4d7640000', '46aa648f5f2611e89e1dfcaa140ebf84', 'training');
INSERT INTO `pe_pri_role` VALUES ('f866dbb2f02d11e7ab1d001e671d0be8', '超级管理员', '1', 'd2470b3cf02d11e7ab1d001e671d0be8', null, '83356b76275211e8baa4fcaa140ebf84', null, 'control');

-- ----------------------------
-- Table structure for pe_schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `pe_schedule_job`;
CREATE TABLE `pe_schedule_job` (
  `ID` varchar(50) NOT NULL,
  `job_name` varchar(50) NOT NULL COMMENT '工作名称',
  `job_group` varchar(50) NOT NULL COMMENT '工作组名',
  `flag_job_valid` varchar(50) NOT NULL COMMENT '是否有效',
  `bean_class` varchar(150) DEFAULT NULL COMMENT '执行任务的类全限定名',
  `method_name` varchar(50) DEFAULT NULL COMMENT '执行任务的方法',
  `spring_id` varchar(50) DEFAULT NULL COMMENT '执行任务的springBean',
  `description` varchar(50) DEFAULT NULL COMMENT '任务描述',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `input_date` datetime DEFAULT NULL COMMENT '创建任务时间',
  `version` int(9) DEFAULT '1' COMMENT '版本号',
  `flag_is_show` varchar(50) DEFAULT NULL COMMENT '管理端是否可见',
  `flag_is_singleton` varchar(50) DEFAULT NULL COMMENT '是否分布式单例',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `schedule_job_name_unique` (`job_group`,`job_name`),
  KEY `FK2C63FFFDAD33341` (`flag_job_valid`),
  KEY `ID` (`ID`),
  CONSTRAINT `pe_schedule_job_ibfk_1` FOREIGN KEY (`flag_job_valid`) REFERENCES `enum_const` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pe_schedule_job
-- ----------------------------

-- ----------------------------
-- Table structure for pe_schedule_job_record
-- ----------------------------
DROP TABLE IF EXISTS `pe_schedule_job_record`;
CREATE TABLE `pe_schedule_job_record` (
  `id` varchar(50) NOT NULL,
  `job_name` varchar(50) DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(50) DEFAULT NULL COMMENT '任务组名',
  `trigger_name` varchar(50) DEFAULT NULL COMMENT '触发器名称',
  `trigger_group` varchar(50) DEFAULT NULL COMMENT '触发器组名',
  `flag_job_status` varchar(50) DEFAULT NULL COMMENT '执行状态',
  `plan_time` datetime DEFAULT NULL COMMENT '计划执行时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始执行时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束执行时间',
  `next_time` datetime DEFAULT NULL COMMENT '下次计划执行时间',
  `init_data` text,
  `exception_message` varchar(2000) DEFAULT NULL COMMENT '异常信息',
  `execute_machine` varchar(25) NOT NULL COMMENT '执行机器',
  PRIMARY KEY (`id`),
  UNIQUE KEY `schedule_record_time_unique` (`job_group`,`job_name`,`trigger_group`,`trigger_name`,`plan_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pe_schedule_job_record
-- ----------------------------

-- ----------------------------
-- Table structure for pe_schedule_trigger
-- ----------------------------
DROP TABLE IF EXISTS `pe_schedule_trigger`;
CREATE TABLE `pe_schedule_trigger` (
  `id` varchar(50) NOT NULL,
  `trigger_name` varchar(50) NOT NULL COMMENT '触发器名称',
  `trigger_group` varchar(50) DEFAULT NULL COMMENT '触发器组名',
  `fk_job_id` varchar(50) NOT NULL COMMENT '所属任务',
  `cron_expression` varchar(50) DEFAULT NULL,
  `data` text,
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `input_date` datetime DEFAULT NULL COMMENT '创建时间',
  `flag_job_valid` varchar(50) NOT NULL COMMENT '是否有效',
  `trigger_date` datetime DEFAULT NULL COMMENT '触发时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `schedule_trigger_name_unique` (`trigger_group`,`trigger_name`),
  KEY `trigger_job_status_index` (`flag_job_valid`),
  KEY `schedule_trigger_job_fk` (`fk_job_id`),
  CONSTRAINT `pe_schedule_trigger_ibfk_1` FOREIGN KEY (`fk_job_id`) REFERENCES `pe_schedule_job` (`ID`),
  CONSTRAINT `pe_schedule_trigger_ibfk_2` FOREIGN KEY (`flag_job_valid`) REFERENCES `enum_const` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pe_schedule_trigger
-- ----------------------------

-- ----------------------------
-- Table structure for pe_server_notice
-- ----------------------------
DROP TABLE IF EXISTS `pe_server_notice`;
CREATE TABLE `pe_server_notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` varchar(1000) NOT NULL,
  `publisher` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `type` char(1) NOT NULL,
  `site_code` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pe_server_notice
-- ----------------------------

-- ----------------------------
-- Table structure for pe_vue_router
-- ----------------------------
DROP TABLE IF EXISTS `pe_vue_router`;
CREATE TABLE `pe_vue_router` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `flag_router_type` varchar(50) NOT NULL COMMENT '路由类型',
  `router_config` varchar(1000) DEFAULT NULL COMMENT '路由配置, json格式',
  `fk_web_site_id` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `pe_vue_router_ibfk_01` (`flag_router_type`) USING BTREE,
  KEY `pe_vue_router_ibfk_02` (`fk_web_site_id`) USING BTREE,
  CONSTRAINT `pe_vue_router_ibfk_1` FOREIGN KEY (`flag_router_type`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `pe_vue_router_ibfk_2` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pe_vue_router
-- ----------------------------

-- ----------------------------
-- Table structure for pe_web_site
-- ----------------------------
DROP TABLE IF EXISTS `pe_web_site`;
CREATE TABLE `pe_web_site` (
  `id` varchar(50) NOT NULL,
  `code` varchar(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `domain` varchar(30) NOT NULL,
  `datasource_code` varchar(20) NOT NULL,
  `is_open_timer_statistisc` varchar(20) DEFAULT NULL,
  `active_status` tinyint(4) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `domain_alias` varchar(20) DEFAULT NULL,
  `app_id` varchar(50) DEFAULT NULL,
  `app_secret` varchar(50) DEFAULT NULL,
  `oauth_password` varchar(50) DEFAULT NULL,
  `sso_app_id` varchar(50) DEFAULT NULL COMMENT '用户中心的客户端id',
  `sso_app_secret` varchar(50) DEFAULT NULL COMMENT '用户中心的客户端密匙',
  `sso_base_path` varchar(50) DEFAULT NULL COMMENT '用户中心的basepath',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pe_web_site
-- ----------------------------
INSERT INTO `pe_web_site` VALUES ('4028ae316a09ee85016a09f4d7640000', 'training', '培训管理平台', 'training.webtrn.cn', 'training', null, '1', null, null, null, 'training.webtrn.cn', null, null, 'tycj_training', 'tycj_training_secret', 'http://training-ucenter.webtrn.cn/center');
INSERT INTO `pe_web_site` VALUES ('83356b76275211e8baa4fcaa140ebf84', 'control', '管理平台', 'control.training.webtrn.cn', 'control', null, '1', null, null, null, 'control.training.webtrn.cn', null, null, 'tycj_trainingcontrol', 'tycj_trainingcontrol_secret', 'http://trainingcontrol-ucenter.webtrn.cn/center');

-- ----------------------------
-- Table structure for pe_web_site_config
-- ----------------------------
DROP TABLE IF EXISTS `pe_web_site_config`;
CREATE TABLE `pe_web_site_config` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `flag_config_type` varchar(50) NOT NULL COMMENT '配置类型',
  `config` text NOT NULL COMMENT '配置内容',
  `fk_web_site_id` varchar(50) NOT NULL COMMENT '所属站点',
  `is_example` varchar(1) NOT NULL DEFAULT '0' COMMENT '是否是样例配置',
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `web_site_config_unique_index` (`flag_config_type`,`fk_web_site_id`) USING BTREE,
  KEY `web_site_config_site_index` (`fk_web_site_id`) USING BTREE,
  CONSTRAINT `pe_web_site_config_ibfk_1` FOREIGN KEY (`flag_config_type`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `pe_web_site_config_ibfk_2` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=281 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pe_web_site_config
-- ----------------------------
INSERT INTO `pe_web_site_config` VALUES ('276', '57d4184a3e2611e9a2c3fcaa140ebf84', '{\"url\": \"jdbc:mysql://192.168.46.81:3306/training_dev?useUnicode=true&characterEncoding=UTF-8&useOldAliasMetadataBehavior=true\", \"username\": \"root\", \"password\": \"whaty123\", \"driverName\": \"com.alibaba.druid.pool.DruidDataSource\", \"maxActive\": 100, \"initialSize\": 1, \"minIdle\": 1, \"maxWait\": 6000, \"maxOpenPreparedStatements\": 50, \"validationQuery\": \"select 1\", \"testWhileIdle\": true, \"timeBetweenEvictionRunsMillis\": 50000, \"testOnBorrow\": false, \"minEvictableIdleTimeMillis\": 60000, \"maxEvictableIdleTimeMillis\": 90000, \"useUnfairLock\": true, \"initMethod\": \"init\", \"destroyMethod\": \"close\", \"filters\": \"stat,log4j\"}', '4028ae316a09ee85016a09f4d7640000', '1', '2019-04-11 09:15:00', null);
INSERT INTO `pe_web_site_config` VALUES ('277', '57d731663e2611e9a2c3fcaa140ebf84', '{\"leanSpaceSiteCode\": \"cep\", \"isOpen\": false, \"domain\": \"cep.kfkc.webtrn.cn\", \"serviceUrl\": \"/learnspace/webService/learningSpaceWebService\", \"httpClientUrl\": \"/learnspace\"}', '4028ae316a09ee85016a09f4d7640000', '1', '2019-04-11 09:15:16', null);
INSERT INTO `pe_web_site_config` VALUES ('278', '57e354cf3e2611e9a2c3fcaa140ebf84', '{\"mailPassword\": \"aro2D4KtApM94tf4\", \"mailSmtp\": \"mail.webtrn.cn\", \"mailUser\": \"tycj@webtrn.cn\", \"registerOrSplit\": \"register\"}', '4028ae316a09ee85016a09f4d7640000', '1', '2019-04-11 09:15:34', null);
INSERT INTO `pe_web_site_config` VALUES ('279', '9ef0d1ee3ff211e9a2c3fcaa140ebf84', '{\"apiKey\": \"cep_platform\", \"isOpen\": false, \"privateKey\": \"MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIUH9KjhrasxCUmLu8sf1Ae6I0T/Zw=\", \"siteCode\": \"cep\", \"suffixUrl\": \"/sso/ssoLogin_studentLoginWithAttach.action\", \"domain\": \"http://cep.kaola.webtrn.cn\"}', '4028ae316a09ee85016a09f4d7640000', '1', '2019-04-11 09:16:19', '2019-04-11 09:25:21');
INSERT INTO `pe_web_site_config` VALUES ('280', '9ef57b0e3ff211e9a2c3fcaa140ebf84', '{\"signatureAuthKey\": \"Ub5eB6td@C)d~7e6UOz~2@MwwYfAuiXo\", \"templatesPath\": \"WEB-INF/classes/templates/\"}', '4028ae316a09ee85016a09f4d7640000', '1', '2019-04-11 09:24:49', null);

-- ----------------------------
-- Table structure for pe_web_site_detail
-- ----------------------------
DROP TABLE IF EXISTS `pe_web_site_detail`;
CREATE TABLE `pe_web_site_detail` (
  `id` varchar(50) NOT NULL DEFAULT '',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `pc_logo` varchar(255) DEFAULT NULL COMMENT '电脑端logo',
  `mobile_logo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of pe_web_site_detail
-- ----------------------------
INSERT INTO `pe_web_site_detail` VALUES ('4028ae316a09ee85016a09f4d7640000', '', '', '');
INSERT INTO `pe_web_site_detail` VALUES ('83356b76275211e8baa4fcaa140ebf84', '', 'incoming/public/83356b76275211e8baa4fcaa140ebf84/pcLogo.png', '');

-- ----------------------------
-- Table structure for pr_base_category_interface
-- ----------------------------
DROP TABLE IF EXISTS `pr_base_category_interface`;
CREATE TABLE `pr_base_category_interface` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fk_base_category_id` bigint(20) NOT NULL,
  `fk_interface_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_pr_bcategory_interface_ibfk_1` (`fk_interface_id`) USING BTREE,
  KEY `FK_pr_bcategory_interface_ibfk_2` (`fk_base_category_id`) USING BTREE,
  CONSTRAINT `pr_base_category_interface_ibfk_1` FOREIGN KEY (`fk_interface_id`) REFERENCES `pe_interface` (`id`),
  CONSTRAINT `pr_base_category_interface_ibfk_2` FOREIGN KEY (`fk_base_category_id`) REFERENCES `pe_base_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=762 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of pr_base_category_interface
-- ----------------------------
INSERT INTO `pr_base_category_interface` VALUES ('111', '456', '97');
INSERT INTO `pr_base_category_interface` VALUES ('155', '260', '156');
INSERT INTO `pr_base_category_interface` VALUES ('156', '261', '157');
INSERT INTO `pr_base_category_interface` VALUES ('157', '483', '158');
INSERT INTO `pr_base_category_interface` VALUES ('162', '495', '164');
INSERT INTO `pr_base_category_interface` VALUES ('208', '673', '318');
INSERT INTO `pr_base_category_interface` VALUES ('209', '673', '317');
INSERT INTO `pr_base_category_interface` VALUES ('210', '674', '317');
INSERT INTO `pr_base_category_interface` VALUES ('211', '675', '319');
INSERT INTO `pr_base_category_interface` VALUES ('212', '676', '320');
INSERT INTO `pr_base_category_interface` VALUES ('213', '678', '318');
INSERT INTO `pr_base_category_interface` VALUES ('214', '692', '318');
INSERT INTO `pr_base_category_interface` VALUES ('215', '693', '318');
INSERT INTO `pr_base_category_interface` VALUES ('216', '694', '318');
INSERT INTO `pr_base_category_interface` VALUES ('217', '694', '336');
INSERT INTO `pr_base_category_interface` VALUES ('218', '258', '337');
INSERT INTO `pr_base_category_interface` VALUES ('282', '620', '257');
INSERT INTO `pr_base_category_interface` VALUES ('292', '628', '264');
INSERT INTO `pr_base_category_interface` VALUES ('293', '629', '265');
INSERT INTO `pr_base_category_interface` VALUES ('294', '630', '266');
INSERT INTO `pr_base_category_interface` VALUES ('375', '713', '351');
INSERT INTO `pr_base_category_interface` VALUES ('673', '923', '611');
INSERT INTO `pr_base_category_interface` VALUES ('674', '924', '612');
INSERT INTO `pr_base_category_interface` VALUES ('703', '951', '634');
INSERT INTO `pr_base_category_interface` VALUES ('704', '952', '635');
INSERT INTO `pr_base_category_interface` VALUES ('705', '953', '636');
INSERT INTO `pr_base_category_interface` VALUES ('720', '967', '646');
INSERT INTO `pr_base_category_interface` VALUES ('721', '968', '647');
INSERT INTO `pr_base_category_interface` VALUES ('722', '969', '648');
INSERT INTO `pr_base_category_interface` VALUES ('723', '970', '649');
INSERT INTO `pr_base_category_interface` VALUES ('724', '971', '650');
INSERT INTO `pr_base_category_interface` VALUES ('725', '972', '651');
INSERT INTO `pr_base_category_interface` VALUES ('726', '973', '652');
INSERT INTO `pr_base_category_interface` VALUES ('727', '974', '653');
INSERT INTO `pr_base_category_interface` VALUES ('728', '975', '654');
INSERT INTO `pr_base_category_interface` VALUES ('729', '976', '655');
INSERT INTO `pr_base_category_interface` VALUES ('731', '978', '657');
INSERT INTO `pr_base_category_interface` VALUES ('732', '979', '658');
INSERT INTO `pr_base_category_interface` VALUES ('733', '980', '659');
INSERT INTO `pr_base_category_interface` VALUES ('734', '981', '660');
INSERT INTO `pr_base_category_interface` VALUES ('745', '992', '662');
INSERT INTO `pr_base_category_interface` VALUES ('746', '993', '663');
INSERT INTO `pr_base_category_interface` VALUES ('747', '994', '664');
INSERT INTO `pr_base_category_interface` VALUES ('748', '995', '665');
INSERT INTO `pr_base_category_interface` VALUES ('749', '996', '666');
INSERT INTO `pr_base_category_interface` VALUES ('750', '997', '667');
INSERT INTO `pr_base_category_interface` VALUES ('751', '998', '668');
INSERT INTO `pr_base_category_interface` VALUES ('752', '999', '669');
INSERT INTO `pr_base_category_interface` VALUES ('753', '1000', '670');
INSERT INTO `pr_base_category_interface` VALUES ('754', '1001', '671');
INSERT INTO `pr_base_category_interface` VALUES ('755', '1002', '672');
INSERT INTO `pr_base_category_interface` VALUES ('756', '1003', '673');
INSERT INTO `pr_base_category_interface` VALUES ('757', '1005', '674');
INSERT INTO `pr_base_category_interface` VALUES ('758', '1006', '675');
INSERT INTO `pr_base_category_interface` VALUES ('759', '1007', '676');
INSERT INTO `pr_base_category_interface` VALUES ('760', '1008', '677');
INSERT INTO `pr_base_category_interface` VALUES ('761', '1009', '678');

-- ----------------------------
-- Table structure for pr_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `pr_role_menu`;
CREATE TABLE `pr_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fk_role_id` varchar(50) NOT NULL,
  `fk_menu_id` varchar(50) NOT NULL,
  `fk_web_site_id` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `pr_role_menu_ibfk_01` (`fk_role_id`),
  KEY `pr_role_menu_ibfk_02` (`fk_menu_id`),
  KEY `pr_role_menu_ibfk_03` (`fk_web_site_id`),
  CONSTRAINT `pr_role_menu_ibfk_1` FOREIGN KEY (`fk_role_id`) REFERENCES `pe_pri_role` (`ID`),
  CONSTRAINT `pr_role_menu_ibfk_2` FOREIGN KEY (`fk_menu_id`) REFERENCES `pe_pri_category` (`ID`),
  CONSTRAINT `pr_role_menu_ibfk_3` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=185236 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pr_role_menu
-- ----------------------------
INSERT INTO `pr_role_menu` VALUES ('184846', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff80808163cf81160163cf85d2750001', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184847', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028adef626fc93801626fe2477a0000', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184848', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028aec7636cb08e01636cba20e80001', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184849', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff8080816557dcf501655b25880d0000', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184850', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff8080816557dcf501655b264ba20001', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184851', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff8080816557dcf501655b2757ce0002', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184852', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff8080816557dcf501655b2782360003', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184853', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff8080816557dcf501655b27f63b0005', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184854', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff8080816557dcf501655b2850a90006', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184855', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff8080816557dcf501655b27c2da0004', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184856', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff8080816557dcf501655b2884410007', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184857', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff80808165d783e80165d78adb460000', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184858', 'f866dbb2f02d11e7ab1d001e671d0be8', 'e55fdd6f26ac11e8ab1d001e671d0be8', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184859', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff80808163cf81160163cf857e340000', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184860', 'f866dbb2f02d11e7ab1d001e671d0be8', 'e54a51f526ac11e8ab1d001e671d0be8', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184861', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae31694c6ac401694c7260f30006', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184862', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff80808163cf81160163cf86961a0002', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184863', 'f866dbb2f02d11e7ab1d001e671d0be8', 'e532421026ac11e8ab1d001e671d0be8', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184864', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028adef633e990601633ea8aa950001', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184865', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028adef633e990601633ea87ce30000', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184866', 'f866dbb2f02d11e7ab1d001e671d0be8', '59a349be0d9440ed811a6e3df2d866d2', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184867', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316380afbc016380b1a1270000', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184868', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028adef626fc93801626fe29c2b0001', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184869', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028adef626fc93801626fe2b7df0002', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184870', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff8080816832aa550168365aa78d0006', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184871', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316560bb54016560e335580000', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184872', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316854b75201685538a53c0000', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184873', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae3169808ca90169809bc100000e', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184874', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae3169808ca90169809bf8a9000f', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184875', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae3169808ca90169809c42660010', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184876', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae31635d9fae01635da224a20000', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184877', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316304d87a01630507533a0000', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184878', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae31635d9fae01635da3c8920001', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184879', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae3162fbab500162fbcbf1b90001', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184880', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae3162fbab500162fbcc8a780002', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184881', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae3162fbab500162fbcda9240003', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184882', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae31634809110163481f11310001', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184883', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316904a6d8016904ae25a50009', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184884', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316904a6d8016904ae4eb0000a', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184885', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316909c14c016909f07d7d0007', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184886', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae31690dd82901690de4c727000f', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184887', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae3169954ce40169955d0ce2000c', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184888', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae31697b51b301697b5f8aa10010', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184889', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae31697b51b301697b5fbcdb0011', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184890', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae31697b51b301697b5ffaea0012', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184891', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae31697b51b301697b60277a0013', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184892', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316976612b0169766685150006', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184893', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316976612b01697666bd390007', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184894', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316976612b0169766df2960011', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184895', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316976612b0169767324420018', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184896', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff80808165d779fe0165d79264550003', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184897', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff80808165d779fe0165d7929abd0004', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184898', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff80808165d779fe0165d79315f80006', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184899', 'f866dbb2f02d11e7ab1d001e671d0be8', 'ff80808165d779fe0165d792e51f0005', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184900', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae3169954ce40169955d413e000d', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184901', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae3169954ce40169955d6f59000e', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184902', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae3169954ce40169955da3da000f', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184903', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316998c277016998cb031a000c', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184904', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316998c277016998cb434d000d', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184905', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028ae316998c277016998cbaf07000e', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('184906', 'f866dbb2f02d11e7ab1d001e671d0be8', '4028adef63d8fd5a0163d93ad3a40000', '83356b76275211e8baa4fcaa140ebf84');
INSERT INTO `pr_role_menu` VALUES ('185196', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b55f9016a0b60fd6f001c', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185197', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0a7d6d016a0a882af70002', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185198', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0a7d6d016a0a885ff90003', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185199', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b0fff016a0b1632e70003', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185200', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b2a2a016a0b2e7e730002', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185201', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b2a2a016a0b44c1d40007', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185202', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b2a2a016a0b44f0910008', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185203', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b2a2a016a0b47ce01000b', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185204', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b2a2a016a0b4b010a000e', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185205', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b2a2a016a0b4dc11e0011', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185206', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b2a2a016a0b50750c0014', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185207', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b55f9016a0b80846d0025', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185208', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b8c30016a0b9570ad0023', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185209', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b8c30016a0b9e58af0024', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185210', '9ac92a164a984b40b09717597d7362eb', '4028ae316a0b8c30016a0ba830e40025', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185211', '9ac92a164a984b40b09717597d7362eb', '4028ae316a10bdef016a10c62ac60009', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185212', '9ac92a164a984b40b09717597d7362eb', '4028ae316a10bdef016a10c64e53000a', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185213', '9ac92a164a984b40b09717597d7362eb', '4028aefc6a1fe358016a2442e24c0015', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185214', '9ac92a164a984b40b09717597d7362eb', '4028aefc6a24c4fa016a24eb1e780000', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185215', '9ac92a164a984b40b09717597d7362eb', '4028aefc6a24ecc7016a24f671970000', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185216', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b55f9016a0b60fd6f001c', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185217', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0a7d6d016a0a882af70002', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185218', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0a7d6d016a0a885ff90003', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185219', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b0fff016a0b1632e70003', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185220', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b2a2a016a0b2e7e730002', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185221', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b2a2a016a0b44c1d40007', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185222', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b2a2a016a0b44f0910008', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185223', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b2a2a016a0b47ce01000b', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185224', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b2a2a016a0b4b010a000e', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185225', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b2a2a016a0b4dc11e0011', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185226', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b2a2a016a0b50750c0014', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185227', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b55f9016a0b80846d0025', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185228', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b8c30016a0b9570ad0023', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185229', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b8c30016a0b9e58af0024', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185230', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a0b8c30016a0ba830e40025', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185231', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a10bdef016a10c62ac60009', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185232', '05199dca0ac54a66bed0422b7dd7a642', '4028ae316a10bdef016a10c64e53000a', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185233', '05199dca0ac54a66bed0422b7dd7a642', '4028aefc6a1fe358016a2442e24c0015', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185234', '05199dca0ac54a66bed0422b7dd7a642', '4028aefc6a24c4fa016a24eb1e780000', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `pr_role_menu` VALUES ('185235', '05199dca0ac54a66bed0422b7dd7a642', '4028aefc6a24ecc7016a24f671970000', '4028ae316a09ee85016a09f4d7640000');

-- ----------------------------
-- Table structure for pr_server_notice_user
-- ----------------------------
DROP TABLE IF EXISTS `pr_server_notice_user`;
CREATE TABLE `pr_server_notice_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fk_notice_id` bigint(20) NOT NULL,
  `fk_user_id` varchar(50) NOT NULL,
  `flag_is_read` char(1) NOT NULL,
  `flag_is_star` char(1) NOT NULL,
  `flag_is_delete` char(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `pr_server_notice_ibfk_1` (`fk_notice_id`),
  CONSTRAINT `pr_server_notice_user_ibfk_1` FOREIGN KEY (`fk_notice_id`) REFERENCES `pe_server_notice` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pr_server_notice_user
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
  KEY `notice_fk` (`FK_NOTICE_ID`),
  KEY `sso_user_fk` (`FK_SSO_USER_ID`),
  KEY `enum_const_readed_fk` (`FLAG_READED`),
  KEY `flag_is_star_fk` (`FLAG_IS_STAR`),
  CONSTRAINT `pr_user_notice_ibfk_1` FOREIGN KEY (`FLAG_READED`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `pr_user_notice_ibfk_2` FOREIGN KEY (`FK_NOTICE_ID`) REFERENCES `pe_notice` (`ID`),
  CONSTRAINT `pr_user_notice_ibfk_3` FOREIGN KEY (`FLAG_IS_STAR`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `pr_user_notice_ibfk_4` FOREIGN KEY (`FK_SSO_USER_ID`) REFERENCES `sso_user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通知用户关联表';

-- ----------------------------
-- Records of pr_user_notice
-- ----------------------------

-- ----------------------------
-- Table structure for send_message_group
-- ----------------------------
DROP TABLE IF EXISTS `send_message_group`;
CREATE TABLE `send_message_group` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(50) NOT NULL COMMENT '编号',
  `flag_active` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `send_message_group_name_index` (`name`) USING BTREE,
  UNIQUE KEY `send_message_group_code_index` (`code`) USING BTREE,
  KEY `send_message_group_active_fk` (`flag_active`),
  CONSTRAINT `send_message_group_ibfk_1` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of send_message_group
-- ----------------------------
INSERT INTO `send_message_group` VALUES ('12', '课程库', 'courseResourceManage', '4028809c1d625bcf011d66fd0dda0006');
INSERT INTO `send_message_group` VALUES ('13', '培训地点', 'placeResourceManage', '4028809c1d625bcf011d66fd0dda0006');
INSERT INTO `send_message_group` VALUES ('14', '合作宾馆', 'hotelResourceManage', '4028809c1d625bcf011d66fd0dda0006');

-- ----------------------------
-- Table structure for send_message_site
-- ----------------------------
DROP TABLE IF EXISTS `send_message_site`;
CREATE TABLE `send_message_site` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fk_send_message_type_id` int(9) NOT NULL COMMENT '关联发送消息类型的id',
  `fk_web_site_id` varchar(50) NOT NULL COMMENT '关联站点的id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `send_message_site_unique_index` (`fk_send_message_type_id`,`fk_web_site_id`) USING BTREE,
  KEY `send_message_site_index` (`fk_web_site_id`) USING BTREE,
  CONSTRAINT `send_message_site_ibfk_1` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`),
  CONSTRAINT `send_message_site_ibfk_2` FOREIGN KEY (`fk_send_message_type_id`) REFERENCES `send_message_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of send_message_site
-- ----------------------------

-- ----------------------------
-- Table structure for send_message_type
-- ----------------------------
DROP TABLE IF EXISTS `send_message_type`;
CREATE TABLE `send_message_type` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fk_send_message_group_id` int(9) NOT NULL COMMENT '所属消息组',
  `flag_message_type` varchar(50) NOT NULL COMMENT '消息类型',
  `message_code` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `send_message_type_unique_index` (`fk_send_message_group_id`,`flag_message_type`) USING BTREE,
  KEY `send_message_type_index` (`flag_message_type`) USING BTREE,
  KEY `send_message_type_message_id_index` (`message_code`),
  CONSTRAINT `send_message_type_ibfk_1` FOREIGN KEY (`flag_message_type`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `send_message_type_ibfk_2` FOREIGN KEY (`fk_send_message_group_id`) REFERENCES `send_message_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of send_message_type
-- ----------------------------

-- ----------------------------
-- Table structure for site_pay_info
-- ----------------------------
DROP TABLE IF EXISTS `site_pay_info`;
CREATE TABLE `site_pay_info` (
  `id` varchar(50) NOT NULL,
  `app_id` varchar(50) DEFAULT NULL,
  `alipay_secret_key` text COMMENT '支付宝应用私钥',
  `alipay_public_key` text COMMENT '支付宝应用公钥',
  `alipay_gateway` varchar(255) DEFAULT NULL COMMENT '支付宝网关',
  `wechat_mch_id` varchar(255) DEFAULT NULL COMMENT '微信商户号',
  `wechat_pay_url` varchar(255) DEFAULT NULL COMMENT '微信支付url',
  `wechat_close_url` varchar(255) DEFAULT NULL COMMENT '微信关闭订单地址',
  `alipay_return_url` varchar(255) DEFAULT NULL COMMENT '支付宝同步返回url',
  `alipay_notify_url` varchar(255) DEFAULT NULL COMMENT '支付宝服务器主动通知url',
  `wechat_api_key` varchar(255) DEFAULT NULL COMMENT '商户平台API密钥',
  `fk_web_site_id` varchar(50) NOT NULL COMMENT '站点id',
  `flag_pay_type` varchar(50) DEFAULT NULL COMMENT '支付方式',
  `wechat_notify_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_site_id` (`fk_web_site_id`) USING BTREE,
  KEY `fk_pay_site_info_2` (`flag_pay_type`),
  CONSTRAINT `site_pay_info_ibfk_1` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`),
  CONSTRAINT `site_pay_info_ibfk_2` FOREIGN KEY (`flag_pay_type`) REFERENCES `enum_const` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of site_pay_info
-- ----------------------------

-- ----------------------------
-- Table structure for sms_message_group
-- ----------------------------
DROP TABLE IF EXISTS `sms_message_group`;
CREATE TABLE `sms_message_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(50) NOT NULL COMMENT '编号',
  `data_sql` text NOT NULL COMMENT '查询手机号的sql',
  `flag_active` varchar(50) NOT NULL COMMENT '是否有效',
  `filter_sql` text COMMENT '过滤sql',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sms_message_group_code_unique` (`code`),
  KEY `sms_message_group_active_index` (`flag_active`) USING BTREE,
  CONSTRAINT `sms_message_group_ibfk_1` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sms_message_group
-- ----------------------------

-- ----------------------------
-- Table structure for sms_message_site
-- ----------------------------
DROP TABLE IF EXISTS `sms_message_site`;
CREATE TABLE `sms_message_site` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fk_web_site_id` varchar(50) NOT NULL,
  `fk_sms_message_group_id` int(9) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sms_message_site_site_fk` (`fk_web_site_id`,`fk_sms_message_group_id`) USING BTREE,
  KEY `sms_message_site_group_fk` (`fk_sms_message_group_id`),
  CONSTRAINT `sms_message_site_ibfk_1` FOREIGN KEY (`fk_sms_message_group_id`) REFERENCES `sms_message_group` (`id`),
  CONSTRAINT `sms_message_site_ibfk_2` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sms_message_site
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
  `site_code` varchar(20) DEFAULT NULL,
  `photo` varchar(50) DEFAULT NULL,
  `true_name` varchar(50) DEFAULT NULL,
  `mobilephone` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `nick_name` varchar(50) DEFAULT NULL,
  `flag_gender` varchar(50) DEFAULT NULL,
  `fk_web_site_id` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `LOGIN_ID` (`LOGIN_ID`) USING BTREE,
  KEY `FLAG_BAK` (`FLAG_BAK`) USING BTREE,
  KEY `FLAG_ISVALID` (`FLAG_ISVALID`) USING BTREE,
  KEY `FK_ROLE_ID` (`FK_ROLE_ID`) USING BTREE,
  KEY `sso_user_ibfk_4` (`flag_gender`) USING BTREE,
  CONSTRAINT `sso_user_ibfk_1` FOREIGN KEY (`FLAG_BAK`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `sso_user_ibfk_2` FOREIGN KEY (`FLAG_ISVALID`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `sso_user_ibfk_3` FOREIGN KEY (`FK_ROLE_ID`) REFERENCES `pe_pri_role` (`ID`),
  CONSTRAINT `sso_user_ibfk_4` FOREIGN KEY (`flag_gender`) REFERENCES `enum_const` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登陆信息表';

-- ----------------------------
-- Records of sso_user
-- ----------------------------
INSERT INTO `sso_user` VALUES ('9999', 'developAdmin', '431de4eb08257211a6fe4698f89a5db4', 'f866dbb2f02d11e7ab1d001e671d0be8', null, null, null, null, null, null, null, null, null, 'control', null, null, null, null, null, null, '83356b76275211e8baa4fcaa140ebf84');

-- ----------------------------
-- Table structure for station_message_group
-- ----------------------------
DROP TABLE IF EXISTS `station_message_group`;
CREATE TABLE `station_message_group` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '名称',
  `data_sql` text NOT NULL COMMENT '查询数据的 sql',
  `flag_active` varchar(50) NOT NULL COMMENT '是否有效',
  `code` varchar(20) NOT NULL COMMENT '编号',
  `filter_sql` text COMMENT '过滤sql',
  PRIMARY KEY (`id`),
  UNIQUE KEY `station_message_group_name_unique` (`name`),
  UNIQUE KEY `station_message_group_code_unique` (`code`),
  KEY `station_message_group_active_id` (`flag_active`),
  CONSTRAINT `station_message_group_ibfk_1` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of station_message_group
-- ----------------------------

-- ----------------------------
-- Table structure for station_message_group_sign
-- ----------------------------
DROP TABLE IF EXISTS `station_message_group_sign`;
CREATE TABLE `station_message_group_sign` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `fk_message_group_id` int(9) NOT NULL COMMENT '关联站内信组',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `sign` varchar(20) NOT NULL COMMENT '占位符',
  `flag_active` varchar(50) NOT NULL COMMENT '是否有效',
  `is_show` char(1) NOT NULL COMMENT '是否显示',
  `example` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `station_message_group_sign_active_id` (`flag_active`),
  KEY `station_message_group_sign_id` (`fk_message_group_id`),
  CONSTRAINT `station_message_group_sign_ibfk_1` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `station_message_group_sign_ibfk_2` FOREIGN KEY (`fk_message_group_id`) REFERENCES `station_message_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=211 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of station_message_group_sign
-- ----------------------------

-- ----------------------------
-- Table structure for station_message_group_site
-- ----------------------------
DROP TABLE IF EXISTS `station_message_group_site`;
CREATE TABLE `station_message_group_site` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `fk_message_group_id` int(9) NOT NULL COMMENT '关联站内信组',
  `fk_web_site_id` varchar(50) NOT NULL COMMENT '关联站点',
  `flag_active` varchar(50) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `station_message_group_site_id` (`fk_web_site_id`),
  KEY `station_message_group_site_active` (`flag_active`),
  KEY `station_message_site_message_group_id` (`fk_message_group_id`),
  CONSTRAINT `station_message_group_site_ibfk_1` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `station_message_group_site_ibfk_2` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`),
  CONSTRAINT `station_message_group_site_ibfk_3` FOREIGN KEY (`fk_message_group_id`) REFERENCES `station_message_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of station_message_group_site
-- ----------------------------

-- ----------------------------
-- Table structure for station_message_template
-- ----------------------------
DROP TABLE IF EXISTS `station_message_template`;
CREATE TABLE `station_message_template` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '名称',
  `fk_message_group_site_id` int(8) NOT NULL COMMENT '站内信组站点关联外键',
  `title` varchar(30) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `foot` varchar(30) NOT NULL COMMENT '脚注',
  `flag_active` varchar(50) NOT NULL COMMENT '是否有效',
  `use_number` int(8) NOT NULL COMMENT '使用次数',
  PRIMARY KEY (`id`),
  KEY `station_message_template_group_site_id` (`fk_message_group_site_id`),
  KEY `station_message_template_active_id` (`flag_active`),
  CONSTRAINT `station_message_template_ibfk_1` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`id`),
  CONSTRAINT `station_message_template_ibfk_2` FOREIGN KEY (`fk_message_group_site_id`) REFERENCES `station_message_group_site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of station_message_template
-- ----------------------------

-- ----------------------------
-- Table structure for system_custom_config
-- ----------------------------
DROP TABLE IF EXISTS `system_custom_config`;
CREATE TABLE `system_custom_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(50) NOT NULL COMMENT '编号',
  `type` varchar(10) NOT NULL COMMENT '配置属性类型',
  `default_value` varchar(10) NOT NULL COMMENT '默认值',
  `fk_base_category_id` bigint(20) DEFAULT NULL COMMENT '对应基础功能',
  `options` text COMMENT '选择类型选项',
  `note` text COMMENT '备注说明',
  PRIMARY KEY (`id`),
  KEY `fk_system_custom_category` (`fk_base_category_id`),
  CONSTRAINT `system_custom_config_ibfk_1` FOREIGN KEY (`fk_base_category_id`) REFERENCES `pe_base_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_custom_config
-- ----------------------------

-- ----------------------------
-- Table structure for system_custom_config_url
-- ----------------------------
DROP TABLE IF EXISTS `system_custom_config_url`;
CREATE TABLE `system_custom_config_url` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fk_system_custom_id` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `system_custom_config_url` (`url`),
  KEY `fk_system_custom_config` (`fk_system_custom_id`),
  CONSTRAINT `system_custom_config_url_ibfk_1` FOREIGN KEY (`fk_system_custom_id`) REFERENCES `system_custom_config` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_custom_config_url
-- ----------------------------

-- ----------------------------
-- Table structure for system_site_custom_config
-- ----------------------------
DROP TABLE IF EXISTS `system_site_custom_config`;
CREATE TABLE `system_site_custom_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fk_web_site_id` varchar(50) NOT NULL,
  `fk_system_custom_id` int(11) NOT NULL,
  `value` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_system_site_custom_config` (`fk_web_site_id`),
  KEY `fk_site_system_custom_config` (`fk_system_custom_id`),
  CONSTRAINT `system_site_custom_config_ibfk_1` FOREIGN KEY (`fk_system_custom_id`) REFERENCES `system_custom_config` (`id`),
  CONSTRAINT `system_site_custom_config_ibfk_2` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_site_custom_config
-- ----------------------------

-- ----------------------------
-- Table structure for user_grid_config
-- ----------------------------
DROP TABLE IF EXISTS `user_grid_config`;
CREATE TABLE `user_grid_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_key` varchar(50) NOT NULL COMMENT '用户表示，一般是loginId',
  `fk_grid_id` varchar(50) DEFAULT NULL COMMENT 'grid id',
  `url` varchar(500) DEFAULT NULL COMMENT 'grid完整路径',
  `name` varchar(30) NOT NULL COMMENT '配置名',
  `search_config` varchar(2000) DEFAULT NULL COMMENT '列表搜索参数配置',
  `order_config` varchar(2000) DEFAULT NULL COMMENT '列表排序配置',
  `column_hidden_config` varchar(2000) DEFAULT NULL COMMENT '列表隐藏列配置',
  `create_date` datetime NOT NULL,
  `update_date` datetime DEFAULT NULL,
  `fk_web_site_id` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_grid_config_ibfk_1` (`user_key`) USING BTREE,
  KEY `user_grid_config_ibfk_2` (`fk_grid_id`) USING BTREE,
  KEY `user_grid_config_ibfk_3` (`fk_web_site_id`) USING BTREE,
  CONSTRAINT `user_grid_config_ibfk_1` FOREIGN KEY (`fk_grid_id`) REFERENCES `grid_basic_config` (`id`),
  CONSTRAINT `user_grid_config_ibfk_2` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=250 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_grid_config
-- ----------------------------

-- ----------------------------
-- Table structure for user_self_category
-- ----------------------------
DROP TABLE IF EXISTS `user_self_category`;
CREATE TABLE `user_self_category` (
  `ID` varchar(50) NOT NULL,
  `FK_PRI_CAT_ID` varchar(50) DEFAULT NULL,
  `PARENT_ID` varchar(50) DEFAULT NULL,
  `SELFCODE` varchar(50) DEFAULT NULL,
  `SELFNAME` varchar(50) DEFAULT NULL,
  `USERID` varchar(50) DEFAULT NULL,
  `LEAF` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记忆：左侧菜单用户自定义信息';

-- ----------------------------
-- Records of user_self_category
-- ----------------------------

-- ----------------------------
-- Table structure for util_excel_table
-- ----------------------------
DROP TABLE IF EXISTS `util_excel_table`;
CREATE TABLE `util_excel_table` (
  `ID` varchar(50) NOT NULL,
  `row` int(11) DEFAULT NULL,
  `NAMESPACE` varchar(100) NOT NULL COMMENT '临时表命名空间',
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
  KEY `COLUMN1` (`COLUMN1`) USING BTREE,
  KEY `COLUMN2` (`COLUMN2`) USING BTREE,
  KEY `COLUMN3` (`COLUMN3`) USING BTREE,
  KEY `COLUMN4` (`COLUMN4`) USING BTREE,
  KEY `COLUMN5` (`COLUMN5`) USING BTREE,
  KEY `COLUMN6` (`COLUMN6`) USING BTREE,
  KEY `COLUMN7` (`COLUMN7`) USING BTREE,
  KEY `COLUMN8` (`COLUMN8`) USING BTREE,
  KEY `COLUMN9` (`COLUMN9`) USING BTREE,
  KEY `COLUMN10` (`COLUMN10`) USING BTREE,
  KEY `COLUMN11` (`COLUMN11`) USING BTREE,
  KEY `COLUMN12` (`COLUMN12`) USING BTREE,
  KEY `COLUMN13` (`COLUMN13`) USING BTREE,
  KEY `COLUMN14` (`COLUMN14`) USING BTREE,
  KEY `COLUMN15` (`COLUMN15`) USING BTREE,
  KEY `COLUMN16` (`COLUMN16`) USING BTREE,
  KEY `COLUMN17` (`COLUMN17`) USING BTREE,
  KEY `COLUMN18` (`COLUMN18`) USING BTREE,
  KEY `COLUMN19` (`COLUMN19`) USING BTREE,
  KEY `COLUMN20` (`COLUMN20`) USING BTREE,
  KEY `COLUMN21` (`COLUMN21`) USING BTREE,
  KEY `COLUMN22` (`COLUMN22`) USING BTREE,
  KEY `COLUMN23` (`COLUMN23`) USING BTREE,
  KEY `COLUMN24` (`COLUMN24`) USING BTREE,
  KEY `COLUMN25` (`COLUMN25`) USING BTREE,
  KEY `COLUMN26` (`COLUMN26`) USING BTREE,
  KEY `COLUMN27` (`COLUMN27`) USING BTREE,
  KEY `COLUMN28` (`COLUMN28`) USING BTREE,
  KEY `COLUMN29` (`COLUMN29`) USING BTREE,
  KEY `COLUMN30` (`COLUMN30`) USING BTREE,
  KEY `COLUMN31` (`COLUMN31`) USING BTREE,
  KEY `COLUMN32` (`COLUMN32`) USING BTREE,
  KEY `COLUMN33` (`COLUMN33`) USING BTREE,
  KEY `COLUMN34` (`COLUMN34`) USING BTREE,
  KEY `COLUMN35` (`COLUMN35`) USING BTREE,
  KEY `COLUMN36` (`COLUMN36`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sql操作辅助表|Excel导入辅助表';

-- ----------------------------
-- Records of util_excel_table
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_template_message_column
-- ----------------------------
DROP TABLE IF EXISTS `wechat_template_message_column`;
CREATE TABLE `wechat_template_message_column` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `label` varchar(50) NOT NULL COMMENT '说明文本',
  `sign` varchar(255) DEFAULT NULL COMMENT '对应微信模板字段',
  `is_dynamic` varchar(1) NOT NULL COMMENT '是否动态字段',
  `template_text` varchar(255) DEFAULT NULL COMMENT '动态字段的实例文本',
  `fk_template_group_id` int(9) NOT NULL COMMENT '所属模板',
  `flag_active` varchar(50) NOT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`),
  KEY `wechat_template_group_index` (`fk_template_group_id`),
  CONSTRAINT `wechat_template_message_column_ibfk_1` FOREIGN KEY (`fk_template_group_id`) REFERENCES `wechat_template_message_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_template_message_column
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_template_message_group
-- ----------------------------
DROP TABLE IF EXISTS `wechat_template_message_group`;
CREATE TABLE `wechat_template_message_group` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '模板组名称',
  `code` varchar(30) NOT NULL COMMENT '模板组编号',
  `data_sql` varchar(500) NOT NULL COMMENT '发送信息的查询sql，必须存在openId字段',
  `flag_active` varchar(50) NOT NULL COMMENT '是否有效',
  `filter_sql` text COMMENT '过滤sql',
  PRIMARY KEY (`id`),
  UNIQUE KEY `wechat_template_message_group_code_unique_index` (`code`),
  UNIQUE KEY `wechat_template_message_group_name_unique_index` (`name`),
  KEY `template_message_group_active` (`flag_active`),
  CONSTRAINT `wechat_template_message_group_ibfk_1` FOREIGN KEY (`flag_active`) REFERENCES `enum_const` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_template_message_group
-- ----------------------------

-- ----------------------------
-- Table structure for wechat_template_message_site
-- ----------------------------
DROP TABLE IF EXISTS `wechat_template_message_site`;
CREATE TABLE `wechat_template_message_site` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `fk_template_group_id` int(9) NOT NULL COMMENT '所属模板组',
  `fk_web_site_id` varchar(50) NOT NULL COMMENT '所属站点',
  `template_id` varchar(100) NOT NULL COMMENT '对应微信模板编号',
  PRIMARY KEY (`id`),
  KEY `wechat_template_message_site_group_index` (`fk_template_group_id`) USING BTREE,
  KEY `wechat_template_message_site_index` (`fk_web_site_id`) USING BTREE,
  CONSTRAINT `wechat_template_message_site_ibfk_1` FOREIGN KEY (`fk_web_site_id`) REFERENCES `pe_web_site` (`id`),
  CONSTRAINT `wechat_template_message_site_ibfk_2` FOREIGN KEY (`fk_template_group_id`) REFERENCES `wechat_template_message_group` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of wechat_template_message_site
-- ----------------------------
