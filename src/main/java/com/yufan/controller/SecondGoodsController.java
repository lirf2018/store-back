package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.pojo.TbShop;
import com.yufan.service.second.ISecondGoodsService;
import com.yufan.service.shop.IShopService;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/30 18:02
 * 功能介绍:  只提供简单浏览的简单商品（闲菜）
 */
@Controller
@RequestMapping(value = "/second/")
public class SecondGoodsController {

    @Autowired
    private ISecondGoodsService iSecondGoodsService;

    @Autowired
    private IShopService iShopService;


    @RequestMapping("secondGoodsPage")
    public ModelAndView secondGoodsPage(HttpServletRequest request, HttpServletResponse response) {
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
        modelAndView.setViewName("second-goods-list");
        return modelAndView;
    }

    /**
     * 加载分页数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("loadSecondGoodsPageData")
    public void loadPageData(TbSecondGoods secondGoods, HttpServletRequest request, HttpServletResponse response) {
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
                secondGoods.setShopId(shopId);
            }

            pageInfo = iSecondGoodsService.loadDataPage(currePage, secondGoods);
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
     * 增加
     *
     * @return
     */
    @RequestMapping("addSecondGoodsPage")
    public ModelAndView addSecondGoodsPage(HttpServletRequest request, HttpServletResponse response, Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        TbSecondGoods goods = new TbSecondGoods();
        goods.setDataIndex(0);
        goods.setTruePrice(new BigDecimal(0));
        goods.setPurchasePrice(new BigDecimal(0));
        goods.setNowPrice(new BigDecimal(0));
        if (id != null && id > 0) {
            goods = iSecondGoodsService.loadSecondGoods(id);
            modelAndView.addObject("img4", goods.getImg4());
            modelAndView.addObject("img3", goods.getImg3());
            modelAndView.addObject("img2", goods.getImg2());
            modelAndView.addObject("img1", goods.getImg1());
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if (!"admin".equals(user.getLoginName()) && goods.getShopId() != user.getShopId()) {
                modelAndView.setViewName("404");
                return modelAndView;
            }
        }
        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if ("admin".equals(user.getLoginName())) {
            shopList = iShopService.findShopAll();
        } else {
            shopList = iShopService.findShopAll(user.getShopId());
        }
        modelAndView.addObject("shopList", shopList);
        modelAndView.addObject("webImg", Constants.IMG_URL);
        modelAndView.addObject("goods", goods);
        modelAndView.setViewName("add-second-goods");
        return modelAndView;
    }


    /**
     * 新增加数据
     *
     * @param request
     * @param response
     * @param secondGoods
     */
    @RequestMapping("saveSecondGoods")
    public void saveSecondGoods(HttpServletRequest request, HttpServletResponse response, TbSecondGoods secondGoods) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = secondGoods.getId() == 0 ? CommonMethod.packagMsg("6") : CommonMethod.packagMsg("5");
            boolean flag = iSecondGoodsService.saveSecondGoods(secondGoods);
            if (!flag) {
                out = CommonMethod.packagMsg("0");
            }
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("updateSecondGoodsStatus")
    public void updateSecondGoodsStatus(HttpServletRequest request, HttpServletResponse response, Integer id, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = CommonMethod.packagMsg("1");
            iSecondGoodsService.updateSecondGoodsStatus(id, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}