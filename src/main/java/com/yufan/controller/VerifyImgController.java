package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.anno.ClassAnnotation;
import com.yufan.bean.GoodsCondition;
import com.yufan.bean.VerifyImgGroupCondition;
import com.yufan.pojo.*;
import com.yufan.service.param.IParamCodeService;
import com.yufan.service.verify.IVerifyImgService;
import com.yufan.utils.Constants;
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
import java.util.*;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/28
 */
@Controller
@RequestMapping(value = "/verify/")
@ClassAnnotation(name = "verify", desc = "校验管理")
public class VerifyImgController {

    @Autowired
    private IVerifyImgService iVerifyImgService;

    @Autowired
    private IParamCodeService iParamCodeService;

    /**
     * 管理页面
     *
     * @return
     */
    @RequestMapping("verifyImgPage")
    public ModelAndView toBannerPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();

        //查询参数列表
        List<TbParam> listParam = iParamCodeService.loadTbParamCodeList(1, "similar_type");

        modelAndView.addObject("imgPath", Constants.IMG_WEB_URL);
        modelAndView.addObject("listParam", listParam);
        modelAndView.setViewName("verify-img-list");
        return modelAndView;
    }


    /**
     * 加载分页数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("loadVerifyImgPageData")
    public void loadPageData(HttpServletRequest request, HttpServletResponse response, VerifyImgGroupCondition condition) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页

            pageInfo = iVerifyImgService.loadDataPage(currePage, condition);

            //输出参数
            int recordSum = pageInfo.getRecordSum();
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
     *
     */
    @RequestMapping("loadVerifyImg")
    public void loadVerifyImgList(HttpServletRequest request, HttpServletResponse response, String verifyCode, Integer status) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            List<Map<String, Object>> list = iVerifyImgService.loadVerifyImgList(verifyCode, status);
            //输出参数
            JSONObject dataJson = new JSONObject();
            dataJson.put("data", list);
            writer.print(dataJson);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改数据状态
     */
    @RequestMapping("updateVerifyGroupStatus")
    public void updateVerifyGroupStatus(HttpServletRequest request, HttpServletResponse response, Integer id, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? HelpCommon.packagMsg("3") : HelpCommon.packagMsg("4");
            iVerifyImgService.updateVerifyGroupStatus(id, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新背景图片
     *
     * @param request
     * @param response
     * @param id
     * @param img
     */
    @RequestMapping("updateBackImg")
    public void updateBackImg(HttpServletRequest request, HttpServletResponse response, Integer id, String img) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg("1");
            iVerifyImgService.updateBackImg(id, img);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改数据状态
     */
    @RequestMapping("updateVerifyGroup")
    public void updateVerifyGroup(HttpServletRequest request, HttpServletResponse response, TbVerifyImgGroup imgGroup, String oldCode) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg("0");
            if (iVerifyImgService.checkVerifyImgCode(imgGroup.getId(), imgGroup.getVerifyCode())) {
                out = HelpCommon.packagMsg("34");
                writer.print(out);
                writer.close();
                return;
            }
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            imgGroup.setCreateman(user.getLoginName());
            imgGroup.setCreatetime(new Timestamp(System.currentTimeMillis()));
            iVerifyImgService.updateVerifyGroup(imgGroup, oldCode);
            out = HelpCommon.packagMsg("1");
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改数据状态
     */
    @RequestMapping("updateVerifyImgStatus")
    public void updateVerifyImgStatus(HttpServletRequest request, HttpServletResponse response, Integer id, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? HelpCommon.packagMsg("3") : HelpCommon.packagMsg("4");
            iVerifyImgService.updateVerifyImgStatus(id, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 新增数据
     */
    @RequestMapping("addVerifyImg")
    public void addVerifyImg(HttpServletRequest request, HttpServletResponse response, TbVerifyImg verifyImg) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            verifyImg.setCreateman(user.getLoginName());
            verifyImg.setCreatetime(new Timestamp(System.currentTimeMillis()));
            verifyImg.setImgUuid(HelpCommon.uuid());
            JSONObject out = verifyImg.getId() == 0 ? HelpCommon.packagMsg("6") : HelpCommon.packagMsg("5");
            iVerifyImgService.addVerifyImg(verifyImg);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
