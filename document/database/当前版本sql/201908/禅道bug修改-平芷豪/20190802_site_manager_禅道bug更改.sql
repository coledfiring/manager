UPDATE `grid_menu_config` SET `id`='30141', `fk_grid_id`='arrangeClassCourseTimetableManage', `flag_menu_type`='dc8475e8ff1a11e7ab1d001e671d0be8', `flag_integrated_menu_type`=NULL, `text`='安排培训地点', `show_type`='top', `column_type`='left', `fix`='0', `must_select_row`='1', `select_limit`='0', `data_index`='id', `null_text`='', `show_confirm`='1', `confirm_text`='', `url`=NULL, `extra_parameters`=NULL, `waiting_msg`='', `success_msg`=NULL, `error_msg`=NULL, `open_mode`='_blank', `form_config`='{formTitle: \'安排流动地点\', requestConfig: {url:\'/entity/clazz/arrangeClassCourseTimetableManage/setTrainingPlace\', waitingMsg:\'处理中，请稍候...\'}, formFieldList:[{name:\'placeId\', type:\'select\', labelText:\'请选择培训地点\', selectDataMode:\'local\', selectDataUrl:\'/common/comboBoxData/getIdNameData?bean=PePlace\'}]}', `user_type`=NULL, `flag_is_valid`='2', `serial_number`='1', `script`=NULL, `column_data_index`=NULL, `value`=NULL, `tips`='', `extend_menu_config`='' WHERE (`id`='30141');
UPDATE `grid_column_config` SET `ID`='4028ae316a6c3171016a6c374e280003', `fk_action_grid_config_id`='itemCompletionManage', `name`='培训班所属项目', `dataindex`='trainingItem.name', `data_column`='', `search`='1', `to_add`='0', `to_update`='0', `column_can_update`='0', `list`='1', `report`='1', `type`='TextField', `dateFormat`='yyyy-MM-dd', `allow_blank`='1', `max_length`='50', `check_message`='', `check_regular`='', `text_field_parameters`=NULL, `combo_sql`='SELECT\n	ti.id,\n	ti. NAME\nFROM\n	training_item ti\nINNER JOIN pe_class c on c.fk_training_item_id = ti.id\nINNER JOIN enum_const st ON st.id = ti.flag_item_status\nLEFT JOIN pe_unit unit ON unit.id = ti.fk_arrange_unit_id\nLEFT JOIN pe_manager manager ON manager.fk_unit_id = unit.id\nWHERE\n	ti.site_code = \'${siteCode}\'\nAND st. CODE = \'3\' \nAND [peUnit|c.fk_unit_id]\ngroup by ti.id', `serial_number`='3', `flag_active`='4028809c1d625bcf011d66fd0dda0006', `flag_bak`=NULL, `user_type`=NULL, `team`='', `s_note`='', `sql_result`='', `is_html`=NULL, `default_text`='', `cascade_columns`=NULL WHERE (`ID`='4028ae316a6c3171016a6c374e280003');