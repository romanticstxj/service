ALTER TABLE `mad_sys_policy` ADD COLUMN returned_quantity_ratio INT(10) UNSIGNED NOT NULL DEFAULT '0' COMMENT '流量返还比(值为返还流量点数+购买流量，理论上大于100)' AFTER limit_speed;

