ALTER TABLE `mad_sys_policy` ADD COLUMN returned_quantity_ratio INT(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '流量返还比(值为返还流量点数+购买流量，理论上大于100)' AFTER limit_speed;

DROP TABLE IF EXISTS `mad_dict_layout_copy`;
CREATE TABLE `mad_dict_layout_copy` (
  `code` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '广告形式',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '广告形式名称',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*Data for the table `mad_dict_layout_copy` */
insert  into `mad_dict_layout_copy`(`code`,`name`) values (101,'横幅'),(102,'焦点图'),(111,'插屏'),(112,'全屏'),(121,'开屏图片'),(131,'开机图片'),(132,'关机图片'),(201,'前贴'),(202,'中贴'),(203,'后贴'),(211,'悬浮/暂停'),(221,'开机视频'),(222,'关机视频'),(231,'开屏视频'),(300,'图文信息流'),(311,'视频信息流');

DROP TABLE IF EXISTS `mad_dict_layout_mapping`;
CREATE TABLE `mad_dict_layout_mapping` (
  `terminal_type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '终端类型(1: Mobile, 2: OTT)',
  `ad_type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '广告类型(1: Banner, 2: Video, 3: Native)',
  `layout` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT 'Code',
  PRIMARY KEY (`terminal_type`,`layout`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*Data for the table `mad_dict_layout_mapping` */
insert  into `mad_dict_layout_mapping`(`terminal_type`,`ad_type`,`layout`) values (1,1,101),(1,1,102),(1,1,111),(1,1,112),(1,1,121),(1,2,201),(1,2,202),(1,2,203),(1,2,211),(1,3,231),(1,3,300),(1,3,311),(2,4,131),(2,4,132),(2,5,221),(2,5,222);

DROP TABLE IF EXISTS `mad_dict_adtype`;
CREATE TABLE `mad_dict_adtype` (
  `code` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '广告类型',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '广告类型名称',
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*Data for the table `mad_dict_adtype` */
insert  into `mad_dict_adtype`(`code`,`name`) values (1,'普通硬广'),(2,'OTV'),(3,'原生'),(4,'开关机图片'),(5,'开关机视频');
