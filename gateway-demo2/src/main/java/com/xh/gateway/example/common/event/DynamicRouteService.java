package com.xh.gateway.example.common.event;

import com.xh.gateway.example.model.GatewayRouteConfigEntity;
import com.xh.gateway.example.service.GatewayRouteConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网关路由器处理程序
 *
 * @author H.Yang
 * @date 2022/10/14
 */
@Slf4j
@Component
public class DynamicRouteService implements ApplicationEventPublisherAware, CommandLineRunner {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;
    @Autowired
    private GatewayRouteConfigService gatewayRouteConfigService;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    /**
     * 启动时执行
     * <p>
     * 实现 CommandLineRunner 接口
     *
     * @param args
     */
    @Override
    public void run(String... args) {
        log.debug("加载路由配置....");
        List<GatewayRouteConfigEntity> list = gatewayRouteConfigService.list();

        list.forEach(routerEntity -> {
            this.addRoute(this.getRouteDefinition(routerEntity));
        });

        log.debug("加载路由配置完成");
    }

    /**
     * 路由数据转换公共方法
     *
     * @param entity
     * @return
     */
    private RouteDefinition getRouteDefinition(GatewayRouteConfigEntity entity) {
        RouteDefinition definition = new RouteDefinition();

        URI uri = (entity.getType() == 1) ? UriComponentsBuilder.fromUriString("lb://" + entity.getUri()).build().toUri() : UriComponentsBuilder.fromHttpUrl(entity.getUri()).build().toUri();

        definition.setUri(uri);
        definition.setId(entity.getId());
        definition.setPredicates(Arrays.asList(this.getPredicateDefinition(entity.getPredicates())));

        return definition;
    }

    /**
     * 谓词定义
     *
     * @param routePattern
     * @return
     */
    private PredicateDefinition getPredicateDefinition(String routePattern) {
        Map<String, String> predicateParams = new HashMap<>(8);
        predicateParams.put("pattern", routePattern);

        PredicateDefinition predicate = new PredicateDefinition();

        // 名称是固定的，spring gateway会根据名称找对应的PredicateFactory
        predicate.setName("Path");
        predicate.setArgs(predicateParams);

        return predicate;
    }


//    private FilterDefinition filterDefinition() {
//        Map<String, String> filterParams = new HashMap<>(8);
//
//        filterParams.put("_genkey_0", entity.getFilters().toString());
//
//        FilterDefinition filterDefinition = new FilterDefinition();
//
//        // 名称是固定的, 路径去前缀
//        filterDefinition.setName("StripPrefix");
//        filterDefinition.setArgs(filterParams);
//
//        return filterDefinition;
//    }

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
