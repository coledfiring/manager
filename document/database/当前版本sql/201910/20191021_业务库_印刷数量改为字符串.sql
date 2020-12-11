ALTER TABLE `pe_printing`
MODIFY COLUMN `printing_number`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '打印数量';