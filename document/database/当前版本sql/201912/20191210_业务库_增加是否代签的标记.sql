ALTER TABLE class_sign_student ADD COLUMN flag_is_on_behalf varchar(32) default null comment '是否代签';
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('4193778c1af211eabd12fcaa140ebf84', '是', '1', 'FlagIsOnBehalf', '0', '2019-12-10 10:10:37', '是否代签', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('419db1fb1af211eabd12fcaa140ebf84', '否', '0', 'FlagIsOnBehalf', '0', '2019-12-10 10:10:37', '是否代签', NULL);
