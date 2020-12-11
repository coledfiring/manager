ALTER TABLE `class_course_timetable`
ADD COLUMN `flag_course_type`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程类型',
ADD INDEX `class_course_timetable_course_type_fk` (`flag_course_type`) USING BTREE ;
ALTER TABLE `class_course_timetable` ADD CONSTRAINT `class_course_timetable_course_type_fk` FOREIGN KEY (`flag_course_type`) REFERENCES `enum_const` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE `class_course_timetable`
ADD COLUMN `flag_place_type`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上课地点类型',
ADD INDEX `class_course_timetable_place_type_fk` (`flag_place_type`) USING BTREE ;
ALTER TABLE `class_course_timetable` ADD CONSTRAINT `class_course_timetable_place_type_fk` FOREIGN KEY (`flag_place_type`) REFERENCES `enum_const` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT;
