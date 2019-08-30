package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.service.second.ISecondGoodsService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/30 18:02
 * 功能介绍:  只提供简单浏览的简单商品
 */
@Controller
@RequestMapping(value = "/second/")
public class SecondGoodsController {

    @Autowired
    private ISecondGoodsService iSecondGoodsService;


    @RequestMapping("secondGoodsPage")
    public ModelAndView secondGoodsPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();

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
    public ModelAndView addSecondGoodsPage(HttpServletRequest request, HttpServletResponse response,Integer id) {
        ModelAndView modelAndView = new ModelAndView();


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
//            writer.print(out);
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
//            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}