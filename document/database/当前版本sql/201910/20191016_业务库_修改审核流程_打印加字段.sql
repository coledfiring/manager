ALTER TABLE `check_flow`
ADD COLUMN `scope_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程数据权限范围id';
ALTER TABLE `pe_printing`
ADD COLUMN `printing_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '印刷资料名称';