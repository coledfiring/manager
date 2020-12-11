UPDATE grid_basic_config
SET dc = 'PeStudent;{siteCode:=${siteCode}};enumConstByFlagGender:left;enumConstByFlagFolk:left;createBy;peClass;enumConstByFlagCardType:left;'
WHERE
	(id = 'classStudentManage');


INSERT INTO grid_column_config (ID, fk_action_grid_config_id, name, dataindex, data_column, search, to_add, to_update, column_can_update, list, report, type, dateFormat, allow_blank, max_length, check_message, check_regular, text_field_parameters, combo_sql, serial_number, flag_active, flag_bak, user_type, team, s_note, sql_result, is_html, default_text, cascade_columns) VALUES ('4028ae8e717d053c01717d0cb8300000', 'classStudentManage', '证件类型', 'enumConstByFlagCardType.name', '', '1', '1', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '0', '50', '', '', NULL, '', '4', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, '', '', '', NULL, '', NULL);
UPDATE grid_basic_config
SET 
 sqlstr = 'SELECT\n	stu.id AS id,\n	pu. NAME AS unitName,\n	ti. NAME AS trainingItem,\n	c. NAME AS className,\n	c. CODE AS classCode,\n	c.start_time AS startTime,\n	area. NAME AS area,\n	stu.true_name AS trueName,\n	ge. NAME AS gender,\n        stu.create_date AS createDate,\n	stu.certificate_number AS certificateNumber,\n	stu.work_unit AS workUnit,\n	stu.positional_title AS positionalTitle,\n	stu.mobile AS mobile,\n	stu.work_phone AS workPhone,\n	stu.email AS email,\n	fo. NAME AS folk,\n\nIF (\n	wu.openid IS NULL,\n	\'否\',\n	\'是\'\n) AS hasWeChat,\n       stu.picture_url as pictureUrl,stu.card_no cardNo, cardType.name cardTypeName\nFROM\n	pe_student stu\nINNER JOIN pe_class c ON c.id = stu.fk_class_id\nINNER JOIN training_item ti ON ti.id = c.fk_training_item_id\nLEFT JOIN entrusted_unit eunit ON eunit.id = ti.fk_entrusted_unit_id\nLEFT JOIN pe_area area ON area.id = eunit.fk_area_id\nLEFT JOIN enum_const cardType on cardType.id = stu.flag_card_type\nINNER JOIN pe_unit pu ON pu.id = c.fk_unit_id\nAND [peUnit|pu.id]\nLEFT JOIN enum_const ge ON ge.id = stu.flag_gender\nLEFT JOIN enum_const fo ON fo.id = stu.flag_folk\nLEFT JOIN wechat_user wu ON wu.fk_sso_user_id = stu.fk_sso_user_id\nWHERE\n	c.site_code = \'${siteCode}\'\ngroup by stu.id'
WHERE
	(id = 'studentSearch');


INSERT INTO grid_column_config (ID, fk_action_grid_config_id, name, dataindex, data_column, search, to_add, to_update, column_can_update, list, report, type, dateFormat, allow_blank, max_length, check_message, check_regular, text_field_parameters, combo_sql, serial_number, flag_active, flag_bak, user_type, team, s_note, sql_result, is_html, default_text, cascade_columns) VALUES ('4028ae8e717d323001717d3f223e000e', 'studentSearch', '证件号', 'cardNo', '', '1', '0', '0', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', NULL, '', '8', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, '', '', '', NULL, '', NULL);
INSERT INTO grid_column_config (ID, fk_action_grid_config_id, name, dataindex, data_column, search, to_add, to_update, column_can_update, list, report, type, dateFormat, allow_blank, max_length, check_message, check_regular, text_field_parameters, combo_sql, serial_number, flag_active, flag_bak, user_type, team, s_note, sql_result, is_html, default_text, cascade_columns) VALUES ('4028ae8e717d323001717d3f8ae8000f', 'studentSearch', '证件类型', 'combobox_enumConstByFlagCardType.cardTypeName', '', '1', '0', '1', '0', '1', '1', 'TextField', 'yyyy-MM-dd', '1', '50', '', '', NULL, '', '8', '4028809c1d625bcf011d66fd0dda0006', NULL, NULL, '', '', '', NULL, '', NULL);


INSERT INTO grid_menu_config ( fk_grid_id, flag_menu_type, flag_integrated_menu_type, text, show_type, column_type, fix, must_select_row, select_limit, data_index, null_text, show_confirm, confirm_text, url, extra_parameters, waiting_msg, success_msg, error_msg, open_mode, form_config, user_type, flag_is_valid, serial_number, script, column_data_index, VALUE, tips, extend_menu_config ) VALUES ( 'classStudentManage', 'd314862aff1a11e7ab1d001e671d0be8', NULL, '学员证书信息导出', 'top', 'left', '0', '1', '0', 'id', '', '0', '确定导出学员证书信息吗', '/entity/clazz/classStudentManage/exportStuCertificateInfo', NULL, '', NULL, NULL, '_blank', NULL, NULL, '2', '10', NULL, NULL, NULL, '', '' );




