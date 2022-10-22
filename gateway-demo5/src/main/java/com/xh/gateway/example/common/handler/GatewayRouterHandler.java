package com.xh.gateway.example.common.handler;

import com.xh.gateway.example.model.GatewayRouterEntity;
import com.xh.gateway.example.model.GatewayRouterItemEntity;
import com.xh.gateway.example.service.GatewayRouterItemService;
import com.xh.gateway.example.service.GatewayRouterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author H.Yang
 * @date 2022/10/14
 */
@Slf4j
@Component
public class GatewayRouterHandler implements ApplicationEventPublisherAware, CommandLineRunner {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    @Qualifier(value = "routeDefinitionRepositoryImpl")
    private RouteDefinitionRepository routeDefinitionWriter;
    @Autowired
    private GatewayRouterService gatewayRouterService;
    @Autowired
    private GatewayRouterItemService gatewayRouterItemService;


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Override
    public void run(String... args) throws Exception {
        this.loadRouteConfig();
    }

    public void loadRouteConfig() {
        gatewayRouterService.list().forEach(routerEntity -> {
            this.addRoute(this.getRouteDefinition(routerEntity));
        });


    }


    /**
     * 路由数据转换公共方法
     *
     * @param entity
     * @return
     */
    private RouteDefinition getRouteDefinition(GatewayRouterEntity entity) {
        RouteDefinition definition = new RouteDefinition();

        URI uri = (entity.getType() == 1) ? UriComponentsBuilder.fromUriString("lb://" + entity.getUri()).build().toUri() : UriComponentsBuilder.fromHttpUrl(entity.getUri()).build().toUri();

        definition.setUri(uri);
        definition.setId(entity.getId());
        definition.setPredicates(this.getPredicateDefinition(entity.getId()));

        return definition;
    }

    private List<PredicateDefinition> getPredicateDefinition(String routerId) {
        List<GatewayRouterItemEntity> routerItemList = gatewayRouterItemService.listByRouterId(routerId);

        List<PredicateDefinition> predicateList = new LinkedList<>();

        PredicateDefinition predicate = null;
        for (GatewayRouterItemEntity routerItem : routerItemList) {
            predicate = new PredicateDefinition();

            // 名称是固定的，spring gateway会根据名称找对应的PredicateFactory
            predicate.setName(routerItem.getParamName());
            predicate.addArg(routerItem.getParamKey(), routerItem.getParamValue());

            predicateList.add(predicate);
        }

        return predicateList;
    }

    /**
     * 添加路由
     *
     * @param definition
     */
    private void addRoute(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();

        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

}
