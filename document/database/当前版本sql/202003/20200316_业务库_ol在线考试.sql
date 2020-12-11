CREATE TABLE `ol_class_online_exam` (
  `id` varchar(50) NOT NULL COMMENT '主键',
  `fk_class_id` varchar(50) NOT NULL COMMENT '班级id',
  `fk_course_id` varchar(50) DEFAULT NULL COMMENT '课程id',
  `paper_no` varchar(50) DEFAULT NULL COMMENT '试卷编号',
  `flag_is_open` varchar(255) DEFAULT NULL COMMENT '是否开启',
  `exam_date` datetime DEFAULT NULL COMMENT '考试日期',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_time` datetime DEFAULT NULL,
  `create_user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ol_student_online_exam_score` (
  `id` varchar(50) NOT NULL,
  `fk_class_online_exam_id` varchar(50) NOT NULL COMMENT '班级考试id',
  `fk_student_id` varchar(50) DEFAULT NULL COMMENT '学生id',
  `flag_score_publish` varchar(50) DEFAULT NULL COMMENT '发布状态',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `score` decimal(4,1) DEFAULT NULL COMMENT '分数',
  PRIMARY KEY (`id`),
  KEY `ol_student_online_exam_score_fk_exam_id` (`fk_class_online_exam_id`),
  KEY `ol_student_online_exam_score_fk_stu_id` (`fk_student_id`),
  CONSTRAINT `ol_student_online_exam_score_fk_exam_id` FOREIGN KEY (`fk_class_online_exam_id`) REFERENCES `ol_class_online_exam` (`id`),
  CONSTRAINT `ol_student_online_exam_score_fk_stu_id` FOREIGN KEY (`fk_student_id`) REFERENCES `ol_pe_student` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ol_student_online_exam_score_history` (
  `id` varchar(50) NOT NULL,
  `fk_exam_student_id` varchar(50) NOT NULL COMMENT '学生考试id',
  `jsonData` varchar(999) DEFAULT NULL COMMENT '回传内容',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `ol_pe_class`
ADD COLUMN `price`  decimal(7,2) NULL DEFAULT NULL COMMENT '价格' AFTER `create_date`,
ADD COLUMN `discount_price`  decimal(7,2) NULL DEFAULT NULL COMMENT '折扣价格' AFTER `price`;

ALTER TABLE `ol_pe_course`
ADD COLUMN `price`  decimal(7,2) NULL DEFAULT NULL COMMENT '价格' AFTER `credit`,
ADD COLUMN `discount_price`  decimal(7,2) NULL DEFAULT NULL COMMENT '折扣价格' AFTER `price`;

