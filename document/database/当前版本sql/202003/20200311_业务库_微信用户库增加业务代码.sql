ALTER TABLE wechat_user DROP FOREIGN KEY wechat_user_ibfk_1;
ALTER TABLE wechat_user ADD COLUMN business_code varchar(50) NOT NULL COMMENT '业务类型';
UPDATE wechat_user set business_code = 'teach';