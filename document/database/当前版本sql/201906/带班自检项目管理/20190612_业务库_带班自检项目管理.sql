ALTER TABLE prepare_item DROP INDEX prepare_item_unique_index;
ALTER TABLE prepare_item ADD UNIQUE INDEX prepare_item_unique_index(site_code, flag_training_type, flag_prepare_type, name);