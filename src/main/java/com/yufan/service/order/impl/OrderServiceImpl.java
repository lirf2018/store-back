package com.yufan.service.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.OrderCondition;
import com.yufan.bean.RequestData;
import com.yufan.bean.ResponeData;
import com.yufan.dao.order.IOrderDao;
import com.yufan.dao.order.IOrderJapDao;
import com.yufan.dao.user.IInfAccountJpaDao;
import com.yufan.pojo.TbInfAccount;
import com.yufan.pojo.TbOrder;
import com.yufan.service.order.IOrderService;
import com.yufan.utils.Constants;
import com.yufan.utils.HelpCommon;
import com.yufan.utils.HttpRequest;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private IInfAccountJpaDao iInfAccountJpaDao;

    @Value("${info.service}")
    private String SERVICE;

    @Value("${info.account}")
    private String ACCOUNT;

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
    public void updateOrderStatus(int orderId, int status, String serviceRemark, String lastalterman) {
        iOrderDao.updateOrderStatus(orderId, status, serviceRemark, lastalterman);
    }

    @Override
    public void updateOrderInfo(JSONObject orderData) {
        iOrderDao.updateOrderInfo(orderData);
    }

    @Override
    public JSONObject createPrivateGoods(String orderNo) {
        JSONObject out = HelpCommon.packagMsg("0");
        try {
            TbInfAccount account = iInfAccountJpaDao.findByAccount(ACCOUNT);
            if (null == account) {
                out = HelpCommon.packagMsg("33");
                return out;
            }
            TbOrder order = iOrderDao.loadOrder(orderNo);
            if (null == order) {
                out = HelpCommon.packagMsg("10045");
                return out;
            }
            String reqType = Constants.BUSINESS_TYPE_2;
            String sid = account.getSid();
            String appsecret = account.getSecretKey();
            JSONObject data = new JSONObject();
            data.put("userId", order.getUserId());
            data.put("orderId", order.getOrderId());
            RequestData requestData = new RequestData(reqType, sid, appsecret, data);
            String param = requestData.requestParam().toJSONString();
            String result = HttpRequest.httpPost(SERVICE, param);
            if (StringUtils.isNotEmpty(result)) {
                JSONObject obj = JSONObject.parseObject(result);
                ResponeData responeData = JSONObject.toJavaObject(obj, ResponeData.class);
                if (responeData.getRespCode() == 1) {
                    out = HelpCommon.packagMsg("1");
                } else {
                    out = HelpCommon.packagMsg("-100", responeData.getRespDesc());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public void updateDetailInfo(Integer orderId, Integer detailId, String rentEntTime, Integer status) {
        iOrderDao.updateDetailInfo(orderId, detailId, rentEntTime, status);
    }
}
