ALTER TABLE pe_student
ADD COLUMN training_id  varchar(50) NULL COMMENT '培训id',
ADD INDEX index_training_id (training_id) USING BTREE ;

