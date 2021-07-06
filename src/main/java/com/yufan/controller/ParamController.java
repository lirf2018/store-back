package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.cache.LoadCacheService;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbParam;
import com.yufan.service.param.IParamCodeService;
import com.yufan.utils.CacheData;

import com.yufan.utils.Constants;
import com.yufan.utils.HelpCommon;
import com.yufan.utils.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/22 17:26
 * 功能介绍:
 */
@RestController
@RequestMapping(value = "/param/")
public class ParamController {
    private Logger LOG = Logger.getLogger(ParamController.class);

    @Autowired
    private IParamCodeService iParamCodeService;

    @RequestMapping(value = "paramPage")
    public ModelAndView toParamCodePage() {

        List<Map<String, Object>> paramGroupNameList = iParamCodeService.loadParamGroupName();

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("paramGroupNameList", paramGroupNameList);

        modelAndView.setViewName("param-list");
        return modelAndView;
    }

    @RequestMapping("loadParamData")
    public void loadParamData(TbParam paramCode, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页

            paramCode.setParamName(StringUtils.isEmpty(request.getParameter("paramName")) ? "" : request.getParameter("paramName"));
            paramCode.setParamCode(StringUtils.isEmpty(request.getParameter("paramCode")) ? "" : request.getParameter("paramCode").trim());
            pageInfo = iParamCodeService.loadParamCodePage(currePage, paramCode);
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
     * 跳转到增加参数页面
     *
     * @return
     */
    @RequestMapping("addParamPage")
    public ModelAndView toAddParamCodePage(HttpServletRequest request, Integer paramId) {

        ModelAndView modelAndView = new ModelAndView();
        TbParam param = new TbParam();
        param.setDataIndex(0);
        if (null != paramId && paramId > 0) {
            param = iParamCodeService.loadTbParamCodeById(paramId);
        }
        modelAndView.addObject("paramObj", param);
        modelAndView.setViewName("add-param");
        return modelAndView;


    }

    /**
     * 保存参数
     *
     * @param paramCode
     * @param request
     * @param response
     */
    @PostMapping("saveParamCode")
    public void saveParamCode(TbParam paramCode, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = HelpCommon.packagMsg("1");
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            paramCode.setCreateman(user.getLoginName());
            paramCode.setCreatetime(new Timestamp(new Date().getTime()));

            if (!StringUtils.isEmpty(request.getParameter("paramObj.paramId"))) {
                paramCode.setParamId(Integer.parseInt(request.getParameter("paramObj.paramId")));
            }
            String code = request.getParameter("paramObj.paramCode");
            paramCode.setParamCode(code);
            String paramKey = request.getParameter("paramObj.paramKey");
            paramCode.setParamKey(paramKey);
            String paramName = request.getParameter("paramObj.paramName");
            paramCode.setParamName(paramName);
            String paramValue = request.getParameter("paramObj.paramValue");
            paramCode.setParamValue(paramValue);
            String paramValue1 = request.getParameter("paramObj.paramValue1");
            paramCode.setParamValue1(paramValue1);
            String paramValue2 = request.getParameter("paramObj.paramValue2");
            paramCode.setParamValue2(paramValue2);
            String remark = request.getParameter("paramObj.remark");
            paramCode.setRemark(remark);
            paramCode.setStatus(1);
            int dataIndex = Integer.parseInt(StringUtils.isEmpty(request.getParameter("paramObj.dataIndex"))?"0":request.getParameter("paramObj.dataIndex"));
            paramCode.setDataIndex(dataIndex);
            iParamCodeService.saveParamCode(paramCode);
            writer.print(result);
            writer.close();
            initParam();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改参数状态
     *
     * @param request
     * @param response
     * @param paramId
     * @param status
     */
    @PostMapping("updateParamCodeStatus")
    public void updateParamCodeStatus(HttpServletRequest request, HttpServletResponse response, Integer paramId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = HelpCommon.packagMsg("1");
            iParamCodeService.updateParamCodeStatus(paramId, status);
            writer.print(result);
            writer.close();
            initParam();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化参数
     */
    private void initParam() {
        LOG.info("-----开始初始华参数----");
        CacheData.PARAMLIST = iParamCodeService.loadTbParamCodeList(1);
        for (int i = 0; i < CacheData.PARAMLIST.size(); i++) {
            TbParam param = CacheData.PARAMLIST.get(i);
            if ("sys_code".equals(param.getParamCode()) && "img_save_root_path".equals(param.getParamKey())) {
                Constants.IMG_SAVE_ROOT_PATH = param.getParamValue().endsWith("\\") ? param.getParamValue() : param.getParamValue() + "\\";
            } else if ("sys_code".equals(param.getParamCode()) && "img_web_path".equals(param.getParamKey())) {
                Constants.IMG_WEB_URL = param.getParamValue().endsWith("/") ? param.getParamValue() : param.getParamValue() + "/";
            }
        }
        LOG.info("-------Constants.IMG_SAVE_ROOT_PATH-------" + Constants.IMG_SAVE_ROOT_PATH);
        LOG.info("--------Constants.IMG_WEB_URL------" + Constants.IMG_WEB_URL);
        LOG.info("-----结束初始华参数----" + CacheData.PARAMLIST.size());
    }


    /**
     * 刷新缓存
     * @param request
     * @param response
     */
    @PostMapping("refreshCache")
    public void refreshCache(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = HelpCommon.packagMsg("1");
            iParamCodeService.refreshCache();
            writer.print(result);
            writer.close();
            initParam();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
