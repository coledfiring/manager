SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM pr_role_menu where fk_web_site_id <>'83356b76275211e8baa4fcaa140ebf84';
DELETE FROM pe_pri_role where fk_web_site_id not in ('83356b76275211e8baa4fcaa140ebf84','4028ae316a09ee85016a09f4d7640000','ff8080816e0063f8016e009d1c160002');
DELETE FROM pe_pri_category where fk_web_site_id not in ('83356b76275211e8baa4fcaa140ebf84','4028ae316a09ee85016a09f4d7640000','ff8080816e0063f8016e009d1c160002');
DELETE FROM operate_guide_description where fk_web_site_id not in ('83356b76275211e8baa4fcaa140ebf84','4028ae316a09ee85016a09f4d7640000','ff8080816e0063f8016e009d1c160002');
DELETE FROM grid_menu_permission where site_id <> '83356b76275211e8baa4fcaa140ebf84';
DELETE FROM wechat_template_message_site WHERE fk_web_site_id not in ('83356b76275211e8baa4fcaa140ebf84','4028ae316a09ee85016a09f4d7640000','ff8080816e0063f8016e009d1c160002');
DELETE FROM sms_message_site WHERE fk_web_site_id not in ('83356b76275211e8baa4fcaa140ebf84','4028ae316a09ee85016a09f4d7640000','ff8080816e0063f8016e009d1c160002');
DELETE FROM station_message_group_site WHERE fk_web_site_id not in ('83356b76275211e8baa4fcaa140ebf84','4028ae316a09ee85016a09f4d7640000','ff8080816e0063f8016e009d1c160002');
DELETE FROM email_message_site WHERE fk_web_site_id not in ('83356b76275211e8baa4fcaa140ebf84','4028ae316a09ee85016a09f4d7640000','ff8080816e0063f8016e009d1c160002');


DELETE FROM pe_web_site_detail where id not in ('83356b76275211e8baa4fcaa140ebf84','4028ae316a09ee85016a09f4d7640000','ff8080816e0063f8016e009d1c160002');
DELETE FROM pe_web_site_config where fk_web_site_id not in ('83356b76275211e8baa4fcaa140ebf84','4028ae316a09ee85016a09f4d7640000','ff8080816e0063f8016e009d1c160002');
DELETE FROM pe_web_site where id not in ('83356b76275211e8baa4fcaa140ebf84','4028ae316a09ee85016a09f4d7640000','ff8080816e0063f8016e009d1c160002');




