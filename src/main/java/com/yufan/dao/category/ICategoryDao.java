package com.yufan.dao.category;

import com.yufan.bean.CategoryCondition;
import com.yufan.pojo.TbCategory;
import com.yufan.pojo.TbItemprops;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/7 9:26
 * 功能介绍:
 */
public interface ICategoryDao {

    /**
     * 分页查询一级分类
     *
     * @param currePage
     * @param categroyCondition
     * @return
     */
    public PageInfo loadLevelPage(int currePage, CategoryCondition categroyCondition);

    /**
     * 根据一级分类查询类目
     *
     * @param levelId
     * @return
     */
    public List<Map<String, Object>> loadLevelCategoryData(Integer levelId, Integer status);

    /**
     * 根据状态查询类目列表
     *
     * @param status
     * @return
     */
    public List<Map<String, Object>> loadCategoryListMap(Integer status);


    /**
     * 查询一级分类列表
     *
     * @param status
     * @return
     */
    public List<Map<String, Object>> loadLevelListMap(Integer status);


    /**
     * 检验一级分类编码
     *
     * @param levelCode
     * @return
     */
    public boolean checkLevelCode(String levelCode, Integer levelId);


    /**
     * 删除一级分类关联
     *
     * @param levelId
     */
    public void deleteLevelCategoryRelByLevelId(int levelId);


    public void saveObject(Object obj);
    public void updateObject(Object obj);


    /**
     * 查询商品属性列表
     *
     * @param goodsId
     * @return
     */
    public List<Map<String, Object>> loadGoodsAttr(int goodsId);

    /**
     * 查询所有类目,属性,属性值
     *
     * @return
     */
    public List<Map<String, Object>> loadCategoryAllRelListMap(Integer categoryId, Integer caStatus, Integer itStatus, Integer pvStatus);

    /**
     * 查询所有类目,属性
     *
     * @return
     */
    public List<Map<String, Object>> loadCategoryItemRelListMap(Integer categoryId, Integer caStatus, Integer itStatus);


    /**
     * 查询所有属性,属性值
     *
     * @return
     */
    public List<Map<String, Object>> loadItemAllRelListMap(Integer categoryId, Integer itStatus, Integer pvStatus);

    /**
     * 更新一级分类状态
     *
     * @param levelId
     * @param status
     */
    public void updateLevelStatus(int levelId, int status);

    /**
     * 根据类目标识查询属性值
     *
     * @param categoryId
     * @return
     */
    public List<Map<String, Object>> loadPropValueByCategoryId(int categoryId);

    /**
     * 查询属性列表
     *
     * @param categoryId
     * @return
     */
    public List<Map<String, Object>> loadItemPropListMap(Integer categoryId, Integer propId, Integer status);

    /**
     * 查询属性值列表
     *
     * @param categoryId
     * @param propId
     * @return
     */
    public List<Map<String, Object>> loadItemPropValueListMap(Integer categoryId, Integer propId, Integer valueId, Integer status);

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
    public void updateCategoryStatus(int categoryId, int status);

    /**
     * 更新类目属性状态
     *
     * @param propId
     * @param status
     */
    public void updateCategoryPropStatus(int propId, int status);


    /**
     * 检查编码是否存在
     *
     * @param categoryId
     * @param categoryCode
     * @return
     */
    public boolean checkCategoryCode(Integer categoryId, String categoryCode);

    /**
     * 查询类目
     *
     * @param categoryId
     * @return
     */
    TbCategory loadTbCategoryById(int categoryId);

    /**
     * 检验值编码
     * (同一个类目下面的属性编码不能相同)
     *
     * @param categoryId
     * @return
     */
    public boolean checkCategoryPropCode(int categoryId, Integer propId, String propCode);

    /**
     * 查询类目属性
     *
     * @param propId
     * @return
     */
    TbItemprops loadTbItempropsById(int propId);

    /**
     * 反向删除属性值
     *
     * @param valueIds
     */
    void deletePropValue(int propId, String valueIds);

    Map<String,Object> loadCategoryMap(int categoryId);

}
