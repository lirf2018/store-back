package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.anno.ClassAnnotation;
import com.yufan.bean.ActivityCondition;
import com.yufan.bean.MenuObj;
import com.yufan.pojo.*;
import com.yufan.service.category.ICategoryService;
import com.yufan.service.func.IMenuService;
import com.yufan.service.param.IParamCodeService;

import com.yufan.utils.Constants;
import com.yufan.utils.HelpCommon;
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
@ClassAnnotation(name = "menu", desc = "菜单管理")
public class MenuController {

    @Autowired
    private IMenuService iMenuService;

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private IParamCodeService iParamCodeService;

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
            JSONObject result = 0 == func.getFunctionId() ? HelpCommon.packagMsg("6") : HelpCommon.packagMsg("5");
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
            JSONObject result = status == 0 ? HelpCommon.packagMsg("3") : HelpCommon.packagMsg("4");
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
            JSONObject result = HelpCommon.packagMsg("3");
            Integer menuId = Integer.parseInt(request.getParameter("menuId"));
            Integer parentId = Integer.parseInt(request.getParameter("parentId"));
            iMenuService.deleteMenu(menuId, parentId);
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 页面菜单页面菜单页面菜单页面菜单页面菜单页面菜单页面菜单页面菜单页面菜单页面菜单页面菜单页面菜单页面菜单页面菜单页面菜单页面菜单页

    /**
     * 页面菜单
     *
     * @return
     */
    @RequestMapping("pageMenu")
    public ModelAndView toActivityPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        //查询一级分类
        List<Map<String, Object>> listLevel = iCategoryService.loadLevelListMap(Constants.DATA_STATUS_YX);

        modelAndView.addObject("listLevel", listLevel);
        modelAndView.setViewName("pagemenu-list");
        return modelAndView;
    }

    /**
     * 加载分页数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("loadPageMenuData")
    public void loadPageData(HttpServletRequest request, HttpServletResponse response, TbPageMenu pageMenu) {
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
                pageMenu.setShopId(shopId);
            }

            pageInfo = iMenuService.loadMenuWebPage(currePage, pageMenu);
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
    @RequestMapping("updatePageMenuStatus")
    public void updateDataStatus(HttpServletRequest request, HttpServletResponse response, Integer id, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? HelpCommon.packagMsg("3") : HelpCommon.packagMsg("4");
            iMenuService.updateMenuWebStatus(id, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 页面菜单
     *
     * @return
     */
    @RequestMapping("toMenuAddPage")
    public ModelAndView toMenuAddPage(HttpServletRequest request, HttpServletResponse response, Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        //查询一级分类
        List<Map<String, Object>> listLevel = iCategoryService.loadLevelListMap(Constants.DATA_STATUS_YX);
        //查询类目列表
        List<Map<String, Object>> listCategory = iCategoryService.loadCategoryListMap(Constants.DATA_STATUS_YX);
        //查询
        List<TbParam> paramList = iParamCodeService.loadTbParamCodeList(Constants.DATA_STATUS_YX, "sys_code", "main_menu");
        //
        Map<String, String> categoryIdRelMap = new HashMap<>();
        List<Map<String, Object>> listRel = iCategoryService.loadLeveCategoryRel(null, null);
        for (int i = 0; i < listRel.size(); i++) {
            String levelId = listRel.get(i).get("level_id").toString();
            String categoryId = listRel.get(i).get("category_id").toString();
            if (categoryIdRelMap.get(categoryId) == null) {
                categoryIdRelMap.put(categoryId, levelId);
            } else {
                categoryIdRelMap.put(categoryId, categoryIdRelMap.get(categoryId) + "," + levelId);
            }
        }
        String goodsDefaultUrl = "";
        if (paramList.size() > 0) {
            goodsDefaultUrl = paramList.get(0).getParamValue();
        }
        TbPageMenu menuPage = new TbPageMenu();
        menuPage.setRelType(1);
        menuPage.setMenuSort(0);
        menuPage.setMenuUrl(goodsDefaultUrl);
        Map<String, String> relMaplev = new HashMap<>();// 已关联的一级分类
        Map<String, String> relMapCa = new HashMap<>();// 已关联的类目
        if (id != null && id > 0) {
            menuPage = iMenuService.loadPageMenu(id);
            if (StringUtils.isEmpty(menuPage.getMenuUrl())) {
                menuPage.setMenuUrl(goodsDefaultUrl);
            }
            String leve1Ids = menuPage.getLeve1Ids();
            if (!StringUtils.isEmpty(leve1Ids)) {
                String[] leve1IdsArray = leve1Ids.split(",");
                for (int i = 0; i < leve1IdsArray.length; i++) {
                    relMaplev.put(leve1IdsArray[i], leve1IdsArray[i]);
                }
            }
            String categoryIds = menuPage.getCategoryIds();
            if (!StringUtils.isEmpty(categoryIds)) {
                String[] categoryIdsArray = categoryIds.split(",");
                for (int i = 0; i < categoryIdsArray.length; i++) {
                    relMapCa.put(categoryIdsArray[i], categoryIdsArray[i]);
                }
            }
        }

        //处理一级分类
        String leveIdsHasRel = "";
        List<Map<String, Object>> allLeveListMapOut = new ArrayList<>();
        for (int i = 0; i < listLevel.size(); i++) {
            Map<String, Object> map = listLevel.get(i);
            map.put("rel", 0);//未关联
            if (relMaplev.get(map.get("level_id").toString()) != null) {
                map.put("rel", 1);//已关联
                leveIdsHasRel = leveIdsHasRel + map.get("level_id") + ",";
            }
            allLeveListMapOut.add(map);
        }

        //处理类目
        String categoryIdsHasRel = "";
        List<Map<String, Object>> allCategoryListMapOut = new ArrayList<>();
        for (int i = 0; i < listCategory.size(); i++) {
            Map<String, Object> map = listCategory.get(i);
            map.put("rel", 0);//未关联
            if (relMapCa.get(map.get("category_id").toString()) != null) {
                map.put("rel", 1);//已关联
                categoryIdsHasRel = categoryIdsHasRel + map.get("category_id") + ",";
            }
            map.put("level_ids",categoryIdRelMap.get(map.get("category_id").toString()));
            allCategoryListMapOut.add(map);
        }
        modelAndView.addObject("webImg", Constants.IMG_WEB_URL);
        modelAndView.addObject("menuPage", menuPage);
        modelAndView.addObject("goodsDefaultUrl", goodsDefaultUrl);
        //
        modelAndView.addObject("allLeveListMapOut", allLeveListMapOut);
        modelAndView.addObject("leveIdsHasRel", leveIdsHasRel);
        //
        modelAndView.addObject("allCategoryListMapOut", allCategoryListMapOut);
        modelAndView.addObject("categoryIdsHasRel", categoryIdsHasRel);
        modelAndView.addObject("listLevel", listLevel);
        //
        modelAndView.setViewName("add-pagemenu");
        return modelAndView;
    }

    /**
     * 保存数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("savePageMenuData")
    public void saveData(HttpServletRequest request, HttpServletResponse response, TbPageMenu pageMenu) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg("6");
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            pageMenu.setCreatetime(new Timestamp(new Date().getTime()));
            // 校验是否已关联 用于分类页面的关联类型 1关联一级分类 2 关联2级分类
            if(pageMenu.getRelType() == 1){
               String leve1Ids =  pageMenu.getLeve1Ids();

            }else{
                String categoryIds = pageMenu.getCategoryIds();

            }



            if(!StringUtils.isEmpty(pageMenu.getLeve1Ids())){
                String leve1Ids = pageMenu.getLeve1Ids();
                pageMenu.setLeve1Ids(leve1Ids.substring(0,leve1Ids.length()-1));
            }
            if(!StringUtils.isEmpty(pageMenu.getCategoryIds())){
                String categoryIds = pageMenu.getCategoryIds();
                pageMenu.setCategoryIds(categoryIds.substring(0,categoryIds.length()-1));
            }
            if (pageMenu.getId() > 0) {
                out = HelpCommon.packagMsg("5");
            }
            iMenuService.savePageMenuData(pageMenu);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 权限资源同步
     */
    @RequestMapping("synSource")
    public void synSource(HttpServletRequest request, HttpServletResponse response, Integer id, Integer status) {
        PrintWriter writer = null;
        try {

//            sdfs
            iMenuService.updateMenuWebStatus(id, status);
//            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
