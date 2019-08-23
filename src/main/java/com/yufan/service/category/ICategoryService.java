package com.yufan.service.category;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.CategoryCondition;
import com.yufan.bean.ItempropObj;
import com.yufan.pojo.TbCategory;
import com.yufan.pojo.TbCategoryLevel;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/7 9:25
 * 功能介绍:
 */
public interface ICategoryService {

    /**
     * 分页查询一级分类
     *
     * @param currePage
     * @param categroyCondition
     * @return
     */
    PageInfo loadLevelPage(int currePage, CategoryCondition categroyCondition);

    /**
     * 根据一级分类查询类目列表
     *
     * @param levelId
     * @return
     */
    List<Map<String, Object>> loadLevelCategoryData(Integer levelId, Integer status);

    /**
     * 根据状态查询类目列表
     *
     * @param status
     * @return
     */
    List<Map<String, Object>> loadCategoryListMap(Integer status);


    /**
     * 查询一级分类列表By状态
     *
     * @param status
     * @return
     */
    List<Map<String, Object>> loadLevelListMap(Integer status);

    /**
     * 查询一级分类byID
     *
     * @param levelId
     * @return
     */
    TbCategoryLevel loadLevelById(int levelId);


    /**
     * 保存一级分类
     *
     * @param data
     * @return
     */
    JSONObject saveLevel(JSONObject data);

    /**
     * 查询商品属性列表
     *
     * @param goodsId
     * @return
     */
    List<Map<String, Object>> loadGoodsAttr(int goodsId);


    /**
     * 查询所有类目,属性,属性值
     *
     * @return
     */
    List<Map<String, Object>> loadCategoryAllRelListMap(Integer categoryId, Integer caStatus, Integer itStatus, Integer pvStatus);

    /**
     * 查询所有类目,属性
     *
     * @return
     */
    List<Map<String, Object>> loadCategoryItemRelListMap(Integer categoryId, Integer caStatus, Integer itStatus);

    /**
     * 查询所有属性,属性值
     *
     * @return
     */
    List<Map<String, Object>> loadItemAllRelListMap(Integer categoryId, Integer itStatus, Integer pvStatus);

    /**
     * 更新一级分类状态
     *
     * @param levelId
     * @param status
     */
    void updateLevelStatus(int levelId, int status);

    /**
     * 查询属性列表
     *
     * @param categoryId
     * @return
     */
    List<Map<String, Object>> loadItemPropListMap(Integer categoryId, Integer propId, Integer status);

    /**
     * 查询属性值列表
     *
     * @param categoryId
     * @param propId
     * @return
     */
    List<Map<String, Object>> loadItemPropValueListMap(Integer categoryId, Integer propId, Integer valueId, Integer status);

    /**
     * 查询一级分类与类目关联表
     *
     * @param levelId
     * @param categoryId
     * @return
     */
    public List<Map<String, Object>> loadLeveCategoryRel(Integer levelId, Integer categoryId);

    /**
     * 更新类目状态
     *
     * @param categoryId
     * @param status
     */
    void updateCategoryStatus(int categoryId, int status);

    /**
     * 更新类目属性状态
     *
     * @param propId
     * @param status
     */
    void updateCategoryPropStatus(int propId, int status);

    /**
     * 检查编码是否存在
     *
     * @param categoryId
     * @param categoryCode
     * @return
     */
    public boolean checkCategoryCode(Integer categoryId, String categoryCode);

    /**
     * 保存类目
     *
     * @param category
     */
    void saveCategory(TbCategory category);


    /**
     * 保存类目属性
     *
     * @param itempropObj
     */
    JSONObject saveCategoryProp(ItempropObj itempropObj);


}
