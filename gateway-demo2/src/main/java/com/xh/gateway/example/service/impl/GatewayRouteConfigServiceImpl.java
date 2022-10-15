package com.xh.gateway.example.service.impl;

import com.xh.gateway.example.common.event.DynamicRouteService;
import com.xh.gateway.example.model.GatewayRouteConfigEntity;
import com.xh.gateway.example.mapper.GatewayRouteConfigMapper;
import com.xh.gateway.example.service.GatewayRouteConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网关路由配置 服务实现类
 * </p>
 *
 * @author H.Yang
 * @since 2022-10-14
 */
@Service
public class GatewayRouteConfigServiceImpl extends ServiceImpl<GatewayRouteConfigMapper, GatewayRouteConfigEntity> implements GatewayRouteConfigService {

}
