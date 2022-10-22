package com.xh.gateway.example.common.router;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 将定义好的路由表信息通过此类读写到内存中
 *
 * @author H.Yang
 * @date 2022/10/14
 */
@Component
public class RouteDefinitionRepositoryImpl implements RouteDefinitionRepository {
    public final Map<String, RouteDefinition> routes = new ConcurrentHashMap();

    /**
     * 请注意，此方法很重要，从redis取路由信息的方法，官方核心包要用，核心路由功能都是从redis取的
     *
     * @return
     */
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routes.values());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            routes.put(r.getId(), r);
            return Mono.empty();
        });

    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (routes.containsKey(id)) {
                routes.remove(id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }
}
