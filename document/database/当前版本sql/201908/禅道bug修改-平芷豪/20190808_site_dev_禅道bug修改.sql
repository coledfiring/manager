-- 添加 数据面板-开班情况 横向权限
UPDATE `pe_chart_def` 
SET `chart_sql`=CONCAT("
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 5 MONTH), '%c月') as month, COUNT(item.id) as count FROM pe_class item 
	WHERE date_format(create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 5 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 4 MONTH), '%c月') as month, COUNT(item.id) as count FROM pe_class item 
	WHERE date_format(create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 4 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 3 MONTH), '%c月') as month, COUNT(item.id) as count FROM pe_class item 
	WHERE date_format(create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 3 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 2 MONTH), '%c月') as month, COUNT(item.id) as count FROM pe_class item 
	WHERE date_format(create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 2 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH), '%c月') as month, COUNT(item.id) as count FROM pe_class item 
	WHERE date_format(create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 0 MONTH), '%c月') as month, COUNT(item.id) as count FROM pe_class item 
	WHERE date_format(create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 0 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id]
")
WHERE `CODE`='3classCreateStatus';

-- 添加 数据面板-项目申报情况 横向权限
UPDATE `pe_chart_def` 
SET `chart_sql`=CONCAT("
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 5 MONTH), '%c月') as month, COUNT(item.id) as count FROM training_item item 
	WHERE date_format(create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 5 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 4 MONTH), '%c月') as month, COUNT(item.id) as count FROM training_item item 
	WHERE date_format(create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 4 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 3 MONTH), '%c月') as month, COUNT(item.id) as count FROM training_item item 
	WHERE date_format(create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 3 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 2 MONTH), '%c月') as month, COUNT(item.id) as count FROM training_item item 
	WHERE date_format(create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 2 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH), '%c月') as month, COUNT(item.id) as count FROM training_item item 
	WHERE date_format(create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 0 MONTH), '%c月') as month, COUNT(item.id) as count FROM training_item item 
	WHERE date_format(create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 0 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id]
")
WHERE `CODE`='2itemApplyStatus';

-- 添加 数据面板-培训学员情况 横向权限
UPDATE `pe_chart_def` 
SET `chart_sql`=CONCAT("
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 5 MONTH), '%c月') as month, COUNT(item.id) as count FROM pe_student item INNER JOIN pe_class cl ON cl.id = item.fk_class_id 
	WHERE date_format(item.create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 5 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|cl.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 4 MONTH), '%c月') as month, COUNT(item.id) as count FROM pe_student item INNER JOIN pe_class cl ON cl.id = item.fk_class_id 
	WHERE date_format(item.create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 4 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|cl.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 3 MONTH), '%c月') as month, COUNT(item.id) as count FROM pe_student item INNER JOIN pe_class cl ON cl.id = item.fk_class_id 
	WHERE date_format(item.create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 3 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|cl.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 2 MONTH), '%c月') as month, COUNT(item.id) as count FROM pe_student item INNER JOIN pe_class cl ON cl.id = item.fk_class_id 
	WHERE date_format(item.create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 2 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|cl.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH), '%c月') as month, COUNT(item.id) as count FROM pe_student item INNER JOIN pe_class cl ON cl.id = item.fk_class_id 
	WHERE date_format(item.create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 1 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|cl.fk_unit_id]
	UNION ALL 
	SELECT date_format(DATE_SUB(curdate(), INTERVAL 0 MONTH), '%c月') as month, COUNT(item.id) as count FROM pe_student item INNER JOIN pe_class cl ON cl.id = item.fk_class_id 
	WHERE date_format(item.create_date, '%Y %m') = date_format(DATE_SUB(curdate(), INTERVAL 0 MONTH),'%Y %m') and item.site_code = '",site_code,"' and [peUnit|cl.fk_unit_id]
")
WHERE `CODE`='4addStudentStatus';

-- 添加 数据面板-培训类型分布情况 横向权限
UPDATE `pe_chart_def` 
SET `chart_sql`=CONCAT("
	SELECT ft.name as type, count(item.id) as count 
	FROM training_item item 
	INNER JOIN enum_const ft ON item.flag_training_item_type = ft.id 
	where item.site_code = '",site_code,"' and [peUnit|item.fk_unit_id] 
	GROUP BY ft.id
")
WHERE `CODE`='5trainItemTypeStatus';



