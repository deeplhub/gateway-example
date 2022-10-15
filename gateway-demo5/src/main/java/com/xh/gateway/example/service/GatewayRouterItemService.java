package com.xh.gateway.example.service;

import com.xh.gateway.example.model.GatewayRouterItemEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 网关路由参数表 服务类
 * </p>
 *
 * @author H.Yang
 * @since 2022-10-14
 */
public interface GatewayRouterItemService extends IService<GatewayRouterItemEntity> {

    List<GatewayRouterItemEntity> listByRouterId(String routerId);
}
