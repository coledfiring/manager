-- 重新创建唯一索引
ALTER TABLE training_item DROP INDEX training_item_unique_code_index;
CREATE UNIQUE INDEX training_item_unique_code_index ON training_item (`code`, `site_code`);