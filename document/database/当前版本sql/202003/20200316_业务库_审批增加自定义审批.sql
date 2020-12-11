ALTER TABLE check_flow_node add column flag_node_type varchar(50) not null comment '审批节点类型';
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('b8355d45672f11ea80f4fcaa140ebf84', '指定成员', '1', 'flagNodeType', '0', '2020-03-16 10:42:03', '审批节点类型', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('b83b7998672f11ea80f4fcaa140ebf84', '发送者自定义', '2', 'flagNodeType', '0', '2020-03-16 10:42:04', '审批节点类型', NULL);
update check_flow_node set flag_node_type = 'b8355d45672f11ea80f4fcaa140ebf84';
ALTER TABLE check_flow_node MODIFY COLUMN flag_auditor_type varchar(50) DEFAULT NULL;
ALTER TABLE check_flow_node MODIFY COLUMN flag_check_type varchar(50) DEFAULT NULL;