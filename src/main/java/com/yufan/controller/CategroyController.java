package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.CategoryCondition;
import com.yufan.bean.CategoryObj;
import com.yufan.bean.ItempropObj;
import com.yufan.bean.PropValueObj;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbCategory;
import com.yufan.pojo.TbCategoryLevel;
import com.yufan.service.category.ICategoryService;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/7 9:23
 * 功能介绍: 分类管理
 */
@Controller
@RequestMapping(value = "/category/")
public class CategroyController {

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 跳转到一级分类管理页面
     *
     * @return
     */
    @RequestMapping("levelPage")
    public ModelAndView toLevelPage() {
        ModelAndView modelAndView = new ModelAndView();

        //查询类目列表
        List<Map<String, Object>> categoryListMap = iCategoryService.loadCategoryListMap(1);


        modelAndView.addObject("categoryListMap", categoryListMap);
        modelAndView.setViewName("level-list");
        return modelAndView;
    }

    @RequestMapping("loadOrderData")
    public void loadOrderData(CategoryCondition categoryCondition, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页


            pageInfo = iCategoryService.loadLevelPage(currePage, categoryCondition);

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
     * 根据一级分类查询类目
     *
     * @param request
     * @param response
     * @param levelId
     */
    @RequestMapping("loadLevelCategoryData")
    public void loadLevelCategoryData(HttpServletRequest request, HttpServletResponse response, Integer levelId, Integer status) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            List<Map<String, Object>> list = iCategoryService.loadLevelCategoryData(levelId, status);
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
     * 增加一级分类页面
     */
    @RequestMapping("addLevelPage")
    public ModelAndView toAddLevelPage(HttpServletRequest request, HttpServletResponse response, Integer levelId) {
        ModelAndView modelAndView = new ModelAndView();
        //查询关联的类目
        List<Map<String, Object>> categoryListMap = new ArrayList<>();
        TbCategoryLevel categoryLevel = new TbCategoryLevel();
        categoryLevel.setStatus(1);
        categoryLevel.setDataIndex(0);
        //已关联的类目
        Map<String, String> relMap = new HashMap<>();
        if (null != levelId && levelId > 0) {
            categoryLevel = iCategoryService.loadLevelById(levelId);
            String imgUrl = categoryLevel.getLevelImg();
            categoryListMap = iCategoryService.loadLevelCategoryData(levelId, 1);
            for (int i = 0; i < categoryListMap.size(); i++) {
                String categoryId = categoryListMap.get(i).get("category_id").toString();
                relMap.put(categoryId, categoryId);
            }
            if(!StringUtils.isEmpty(imgUrl)){
                modelAndView.addObject("imgWebUrl", Constants.IMG_WEB_URL+imgUrl);//
            }
        }

        String categoryIdsHasRel = "";//已关联的类目
        //查询所有类目列表
        List<Map<String, Object>> allCategoryListMap = iCategoryService.loadCategoryListMap(1);
        //处理类目
        List<Map<String, Object>> allCategoryListMapOut = new ArrayList<>();
        for (int i = 0; i < allCategoryListMap.size(); i++) {
            Map<String, Object> map = allCategoryListMap.get(i);
            map.put("rel", 0);//未关联
            if (relMap.get(map.get("category_id").toString()) != null) {
                map.put("rel", 1);//已关联
                categoryIdsHasRel = categoryIdsHasRel + map.get("category_id") + ",";
            }
            allCategoryListMapOut.add(map);
        }
        modelAndView.addObject("categoryLevel", categoryLevel);//一级分类对象
        modelAndView.addObject("allCategoryListMapOut", allCategoryListMapOut);//标记未关联和已关联的全部类目
        modelAndView.addObject("categoryIdsHasRel", categoryIdsHasRel);//已关联的类目

        modelAndView.setViewName("add-level");
        return modelAndView;
    }

    /**
     * 增加一级分类
     *
     * @param request
     * @param response
     */
    @RequestMapping("saveLevel")
    public void saveLevel(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject data = new JSONObject();
            data.put("levelId", request.getParameter("categoryLevel.levelId"));
            data.put("levelCode", request.getParameter("categoryLevel.levelCode"));
            data.put("levelName", request.getParameter("categoryLevel.levelName"));
            data.put("levelImg", request.getParameter("categoryLevel.levelImg"));
            data.put("dataIndex", request.getParameter("categoryLevel.dataIndex"));
            data.put("status", request.getParameter("categoryLevel.status"));
            data.put("remark", request.getParameter("categoryLevel.remark"));
            data.put("retain", request.getParameter("retain"));//是否保留原来关联
            data.put("categoryIds", request.getParameter("categoryIds"));//新关联的类目
            data.put("categoryIdsHasRel", request.getParameter("categoryIdsHasRel"));//已关联的类目
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            data.put("createman", user.getLoginName());
            JSONObject out = iCategoryService.saveLevel(data);

            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改数据状态
     */
    @RequestMapping("updateLevelStatus")
    public void updateDataStatus(HttpServletRequest request, HttpServletResponse response, Integer LevelId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? CommonMethod.packagMsg("3") : CommonMethod.packagMsg("4");
            iCategoryService.updateLevelStatus(LevelId, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //**********************************************************类目管理**************************************************************************//

    /**
     * 类目管理页面
     *
     * @param request
     * @param response
     * @param levelId
     * @return
     */
    @RequestMapping("categoryPage")
    public ModelAndView toCategoryPage(HttpServletRequest request, HttpServletResponse response, Integer levelId) {
        ModelAndView modelAndView = new ModelAndView();
        //查询一级分类列表
        List<Map<String, Object>> levelListMap = iCategoryService.loadLevelListMap(Constants.DATA_STATUS_YX);

        List<Map<String, Object>> categoryItemRelListMap = iCategoryService.loadCategoryItemRelListMap(null, null, null);

        //一级分类与类目关系
        Map<Integer, Integer> rel = null;
        if (levelId != null) {
            rel = new HashMap<>();
            List<Map<String, Object>> relList = iCategoryService.loadLeveCategoryRel(levelId, null);
            for (int i = 0; i < relList.size(); i++) {
                Integer categoryIdRel = Integer.parseInt(relList.get(i).get("category_id").toString());
                //Integer levelIdRel = Integer.parseInt(relList.get(i).get("level_id").toString());
                rel.put(categoryIdRel, categoryIdRel);
            }
        }

        //去重
        Map<Integer, Integer> distinctMap = new HashMap<>();
        //类目列表
        List<CategoryObj> categoryObjList = new ArrayList<>();
        for (int i = 0; i < categoryItemRelListMap.size(); i++) {
            Map<String, Object> map = categoryItemRelListMap.get(i);
            int categoryId = Integer.parseInt(map.get("ca_category_id").toString());
            String categoryName = map.get("ca_category_name").toString();
            Integer dataIndex = Integer.parseInt(map.get("ca_data_index").toString());
            String categoryImg = map.get("ca_category_img").toString();
            String categoryWebImg = map.get("ca_category_web_img").toString();
            String categoryCode = map.get("ca_category_code").toString();
            Integer status = Integer.parseInt(map.get("ca_status").toString());
            //编号	类目名称	编码	图片	排序   状态	 操作
            if (null != distinctMap.get(categoryId)) {
                continue;
            }
            //条件levelId
            if (null != rel && rel.get(categoryId) == null) {
                continue;
            }

            CategoryObj categoryObj = new CategoryObj();
            categoryObj.setCategoryId(categoryId);
            categoryObj.setCategoryCode(categoryCode);
            categoryObj.setDataIndex(dataIndex);
            categoryObj.setCategoryName(categoryName);
            categoryObj.setCategoryImg(categoryImg);
            categoryObj.setCategoryWebImg(categoryWebImg);
            categoryObj.setStatus(status);
            categoryObjList.add(categoryObj);
            distinctMap.put(categoryId, categoryId);
        }
        distinctMap.clear();
        //处理类目属性
        for (int i = 0; i < categoryObjList.size(); i++) {
            CategoryObj categoryObj = categoryObjList.get(i);
            List<ItempropObj> itempropObjList = new ArrayList<>();
            for (int j = 0; j < categoryItemRelListMap.size(); j++) {
                Map<String, Object> map = categoryItemRelListMap.get(j);
                //属性名称	状态	排序	是否销售属性	操作
                ItempropObj itempropObj = new ItempropObj();
                if (null == map.get("it_prop_id")) {
                    continue;
                }
                Integer propId = Integer.parseInt(map.get("it_prop_id").toString());
                String propName = map.get("it_prop_name").toString();//属性名
                Integer status = Integer.parseInt(map.get("it_status").toString());
                Integer dataIndex = Integer.parseInt(map.get("it_data_index").toString());
                Integer categoryId = Integer.parseInt(map.get("it_category_id").toString());
                Integer isSales = Integer.parseInt(map.get("it_is_sales").toString());
                String propImg = map.get("it_prop_img").toString();
                String propWebImg = map.get("it_prop_web_img").toString();
                if (distinctMap.get(propId) != null || categoryObj.getCategoryId() != categoryId) {
                    continue;
                }
                itempropObj.setPropId(propId);
                itempropObj.setPropName(propName);
                itempropObj.setStatus(status);
                itempropObj.setDataIndex(dataIndex);
                itempropObj.setIsSales(isSales);
                itempropObj.setPropImg(propImg);
                itempropObj.setPropWebImg(propWebImg);
                itempropObjList.add(itempropObj);
                distinctMap.put(propId, propId);
            }
            categoryObj.setItempropObjList(itempropObjList);
        }


        modelAndView.addObject("levelId", levelId);
        modelAndView.addObject("levelListMap", levelListMap);
        modelAndView.addObject("categoryObjList", categoryObjList);
        modelAndView.addObject("webImg", Constants.IMG_WEB_URL);
//        modelAndView.addObject("level", level);
        modelAndView.setViewName("category-list");
        return modelAndView;
    }

    /**
     * 根据类目查询属性
     */
    @RequestMapping("loadItemProp")
    public void loadItemProp(HttpServletRequest request, HttpServletResponse response, Integer propId, Integer categoryId) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            List<Map<String, Object>> list = iCategoryService.loadItemPropListMap(categoryId, propId, Constants.DATA_STATUS_YX);
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
     * 根据类目查询属性值
     */
    @RequestMapping("loadItemPropValue")
    public void loadItemPropValue(HttpServletRequest request, HttpServletResponse response, Integer categoryId, Integer propId, Integer valueId) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            List<Map<String, Object>> list = iCategoryService.loadItemPropValueListMap(categoryId, propId, valueId, Constants.DATA_STATUS_YX);
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
     * 更新类目状态
     */
    @RequestMapping("updateCategoryStatus")
    public void updateCategoryStatus(HttpServletRequest request, HttpServletResponse response, Integer categoryId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? CommonMethod.packagMsg("3") : CommonMethod.packagMsg("4");
            iCategoryService.updateCategoryStatus(categoryId, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新类目属性状态
     */
    @RequestMapping("updateCategoryPropStatus")
    public void updateCategoryPropStatus(HttpServletRequest request, HttpServletResponse response, Integer propId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? CommonMethod.packagMsg("3") : CommonMethod.packagMsg("4");
            iCategoryService.updateCategoryPropStatus(propId, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询类目ById
     */
    @RequestMapping("loadCategory")
    public void loadCategory(HttpServletRequest request, HttpServletResponse response, Integer categoryId) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            List<Map<String, Object>> list = iCategoryService.loadCategoryItemRelListMap(categoryId, null, null);
            JSONObject result = new JSONObject();
            result.put("data", list.get(0));
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 查询类目属性ById
     */
    @RequestMapping("loadCategoryProp")
    public void loadCategoryProp(HttpServletRequest request, HttpServletResponse response, Integer categoryId, Integer propId) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();

            //查询属性和属性值
            List<Map<String, Object>> listItemPropRelAll = iCategoryService.loadItemAllRelListMap(categoryId, null, 1);

            //属性
            ItempropObj itempropObj = new ItempropObj();
            for (int i = 0; i < listItemPropRelAll.size(); i++) {
                if (null == listItemPropRelAll.get(i).get("it_prop_id")) {
                    continue;
                }
                Integer itPropId = Integer.parseInt(listItemPropRelAll.get(i).get("it_prop_id").toString());//属性ID
                String propName = listItemPropRelAll.get(i).get("it_prop_name").toString();//属性名
                Integer isSales = Integer.parseInt(listItemPropRelAll.get(i).get("it_is_sales").toString());//是否销售属性:0不是销售属性1是销售属性
                Integer dataIndex = Integer.parseInt(listItemPropRelAll.get(i).get("it_data_index").toString());//
                String showView = listItemPropRelAll.get(i).get("it_show_view").toString();//显示类型:checkbox：多选 select：下拉列表',
                String propCode = listItemPropRelAll.get(i).get("it_prop_code").toString();//
                String propWebImg = listItemPropRelAll.get(i).get("it_prop_web_img").toString();//
                String propImg = listItemPropRelAll.get(i).get("it_prop_img").toString();//
                String remark = listItemPropRelAll.get(i).get("it_remark").toString();//
                String outeId = listItemPropRelAll.get(i).get("it_oute_id").toString();//

                if (itPropId == propId) {
                    itempropObj.setPropId(propId);
                    itempropObj.setPropName(propName);
                    itempropObj.setIsSales(isSales);
                    itempropObj.setShowView(showView);
                    itempropObj.setPropCode(propCode);
                    itempropObj.setPropWebImg(propWebImg);
                    itempropObj.setPropImg(propImg);
                    itempropObj.setOuteId(outeId);
                    itempropObj.setDataIndex(dataIndex);
                    itempropObj.setRemark(remark);
                    break;
                }
            }
            //属性值
            Map<Integer, Integer> repeatMap = new HashMap<>();
            List<PropValueObj> propValueObjList = new ArrayList<>();
            for (int j = 0; j < listItemPropRelAll.size(); j++) {
                if (listItemPropRelAll.get(j).get("pv_prop_id") == null) {
                    continue;
                }
                Integer pvPropId = Integer.parseInt(listItemPropRelAll.get(j).get("pv_prop_id").toString());
                Integer dataIndex = Integer.parseInt(listItemPropRelAll.get(j).get("pv_data_index").toString());
                Integer valueId = Integer.parseInt(listItemPropRelAll.get(j).get("pv_value_id").toString());
                String valueName = String.valueOf(listItemPropRelAll.get(j).get("pv_value_name"));
                String value = String.valueOf(listItemPropRelAll.get(j).get("pv_value"));
                String pvOuteId = String.valueOf(listItemPropRelAll.get(j).get("pv_oute_id"));
                if (pvPropId == propId && null == repeatMap.get(valueId)) {
                    PropValueObj propValueObj = new PropValueObj();
                    propValueObj.setValue(value);
                    propValueObj.setValueId(valueId);
                    propValueObj.setValueName(valueName);
                    propValueObj.setOuteId(pvOuteId);
                    propValueObj.setDataIndex(dataIndex);

                    propValueObjList.add(propValueObj);
                    repeatMap.put(valueId, valueId);
                }
            }
            itempropObj.setPropValueObjList(propValueObjList);
            JSONObject result = new JSONObject();
            result.put("data", itempropObj);
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  保存类目
     */
    @RequestMapping("saveCategory")
    public void saveCategory(HttpServletRequest request, HttpServletResponse response, TbCategory category) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            //校验编码
            boolean flag = iCategoryService.checkCategoryCode(category.getCategoryId(),category.getCategoryCode());
            if(flag){
                JSONObject out = CommonMethod.packagMsg("15");
                writer.print(out);
                writer.close();
                return;
            }
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            category.setCreateman(user.getLoginName());
            JSONObject out = category.getCategoryId() > 0 ? CommonMethod.packagMsg("5") : CommonMethod.packagMsg("6");
            iCategoryService.saveCategory(category);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  保存类目属性
     */
    @RequestMapping("saveCategoryProp")
    public void saveCategoryProp(HttpServletRequest request, HttpServletResponse response, ItempropObj itempropObj) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = iCategoryService.saveCategoryProp(itempropObj);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
