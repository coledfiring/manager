
UPDATE `grid_menu_config` SET `extend_menu_config`='{\"routerName\":\"addFaceTeachingClassCourseTimetable\",\"fieldName\":\"id\"}' WHERE (`fk_grid_id` = 'arrangeClassCourseTimetable' AND `text` = '添加课程表');

UPDATE `pe_base_category` SET `code`='add-face-teaching-class-course-timetable', `router_name`='addFaceTeachingClassCourseTimetable' WHERE (`name`='添加课程表');
