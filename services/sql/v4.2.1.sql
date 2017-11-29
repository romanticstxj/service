USE `premiummad_dev`;

/*Table structure for table `mad_dict_reqblocktype` */
DROP TABLE IF EXISTS `mad_dict_reqblocktype`;
CREATE TABLE `mad_dict_reqblocktype` (
  `code` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '黑名单流量的类型',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '黑名单流量的类型名称',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*Data for the table `mad_dict_reqblocktype` */
insert  into `mad_dict_reqblocktype`(`code`,`name`) values (1,'DID(IMEI)'),(2,'DPID(AndroidId/OpenUDID)'),(3,'IFA(AAID/DIFA)'),(4,'IP\r\n');

/*Table structure for table `mad_sys_reqblock` */
DROP TABLE IF EXISTS `mad_sys_reqblock`;
CREATE TABLE `mad_sys_reqblock` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT '设备ID或IP',
  `type` tinyint(3) NOT NULL DEFAULT '0' COMMENT '黑名单流量的类型',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态(0:停用,1:启用）',
  `description` varchar(400) DEFAULT '' COMMENT '黑名单设备的描述',
  `created_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建者',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新者',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `type_code` (`status`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;