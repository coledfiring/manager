
-- 新增有效列
alter table pe_place add `flag_is_valid` varchar(50) null comment '是否有效'; 

-- 新增联系人列
alter table pe_class add `entrust_unit_linkman` varchar(50) null comment '联系人姓名'; 
alter table pe_class add `entrust_unit_link_phone` varchar(50) null comment '联系人电话'; 