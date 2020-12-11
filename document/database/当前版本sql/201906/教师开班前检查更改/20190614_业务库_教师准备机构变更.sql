ALTER TABLE teacher_prepare_item ADD COLUMN fk_class_id varchar(32) NOT NULL COMMENT '所属班级';
DELETE from teacher_prepare_item;
ALTER TABLE teacher_prepare_item ADD CONSTRAINT teacher_prepare_item_class_fk FOREIGN KEY(fk_class_id) REFERENCES pe_class(id);