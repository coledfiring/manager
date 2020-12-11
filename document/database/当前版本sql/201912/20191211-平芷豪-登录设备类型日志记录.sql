ALTER TABLE sso_user_login CHANGE FLAG_LOTIN_TYPE FLAG_LOGIN_TYPE varchar(50);

INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) 
VALUES (REPLACE(UUID(),'-',''), 'PC', '1', 'FlagLoginType', '0', NOW(), 'pc端登录', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) 
VALUES (REPLACE(UUID(),'-',''), 'WeChat', '2', 'FlagLoginType', '0', NOW(), '微信登录', NULL);
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) 
VALUES (REPLACE(UUID(),'-',''), 'WeChatApp', '3', 'FlagLoginType', '0', NOW(), '微信小程序登录', NULL);