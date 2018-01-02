/* dsp媒体广告位权限表  */
DROP TABLE IF EXISTS `mad_sys_dsp_media`;
CREATE TABLE `mad_sys_dsp_media` (
  `id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `dsp_id` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'dsp id',
  `media_id` INT(10) NOT NULL DEFAULT 0 COMMENT '媒体id，-1为所有媒体权限',
  `adspace_id` INT(10) NOT NULL DEFAULT 0 COMMENT '广告位id，-1为所有广告位权限',
  `created_user` INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建者',
  `created_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dsp_media_adspace` (`dsp_id`,`media_id`,`adspace_id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
