package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.ActivityCondition;
import com.yufan.pojo.TbActivity;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbInfo;
import com.yufan.pojo.TbShop;
import com.yufan.service.activity.IActivityService;
import com.yufan.service.info.IInfoService;
import com.yufan.service.shop.IShopService;

import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.HelpCommon;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2020/1/16 13:33
 * 功能介绍: 资讯
 */
@Controller
@RequestMapping(value = "/info/")
public class InfoController {

    @Autowired
    private IShopService iShopService;

    @Autowired
    private IInfoService infoService;

    @RequestMapping("infoPage")
    public ModelAndView infoPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();

        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if("admin".equals(user.getLoginName())){
            shopList = iShopService.findShopAll();
        }else{
            shopList = iShopService.findShopAll(user.getShopId());
        }

        modelAndView.addObject("shopList",shopList);
        modelAndView.setViewName("info-list");
        return modelAndView;
    }

    @RequestMapping("loadInfoData")
    public void loadInfoData(HttpServletRequest request, HttpServletResponse response, TbInfo info) {
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
                info.setShopId(shopId);
            }

            pageInfo = infoService.loadDataPage(currePage, info);
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
     * 修改数据状态
     */
    @RequestMapping("updateInfoStatus")
    public void updateDataStatus(HttpServletRequest request, HttpServletResponse response, Integer id, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? HelpCommon.packagMsg("3") : HelpCommon.packagMsg("4");
            infoService.updateInfoStatus(id, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增加页面
     */
    @RequestMapping("addInfoPage")
    public ModelAndView addInfoPage(HttpServletRequest request, HttpServletResponse response, Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");

        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        if("admin".equals(user.getLoginName())){
            shopList = iShopService.findShopAll();
        }else{
            shopList = iShopService.findShopAll(user.getShopId());
        }


        TbInfo info = new TbInfo();
        info.setInfoIndex(0);
        if (null != id && id > 0) {
            info = infoService.loadInfo(id);
            if (!"admin".equals(user.getLoginName()) && info.getShopId() != user.getShopId()) {
                modelAndView.setViewName("404");
                return modelAndView;
            }
        }

        modelAndView.addObject("shopList",shopList);
        modelAndView.addObject("webImg", Constants.IMG_WEB_URL);
        modelAndView.addObject("nowDate", DatetimeUtil.getNow("yyyy-MM-dd"));
        modelAndView.addObject("info", info);
        modelAndView.setViewName("add-info");
        return modelAndView;
    }

    /**
     * 保存数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("saveInfoData")
    public void saveInfoData(HttpServletRequest request, HttpServletResponse response, TbInfo info) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg("6");
            info.setCreateTime(new Timestamp(new Date().getTime()));
            if (info.getInfoId() > 0) {
                out = HelpCommon.packagMsg("5");
            }
            infoService.saveInfo(info);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
