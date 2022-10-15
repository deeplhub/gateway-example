package com.xh.gateway.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xh.gateway.example.model.GatewayRouterItemEntity;
import com.xh.gateway.example.mapper.GatewayRouterItemMapper;
import com.xh.gateway.example.service.GatewayRouterItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 网关路由参数表 服务实现类
 * </p>
 *
 * @author H.Yang
 * @since 2022-10-14
 */
@Service
public class GatewayRouterItemServiceImpl extends ServiceImpl<GatewayRouterItemMapper, GatewayRouterItemEntity> implements GatewayRouterItemService {

    @Override
    public List<GatewayRouterItemEntity> listByRouterId(String routerId) {
        QueryWrapper<GatewayRouterItemEntity> queryWrapper = new QueryWrapper();

        queryWrapper.lambda().eq(GatewayRouterItemEntity::getValid, 1);
        queryWrapper.lambda().eq(GatewayRouterItemEntity::getRouterId, routerId);

        return super.list(queryWrapper);
    }
}
