ALTER TABLE class_course_timetable MODIFY COLUMN fk_course_time_id varchar(32) DEFAULT NULL;
ALTER TABLE class_course_timetable ADD COLUMN teacher_fee decimal(9, 2) DEFAULT NULL COMMENT '师资费用';
