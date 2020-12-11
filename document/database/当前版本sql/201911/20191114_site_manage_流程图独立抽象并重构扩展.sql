
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for flow_config
-- ----------------------------
DROP TABLE IF EXISTS `flow_config`;
CREATE TABLE `flow_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `flow_config` text,
  PRIMARY KEY (`id`),
  KEY `name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
DROP TABLE operate_guide;
ALTER TABLE operate_guide_description DROP foreign key operate_guide_description_ibfk_2;
ALTER TABLE operate_guide_description DROP INDEX fk_operate_guide;
ALTER TABLE operate_guide_description change column fk_operate_guide_id fk_flow_config_id int(11);
UPDATE `grid_basic_config` SET `id`='operateGuideManage', `title`='操作指导管理', `can_search`='1', `can_add`='1', `can_delete`='1', `can_update`='1', `can_batch_add`='0', `can_projections`='1', `first_search`='0', `entity_class`='com.whaty.domain.bean.OperateGuideDescription', `note`=NULL, `list_type`='0', `dc`='OperateGuideDescription;flowConfig;enumConstByFlagActive;peWebSite;', `flag_active`='4028809c1d625bcf011d66fd0dda0006', `flag_bak`=NULL, `dateFormat`='yyyy-MM-dd', `sqlstr`=NULL, `statistics`=NULL, `s_note`=NULL, `update_note`=NULL, `is_enum`=NULL, `countsql`=NULL, `isGroupSql`=NULL, `LIST_FUNCTION`=NULL, `FLAG_INTE_ACTIVE`=NULL, `fk_web_site_id`='83356b76275211e8baa4fcaa140ebf84', `can_checked_all`='1', `add_mode`=NULL, `add_cmp_name`=NULL, `fk_chart_id`=NULL WHERE (`id`='operateGuideManage');
UPDATE `grid_column_config` SET `ID`='4028aed0635dba0501635dbb95df0001', `fk_action_grid_config_id`='operateGuideManage', `name`='关联操作流程图', `dataindex`='flowConfig.name', `data_column`='', `search`='1', `to_add`='1', `to_update`='1', `column_can_update`='1', `list`='1', `report`='1', `type`='TextField', `dateFormat`=NULL, `allow_blank`='0', `max_length`='50', `check_message`='', `check_regular`='', `text_field_parameters`=NULL, `combo_sql`='', `serial_number`='4', `flag_active`='4028809c1d625bcf011d66fd0dda0006', `flag_bak`=NULL, `user_type`=NULL, `team`='', `s_note`='', `sql_result`='', `is_html`=NULL, `default_text`=NULL, `cascade_columns`=NULL WHERE (`ID`='4028aed0635dba0501635dbb95df0001');
