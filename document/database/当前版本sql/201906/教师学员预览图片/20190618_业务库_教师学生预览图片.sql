ALTER TABLE pe_teacher ADD COLUMN flag_in_school varchar(50) DEFAULT NULL COMMENT '校内外';
ALTER TABLE pe_teacher ADD COLUMN picture_url varchar(255) DEFAULT NULL COMMENT '课程图片';
ALTER TABLE pe_teacher ADD CONSTRAINT teacher_is_school_fk FOREIGN KEY (flag_in_school) REFERENCES enum_const(id);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('4028ae316b69c8eb016b69d58f4a0000', '校内', '1', 'flagInSchool', '0', NULL, '教师校内校外', '');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('4028ae316b69c8eb016b69d5b8b40001', '校外', '2', 'flagInSchool', '0', NULL, '教师校内校外', '');
ALTER TABLE pe_student ADD picture_url varchar(255) DEFAULT NULL COMMENT '图片地址';