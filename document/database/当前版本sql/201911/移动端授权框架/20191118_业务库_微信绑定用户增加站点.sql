SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE wechat_user ADD column site_code varchar(50) NOT NULL;
ALTER TABLE wechat_user ADD INDEX wechat_user_site_code_index(site_code);
UPDATE wechat_user wu inner join sso_user ss on ss.id = wu.fk_sso_user_id set wu.site_code = ss.site_code;