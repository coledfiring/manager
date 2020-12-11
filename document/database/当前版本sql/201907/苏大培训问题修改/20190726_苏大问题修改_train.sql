ALTER TABLE `pe_student`
ADD COLUMN `enroll_fee`  decimal(7,2) NULL COMMENT '报名费';
-- 设置学生报名费默认为项目总费用
UPDATE
	pe_student stu
INNER JOIN training_item item ON item.id = stu.fk_training_item_id
INNER JOIN pe_unit unit ON unit.id = item.fk_unit_id
INNER JOIN enum_const enumConstByFlagFeeStatus ON enumConstByFlagFeeStatus .id = stu.flag_fee_status
INNER JOIN enum_const st ON st.id = stu.flag_student_status
SET stu.enroll_fee=item.total_fee
WHERE item.total_fee is not null;