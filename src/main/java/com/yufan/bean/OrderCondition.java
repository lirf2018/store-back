package com.yufan.bean;


import lombok.Getter;
import lombok.Setter;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/26 18:33
 * 功能介绍: 订单查询条件
 */
@Getter
@Setter
public class OrderCondition {


    private String orderNo;
    private String userPhone;
    private String userName;
    private Integer orderStatus;
    private Integer userId;
    private String startOrderDate;
    private String endOrderDate;
    private String payCode;
    private String channelCode;
    private String orderType;
    private String startPayDate;
    private String endPayDate;
    private Integer businessType;
    private String traceNo;
    private String goodsName;
    private String iccid;
    private Integer postWay;
    private Integer orderId;
    private Integer shopId;

}
