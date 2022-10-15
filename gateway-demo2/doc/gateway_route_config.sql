CREATE TABLE `gateway_route_config` (
    `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '路由ID',
    `name` VARCHAR(50) NOT NULL COMMENT '服务名称' COLLATE 'utf8_german2_ci',
    `uri` VARCHAR(200) NOT NULL COMMENT '路由地址' COLLATE 'utf8_german2_ci',
    `predicates` VARCHAR(200) NOT NULL COMMENT '路由模式' COLLATE 'utf8_german2_ci',
    `sort` TINYINT(4) NULL DEFAULT NULL COMMENT '路由顺序',
    `type` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '类型：1-lb模式(默认)，2-http模式',
    `valid` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '启用状态：0禁用(默认)，1启动',
    `deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：1-删除',
    `create_at` DATETIME NOT NULL,
    `update_at` DATETIME NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
)
COMMENT='网关路由配置'
COLLATE='utf8_german2_ci'
ENGINE=InnoDB
AUTO_INCREMENT=3
;
