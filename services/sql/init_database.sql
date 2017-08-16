/* 媒体数据权限表  */
DROP TABLE IF EXISTS `mad_sys_user_media`;
CREATE TABLE `mad_sys_user_media` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL DEFAULT '0',
  `media_id` int(10) NOT NULL DEFAULT '0',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态(0:删除,1:正常）',
  `created_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建者',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新者',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_status` (`user_id`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/* 策略数据权限表  */
DROP TABLE IF EXISTS `mad_sys_user_policy`;
CREATE TABLE `mad_sys_user_policy` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL DEFAULT '0',
  `policy_id` int(10) NOT NULL,
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态(0:删除,1:正常）',
  `created_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建者',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新者',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `user_status` (`user_id`,`status`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8


/* 媒体表 */
DROP TABLE IF EXISTS `mad_sys_media`;
CREATE TABLE `mad_sys_media` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '类型(1: APP, 2: WAP)',
  `category` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '分类',
  `access_type` tinyint(3) NOT NULL DEFAULT '1' COMMENT '对接方式(1: API, 2: SDK)',
  `api_type` tinyint(3) NOT NULL DEFAULT '1' COMMENT 'API类型',
  `advertiser_audit_mode` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '广告主审核方式(0: 不审核, 1: 平台审核, 2: 媒体审核)',
  `material_audit_mode` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '物料审核方式(0: 不审核, 1: 平台审核, 2: 媒体审核)',
  `timeout` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '超时时间',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态(0:停用,1:启用）',
  `description` varchar(400) DEFAULT NULL COMMENT '描述',
  `created_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建者',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新者',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `name` (`name`),
  KEY `status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8;


/* 广告位表 */
DROP TABLE IF EXISTS `mad_sys_adspace`;
CREATE TABLE `mad_sys_adspace` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `adspace_key` char(16) NOT NULL DEFAULT '' COMMENT '广告位Key',
  `media_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '所属媒体ID',
  `adblock_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '广告限制ID',
  `terminal_type` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '终端类型(1: Mobile, 2: OTT)',
  `terminal_os` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '平台类型(1: Android, 2: iOS)',
  `support_https` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否支持https(0:否,1:是)',
  `bid_type` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '结算方式(1:CPM,2:CPC)',
  `bid_floor` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '底价(分)',
  `adtype` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '广告类型(1: 普通硬广, 2: 原生, 3: 视频)',
  `layout` smallint(3) NOT NULL DEFAULT '0' COMMENT '广告形式',
  `material_type` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '主物料格式(1: gif, 2: png, 4: jpg, 8: mp4, 16: flv, 32: swf)',
  `material_size` varchar(255) NOT NULL DEFAULT '' COMMENT '主物料尺寸',
  `material_max_kbyte` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '主物料最大K数',
  `material_count` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '主物料个数',
  `material_duration` varchar(255) NOT NULL DEFAULT '' COMMENT '主物料视频时长(半角逗号分隔)',
  `logo_type` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT 'logo格式(1: gif, 2: png, 4: jpg)',
  `logo_size` varchar(255) NOT NULL DEFAULT '' COMMENT 'logo尺寸(空: 不支持)',
  `logo_max_kbyte` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT 'logo最大K数',
  `cover_type` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '视频格式(1: gif, 2: png, 4: jpg)',
  `cover_size` varchar(255) NOT NULL DEFAULT '' COMMENT '视频尺寸',
  `cover_max_kbyte` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '视频最大K数',
  `title_max_length` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '标题最大长度',
  `desc_max_length` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '描述最大长度',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态(0:停用,1:启用)',
  `description` varchar(400) DEFAULT NULL COMMENT '描述',
  `created_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建者',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '修改者',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`adspace_key`),
  KEY `media` (`media_id`),
  KEY `status` (`status`),
  KEY `name` (`name`),
  KEY `ad_mode_status_name` (`layout`,`status`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=200053 DEFAULT CHARSET=utf8;

/* 广告位dsp映射表 */
DROP TABLE IF EXISTS `mad_sys_adspace_mapping_dsp`;
CREATE TABLE `mad_sys_adspace_mapping_dsp` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adspace_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '广告位ID',
  `dsp_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'DSP ID',
  `dsp_media_id` varchar(255) DEFAULT NULL COMMENT 'DSP媒体ID',
  `dsp_adspace_key` varchar(255) DEFAULT NULL COMMENT 'DSP广告位Key',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态(0: 删除，1: 正常)',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建更新时间',
  PRIMARY KEY (`id`),
  KEY `status` (`status`),
  KEY `adspace_id` (`adspace_id`),
  KEY `dsp_id` (`dsp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='PremiumMAD与DSP广告位映射';

/* 广告位媒体映射表 */
DROP TABLE IF EXISTS `mad_sys_adspace_mapping_media`;
CREATE TABLE `mad_sys_adspace_mapping_media` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `adspace_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '广告位ID',
  `media_adspace_key` varchar(255) NOT NULL DEFAULT '' COMMENT '媒体方广告位ID(Key)',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态(0: 删除，1: 正常)',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建更新时间',
  PRIMARY KEY (`id`),
  KEY `media_adspace_id` (`media_adspace_key`),
  KEY `pmd_adspace_id` (`adspace_id`),
  KEY `status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='媒体方与PremiumMAD广告位映射'


/* DSP表 */
DROP TABLE IF EXISTS `mad_sys_dsp`;
CREATE TABLE `mad_sys_dsp` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `bid_url` varchar(255) NOT NULL DEFAULT '' COMMENT 'DSP URL',
  `winnotice_url` varchar(255) DEFAULT '' COMMENT 'WINNOTICE URL',
  `delivery_type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '合作模式(1: PDB, 2: PD, 4: PMP, 8: RTB)',
  `bid_percent` int(10) unsigned DEFAULT '0' COMMENT '加价百分比',
  `api_type` tinyint(3) NOT NULL DEFAULT '1' COMMENT 'API类型',
  `token` char(32) NOT NULL DEFAULT '' COMMENT 'Token',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态(0: 停用, 1: 启用)',
  `max_qps` int(10) unsigned DEFAULT NULL COMMENT '最大QPS',
  `created_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建者',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_user` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新者',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `name` (`name`),
  KEY `status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=601040 DEFAULT CHARSET=utf8;