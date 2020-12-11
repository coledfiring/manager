
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `pe_area`;
CREATE TABLE `pe_area` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '详细名称',
  `province` varchar(20) NOT NULL COMMENT '省/直辖市',
  `city` varchar(20) DEFAULT NULL COMMENT '市',
  `county` varchar(20) NOT NULL COMMENT '县/区',
  `zip_code` varchar(10) NOT NULL COMMENT '邮政编号',
  `area_code` varchar(10) NOT NULL COMMENT '电话区号',
  `create_by` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_by` varchar(50) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `site_code` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `area_unique_index` (`site_code`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

ALTER TABLE cooperate_unit drop foreign key cooperate_unit_fk_training_area;
UPDATE cooperate_unit set flag_training_area = null;
ALTER TABLE cooperate_unit CHANGE flag_training_area fk_area_id int(9);

ALTER TABLE entrusted_unit drop foreign key entrusted_unit_fk_training_area;
UPDATE entrusted_unit set flag_training_area = null;
ALTER TABLE entrusted_unit CHANGE flag_training_area fk_area_id int(9);