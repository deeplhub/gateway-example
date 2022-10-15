package com.xh.gateway.example.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 网关路由参数表
 * </p>
 *
 * @author H.Yang
 * @since 2022-10-14
 */
@Getter
@Setter
@TableName("gateway_router_item")
public class GatewayRouterItemEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 路由ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 服务名称
     */
    private String routerId;

    /**
     * 参数name
     */
    private String paramName;

    /**
     * 参数key
     */
    private String paramKey;

    /**
     * 参数value
     */
    private String paramValue;

    /**
     * 参数类型，1为 predicate，2为过 filter
     */
    private Integer type;

    /**
     * 启用状态：0禁用(默认)，1启动
     */
    private Boolean valid;

    /**
     * 逻辑删除：1-删除
     */
    private Boolean deleted;

    private Date createAt;

    private Date updateAt;


}
