package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.MenuObj;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbFunctions;
import com.yufan.service.func.IMenuService;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/29 17:36
 * 功能介绍: 菜单管理
 */
@Controller
@RequestMapping("/menu/")
public class MenuController {

    @Autowired
    private IMenuService iMenuService;

    @RequestMapping("menuPage")
    public ModelAndView toMenuPage() {

        ModelAndView modelAndView = new ModelAndView();

        List<TbFunctions> listFunc = iMenuService.listUserPrivFunctionParent(0);

        modelAndView.addObject("listFunc", listFunc);
        modelAndView.setViewName("menu-list");
        return modelAndView;
    }

    /**
     * 查询菜单列表
     *
     * @return
     */
    @RequestMapping("loadMenuList")
    public void loadMenuList(TbFunctions func, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页
            pageInfo = iMenuService.loadMenuPage(currePage, func);
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
     * 跳转到菜单树页面
     *
     * @return
     */
    @RequestMapping("toMenuTreePage")
    public ModelAndView toMenuTreePage(HttpServletRequest request, HttpServletResponse response) {

        List<MenuObj> menuTreeList = new ArrayList<>();
        //查询菜单
        List<TbFunctions> functions = iMenuService.listUserPrivFunction();
        for (TbFunctions f : functions) {
            if (f.getFunctionParentid() == 0) {
                MenuObj menu = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(f)), MenuObj.class);
                menuTreeList.add(menu);
            }
        }
        for (MenuObj m : menuTreeList) {
            List<TbFunctions> sun = new ArrayList<>();
            for (TbFunctions f : functions) {
                if (f.getFunctionParentid() == m.getFunctionId()) {
                    sun.add(f);
                }
            }
            m.setChildMenu(sun);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("menuTreeList", menuTreeList);
        modelAndView.addObject("functions", functions);
        modelAndView.addObject("parentId", StringUtils.isEmpty(request.getParameter("parentId")) ? 0 : Integer.parseInt(request.getParameter("parentId")));
        modelAndView.setViewName("menu-tree");
        return modelAndView;
    }


    /**
     * 增加菜单
     */
    @RequestMapping("addMenuTree")
    public void addMenuTree(TbFunctions func, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            Integer parentId = Integer.parseInt(request.getParameter("func.functionParentid"));
            func.setFunctionParentid(parentId);
            Integer dataIndex = Integer.parseInt(request.getParameter("func.dataIndex"));
            func.setDataIndex(dataIndex);
            String menuName = request.getParameter("func.functionName");
            func.setFunctionName(menuName.trim());
            String menuCode = request.getParameter("func.functionCode");
            func.setFunctionCode(menuCode.trim());
            String menuUrl = request.getParameter("func.functionAction");
            func.setFunctionAction(parentId > 0 ? menuUrl.trim() : "");
            String remark = request.getParameter("func.remark");
            func.setRemark(remark.trim());
            Integer functionType = Integer.parseInt(request.getParameter("func.functionType"));
            func.setFunctionType(functionType);
            Integer status = Integer.parseInt(request.getParameter("func.status"));
            func.setStatus(status);
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            func.setCreateman(user.getLoginName());
            func.setCreatetime(new Timestamp(new Date().getTime()));
            if (!StringUtils.isEmpty(request.getParameter("func.functionId"))) {
                Integer menuId = Integer.parseInt(request.getParameter("func.functionId"));
                func.setFunctionId(menuId);
            }
            JSONObject result = 0 == func.getFunctionId() ? CommonMethod.packagMsg("6") : CommonMethod.packagMsg("5");
            iMenuService.saveFuntion(func);
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改菜单状态
     *
     * @param request
     * @param response
     * @param menuId
     * @param status
     */
    @PostMapping("updateMenuStatus")
    public void updateMenuStatus(HttpServletRequest request, HttpServletResponse response, Integer menuId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = status == 0 ? CommonMethod.packagMsg("3") : CommonMethod.packagMsg("4");
            iMenuService.updateMenuStatus(menuId, status);
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除菜单
     *
     * @param request
     * @param response
     */
    @RequestMapping("deleteMenu")
    public void deleteMenu(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = CommonMethod.packagMsg("3");
            Integer menuId = Integer.parseInt(request.getParameter("menuId"));
            Integer parentId = Integer.parseInt(request.getParameter("parentId"));
            iMenuService.deleteMenu(menuId, parentId);
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
