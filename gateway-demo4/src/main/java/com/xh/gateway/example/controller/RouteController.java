package com.xh.gateway.example.controller;

import com.xh.gateway.example.common.handler.GatewayRouterHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author H.Yang
 * @date 2022/10/15
 */
@RestController
@RequestMapping("/api/route")
public class RouteController {

    @Autowired
    private GatewayRouterHandler gatewayRouterHandler;

    /**
     * 刷新路由配置
     *
     * @return
     */
    @GetMapping("/refresh")
    public String refresh() {

        gatewayRouterHandler.loadRouteConfig();

        return null;
    }
}
