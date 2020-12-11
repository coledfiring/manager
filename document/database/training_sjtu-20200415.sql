ALTER TABLE `pe_student`
ADD COLUMN `flag_card_type`  varchar(50) NULL AFTER `original_work_type`,
ADD INDEX `index_enum_const_flag_card_type` (`flag_card_type`) USING BTREE ;

