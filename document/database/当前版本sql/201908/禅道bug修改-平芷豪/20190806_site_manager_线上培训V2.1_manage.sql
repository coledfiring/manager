UPDATE `grid_column_config` SET `ID`='4028ae316a61d982016a6204193e0014', `fk_action_grid_config_id`='classManage', `name`='班主任', `dataindex`='classMaster.name', `data_column`=NULL, `search`='1', `to_add`='1', `to_update`='1', `column_can_update`='0', `list`='1', `report`='1', `type`='TextField', `dateFormat`=NULL, `allow_blank`='1', `max_length`='50', `check_message`=NULL, `check_regular`=NULL, `text_field_parameters`=NULL, `combo_sql`='select tea.id, tea.true_name from pe_teacher tea inner join enum_const ic on ic.id = tea.flag_isclassmaster where ic.code = \'1\' AND tea.site_code = \'${siteCode}\' AND [peUnit|tea.fk_unit_id]', `serial_number`='20', `flag_active`='4028809c1d625bcf011d66fd0dda0006', `flag_bak`=NULL, `user_type`=NULL, `team`=NULL, `s_note`=NULL, `sql_result`=NULL, `is_html`=NULL, `default_text`=NULL, `cascade_columns`=NULL WHERE (`ID`='4028ae316a61d982016a6204193e0014');