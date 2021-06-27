package com.yufan.service.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.OrderCondition;
import com.yufan.pojo.TbOrder;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/22 13:40
 * 功能介绍:
 */
public interface IOrderService {


    PageInfo loadOrderPage(int currePage, OrderCondition orderCondition);


    TbOrder loadOrderById(int orderId);

    TbOrder loadOrderByOrderno(String orderNo);

    List<Map<String, Object>> queryOrderDetailById(int orderId);

    List<Map<String, Object>> queryOrderDetailPropByOrderId(int orderId);

    List<Map<String, Object>> queryOrderDetailPropByDetailId(int detailId);

    List<Map<String, Object>> queryOrderStatusLogByOrderNo(String orderNo);

    void updateOrderDetailStatus(int detailId, int status);

    void updateOrderStatus(int orderId, int status, String serviceRemark, String lastalterman);

    void updateOrderInfo(JSONObject orderData);

    JSONObject createPrivateGoods(String orderId);
}
