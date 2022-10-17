package com.xh.gateway.example;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author H.Yang
 * @date 2022/10/16
 */
@Slf4j
@Component
public class NacosDynamicRouteService implements ApplicationEventPublisherAware, CommandLineRunner {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private LocalRouteDefinitionRepository localRouteDefinitionRepository;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Override
    public void run(String... args) throws Exception {
        log.debug("启动时加载....");
        ConfigService configService = NacosFactory.createConfigService(nacosConfigProperties.assembleConfigServiceProperties());
        // 程序首次启动, 并加载初始化路由配置
        String configInfo = configService.getConfig("gateway-router.json", nacosConfigProperties.getGroup(), 5000);

        this.addRouteConfig(configInfo);

        // 添加监听器监听nacos配置文件内的路由变化
        configService.addListener("gateway-router.json", nacosConfigProperties.getGroup(), new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                log.debug("配置发生变化时加载....");

                refreshRouteConfig(configInfo);
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
        });

    }

    /**
     * 刷新路由配置
     *
     * @param configInfo
     */
    private void refreshRouteConfig(String configInfo) {
        // 刷新配置时需要先清空缓存
        localRouteDefinitionRepository.clear();

        this.addRouteConfig(configInfo);
    }


    /**
     * 添加路由
     *
     * @param configInfo
     */
    private void addRouteConfig(String configInfo) {
        log.debug("动态添加路由配置...");

        List<RouteDefinition> routeDefinitions = JSONUtil.toList(configInfo, RouteDefinition.class);
        for (RouteDefinition routeDefinition : routeDefinitions) {
            // 添加路由
            localRouteDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
        }

        this.publish();

        log.debug("动态配置路由加载完成 {}", JSONUtil.toJsonStr(routeDefinitions));
    }


    /**
     * 发布
     */
    public void publish() {
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }

}
