ALTER TABLE pe_bulletin ADD COLUMN site_code varchar(20) NOT NULL COMMENT '站点编号';
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('4028826a1e1bcbd0011e1bd8ab6b0004', '否', '0', 'FlagIstop', '1', '2008-12-09 00:00:00', '是否置顶', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('4028826a1e1bcbd0011e1bd94f3d0005', '是', '1', 'FlagIstop', '0', '2008-12-09 00:00:00', '是否置顶', NULL);
