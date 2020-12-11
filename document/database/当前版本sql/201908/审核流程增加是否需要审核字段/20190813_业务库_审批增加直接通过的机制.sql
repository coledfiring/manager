ALTER TABLE check_flow_group ADD COLUMN flag_need_check varchar(50) NOT NULL COMMENT '是否需要审核，为否可以申请时直接审批通过';
ALTER TABLE check_flow ADD COLUMN flag_need_check varchar(50) NOT NULL COMMENT '是否需要审核，为否可以申请时直接审批通过';
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('4028ae316c898a53016c89aafbec0000', '是', '1', 'flagNeedCheck', '0', NULL, '是否需要审核，为否可以申请时直接审批通过', '');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('4028ae316c898a53016c89ab30fd0001', '否', '0', 'flagNeedCheck', '0', NULL, '是否需要审核，为否可以申请时直接审批通过', '');
update check_flow_group set flag_need_check = '4028ae316c898a53016c89aafbec0000';
update check_flow set flag_need_check = '4028ae316c898a53016c89aafbec0000';