package com.yufan.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.OrderCondition;
import com.yufan.dao.order.IOrderDao;
import com.yufan.dao.order.IOrderJapDao;
import com.yufan.pojo.TbOrder;
import com.yufan.service.order.IOrderService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/22 13:40
 * 功能介绍:
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrderDao iOrderDao;

    @Autowired
    private IOrderJapDao iOrderJapDao;

    @Override
    public PageInfo loadOrderPage(int currePage, OrderCondition orderCondition) {
        return iOrderDao.loadOrderPage(currePage, orderCondition);
    }

    @Override
    public TbOrder loadOrderById(int orderId) {
        return iOrderJapDao.getOne(orderId);
    }

    @Override
    public TbOrder loadOrderByOrderno(String orderNo) {
        return iOrderJapDao.loadOrderByOrderno(orderNo);
    }

    @Override
    public List<Map<String, Object>> queryOrderDetailById(int orderId) {
        return iOrderDao.queryOrderDetailById(orderId);
    }

    @Override
    public List<Map<String, Object>> queryOrderDetailPropByOrderId(int orderId) {
        return iOrderDao.queryOrderDetailPropByOrderId(orderId);
    }

    @Override
    public List<Map<String, Object>> queryOrderDetailPropByDetailId(int detailId) {
        return iOrderDao.queryOrderDetailPropByDetailId(detailId);
    }

    @Override
    public List<Map<String, Object>> queryOrderStatusLogByOrderNo(String orderNo) {
        return iOrderDao.queryOrderStatusLogByOrderNo(orderNo);
    }

    @Override
    public void updateOrderDetailStatus(int detailId, int status) {
        iOrderDao.updateOrderDetailStatus(detailId, status);
    }

    @Override
    public void updateOrderStatus(int orderId, int status, String serviceRemark,String lastalterman) {
        iOrderDao.updateOrderStatus(orderId, status, serviceRemark,lastalterman);
    }

    @Override
    public void updateOrderInfo(JSONObject orderData) {
        iOrderDao.updateOrderInfo(orderData);
    }
}
