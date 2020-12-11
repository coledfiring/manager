SET FOREIGN_KEY_checks = 0;
ALTER TABLE analyse_config_detail DROP INDEX analyse_config_detail_config_fk;
ALTER TABLE analyse_config_detail ADD UNIQUE analyse_config_detail_config_fk(fk_basic_config_id);