package com.yufan.controller;


import com.alibaba.fastjson.JSONObject;
import com.yufan.anno.ClassAnnotation;
import com.yufan.bean.CouponCondition;
import com.yufan.exception.ApplicationException;
import com.yufan.pojo.*;
import com.yufan.service.category.ICategoryService;
import com.yufan.service.coupon.ICouponService;
import com.yufan.service.param.IParamCodeService;
import com.yufan.service.shop.IShopService;

import com.yufan.service.user.IUserService;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.HelpCommon;
import com.yufan.utils.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.ClientEndpoint;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

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

    @Autowired
    private IUserService iUserService;

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

            String limitBeginTimeStr = request.getParameter("limitBeginTimeStr");
            if (StringUtils.isNotEmpty(limitBeginTimeStr)) {
                coupon.setLimitBeginTime(new Timestamp(DatetimeUtil.convertStrToDate(limitBeginTimeStr, DatetimeUtil.DEFAULT_DATE_FORMAT_STRING).getTime()));
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

    //===================================================================赠送福利优惠券================================

    /**
     * 赠送福利优惠券
     */
    @RequestMapping("giveCouponPage")
    public ModelAndView toGiveCouponPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("give-coupon-list");
        return modelAndView;
    }

    @RequestMapping("giveCouponListData")
    public void giveCouponListData(HttpServletRequest request, HttpServletResponse response, CouponCondition couponCondition) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页
            pageInfo = iCouponService.giveCouponListData(currePage, couponCondition);
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
     * 保存赠送的卡券
     *
     * @param request
     * @param response
     * @param couponId
     * @param userPhones
     */
    @RequestMapping("saveGiveCouponData")
    public void saveGiveCouponData(HttpServletRequest request, HttpServletResponse response, Integer couponId, String userPhones) {
        PrintWriter writer = null;
        try {
            //输出参数
            JSONObject dataJson = new JSONObject();
            dataJson.put("code", 1);
            dataJson.put("msg", "操作成功");
            boolean flag = true;
            // 校验用户合法性
            if (StringUtils.isEmpty(userPhones) || couponId == null) {
                flag = false;
            }
            // 用户信息 key = phone
            Map<String, Map<String, Object>> userDbMap = new HashMap<>();
            String checkPhoneError = "";
            if (flag) {
                // 校验用户
                if (userPhones.endsWith(";")) {
                    userPhones = userPhones.substring(0, userPhones.length() - 1);
                }
                checkPhoneError = checkGiveUserPhoneRepeat(userPhones);
                if (StringUtils.isNotEmpty(checkPhoneError)) {
                    flag = false;
                    if (checkPhoneError.endsWith(",")) {
                        checkPhoneError = checkPhoneError.substring(0, checkPhoneError.length() - 1);
                    }
                    dataJson.put("msg", "当前用户存在重复赠送【" + checkPhoneError + "】");
                }
            }
            if (flag) {
                // 查询用户信息  0待验证 1正常 2锁定 3已注销
                String phones = userPhones.replace(",", "','");
                List<Map<String, Object>> userList = iUserService.findUserListMapByPhones(phones);
                //
                for (int i = 0; i < userList.size(); i++) {
                    String userState = userList.get(i).get("user_mobile").toString();
                    userDbMap.put(userState, userList.get(i));
                }
                //
                checkPhoneError = checkGiveUserPhoneDb(phones, userDbMap);
                if (StringUtils.isNotEmpty(checkPhoneError)) {
                    flag = false;
                    if (checkPhoneError.endsWith(",")) {
                        checkPhoneError = checkPhoneError.substring(0, checkPhoneError.length() - 1);
                    }
                    dataJson.put("msg", "校验不通过【" + checkPhoneError + "】");
                }
            }
//            if(flag){
//                // 查询用户是否存在未使用的优惠券
//            }
            // 保存
            if (flag) {
                // 过期日期 一个月 15天
                int day = 15;
                List<TbParam> paramList = iParamCodeService.loadTbParamCodeList(Constants.DATA_STATUS_YX, "get_coupon_out_day");
                if (CollectionUtils.isNotEmpty(paramList)) {
                    TbParam param = paramList.get(0);
                    day = Integer.parseInt(StringUtils.isEmpty(param.getParamValue()) ? "15" : param.getParamValue());
                }
                Date outDate = DatetimeUtil.addDays(new Date(), day);
                String[] phonesArr = userPhones.split(",");
                for (int i = 0; i < phonesArr.length; i++) {
                    String phone = phonesArr[i];
                    if (StringUtils.isEmpty(phone)) {
                        continue;
                    }
                    TbGiveUserCoupon giveUserCoupon = new TbGiveUserCoupon();
                    giveUserCoupon.setAddFrom(0);// 来源：0赠送 1 订单3抽奖
                    giveUserCoupon.setCouponId(couponId);
                    giveUserCoupon.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    giveUserCoupon.setStatus(0);// 状态：0未领取1已领取2已使用3已过期
                    giveUserCoupon.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                    giveUserCoupon.setOutTime(outDate);
                    int userId = Integer.parseInt(userDbMap.get(phone).get("user_id").toString());
                    giveUserCoupon.setUserId(userId);
                    iUserService.saveObj(giveUserCoupon);
                }
            } else {
                dataJson.put("code", 0);
            }
            writer = response.getWriter();
            writer.print(dataJson);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验用户用户是否有效  0待验证 1正常 2锁定 3已注销
     *
     * @param phones
     * @param userDbMap
     * @return
     */
    private String checkGiveUserPhoneDb(String phones, Map<String, Map<String, Object>> userDbMap) {
        //
        String[] phonesArr = phones.split(",");
        StringBuilder checkPhoneError = new StringBuilder();
        for (int i = 0; i < phonesArr.length; i++) {
            String phone = phonesArr[i];
            if (userDbMap.get(phone) == null) {
                checkPhoneError.append(phone).append("(无效)").append(",");
                continue;
            }
            int userState = Integer.parseInt(userDbMap.get(phone).get("user_state").toString());
            if (userState == Constants.USER_STATUS_0) {
                checkPhoneError.append(phone).append("(待验证)").append(",");
            } else if (userState == Constants.USER_STATUS_2) {
                checkPhoneError.append(phone).append("(锁定)").append(",");
            } else if (userState == Constants.USER_STATUS_3) {
                checkPhoneError.append(phone).append("(已注销)").append(",");
            }
        }
        return checkPhoneError.toString();
    }

    /**
     * 校验用户重复性
     *
     * @param phones
     * @return
     */
    private String checkGiveUserPhoneRepeat(String phones) {
        Map<String, Integer> maps = new HashMap<>();
        String[] phonesArr = phones.split(",");
        for (int i = 0; i < phonesArr.length; i++) {
            String phone = phonesArr[i];
            if (maps.get(phone) != null) {
                maps.put(phone, maps.get(phone) + 1);
            } else {
                maps.put(phone, 1);
            }
        }
        StringBuilder checkPhoneError = new StringBuilder();
        for (Map.Entry<String, Integer> m : maps.entrySet()) {
            if (m.getValue() > 1) {
                checkPhoneError.append(m.getKey()).append(",");
            }
        }
        return checkPhoneError.toString();
    }

    /**
     * 删除赠送的卡券
     */
    @RequestMapping("deleteGiveCouponData")
    public void deleteGiveCouponData(HttpServletRequest request, HttpServletResponse response, Integer id) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg("3");
            iCouponService.deleteGiveCouponData(id);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String phones = "134,135,134,168";
        String[] phonesArr = phones.split(",");

//        Collections.to

    }
}
