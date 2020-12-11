INSERT INTO `grid_basic_config` (`id`, `title`, `can_search`, `can_add`, `can_delete`, `can_update`, `can_batch_add`, `can_projections`, `first_search`, `entity_class`, `note`, `list_type`, `dc`, `flag_active`, `flag_bak`, `dateFormat`, `sqlstr`, `statistics`, `s_note`, `update_note`, `is_enum`, `countsql`, `isGroupSql`, `LIST_FUNCTION`, `FLAG_INTE_ACTIVE`, `fk_web_site_id`, `can_checked_all`, `add_mode`, `add_cmp_name`, `fk_chart_id`) VALUES ('olClassOnlineExamManage', 'ol班级在线考试管理', '1', NULL, NULL, NULL, NULL, '0', '0', 'com.whaty.domain.bean.online.OlClassOnlineExam', NULL, '0', 'OlClassOnlineExam;\nenumConstByFlagIsOpen;\nolPeClass,{siteCode:=${siteCode}};\nolPeCourse:left;', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, '4028ae316a09ee85016a09f4d7640000', '0', NULL, NULL, NULL);

INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('5b447045674a11ea80f4fcaa140ebf84', 'olClassOnlineExamManage', 'id', 'id', '', '0', '0', NULL, '0', '0', '0', 'TextField', '', '1', '100', '', '', NULL, '', '0', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('5b49b88b674a11ea80f4fcaa140ebf84', 'olClassOnlineExamManage', '班级名称', 'olPeClass.name', '', '1', '1', '1', '0', '1', '1', 'TextField', '', '0', '100', '', '', NULL, '', '1', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, '5b49becb674a11ea80f4fcaa140ebf84');
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('5b49bc1e674a11ea80f4fcaa140ebf84', 'olClassOnlineExamManage', '班级编号', 'olPeClass.code', '', '1', '0', '0', '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '2', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('5b49becb674a11ea80f4fcaa140ebf84', 'olClassOnlineExamManage', '课程', 'olPeCourse.name', '', '1', '1', '1', '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, 'SELECT\n	cou.id,\n  cou.name\nFROM\n	ol_class_course cc\nINNER JOIN ol_pe_class cl ON cl.id = cc.fk_class_id\nINNER JOIN ol_pe_course cou on cou.id=cc.fk_course_id\nWHERE cl.site_code = \'${siteCode}\'\nAND cc.fk_class_id=:olPeClass_name', '3', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('5b49c18e674a11ea80f4fcaa140ebf84', 'olClassOnlineExamManage', '是否开启', 'enumConstByFlagIsOpen.name', '', '1', '1', NULL, '0', '1', '1', 'TextField', '', '0', '100', '', '', NULL, '', '4', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('5b49c461674a11ea80f4fcaa140ebf84', 'olClassOnlineExamManage', '试卷编号', 'paperNo', '', '1', '1', NULL, '0', '1', '1', 'TextField', '', '0', '50', '', '', NULL, '', '5', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('5b49e4b2674a11ea80f4fcaa140ebf84', 'olClassOnlineExamManage', '开始时间', 'startTime', '', '1', '1', '1', '0', '1', '1', 'datetime', 'yyyy-MM-dd HH:mm:ss', '0', '100', '', '', NULL, '', '7', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('5b49ec28674a11ea80f4fcaa140ebf84', 'olClassOnlineExamManage', '结束时间', 'endTime', '', '1', '1', '1', '0', '1', '1', 'datetime', 'yyyy-MM-dd HH:mm:ss', '0', '100', '', '', NULL, '', '8', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('5b49f01b674a11ea80f4fcaa140ebf84', 'olClassOnlineExamManage', 'olPeClass.id', 'olPeClass.id', '', '0', '0', '0', '0', '0', '0', 'TextField', '', '1', '100', '', '', NULL, '', '99', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('5b49f415674a11ea80f4fcaa140ebf84', 'olClassOnlineExamManage', 'olPeCourse.id', 'olPeCourse.id', '', '0', '0', '0', '0', '0', '0', 'TextField', '', '1', '100', '', '', NULL, '', '99', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('5b49f83d674a11ea80f4fcaa140ebf84', 'olClassOnlineExamManage', 'enumConstByFlagIsOpen.id', 'enumConstByFlagIsOpen.id', '', '0', '0', NULL, '1', '0', '0', 'date', '', '1', '100', '', '', NULL, '', '99', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);

INSERT INTO `grid_menu_config` (`fk_grid_id`, `flag_menu_type`, `flag_integrated_menu_type`, `text`, `show_type`, `column_type`, `fix`, `must_select_row`, `select_limit`, `data_index`, `null_text`, `show_confirm`, `confirm_text`, `url`, `extra_parameters`, `waiting_msg`, `success_msg`, `error_msg`, `open_mode`, `form_config`, `user_type`, `flag_is_valid`, `serial_number`, `script`, `column_data_index`, `value`, `tips`, `extend_menu_config`) VALUES ('olClassOnlineExamManage', NULL, 'd54b3e85479a11e89697fcaa140ebf84', '添加', '', '', NULL, '0', '0', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', '1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `grid_menu_config` (`fk_grid_id`, `flag_menu_type`, `flag_integrated_menu_type`, `text`, `show_type`, `column_type`, `fix`, `must_select_row`, `select_limit`, `data_index`, `null_text`, `show_confirm`, `confirm_text`, `url`, `extra_parameters`, `waiting_msg`, `success_msg`, `error_msg`, `open_mode`, `form_config`, `user_type`, `flag_is_valid`, `serial_number`, `script`, `column_data_index`, `value`, `tips`, `extend_menu_config`) VALUES ('olClassOnlineExamManage', NULL, 'fc43396e479a11e89697fcaa140ebf84', '修改', '', '', NULL, '0', '0', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', '2', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `grid_menu_config` (`fk_grid_id`, `flag_menu_type`, `flag_integrated_menu_type`, `text`, `show_type`, `column_type`, `fix`, `must_select_row`, `select_limit`, `data_index`, `null_text`, `show_confirm`, `confirm_text`, `url`, `extra_parameters`, `waiting_msg`, `success_msg`, `error_msg`, `open_mode`, `form_config`, `user_type`, `flag_is_valid`, `serial_number`, `script`, `column_data_index`, `value`, `tips`, `extend_menu_config`) VALUES ('olClassOnlineExamManage', NULL, '030ebfea479b11e89697fcaa140ebf84', '删除', '', '', NULL, '0', '0', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', '3', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `grid_menu_config` (`fk_grid_id`, `flag_menu_type`, `flag_integrated_menu_type`, `text`, `show_type`, `column_type`, `fix`, `must_select_row`, `select_limit`, `data_index`, `null_text`, `show_confirm`, `confirm_text`, `url`, `extra_parameters`, `waiting_msg`, `success_msg`, `error_msg`, `open_mode`, `form_config`, `user_type`, `flag_is_valid`, `serial_number`, `script`, `column_data_index`, `value`, `tips`, `extend_menu_config`) VALUES ('olClassOnlineExamManage', NULL, 'f82ae75b479a11e89697fcaa140ebf84', '批量导入', '', '', NULL, '0', '0', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '3', '4', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `grid_menu_config` (`fk_grid_id`, `flag_menu_type`, `flag_integrated_menu_type`, `text`, `show_type`, `column_type`, `fix`, `must_select_row`, `select_limit`, `data_index`, `null_text`, `show_confirm`, `confirm_text`, `url`, `extra_parameters`, `waiting_msg`, `success_msg`, `error_msg`, `open_mode`, `form_config`, `user_type`, `flag_is_valid`, `serial_number`, `script`, `column_data_index`, `value`, `tips`, `extend_menu_config`) VALUES ('olClassOnlineExamManage', '4b808c1f071c11e89847fcaa140ebf84', NULL, '开启考试', 'top', 'left', NULL, '1', '0', 'id', '', '1', '', NULL, NULL, '', NULL, NULL, '_blank', NULL, NULL, '2', '1', NULL, 'enumConstByFlagIsOpen.id', 'd855f88a927e11e9a4b4fcaa140ebf84', '', '');
INSERT INTO `grid_menu_config` (`fk_grid_id`, `flag_menu_type`, `flag_integrated_menu_type`, `text`, `show_type`, `column_type`, `fix`, `must_select_row`, `select_limit`, `data_index`, `null_text`, `show_confirm`, `confirm_text`, `url`, `extra_parameters`, `waiting_msg`, `success_msg`, `error_msg`, `open_mode`, `form_config`, `user_type`, `flag_is_valid`, `serial_number`, `script`, `column_data_index`, `value`, `tips`, `extend_menu_config`) VALUES ('olClassOnlineExamManage', '4b808c1f071c11e89847fcaa140ebf84', NULL, '关闭考试', 'top', 'left', NULL, '1', '0', 'id', '', '1', '', NULL, NULL, '', NULL, NULL, '_blank', NULL, NULL, '2', '2', NULL, 'enumConstByFlagIsOpen.id', 'd85b0410927e11e9a4b4fcaa140ebf84', '', '');

INSERT INTO `grid_basic_config` (`id`, `title`, `can_search`, `can_add`, `can_delete`, `can_update`, `can_batch_add`, `can_projections`, `first_search`, `entity_class`, `note`, `list_type`, `dc`, `flag_active`, `flag_bak`, `dateFormat`, `sqlstr`, `statistics`, `s_note`, `update_note`, `is_enum`, `countsql`, `isGroupSql`, `LIST_FUNCTION`, `FLAG_INTE_ACTIVE`, `fk_web_site_id`, `can_checked_all`, `add_mode`, `add_cmp_name`, `fk_chart_id`) VALUES ('olOnlineExamScoreManage', 'ol在线考试成绩管理', '1', NULL, NULL, NULL, NULL, '0', '0', 'com.whaty.domain.bean.online.OlStudentOnlineExamScore', NULL, '1', 'StudentOnlineExamScore;\nclassOnlineExam,[peCourse];\npeStudent,[peClass;[trainingItem;[enumConstByFlagTrainingType]]];\nenumConstByFlagScorePublish;', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, 'SELECT\n  ifnull(sc.id,md5(concat(stu.id,exam.id))) as id,\n  cl.name as className,\n  cl.code as classCode,\n  cou.name as courseName,\n  exam.paper_no as paperNo,\n  stu.true_name as stuName,\n  stu.mobile as mobile,\n  sc.score as score,\nif(sc.id is null,\'否\',pub.name) as publishStatus\nFROM\nol_class_online_exam exam\nINNER JOIN ol_pe_class cl on cl.id=exam.fk_class_id and cl.site_code = \'${siteCode}\'\nINNER JOIN ol_pe_student stu ON stu.fk_class_id = exam.fk_class_id\nLEFT JOIN ol_pe_course cou on cou.id=exam.fk_course_id\nLEFT JOIN ol_student_online_exam_score sc on sc.id=md5(concat(exam.id,stu.id))\nLEFT JOIN enum_const pub on pub.id=sc.flag_score_publish', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, '4028ae316a09ee85016a09f4d7640000', '1', NULL, NULL, NULL);

INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('66314309674a11ea80f4fcaa140ebf84', 'olOnlineExamScoreManage', 'id', 'id', '', '0', '0', NULL, '0', '0', '0', 'TextField', '', '0', '100', '', '', NULL, '', '0', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('66314bef674a11ea80f4fcaa140ebf84', 'olOnlineExamScoreManage', '班级名称', 'combobox_olPeClass.className', '', '1', '0', '0', '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '3', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('66314e29674a11ea80f4fcaa140ebf84', 'olOnlineExamScoreManage', '班级编号', 'classCode', '', '1', '0', NULL, '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '4', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('66315081674a11ea80f4fcaa140ebf84', 'olOnlineExamScoreManage', '课程', 'combobox_olPeCourse.courseName', '', '1', '0', '0', '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '5', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('66315655674a11ea80f4fcaa140ebf84', 'olOnlineExamScoreManage', '试卷编号', 'paperNo', '', '1', '0', NULL, '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '6', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('663158d3674a11ea80f4fcaa140ebf84', 'olOnlineExamScoreManage', '姓名', 'stuName', '', '1', '0', NULL, '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '100', '', '', NULL, '', '7', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('6631604d674a11ea80f4fcaa140ebf84', 'olOnlineExamScoreManage', '手机号', 'mobile', '', '1', '0', NULL, '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '8', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('663162f9674a11ea80f4fcaa140ebf84', 'olOnlineExamScoreManage', '成绩', 'score', '', '1', '0', NULL, '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '9', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('66316601674a11ea80f4fcaa140ebf84', 'olOnlineExamScoreManage', '发布状态', 'combobox_enumConstByFlagScorePublish.publishStatus', '', '1', '0', NULL, '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '10', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);

INSERT INTO `grid_menu_config` (`fk_grid_id`, `flag_menu_type`, `flag_integrated_menu_type`, `text`, `show_type`, `column_type`, `fix`, `must_select_row`, `select_limit`, `data_index`, `null_text`, `show_confirm`, `confirm_text`, `url`, `extra_parameters`, `waiting_msg`, `success_msg`, `error_msg`, `open_mode`, `form_config`, `user_type`, `flag_is_valid`, `serial_number`, `script`, `column_data_index`, `value`, `tips`, `extend_menu_config`) VALUES ('olOnlineExamScoreManage', 'cb245573ff1a11e7ab1d001e671d0be8', NULL, '成绩发布', 'top', 'left', NULL, '1', '0', 'id', '', '1', '', '/entity/onlineExam/onlineExamScoreManage/publishStudentScore', NULL, '', NULL, NULL, '_blank', NULL, NULL, '2', '1', NULL, 'publishStatusId', '402880911da8c3b2011da8d5f4100009', '', '');
INSERT INTO `grid_menu_config` (`fk_grid_id`, `flag_menu_type`, `flag_integrated_menu_type`, `text`, `show_type`, `column_type`, `fix`, `must_select_row`, `select_limit`, `data_index`, `null_text`, `show_confirm`, `confirm_text`, `url`, `extra_parameters`, `waiting_msg`, `success_msg`, `error_msg`, `open_mode`, `form_config`, `user_type`, `flag_is_valid`, `serial_number`, `script`, `column_data_index`, `value`, `tips`, `extend_menu_config`) VALUES ('olOnlineExamScoreManage', 'cb245573ff1a11e7ab1d001e671d0be8', NULL, '取消发布', 'top', 'left', NULL, '1', '0', 'id', '', '1', '', '/entity/onlineExam/onlineExamScoreManage/cancelPublishStudentScore', NULL, '', NULL, NULL, '_blank', NULL, NULL, '2', '1', NULL, 'publishStatusId', '402880911da8c3b2011da8d590b30008', '', '');

INSERT INTO `grid_basic_config` (`id`, `title`, `can_search`, `can_add`, `can_delete`, `can_update`, `can_batch_add`, `can_projections`, `first_search`, `entity_class`, `note`, `list_type`, `dc`, `flag_active`, `flag_bak`, `dateFormat`, `sqlstr`, `statistics`, `s_note`, `update_note`, `is_enum`, `countsql`, `isGroupSql`, `LIST_FUNCTION`, `FLAG_INTE_ACTIVE`, `fk_web_site_id`, `can_checked_all`, `add_mode`, `add_cmp_name`, `fk_chart_id`) VALUES ('olOnlineExamScoreAnalyse', 'ol考试成绩统计', '1', NULL, NULL, NULL, NULL, '0', '0', 'com.whaty.domain.bean.online.OlStudentOnlineExamScore', NULL, '1', 'StudentOnlineExamScore;\nclassOnlineExam,[peCourse];\npeStudent,[peClass;[trainingItem;[enumConstByFlagTrainingType]]];\nenumConstByFlagScorePublish;', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, 'SELECT\n  exam.id as id,\n  cl.name as className,\n  cl.code as classCode,\n  cou.name as courseName,\n  exam.paper_no as paperNo,\n  count(stu.id) as stuCount,\n  count(if(sc.id is not null,1,null)) as scCount,\n  COUNT(if(sc.id is null,1,null)) as notScCount,\n  concat(ROUND(COUNT(if(sc.id is null,1,null))/count(stu.id)*100,1),\'%\') as abRate,\n  concat(ROUND(COUNT(if(sc.score<60,1,null))/count(stu.id)*100,1),\'%\') as notPassRate,\n  concat(ROUND(COUNT(if(sc.score>=60,1,null))/count(stu.id)*100,1),\'%\') as passRate\nFROM\nol_class_online_exam exam\nINNER JOIN ol_pe_class cl on cl.id=exam.fk_class_id and cl.site_code = \'${siteCode}\'\nINNER JOIN ol_pe_student stu ON stu.fk_class_id = exam.fk_class_id\nLEFT JOIN ol_pe_course cou on cou.id=exam.fk_course_id\nLEFT JOIN ol_student_online_exam_score sc on sc.fk_class_online_exam_id=exam.id\nAND sc.fk_student_id=stu.id\nLEFT JOIN enum_const pub on pub.id=sc.flag_score_publish\nGROUP BY exam.id', NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, '4028ae316a09ee85016a09f4d7640000', '1', NULL, NULL, NULL);

INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('595dfd5d674d11ea80f4fcaa140ebf84', 'olOnlineExamScoreAnalyse', '及格率', 'passRate', '', '1', '0', NULL, '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', NULL, '', '11', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, '', '', '', NULL, '', NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('595e010a674d11ea80f4fcaa140ebf84', 'olOnlineExamScoreAnalyse', '不及格率', 'notPassRate', '', '1', '0', NULL, '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', NULL, '', '12', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, '', '', '', NULL, '', NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('595e02f6674d11ea80f4fcaa140ebf84', 'olOnlineExamScoreAnalyse', 'id', 'id', '', '0', '0', NULL, '0', '0', '0', 'TextField', '', '0', '100', '', '', NULL, '', '0', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('595e079b674d11ea80f4fcaa140ebf84', 'olOnlineExamScoreAnalyse', '班级名称', 'combobox_olPeClass.className', '', '1', '0', '0', '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '3', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('595e090c674d11ea80f4fcaa140ebf84', 'olOnlineExamScoreAnalyse', '班级编号', 'classCode', '', '1', '0', NULL, '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '4', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('595e0a63674d11ea80f4fcaa140ebf84', 'olOnlineExamScoreAnalyse', '课程', 'combobox_olPeCourse.courseName', '', '1', '0', '0', '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '5', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('595e0bb2674d11ea80f4fcaa140ebf84', 'olOnlineExamScoreAnalyse', '试卷编号', 'paperNo', '', '1', '0', NULL, '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '6', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('595e0d04674d11ea80f4fcaa140ebf84', 'olOnlineExamScoreAnalyse', '总人数', 'stuCount', '', '1', '0', NULL, '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '100', '', '', NULL, '', '7', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('595e0e51674d11ea80f4fcaa140ebf84', 'olOnlineExamScoreAnalyse', '参考人数', 'scCount', '', '1', '0', NULL, '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '8', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('595e0f98674d11ea80f4fcaa140ebf84', 'olOnlineExamScoreAnalyse', '未考人数', 'notScCount', '', '1', '0', NULL, '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '9', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);
INSERT INTO `grid_column_config` (`ID`, `fk_action_grid_config_id`, `name`, `dataindex`, `data_column`, `search`, `to_add`, `to_update`, `column_can_update`, `list`, `report`, `type`, `dateFormat`, `allow_blank`, `max_length`, `check_message`, `check_regular`, `text_field_parameters`, `combo_sql`, `serial_number`, `flag_active`, `flag_bak`, `user_type`, `team`, `s_note`, `sql_result`, `is_html`, `default_text`, `cascade_columns`) VALUES ('595e10e1674d11ea80f4fcaa140ebf84', 'olOnlineExamScoreAnalyse', '未考率', 'abRate', '', '1', '0', NULL, '0', '1', '1', 'TextField', '', '1', '100', '', '', NULL, '', '10', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, NULL, NULL, '', '0', NULL, NULL);

INSERT INTO `pe_base_category` (`id`, `name`, `code`, `router_name`, `url`, `fk_grid_id`, `flag_isvalid`, `flag_is_system`, `fk_base_category_group`) VALUES ('ol在线考试成绩管理', '', '', '/entity/olTrain/olOnlineExamScoreManage/abstractGrid', 'olOnlineExamScoreManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '20');
INSERT INTO `pe_base_category` (`id`, `name`, `code`, `router_name`, `url`, `fk_grid_id`, `flag_isvalid`, `flag_is_system`, `fk_base_category_group`) VALUES ('ol班级在线考试管理', '', '', '/entity/olTrain/olClassOnlineExamManage/abstractGrid', 'olClassOnlineExamManage', '2', '058cfaf74df211e8aac8fcaa140ebf84', '20');
INSERT INTO `pe_base_category` (`id`, `name`, `code`, `router_name`, `url`, `fk_grid_id`, `flag_isvalid`, `flag_is_system`, `fk_base_category_group`) VALUES ('ol在线考试成绩统计', '', '', '/entity/olTrain/olOnlineExamScoreAnalyse/abstractGrid', 'olOnlineExamScoreAnalyse', '2', '0bc108f54df211e8aac8fcaa140ebf84', '20');

INSERT INTO `pe_interface` (`id`, `name`, `url`, `method`, `flag_isvalid`) VALUES ('ol班级在线考试管理', '/entity/olTrain/olClassOnlineExamManage/**', '', '2');
INSERT INTO `pe_interface` (`id`, `name`, `url`, `method`, `flag_isvalid`) VALUES ('ol在线考试成绩管理', '/entity/olTrain/olOnlineExamScoreManage/**', '', '2');
INSERT INTO `pe_interface` (`id`, `name`, `url`, `method`, `flag_isvalid`) VALUES ('ol在线考试成绩统计', '/entity/olTrain/olOnlineExamScoreAnalyse/**', '', '2');
