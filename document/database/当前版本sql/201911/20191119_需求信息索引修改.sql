ALTER TABLE `requirement_info`
DROP INDEX `requirement_info_serial_unique` ,
ADD UNIQUE INDEX `requirement_info_serial_unique` (`serial`, `site_code`) USING BTREE ;