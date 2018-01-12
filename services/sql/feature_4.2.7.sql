CREATE TABLE `mad_sys_media_whitelist` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `media_id` int(10) NOT NULL DEFAULT '0' COMMENT '媒体ID',
  `description` varchar(400) NOT NULL DEFAULT '' COMMENT '描述',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态(0:删除,1:正常）',
  `created_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建者',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新者',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `media_status` (`media_id`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
