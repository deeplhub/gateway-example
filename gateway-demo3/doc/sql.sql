CREATE TABLE `gateway_router` (
  `id` CHAR(22) NOT NULL DEFAULT '' COLLATE 'utf8_german2_ci',
  `uri` VARCHAR(200) NOT NULL COMMENT '路由地址' COLLATE 'utf8_german2_ci',
  `sort` TINYINT(4) NULL DEFAULT NULL COMMENT '路由顺序',
  `type` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '类型：1-lb模式(默认)，2-http模式',
  `memo` VARCHAR(50) NOT NULL COMMENT '说明' COLLATE 'utf8_german2_ci',
  `valid` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '启用状态：0禁用(默认)，1启动',
  `deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：1-删除',
  `create_at` DATETIME NOT NULL,
  `update_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
)
COMMENT='网关路由配置'
COLLATE='utf8_german2_ci'
ENGINE=InnoDB
;


CREATE TABLE `gateway_router_item` (
   `id` INT(11) NOT NULL AUTO_INCREMENT,
   `router_id` CHAR(22) NOT NULL DEFAULT '' COMMENT '路由ID' COLLATE 'utf8_german2_ci',
   `param_name` VARCHAR(200) NOT NULL COMMENT '参数name' COLLATE 'utf8_german2_ci',
   `param_key` VARCHAR(200) NOT NULL COMMENT '参数key' COLLATE 'utf8_german2_ci',
   `param_value` VARCHAR(50) NULL DEFAULT NULL COMMENT '参数value' COLLATE 'utf8_german2_ci',
   `type` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '参数类型，1为 predicate，2为过 filter',
   `valid` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '启用状态：0禁用(默认)，1启动',
   `deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：1-删除',
   `create_at` DATETIME NOT NULL,
   `update_at` DATETIME NOT NULL,
   PRIMARY KEY (`id`) USING BTREE
)
COMMENT='网关路由参数表'
COLLATE='utf8_german2_ci'
ENGINE=InnoDB
AUTO_INCREMENT=4
;
