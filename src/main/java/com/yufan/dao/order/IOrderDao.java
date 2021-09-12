package com.yufan.dao.order;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.OrderCondition;
import com.yufan.pojo.TbOrder;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/22 15:15
 * 功能介绍:
 */
public interface IOrderDao {

    public PageInfo loadOrderPage(int currePage, OrderCondition orderCondition);

    public List<Map<String, Object>> queryOrderDetailById(int orderId);

    public List<Map<String, Object>> queryOrderDetailPropByOrderId(int orderId);

    public List<Map<String, Object>> queryOrderDetailPropByDetailId(int detailId);

    public List<Map<String, Object>> queryOrderStatusLogByOrderNo(String orderNo);

    public void updateOrderDetailStatus(int detailId, int status);

    public void updateOrderStatus(int orderId, int status, String serviceRemark, String lastalterman);

    public void updateOrderInfo(JSONObject orderData);

    public TbOrder loadOrder(String orderNo);

    public void updateDetailInfo(Integer orderId,Integer detailId, String rentEntTime,Integer status );

}
