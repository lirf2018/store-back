package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.OrderCondition;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbOrder;
import com.yufan.pojo.TbParam;
import com.yufan.pojo.TbShop;
import com.yufan.service.order.IOrderService;
import com.yufan.service.param.IParamCodeService;
import com.yufan.service.shop.IShopService;

import com.yufan.utils.Constants;
import com.yufan.utils.HelpCommon;
import com.yufan.utils.MD5;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/22 11:56
 * 功能介绍: 订单管理
 */
@Controller
@RequestMapping(value = "/order/")
public class OrderController {

    @Autowired
    private IParamCodeService iParamCodeService;

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IShopService iShopService;

    /**
     * 跳转到订单管理页面
     *
     * @return
     */
    @RequestMapping("orderPage")
    public ModelAndView toOrderPage(HttpServletRequest request) {
        List<TbParam> paramList = iParamCodeService.loadTbParamCodeList();

        ModelAndView modelAndView = new ModelAndView();
        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if ("admin".equals(user.getLoginName())) {
            shopList = iShopService.findShopAll();
        } else {
            shopList = iShopService.findShopAll(user.getShopId());
        }
        modelAndView.addObject("shopList", shopList);
        modelAndView.addObject("paramList", paramList);
        modelAndView.setViewName("order-list");
        return modelAndView;
    }


    @RequestMapping("loadOrderData")
    public void loadOrderData(OrderCondition orderCondition, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页

            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if (!"admin".equals(user.getLoginName())) {
                int shopId = user.getShopId();
                orderCondition.setShopId(shopId);
            }
            String orderNo = request.getParameter("orderNo");
            if (!StringUtils.isEmpty(orderNo.trim())) {
                orderCondition.setOrderNo(orderNo.trim());
            }
            String userPhone = request.getParameter("userPhone");
            if (!StringUtils.isEmpty(userPhone.trim())) {
                orderCondition.setUserPhone(userPhone.trim());
            }
            String userName = request.getParameter("userName");
            if (!StringUtils.isEmpty(userName.trim())) {
                orderCondition.setUserName(userName.trim());
            }
            String orderStatus = request.getParameter("orderStatus");
            if (!StringUtils.isEmpty(orderStatus.trim())) {
                orderCondition.setOrderStatus(Integer.parseInt(orderStatus));
            }
            String userId = request.getParameter("userId");
            if (!StringUtils.isEmpty(userId.trim())) {
                orderCondition.setUserId(Integer.parseInt(userId.trim()));
            }
            String startOrderDate = request.getParameter("startOrderDate");
            if (!StringUtils.isEmpty(startOrderDate.trim())) {
                orderCondition.setStartOrderDate(startOrderDate);
            }
            String endOrderDate = request.getParameter("endOrderDate");
            if (!StringUtils.isEmpty(endOrderDate.trim())) {
                orderCondition.setEndOrderDate(endOrderDate);
            }
            String startPayDate = request.getParameter("startPayDate");
            if (!StringUtils.isEmpty(startPayDate.trim())) {
                orderCondition.setStartPayDate(startPayDate);
            }
            String endPayDate = request.getParameter("endPayDate");
            if (!StringUtils.isEmpty(endPayDate.trim())) {
                orderCondition.setEndPayDate(endPayDate);
            }
            String businessType = request.getParameter("businessType");
            if (!StringUtils.isEmpty(businessType.trim())) {
                orderCondition.setBusinessType(Integer.parseInt(businessType));
            }
            String postWay = request.getParameter("postWay");
            if (!StringUtils.isEmpty(postWay.trim())) {
                orderCondition.setPostWay(Integer.parseInt(postWay));
            }

            pageInfo = iOrderService.loadOrderPage(currePage, orderCondition);

            //
            List<TbParam> paramList = iParamCodeService.loadTbParamCodeList();
            Map<String, String> paramMap = new HashMap<>();
            for (int i = 0; i < paramList.size(); i++) {
                String key = paramList.get(i).getParamCode() + "-" + paramList.get(i).getParamKey();
                String value = paramList.get(i).getParamValue();
                paramMap.put(key, value);
            }

            //处理数据
            int recordSum = pageInfo.getRecordSum();

            //输出参数
            JSONObject dataJson = new JSONObject();
            dataJson.put("draw", Integer.parseInt(request.getParameter("draw")));
            dataJson.put("recordsTotal", recordSum);
            dataJson.put("recordsFiltered", recordSum);
            dataJson.put("data", pageInfo.getResultListMap());
            if (null != pageInfo.getResultListMap() && pageInfo.getResultListMap().size() > 0) {
                //处理数据
                List<Map<String, Object>> dataList = pageInfo.getResultListMap();
                List<Map<String, Object>> outList = new ArrayList<>();
                for (int i = 0; i < dataList.size(); i++) {
                    Map<String, Object> map = dataList.get(i);
                    //pay_way_name
                    map.put("pay_way_name", paramMap.get("pay_way-" + map.get("pay_way")));
                    //advance_pay_way_name
                    map.put("advance_pay_way_name", paramMap.get("pay_way-" + map.get("pay_way")));
                    //business_type_name
                    map.put("business_type_name", paramMap.get("business_type-" + map.get("business_type")));
                    //order_status_name
                    map.put("order_status_name", paramMap.get("order_status-" + map.get("order_status")));
                    //post_way_name
                    map.put("post_way_name", paramMap.get("post_way-" + map.get("post_way")));
                    outList.add(map);
                }
                pageInfo.setResultListMap(outList);
            }
            writer.print(dataJson);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询订单详情
     */
    @RequestMapping("orderDetail")
    public void orderDetail(HttpServletRequest request, HttpServletResponse response, Integer orderId) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            List<Map<String, Object>> detailList = iOrderService.queryOrderDetailById(orderId);
            List<Map<String, Object>> propList = iOrderService.queryOrderDetailPropByOrderId(orderId);
            for (int i = 0; i < detailList.size(); i++) {
                Map<String, Object> detailMap = detailList.get(i);
                String detailId = detailMap.get("detail_id").toString();
                for (int j = 0; j < propList.size(); j++) {
                    Map<String, Object> propMap = propList.get(j);
                    String detailId_ = propMap.get("detail_id").toString();
                    if (detailId.equals(detailId_)) {
                        detailMap.putAll(propMap);
                    }
                }
            }

            //输出参数
            JSONObject dataJson = new JSONObject();
            dataJson.put("data", detailList);
            writer.print(dataJson);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到订单详情页面
     *
     * @param
     * @param response
     * @return
     */
    @RequestMapping("orderDetailPage")
    public ModelAndView toOrderDetailPage(HttpServletRequest request, HttpServletResponse response, Integer orderId) {
        ModelAndView modelAndView = new ModelAndView();

        //查询参数
        List<TbParam> paramList = iParamCodeService.loadTbParamCodeList(Constants.DATA_STATUS_YX);
        Map<String, String> paramMap = new HashMap<>();
        for (int i = 0; i < paramList.size(); i++) {
            String key = paramList.get(i).getParamCode() + "-" + paramList.get(i).getParamKey();
            String value = paramList.get(i).getParamValue();
            paramMap.put(key, value);
        }

        //订单详情输出
        List<Map<String, Object>> detailOutList = new ArrayList<>();
        //查询订单
        TbOrder order = iOrderService.loadOrderById(orderId);

        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if (!"admin".equals(user.getLoginName()) && order.getShopId() != user.getShopId()) {
            modelAndView.setViewName("404");
            return modelAndView;
        }


        //查询订单状态日志
        List<Map<String, Object>> orderLog = iOrderService.queryOrderStatusLogByOrderNo(order.getOrderNo());
        //查询详情列表
        List<Map<String, Object>> detailList = iOrderService.queryOrderDetailById(orderId);
        //查询详情属性列表
        List<Map<String, Object>> detailPropList = iOrderService.queryOrderDetailPropByOrderId(orderId);
        for (int i = 0; i < detailList.size(); i++) {
            Map<String, Object> detailMap = detailList.get(i);
            List<Map<String, Object>> detailPropOutList = new ArrayList<>();
            for (int j = 0; j < detailPropList.size(); j++) {
                if (Integer.parseInt(detailPropList.get(j).get("detail_id").toString()) == Integer.parseInt(detailMap.get("detail_id").toString())) {
                    detailPropOutList.add(detailPropList.get(j));
                }
            }
            detailMap.put("detailPropOutList", detailPropOutList);
            detailOutList.add(detailMap);
        }

        //
        modelAndView.addObject("order_status_name", paramMap.get("order_status-" + order.getOrderStatus()));
        modelAndView.addObject("business_type_name", paramMap.get("business_type-" + order.getBusinessType()));
        modelAndView.addObject("pay_way_name", paramMap.get("pay_way-" + order.getPayWay()));
        modelAndView.addObject("advance_pay_way_name", paramMap.get("pay_way-" + order.getAdvancePayCode()));
        modelAndView.addObject("post_way_name", paramMap.get("post_way-" + order.getPostWay()));
        modelAndView.addObject("company_code_name", paramMap.get("company_code-" + order.getCompanyCode()));


        //标识关系名称
        modelAndView.addObject("order", order);
        modelAndView.addObject("detailOutList", detailOutList);
        modelAndView.addObject("orderLog", orderLog);
        modelAndView.addObject("paramList", paramList);
        modelAndView.setViewName("order-detail");
        return modelAndView;
    }

    /**
     * 修改详情状态
     */
    @PostMapping("updateDetailStatus")
    public void updateDetailStatus(HttpServletRequest request, HttpServletResponse response, Integer detailId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg("1");
            iOrderService.updateOrderDetailStatus(detailId, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 订单重置
     */
    @PostMapping("orderReset")
    public void orderReset(HttpServletRequest request, HttpServletResponse response, Integer orderId) {
        PrintWriter writer = null;
        try {
            JSONObject out = HelpCommon.packagMsg("1");
            writer = response.getWriter();
            String resetKey = request.getParameter("resetKey");
            String resetKeyMd5 = MD5.enCodeStandard(resetKey);
            if (!resetKeyMd5.equals(Constants.ORDER_RESET_KEY)) {
                out = HelpCommon.packagMsg("11");
                writer.print(out);
                writer.close();
                return;
            }

            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            String lastalterman = user.getLoginName();
            Integer orderStatus = Integer.parseInt(request.getParameter("orderStatus"));
            String refundPrice = request.getParameter("refundPrice");//退款金额
            String refundRemark = request.getParameter("refundRemark");//退款描述
            String serviceRemark = request.getParameter("serviceRemark");//客服备注
            String postMan = request.getParameter("postMan");//快递人员姓名
            String postPhone = request.getParameter("postPhone");//快递人员电话

            JSONObject orderData = new JSONObject();
            orderData.put("lastalterman", lastalterman);
            orderData.put("orderStatus", orderStatus);
            orderData.put("refundPrice", refundPrice);
            orderData.put("refundRemark", refundRemark);
            orderData.put("serviceRemark", serviceRemark);
            orderData.put("postMan", postMan);
            orderData.put("postPhone", postPhone);
            orderData.put("orderId", orderId);
            iOrderService.updateOrderInfo(orderData);

            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改订单状态
     *
     * @param request
     * @param response
     */
    @PostMapping("updateOrderStatus")
    public void updateOrderInfo(HttpServletRequest request, HttpServletResponse response, Integer orderId, Integer orderStatus, String serviceRemark) {
        PrintWriter writer = null;
        try {
            JSONObject out = HelpCommon.packagMsg("5");
            writer = response.getWriter();
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            String lastalterman = user.getLoginName();
            iOrderService.updateOrderStatus(orderId, orderStatus, serviceRemark, lastalterman);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改详情状态
     */
    @PostMapping("createPrivateGoods")
    public void createPrivateGoods(HttpServletRequest request, HttpServletResponse response, String orderNo) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = iOrderService.createPrivateGoods(orderNo);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
