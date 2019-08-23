package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbParam;
import com.yufan.service.param.IParamCodeService;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.PageInfo;
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
        try{
            writer = response.getWriter();
            JSONObject result = CommonMethod.packagMsg("1");
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            paramCode.setCreateman(user.getLoginName());
            paramCode.setCreatetime(new Timestamp(new Date().getTime()));

            if(!StringUtils.isEmpty(request.getParameter("paramObj.paramId"))){
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
            iParamCodeService.saveParamCode(paramCode);
            writer.print(result);
            writer.close();
        }catch (Exception e){
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
            JSONObject result = CommonMethod.packagMsg("1");
            iParamCodeService.updateParamCodeStatus(paramId, status);
            writer.print(result);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
