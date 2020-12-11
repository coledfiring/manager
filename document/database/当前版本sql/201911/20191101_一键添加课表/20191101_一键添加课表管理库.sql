INSERT INTO `pe_base_category` (`name`, `code`, `router_name`, `url`, `fk_grid_id`, `flag_isvalid`, `flag_is_system`, `fk_base_category_group`) VALUES ('添加课程表', 'add-class-course-timetable', 'addClassCourseTimetable', '', NULL, '2', '058cfaf74df211e8aac8fcaa140ebf84', '5');
INSERT INTO `pe_pri_category` (`NAME`, `FK_PARENT_ID`, `CODE`, `PATH`, `FLAG_LEFT_MENU`, `serial_Number`, `isActive`, `LEVEL`, `fk_grid_id`, `fk_web_site_id`, `icon`, `fk_base_category_id`, `base_id`, `show_in_left_menu`) VALUES ('添加课程表', '4028ae316aaf5a3f016aafd8a57d000a', NULL, NULL, '1', '0', '1', '4', NULL, '4028ae316a09ee85016a09f4d7640000', NULL, '1159', '77b47e32cda64377bf622692481b82e2', '0');
INSERT INTO `grid_menu_config` (`fk_grid_id`, `flag_menu_type`, `flag_integrated_menu_type`, `text`, `show_type`, `column_type`, `fix`, `must_select_row`, `select_limit`, `data_index`, `null_text`, `show_confirm`, `confirm_text`, `url`, `extra_parameters`, `waiting_msg`, `success_msg`, `error_msg`, `open_mode`, `form_config`, `user_type`, `flag_is_valid`, `serial_number`, `script`, `column_data_index`, `value`, `tips`, `extend_menu_config`) VALUES ('arrangeClassCourseTimetable', 'a92817f1361611e8a37efcaa140ebf84', NULL, '添加课程表', 'top', 'left', '0', '0', '1', 'id', '', '1', '', NULL, NULL, '', NULL, NULL, '_blank', NULL, NULL, '2', '7', NULL, NULL, NULL, '', '{\"routerName\":\"addClassCourseTimetable\",\"fieldName\":\"id\"}');
INSERT INTO `pe_pri_category` (
	`ID`,
	`NAME`,
	`FK_PARENT_ID`,
	`CODE`,
	`PATH`,
	`FLAG_LEFT_MENU`,
	`serial_Number`,
	`isActive`,
	`LEVEL`,
	`fk_grid_id`,
	`fk_web_site_id`,
	`icon`,
	`fk_base_category_id`,
	`base_id`,
	`show_in_left_menu`
) SELECT
	REPLACE (uuid(), '-', ''),
	'添加课程表',
	ppc.id,
	NULL,
	NULL,
	'1',
	'0',
	'1',
	'4',
	NULL,
	ps.id,
	NULL,
	pbc.id,
	'77b47e32cda64377bf622692481b82e2',
	'0'
FROM
	pe_web_site ps
INNER JOIN pe_pri_category ppc on ppc.base_id='10fd8fe3732345d3884e6219d106862f'
AND ppc.fk_web_site_id=ps.id
INNER JOIN pe_base_category pbc on pbc.name='添加课程表'
WHERE
	ps.CODE not in ('training','control');