CREATE TABLE `training_manager_task` (
  `id` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '任务名称',
  `flag_task_type` varchar(50) NOT NULL COMMENT '任务类型',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `charge_persons` varchar(500) DEFAULT NULL COMMENT '负责人',
  `copy_persons` varchar(500) DEFAULT NULL COMMENT '抄送人',
  `flag_priority_level` varchar(50) DEFAULT NULL COMMENT '优先级',
  `flag_notify_way` varchar(50) DEFAULT NULL COMMENT '推送方式',
  `note` varchar(255) DEFAULT NULL COMMENT '任务描述',
  `flag_training_task_status` varchar(50) DEFAULT NULL COMMENT '培训任务状态',
  `create_manager` varchar(50) DEFAULT NULL COMMENT '创建人',
  `site_code` varchar(50) NOT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_manager` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `training_manager_task_fk_task_type` (`flag_task_type`),
  KEY `training_manager_task_fk_priority_level` (`flag_priority_level`),
  KEY `training_manager_task_fk_notify_way` (`flag_notify_way`),
  KEY `training_manager_task_fk_training_task_status` (`flag_training_task_status`),
  KEY `training_manager_task_fk_create_manager` (`create_manager`),
  KEY `training_manager_task_fk_update_manager` (`update_manager`),
  CONSTRAINT `training_manager_task_fk_create_manager` FOREIGN KEY (`create_manager`) REFERENCES `pe_manager` (`id`),
  CONSTRAINT `training_manager_task_fk_notify_way` FOREIGN KEY (`flag_notify_way`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `training_manager_task_fk_priority_level` FOREIGN KEY (`flag_priority_level`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `training_manager_task_fk_task_type` FOREIGN KEY (`flag_task_type`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `training_manager_task_fk_training_task_status` FOREIGN KEY (`flag_training_task_status`) REFERENCES `enum_const` (`ID`),
  CONSTRAINT `training_manager_task_fk_update_manager` FOREIGN KEY (`update_manager`) REFERENCES `pe_manager` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('0498a37a35b011ea809dfcaa140ebf84', '个人工作', '1', 'FlagTaskType', '0', '2019-06-12 11:35:31', '任务类型', '');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('049dbb1935b011ea809dfcaa140ebf84', '催办任务', '0', 'FlagTaskType', '0', '2019-06-12 11:35:32', '任务类型', '');

INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('b79d044f367111ea809dfcaa140ebf84', '已完成', '1', 'FlagTrainingTaskStatus', '0', '2019-06-12 11:35:31', '任务状态', '');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('b7a1bc1c367111ea809dfcaa140ebf84', '未完成', '0', 'FlagTrainingTaskStatus', '0', '2019-06-12 11:35:32', '任务状态', '');

INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('04a22e2935b011ea809dfcaa140ebf84', '非常紧急', '2', 'FlagPriorityLevel', '0', '2019-06-12 11:35:31', '任务优先级', '');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('04a68fb135b011ea809dfcaa140ebf84', '紧急', '1', 'FlagPriorityLevel', '0', '2019-06-12 11:35:32', '任务优先级', '');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('04ad188335b011ea809dfcaa140ebf84', '普通', '0', 'FlagPriorityLevel', '0', '2019-06-12 11:35:32', '任务优先级', '');

INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('04b4a26635b011ea809dfcaa140ebf84', '短信', '1', 'FlagNotifyWay', '0', '2019-06-12 11:35:31', '任务通知方式', '');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('04b9d3fd35b011ea809dfcaa140ebf84', '微信', '2', 'FlagNotifyWay', '0', '2019-06-12 11:35:32', '任务通知方式', '');
INSERT INTO `enum_const` (`ID`, `NAME`, `CODE`, `NAMESPACE`, `IS_DEFAULT`, `CREATE_DATE`, `NOTE`, `TEAM`) VALUES ('04c020da35b011ea809dfcaa140ebf84', '站内信', '3', 'FlagNotifyWay', '0', '2019-06-12 11:35:32', '任务通知方式', '');
