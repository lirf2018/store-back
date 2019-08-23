package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.BannerCondition;
import com.yufan.pojo.TbActivity;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbBanner;
import com.yufan.service.banner.IBannerService;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 11:41
 * 功能介绍:  banner管理
 */
@Controller
@RequestMapping(value = "/banner/")
public class BannerController {

    @Autowired
    private IBannerService iBannerService;

    /**
     * 管理页面
     *
     * @return
     */
    @RequestMapping("bannerPage")
    public ModelAndView toBannerPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("banner-list");
        return modelAndView;
    }

    /**
     * 加载分页数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("loadBannerPageData")
    public void loadPageData(HttpServletRequest request, HttpServletResponse response, BannerCondition bannerCondition) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页

            //条件
            String bannerName = request.getParameter("bannerName");
            bannerCondition.setBannerName(bannerName);

            pageInfo = iBannerService.loadDataPage(currePage, bannerCondition);
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
    @RequestMapping("updateBannerStatus")
    public void updateDataStatus(HttpServletRequest request, HttpServletResponse response, Integer bannerId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? CommonMethod.packagMsg("3") : CommonMethod.packagMsg("4");
            iBannerService.updateBannerStatus(bannerId, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 新增加活动页面
     */
    @RequestMapping("addBannerPage")
    public ModelAndView addBannerPage(HttpServletRequest request, HttpServletResponse response, Integer bannerId) {
        ModelAndView modelAndView = new ModelAndView();

        TbBanner banner = new TbBanner();
        banner.setDataIndex(0);
        if (null != bannerId && bannerId > 0) {
            banner = iBannerService.loadBanner(bannerId);
        }
        modelAndView.addObject("webImg", Constants.IMG_URL);
        modelAndView.addObject("nowDate", DatetimeUtil.getNow("yyyy-MM-dd"));
        modelAndView.addObject("banner", banner);
        modelAndView.setViewName("add-Banner");
        return modelAndView;
    }

    /**
     * 保存数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("saveBannerData")
    public void saveData(HttpServletRequest request, HttpServletResponse response, TbBanner banner) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = CommonMethod.packagMsg("6");
            if (banner.getBannerId() > 0) {
                out = CommonMethod.packagMsg("5");
            }
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            banner.setCreateman(user.getLoginName());
            banner.setCreatetime(new Timestamp(new Date().getTime()));
            iBannerService.saveBanner(banner);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
