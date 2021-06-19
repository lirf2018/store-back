package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.ActivityCondition;
import com.yufan.pojo.TbActivity;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbShop;
import com.yufan.service.activity.IActivityService;
import com.yufan.service.shop.IShopService;

import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.HelpCommon;
import com.yufan.utils.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 11:51
 * 功能介绍: 活动管理
 */
@Controller
@RequestMapping(value = "/activity/")
public class ActivityController {

    @Autowired
    private IActivityService iActivityService;

    @Autowired
    private IShopService iShopService;

    /**
     * 管理页面
     *
     * @return
     */
    @RequestMapping("activityPage")
    public ModelAndView toActivityPage(HttpServletRequest request, HttpServletResponse response) {
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
        modelAndView.setViewName("activity-list");
        return modelAndView;
    }

    /**
     * 加载分页数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("loadActivityPageData")
    public void loadPageData(HttpServletRequest request, HttpServletResponse response, ActivityCondition activityCondition) {
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
                activityCondition.setShopId(shopId);
            }

            pageInfo = iActivityService.loadDataPage(currePage, activityCondition);
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
    @RequestMapping("updateActivityStatus")
    public void updateDataStatus(HttpServletRequest request, HttpServletResponse response, Integer activityId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? HelpCommon.packagMsg(3) : HelpCommon.packagMsg(4);
            iActivityService.updateActivityStatus(activityId, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增加活动页面
     */
    @RequestMapping("addActivityPage")
    public ModelAndView addActivityPage(HttpServletRequest request, HttpServletResponse response, Integer activityId) {
        ModelAndView modelAndView = new ModelAndView();
        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        TbActivity activity = new TbActivity();
        if("admin".equals(user.getLoginName())){
            shopList = iShopService.findShopAll();
        }else{
            shopList = iShopService.findShopAll(user.getShopId());
            if(CollectionUtils.isNotEmpty(shopList)){
                activity.setShopId(shopList.get(0).getShopId());
            }
        }
        activity.setDataIndex(0);
        if (null != activityId && activityId > 0) {
            activity = iActivityService.loadActivity(activityId);
            if(!"admin".equals(user.getLoginName())&&activity.getShopId()!=user.getShopId()){
                modelAndView.setViewName("404");
                return modelAndView;
            }
        }

        modelAndView.addObject("shopList",shopList);
        modelAndView.addObject("webImg", Constants.IMG_WEB_URL);
        modelAndView.addObject("nowDate", DatetimeUtil.getNow("yyyy-MM-dd"));
        modelAndView.addObject("activity", activity);
        modelAndView.setViewName("add-activity");
        return modelAndView;
    }

    /**
     * 保存数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("saveActivityData")
    public void saveData(HttpServletRequest request, HttpServletResponse response, TbActivity activity) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg(6);
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            activity.setCreatetime(new Timestamp(new Date().getTime()));
            activity.setCreateman(user.getLoginName());
            if (activity.getActivityId() > 0) {
                out = HelpCommon.packagMsg(5);
            }
            iActivityService.saveActivity(activity);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
