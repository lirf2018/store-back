package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.anno.ClassAnnotation;
import com.yufan.bean.ShopCondition;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbImg;
import com.yufan.pojo.TbShop;
import com.yufan.service.commonrel.ICommonRelService;
import com.yufan.service.shop.IShopService;

import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.HelpCommon;
import com.yufan.utils.PageInfo;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 11:28
 * 功能介绍: 店铺管理
 */
@Controller
@RequestMapping(value = "/shop/")
@ClassAnnotation(name = "shop", desc = "店铺管理")
public class ShopController {

    @Autowired
    private IShopService iShopService;

    @Autowired
    private ICommonRelService iCommonRelService;

    /**
     * 管理页面
     *
     * @return
     */
    @RequestMapping("shopPage")
    public ModelAndView toShopPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        modelAndView.addObject("loginName", user.getLoginName());
        modelAndView.setViewName("shop-list");
        return modelAndView;
    }

    /**
     * 加载分页数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("loadShopPageData")
    public void loadPageData(ShopCondition shopCondition, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页

            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if (!"admin".equals(user.getLoginName())) {
                int shopId = user.getShopId();
                shopCondition.setShopId(shopId);
            }

            pageInfo = iShopService.loadDataPage(currePage, shopCondition);
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
     * 增加店铺页面
     *
     * @return
     */
    @RequestMapping("addShopPage")
    public ModelAndView addShopPage(HttpServletRequest request, HttpServletResponse response, Integer shopId) {
        ModelAndView modelAndView = new ModelAndView();

        TbShop shop = new TbShop();
        shop.setShopMoney(new BigDecimal(0));
        shop.setDeposit(new BigDecimal(0));
        shop.setWeight(0);
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if (null != shopId && shopId > 0) {
            if (!"admin".equals(user.getLoginName()) && shopId != user.getShopId()) {
                modelAndView.setViewName("404");
                return modelAndView;
            }

            shop = iShopService.loadShop(shopId);
            //查询关联图片
            List<Map<String, Object>> listImg = iCommonRelService.queryTableRelImg(4, shopId, 2);
            if (listImg.size() > 0) {
                for (int i = 0; i < listImg.size(); i++) {
                    String imgUrl = listImg.get(i).get("img_url") == null ? "" : listImg.get(i).get("img_url").toString();
                    int imgSort = Integer.parseInt(listImg.get(i).get("img_sort").toString());
                    modelAndView.addObject("img" + imgSort, imgUrl);
                }
            }
        } else {
            if (!"admin".equals(user.getLoginName())) {
                //只有admin账号才能添加店铺
                modelAndView.setViewName("404");
                return modelAndView;
            }
        }


        modelAndView.addObject("webImg", Constants.IMG_WEB_URL);
        modelAndView.addObject("shop", shop);
        modelAndView.setViewName("add-shop");
        return modelAndView;
    }


    /**
     * 保存数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("saveShopData")
    public void saveData(HttpServletRequest request, HttpServletResponse response, TbShop shop) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = new JSONObject();
            String shopId = request.getParameter("shop.shopId");
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if (!StringUtils.isEmpty(shopId) && !"0".equals(shopId)) {
                shop.setShopId(Integer.parseInt(shopId));
            }
            shop.setCreatetime(new Timestamp(new Date().getTime()));
            shop.setCreateman(user.getLoginName());

            String shopName = request.getParameter("shop.shopName");
            shop.setShopName(shopName.trim());
            String shopLogo = request.getParameter("shop.shopLogo");
            shop.setShopLogo(shopLogo);
            String shopCode = request.getParameter("shop.shopCode");
            shop.setShopCode(shopCode.trim());
            String weight = request.getParameter("shop.weight");
            shop.setWeight(Integer.parseInt(weight));
            String status = request.getParameter("shop.status");
            shop.setStatus(Integer.parseInt(status));
            String shopLng = request.getParameter("shop.shopLng");
            shop.setShopLng(shopLng);
            String shopLat = request.getParameter("shop.shopLat");
            shop.setShopLat(shopLat);
            String shopTel1 = request.getParameter("shop.shopTel1");
            shop.setShopTel1(shopTel1);
            String shopTel2 = request.getParameter("shop.shopTel2");
            shop.setShopTel2(shopTel2);
            String shopMoney = request.getParameter("shop.shopMoney");
            shop.setShopMoney(new BigDecimal(shopMoney));
            String deposit = request.getParameter("shop.deposit");
            shop.setDeposit(new BigDecimal(deposit));
            String enterEndTime = request.getParameter("shop.enterEndTime");
            shop.setEnterStartTime(new Timestamp(new Date().getTime()));
            shop.setEnterEndTime(new Timestamp(DatetimeUtil.convertStrToDate(enterEndTime).getTime()));
            String depositTime = request.getParameter("shop.depositTime");
            if (!StringUtils.isEmpty(depositTime)) {
                shop.setDepositTime(new Timestamp(DatetimeUtil.convertStrToDate(depositTime).getTime()));
            }
            String isOutShop = request.getParameter("shop.isOutShop");
            shop.setIsOutShop(Integer.parseInt(isOutShop));
            String introduce = request.getParameter("shop.introduce");
            shop.setIntroduce(introduce);
            String address = request.getParameter("shop.address");
            shop.setAddress(address);
            String toway = request.getParameter("shop.toway");
            shop.setToway(toway);
            String remark = request.getParameter("shop.remark");
            shop.setRemark(remark);

            //校验店铺编码
            boolean flag = iShopService.checkShopCode(shop.getShopId(), shop.getShopCode());
            if (flag) {
                out = HelpCommon.packagMsg("15");
                writer.print(out);
                writer.close();
                return;
            }
            //添加图片
            List<TbImg> listImg = new ArrayList<>();
            String img1 = request.getParameter("img1");
            if (!StringUtils.isEmpty(img1)) {
                TbImg imgObj1 = new TbImg();
                imgObj1.setImgUrl(img1);
                imgObj1.setImgSort(1);
                listImg.add(imgObj1);
            }
            String img2 = request.getParameter("img2");
            if (!StringUtils.isEmpty(img2)) {
                TbImg imgObj2 = new TbImg();
                imgObj2.setImgUrl(img2);
                imgObj2.setImgSort(2);
                listImg.add(imgObj2);
            }

            String img3 = request.getParameter("img3");
            if (!StringUtils.isEmpty(img3)) {
                TbImg imgObj3 = new TbImg();
                imgObj3.setImgUrl(img3);
                imgObj3.setImgSort(3);
                listImg.add(imgObj3);
            }
            String img4 = request.getParameter("img4");
            if (!StringUtils.isEmpty(img4)) {
                TbImg imgObj4 = new TbImg();
                imgObj4.setImgUrl(img4);
                imgObj4.setImgSort(4);
                listImg.add(imgObj4);
            }

            out = StringUtils.isEmpty(shopId) || "0".equals(shopId) ? HelpCommon.packagMsg("6") : HelpCommon.packagMsg("5");
            iShopService.saveShop(shop, listImg);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改数据状态
     */
    @RequestMapping("updateShopStatus")
    public void updateDataStatus(HttpServletRequest request, HttpServletResponse response, Integer shopId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 1 ? HelpCommon.packagMsg("4") : HelpCommon.packagMsg("3");
            iShopService.updateShopStatus(shopId, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成秘钥
     */
    @RequestMapping("generateShopSecretKey")
    public void generateShopSecretKey(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String uuid = UUID.randomUUID().toString().replace("-", "");

            JSONObject out = new JSONObject();
            out.put("uuid", uuid);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
