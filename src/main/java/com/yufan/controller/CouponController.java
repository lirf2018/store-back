package com.yufan.controller;


import com.alibaba.fastjson.JSONObject;
import com.yufan.anno.ClassAnnotation;
import com.yufan.bean.CouponCondition;
import com.yufan.exception.ApplicationException;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbParam;
import com.yufan.pojo.TbShop;
import com.yufan.service.category.ICategoryService;
import com.yufan.service.coupon.ICouponService;
import com.yufan.service.param.IParamCodeService;
import com.yufan.service.shop.IShopService;

import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.HelpCommon;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @功能名称 卡券管理
 * @作者 lirongfan
 * @时间 2016年8月11日 上午11:02:45
 */
@Controller
@RequestMapping(value = "coupon")
@ClassAnnotation(name = "coupon", desc = "卡券管理")
public class CouponController {

    @Autowired
    private ICouponService iCouponService;

    @Autowired
    private IParamCodeService iParamCodeService;

    @Autowired
    private IShopService iShopService;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * @return
     */
    @RequestMapping("couponPage")
    public ModelAndView couponPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        //查询参数列表
        List<TbParam> listParam = iParamCodeService.loadTbParamCodeList(1);
        //查询一级分类
        List<Map<String, Object>> listLevel = iCategoryService.loadLevelListMap(Constants.DATA_STATUS_YX);

        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if ("admin".equals(user.getLoginName())) {
            shopList = iShopService.findShopAll();
        } else {
            shopList = iShopService.findShopAll(user.getShopId());
        }

        modelAndView.addObject("listLevel", listLevel);
        modelAndView.addObject("listParam", listParam);
        modelAndView.addObject("shopList", shopList);
        Object show = request.getParameter("show");
        if (null != show) {
            if (Integer.parseInt(show.toString()) == 0) {
                modelAndView.setViewName("pcoupon-list");
                return modelAndView;
            }
        }
        modelAndView.setViewName("coupon-list");
        return modelAndView;
    }

    /**
     *
     */
    @RequestMapping("couponListData")
    public void couponListData(HttpServletRequest request, HttpServletResponse response, CouponCondition couponCondition) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页
            pageInfo = iCouponService.loadCouponPage(currePage, couponCondition);
            //
            replaceData(pageInfo);
            //处理数据
            int recordSum = pageInfo.getRecordSum();

            //输出参数
            JSONObject dataJson = new JSONObject();
            dataJson.put("draw", Integer.parseInt(request.getParameter("draw")));
            dataJson.put("recordsTotal", recordSum);
            dataJson.put("recordsFiltered", recordSum);
            dataJson.put("data", pageInfo.getResultListMap());
            writer.print(dataJson);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replaceData(PageInfo pageInfo) {
        List<TbParam> paramList = iParamCodeService.loadTbParamCodeList(1);
        Map<String, String> paramMap = new HashMap<>();
        for (int i = 0; i < paramList.size(); i++) {
            TbParam param = paramList.get(i);
            String key = param.getParamCode() + "_" + param.getParamKey();
            String value = param.getParamValue();
            paramMap.put(key, value);
        }
        List<Map<String, Object>> result = pageInfo.getResultListMap();
        List<Map<String, Object>> outResult = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            Map<String, Object> map = result.get(i);
            String couponTypeName = paramMap.get("coupon_type_" + map.get("coupon_type"));
            map.put("coupon_type_name", couponTypeName);
            String getTypeName = paramMap.get("coupon_get_type_" + map.get("get_type"));
            map.put("get_type_name", getTypeName);
            String appointType = paramMap.get("coupon_unuse_type_" + map.get("appoint_type"));
            map.put("appoint_type_name", appointType);
            outResult.add(map);
        }
        pageInfo.setResultListMap(outResult);
    }


    /**
     * @return
     */
    @RequestMapping("toAddcouponPage")
    public ModelAndView toAddcouponPage(HttpServletRequest request, HttpServletResponse response, Integer id, Integer editorFlag) {

        ModelAndView modelAndView = new ModelAndView();
        //查询参数列表
        List<TbParam> listParam = iParamCodeService.loadTbParamCodeList(Constants.DATA_STATUS_YX);
        //查询一级分类
        List<Map<String, Object>> listLevel = iCategoryService.loadLevelListMap(Constants.DATA_STATUS_YX);

        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if ("admin".equals(user.getLoginName())) {
            shopList = iShopService.findShopAll();
        } else {
            shopList = iShopService.findShopAll(user.getShopId());
        }

        TbCoupon coupon = new TbCoupon();
        // 默认
        coupon.setOutDate(30);
        coupon.setWeight(0);
        coupon.setStartTime(new Timestamp(System.currentTimeMillis()));
        coupon.setLimitWay(4);
        coupon.setCouponType(2);
        coupon.setGetType(0);
        coupon.setCouponPrice(BigDecimal.ZERO);
        if (id != null && id > 0) {
            coupon = iCouponService.loadCouponById(id);
        }

        modelAndView.addObject("coupon", coupon);
        modelAndView.addObject("listParam", listParam);
        modelAndView.addObject("listLevel", listLevel);
        modelAndView.addObject("shopList", shopList);
        modelAndView.addObject("webImg", Constants.IMG_WEB_URL);
        modelAndView.addObject("nowDate", DatetimeUtil.getNow("yyyy-MM-dd"));
        modelAndView.addObject("editorFlag", editorFlag);
        //
        modelAndView.setViewName("add-coupon");
        return modelAndView;
    }

    /**
     * 保存数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("saveCouponData")
    public void saveData(HttpServletRequest request, HttpServletResponse response, TbCoupon coupon) {
        PrintWriter writer = null;
        JSONObject out = null;
        try {
            writer = response.getWriter();
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            coupon.setCreateman(user.getLoginName());
            coupon.setCreatetime(new Timestamp(System.currentTimeMillis()));
            coupon.setLastalterman(user.getLoginName());
            coupon.setLastaltertime(new Timestamp(System.currentTimeMillis()));
            String appointDateStr = request.getParameter("appointDateStr");
            if (StringUtils.isNotEmpty(appointDateStr)) {
                appointDateStr = appointDateStr.split(" ")[0] + " 23:59:59";
                coupon.setAppointDate(new Timestamp(DatetimeUtil.convertStrToDate(appointDateStr, DatetimeUtil.DEFAULT_DATE_FORMAT_STRING).getTime()));
            }
            out = iCouponService.saveCouponData(coupon);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            ApplicationException applicationException = (ApplicationException) e;
            writer.print(applicationException.getOut());
            writer.close();
        }
    }

    /**
     * 修改数据状态
     */
    @RequestMapping("updateCouponStatus")
    public void updateDataStatus(HttpServletRequest request, HttpServletResponse response, Integer id, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? HelpCommon.packagMsg("3") : HelpCommon.packagMsg("4");
            iCouponService.updateStatus(id, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //=====================================QR码===================================================================


    /**
     * @return
     */
    @RequestMapping("couponQrPage")
    public ModelAndView couponQrPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        //查询参数列表
        List<TbParam> listParam = iParamCodeService.loadTbParamCodeList(1);
        //查询一级分类
        List<Map<String, Object>> listLevel = iCategoryService.loadLevelListMap(Constants.DATA_STATUS_YX);

        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if ("admin".equals(user.getLoginName())) {
            shopList = iShopService.findShopAll();
        } else {
            shopList = iShopService.findShopAll(user.getShopId());
        }

        modelAndView.addObject("listLevel", listLevel);
        modelAndView.addObject("listParam", listParam);
        modelAndView.addObject("shopList", shopList);
        modelAndView.setViewName("qr-list");
        return modelAndView;
    }

    /**
     *
     */
    @RequestMapping("couponQrListData")
    public void couponQrListData(HttpServletRequest request, HttpServletResponse response, CouponCondition couponCondition) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页
            pageInfo = iCouponService.loadCouponQrPage(currePage, couponCondition);
            //
            replaceData(pageInfo);
            //处理数据
            int recordSum = pageInfo.getRecordSum();

            //输出参数
            JSONObject dataJson = new JSONObject();
            dataJson.put("draw", Integer.parseInt(request.getParameter("draw")));
            dataJson.put("recordsTotal", recordSum);
            dataJson.put("recordsFiltered", recordSum);
            dataJson.put("data", pageInfo.getResultListMap());
            writer.print(dataJson);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * qr兑换
     *
     * @param request
     * @param response
     * @param changeCode
     * @param checkCode
     */
    @RequestMapping("changeQrCode")
    public void changeQrCode(HttpServletRequest request, HttpServletResponse response, String changeCode, String checkCode) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = iCouponService.changeQrCode(changeCode, checkCode);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
