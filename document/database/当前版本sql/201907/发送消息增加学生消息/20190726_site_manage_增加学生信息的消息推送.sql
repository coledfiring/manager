INSERT INTO `send_message_group` (`id`, `name`, `code`, `flag_active`) VALUES ('17', '学生信息', 'studentMessage', '4028809c1d625bcf011d66fd0dda0006');
INSERT INTO `send_message_type` (`id`, `fk_send_message_group_id`, `flag_message_type`, `message_code`) VALUES ('10', '17', '4028ae31697b6cc901697b6e5ecf000a', 'studentMessage');
INSERT INTO `send_message_type` (`id`, `fk_send_message_group_id`, `flag_message_type`, `message_code`) VALUES ('11', '17', '4028ae31697b6cc901697b6eacd1000a', 'studentMessage');
INSERT INTO `send_message_type` (`id`, `fk_send_message_group_id`, `flag_message_type`, `message_code`) VALUES ('12', '17', '4028ae3169903ce0016993ae1f2d000a', 'studentMessage');
INSERT INTO `send_message_type` (`id`, `fk_send_message_group_id`, `flag_message_type`, `message_code`) VALUES ('13', '17', '4028ae3169903ce0016993ae74d0000a', 'studentMessage');
INSERT INTO `send_message_site` (`fk_send_message_type_id`, `fk_web_site_id`) VALUES ('10', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `send_message_site` (`fk_send_message_type_id`, `fk_web_site_id`) VALUES ('11', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `send_message_site` (`fk_send_message_type_id`, `fk_web_site_id`) VALUES ('12', '4028ae316a09ee85016a09f4d7640000');
INSERT INTO `send_message_site` (`fk_send_message_type_id`, `fk_web_site_id`) VALUES ('13', '4028ae316a09ee85016a09f4d7640000');

INSERT INTO `sms_message_group` (`id`, `name`, `code`, `data_sql`, `flag_active`, `filter_sql`) VALUES ('3', '学生信息', 'studentMessage', 'select mobile from pe_student where MOBILE is not null and [ids|id]', '4028809c1d625bcf011d66fd0dda0006', 'select id from pe_student where mobile is not null and [ids|id]');
INSERT INTO `sms_message_site` (`id`, `fk_web_site_id`, `fk_sms_message_group_id`) VALUES ('3', '4028ae316a09ee85016a09f4d7640000', '3');

INSERT INTO `email_message_group` (`id`, `name`, `code`, `data_sql`, `flag_active`, `filter_sql`) VALUES ('3', '学生信息', 'studentMessage', 'select EMAIL from pe_student where email is not null AND [ids|id]', '4028809c1d625bcf011d66fd0dda0006', 'select EMAIL from pe_student where email is not null AND [ids|id]');
INSERT INTO `email_message_site` (`id`, `fk_web_site_id`, `fk_email_message_group_id`) VALUES ('13', '4028ae316a09ee85016a09f4d7640000', '3');

INSERT INTO `station_message_group` (`id`, `name`, `data_sql`, `flag_active`, `code`, `filter_sql`) VALUES ('4', '学生信息', 'SELECT\n	stu.true_name AS stuName,\n	stu.FK_SSO_USER_ID AS userId\nFROM\n	pe_student stu\nWHERE\n  [ids|stu.id]\n AND stu.FK_SSO_USER_ID IS NOT null;', '4028809c1d625bcf011d66fd0dda0006', 'studentMessage', 'SELECT\n	stu.id\nFROM\n	pe_student stu\nWHERE\n  [ids|stu.id]\n AND stu.FK_SSO_USER_ID IS NOT null');
INSERT INTO `station_message_group_site` (`id`, `fk_message_group_id`, `fk_web_site_id`, `flag_active`) VALUES ('3', '4', '4028ae316a09ee85016a09f4d7640000', '4028809c1d625bcf011d66fd0dda0006');

INSERT INTO `wechat_template_message_group` (`id`, `name`, `code`, `data_sql`, `flag_active`, `filter_sql`) VALUES ('3', '学生信息', 'studentMessage', 'SELECT\n	wu.openid AS openId\nFROM\n	pe_student man\nINNER JOIN wechat_user wu ON wu.fk_sso_user_id = man.FK_SSO_USER_ID\nWHERE\n	wu.openid IS NOT NULL\nAND [ids|man.id]', '4028809c1d625bcf011d66fd0dda0006', 'SELECT\n	wu.openid AS openId\nFROM\n	pe_student man\nINNER JOIN wechat_user wu ON wu.fk_sso_user_id = man.FK_SSO_USER_ID\nWHERE\n	wu.openid IS NOT NULL\nAND [ids|man.id]');
INSERT INTO `wechat_template_message_column` (`id`, `label`, `sign`, `is_dynamic`, `template_text`, `fk_template_group_id`, `flag_active`) VALUES ('9', '学校', 'keyword1', '0', 'XX大学（示例）', '3', '4028809c1d625bcf011d66fd0dda0006');
INSERT INTO `wechat_template_message_column` (`id`, `label`, `sign`, `is_dynamic`, `template_text`, `fk_template_group_id`, `flag_active`) VALUES ('10', '通知人', 'keyword2', '0', '王老师（示例）', '3', '4028809c1d625bcf011d66fd0dda0006');
INSERT INTO `wechat_template_message_column` (`id`, `label`, `sign`, `is_dynamic`, `template_text`, `fk_template_group_id`, `flag_active`) VALUES ('11', '时间', 'keyword3', '0', '2019年4月23(示例)', '3', '4028809c1d625bcf011d66fd0dda0006');
INSERT INTO `wechat_template_message_column` (`id`, `label`, `sign`, `is_dynamic`, `template_text`, `fk_template_group_id`, `flag_active`) VALUES ('12', '通知内容', 'keyword4', '0', '我校启用微信家校互动平台（示例）', '3', '4028809c1d625bcf011d66fd0dda0006');
INSERT INTO `wechat_template_message_site` (`id`, `fk_template_group_id`, `fk_web_site_id`, `template_id`) VALUES ('3', '3', '4028ae316a09ee85016a09f4d7640000', 'Ka7969aqM48Wqi0ZKG5k1rEVkc0HJYJrqw3froxF7lM');

UPDATE `pe_base_category` SET `code`='send-message-type-manage' WHERE `name`='发送消息类型管理';
INSERT INTO `grid_menu_config` (`fk_grid_id`, `flag_menu_type`, `flag_integrated_menu_type`, `text`, `show_type`, `column_type`, `fix`, `must_select_row`, `select_limit`, `data_index`, `null_text`, `show_confirm`, `confirm_text`, `url`, `extra_parameters`, `waiting_msg`, `success_msg`, `error_msg`, `open_mode`, `form_config`, `user_type`, `flag_is_valid`, `serial_number`, `script`, `column_data_index`, `value`, `tips`, `extend_menu_config`) VALUES ('studentSearch', 'a92817f1361611e8a37efcaa140ebf84', NULL, '发送消息', 'top', 'left', '0', '1', '0', 'id', '', '1', '', NULL, NULL, '', NULL, NULL, '_blank', NULL, NULL, '2', '1', NULL, NULL, NULL, '', '{\"routerName\": \"sendAllMessage\", \"fieldName\": \"parentId\", \"templateCode\": \"studentMessage\"}');
