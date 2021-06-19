package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.ComplainCondition;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbComplain;
import com.yufan.pojo.TbInfo;
import com.yufan.pojo.TbShop;
import com.yufan.service.commonrel.ICommonRelService;
import com.yufan.service.complain.IComplainService;
import com.yufan.service.shop.IShopService;

import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.HelpCommon;
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
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 11:54
 * 功能介绍:  投诉建议
 */
@Controller
@RequestMapping(value = "/complain/")
public class ComplainController {

    @Autowired
    private IComplainService iComplainService;

    @Autowired
    private IShopService iShopService;

    @Autowired
    private ICommonRelService iCommonRelService;

    /**
     * 管理页面
     *
     * @return
     */
    @RequestMapping("complainPage")
    public ModelAndView toComplainPage(HttpServletRequest request, HttpServletResponse response) {
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
        modelAndView.setViewName("complain-list");
        return modelAndView;
    }

    /**
     * 加载分页数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("loadComplainPageData")
    public void loadPageData(HttpServletRequest request, HttpServletResponse response, ComplainCondition complainCondition) {
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
                complainCondition.setShopId(shopId);
            }
            //条件
            String userId = request.getParameter("userId");
            if (!StringUtils.isEmpty(userId.trim())) {
                complainCondition.setUserId(Integer.parseInt(userId.trim()));
            }
            String information = request.getParameter("information");
            complainCondition.setInformation(information);
            String contents = request.getParameter("contents");
            complainCondition.setContents(contents);
            String status = request.getParameter("status");
            if (!"-1".equals(status)) {
                complainCondition.setStatus(Integer.parseInt(status));
            }
            String isRead = request.getParameter("isRead");
            if (!"-1".equals(isRead)) {
                complainCondition.setIsRead(Integer.parseInt(isRead));
            }
            String complainType = request.getParameter("complainType");
            if (!"-1".equals(complainType)) {
                complainCondition.setComplainType(Integer.parseInt(complainType));
            }

            pageInfo = iComplainService.loadDataPage(currePage, complainCondition);
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
    @RequestMapping("updateComplainStatus")
    public void updateDataStatus(HttpServletRequest request, HttpServletResponse response, Integer complainId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg("3");
            iComplainService.updateComplainStatus(complainId, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改数据已读
     */
    @RequestMapping("updateComplainIsRead")
    public void updateDataIsRead(HttpServletRequest request, HttpServletResponse response, Integer complainId, Integer isRead) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg("17");
            iComplainService.updateComlpainIsRead(complainId, isRead);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 回复
     */
    @RequestMapping("updateAnswer")
    public void updateDataAnswer(HttpServletRequest request, HttpServletResponse response, Integer complainId, String answer) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg("1");
            iComplainService.updateAnswer(complainId, answer);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增加页面
     */
    @RequestMapping("complainInfo")
    public ModelAndView complainInfo(HttpServletRequest request, HttpServletResponse response, Integer id) {
        ModelAndView modelAndView = new ModelAndView();

        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if (null == id || id == 0 || !"admin".equals(user.getLoginName())) {
            modelAndView.setViewName("404");
            return modelAndView;
        }

        if (!"admin".equals(user.getLoginName())) {
            modelAndView.setViewName("404");
            return modelAndView;
        }

        TbComplain complain = iComplainService.loadComplain(id);
        //查询关联图片
        List<String> outImgList = new ArrayList<>();
        List<Map<String, Object>> listImg = iCommonRelService.queryTableRelImg(null, id, Constants.IMG_CLASSIFY_SUGGEST);
        if (listImg.size() > 0) {
            for (int i = 0; i < listImg.size(); i++) {
                String imgUrl = listImg.get(i).get("img_url") == null ? "" : listImg.get(i).get("img_url").toString();
                outImgList.add(Constants.IMG_WEB_URL + imgUrl);
            }
        }
        modelAndView.addObject("webImg", Constants.IMG_WEB_URL);
        modelAndView.addObject("complain", complain);
        modelAndView.addObject("outImgList", outImgList);
        modelAndView.setViewName("complain-info");
        return modelAndView;
    }

}
