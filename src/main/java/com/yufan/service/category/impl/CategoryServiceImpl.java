package com.yufan.service.category.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.CategoryCondition;
import com.yufan.bean.ItempropObj;
import com.yufan.dao.category.ICategoryDao;
import com.yufan.dao.category.ICategoryJpaDao;
import com.yufan.pojo.*;
import com.yufan.service.category.ICategoryService;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/7 9:25
 * 功能介绍:
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private ICategoryDao iCategoryDao;

    @Autowired
    private ICategoryJpaDao iCategoryJpaDao;

    @Override
    public PageInfo loadLevelPage(int currePage, CategoryCondition categroyCondition) {
        return iCategoryDao.loadLevelPage(currePage, categroyCondition);
    }

    @Override
    public List<Map<String, Object>> loadLevelCategoryData(Integer levelId, Integer status) {
        return iCategoryDao.loadLevelCategoryData(levelId, status);
    }

    @Override
    public List<Map<String, Object>> loadCategoryListMap(Integer status) {
        return iCategoryDao.loadCategoryListMap(status);
    }

    @Override
    public List<Map<String, Object>> loadLevelListMap(Integer status) {
        return iCategoryDao.loadLevelListMap(status);
    }

    @Override
    public TbCategoryLevel loadLevelById(int levelId) {
        return iCategoryJpaDao.getOne(levelId);
    }

    @Transactional
    @Override
    public JSONObject saveLevel(JSONObject data) {
        try {

            Integer levelId = data.getInteger("levelId");
            String levelCode = data.getString("levelCode");
            String levelName = data.getString("levelName");
            String levelImg = data.getString("levelImg");
            Integer dataIndex = data.getInteger("dataIndex");
            Integer status = data.getInteger("status");
            String remark = data.getString("remark");
            Integer retain = data.getInteger("retain");//是否保留原来关联 1保留
            String categoryIds = data.getString("categoryIds");//新关联的类目
            String createman = data.getString("createman");
            String categoryIdsHasRel = data.getString("categoryIdsHasRel");//已关联的类目
            Map<String, String> hasRelMap = new HashMap<>();//已关联的类目

            //检验编码是否重复
            boolean flag = iCategoryDao.checkLevelCode(levelCode, levelId);
            if (flag) {
                return CommonMethod.packagMsg("15");
            }
            //不保留
            if (levelId != null && levelId > 0 && retain == 0) {
                //删除关联
                iCategoryDao.deleteLevelCategoryRelByLevelId(levelId);
            } else if (levelId != null && levelId > 0 && retain == 1) {
                //保留关联
                if (!StringUtils.isEmpty(categoryIdsHasRel)) {
                    String array[] = categoryIdsHasRel.split(",");
                    for (int i = 0; i < array.length; i++) {
                        if (!StringUtils.isEmpty(array[i])) {
                            hasRelMap.put(array[i], array[i]);
                        }
                    }
                }
            }

            String code = "0";
            if (levelId == null || levelId == 0) {
                //新增加
                TbCategoryLevel categoryLevel = new TbCategoryLevel();
                categoryLevel.setLevelCode(levelCode);
                categoryLevel.setLevelName(levelName);
                categoryLevel.setLevelImg(StringUtils.isEmpty(levelImg) ? null : levelImg);
                categoryLevel.setDataIndex(dataIndex);
                categoryLevel.setCreateman(createman);
                categoryLevel.setCreatetime(new Timestamp(new Date().getTime()));
                categoryLevel.setStatus(status);
                categoryLevel.setRemark(remark);
                categoryLevel.setShopId(0);
                categoryLevel.setCategoryType(0);
                levelId = iCategoryJpaDao.save(categoryLevel).getLevelId();
                code = "6";
            } else {
                //修改
                TbCategoryLevel categoryLevel = iCategoryJpaDao.getOne(levelId);
                categoryLevel.setLevelCode(levelCode);
                categoryLevel.setLevelName(levelName);
                categoryLevel.setLevelImg(StringUtils.isEmpty(levelImg) ? null : levelImg);
                categoryLevel.setDataIndex(dataIndex);
                categoryLevel.setCreateman(createman);
                categoryLevel.setCreatetime(new Timestamp(new Date().getTime()));
                categoryLevel.setStatus(status);
                categoryLevel.setRemark(remark);
                categoryLevel.setShopId(0);
                iCategoryJpaDao.save(categoryLevel);
                code = "5";
            }
            //保存关联
            if (!StringUtils.isEmpty(categoryIds)) {
                String array[] = categoryIds.split(",");
                for (int i = 0; i < array.length; i++) {
                    if (!StringUtils.isEmpty(array[i])) {
                        if (null == hasRelMap.get(array[i]) && !StringUtils.isEmpty(array[i])) {
                            //保存关联
                            TbLevelCategoryRel categoryRel = new TbLevelCategoryRel();
                            categoryRel.setCategoryId(Integer.parseInt(array[i]));
                            categoryRel.setLevelId(levelId);
                            iCategoryDao.saveObject(categoryRel);
                        }
                    }
                }
            }
            return CommonMethod.packagMsg(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonMethod.packagMsg("0");
    }

    @Override
    public List<Map<String, Object>> loadGoodsAttr(int goodsId) {
        return iCategoryDao.loadGoodsAttr(goodsId);
    }

    @Override
    public List<Map<String, Object>> loadCategoryAllRelListMap(Integer categoryId, Integer caStatus, Integer itStatus, Integer pvStatus) {
        return iCategoryDao.loadCategoryAllRelListMap(categoryId, caStatus, itStatus, pvStatus);
    }

    @Override
    public List<Map<String, Object>> loadCategoryItemRelListMap(Integer categoryId, Integer caStatus, Integer itStatus) {
        return iCategoryDao.loadCategoryItemRelListMap(categoryId, caStatus, itStatus);
    }

    @Override
    public List<Map<String, Object>> loadItemAllRelListMap(Integer categoryId, Integer itStatus, Integer pvStatus) {
        return iCategoryDao.loadItemAllRelListMap(categoryId, itStatus, pvStatus);
    }

    @Override
    public void updateLevelStatus(int levelId, int status) {
        iCategoryDao.updateLevelStatus(levelId, status);
    }

    @Override
    public List<Map<String, Object>> loadItemPropListMap(Integer categoryId, Integer propId, Integer status) {
        return iCategoryDao.loadItemPropListMap(categoryId, propId, status);
    }

    @Override
    public List<Map<String, Object>> loadItemPropValueListMap(Integer categoryId, Integer propId, Integer valueId, Integer status) {
        return iCategoryDao.loadItemPropValueListMap(categoryId, propId, valueId, status);
    }

    @Override
    public List<Map<String, Object>> loadLeveCategoryRel(Integer levelId, Integer categoryId) {
        return iCategoryDao.loadLeveCategoryRel(levelId, categoryId);
    }

    @Override
    public void updateCategoryStatus(int categoryId, int status) {
        iCategoryDao.updateCategoryStatus(categoryId, status);
    }

    @Override
    public void updateCategoryPropStatus(int propId, int status) {
        iCategoryDao.updateCategoryPropStatus(propId, status);
    }

    @Override
    public boolean checkCategoryCode(Integer categoryId, String categoryCode) {
        return iCategoryDao.checkCategoryCode(categoryId, categoryCode);
    }

    @Override
    public void saveCategory(TbCategory category) {
        try {
            category.setCategoryType(0);
            category.setCreatetime(new Timestamp(new Date().getTime()));
            category.setCreateman("");
            category.setIsParent(0);
            category.setIsShow(1);
            category.setStatus(1);
            if (category.getCategoryId() > 0) {
                Map<String, Object> old = iCategoryDao.loadCategoryMap(category.getCategoryId());
                int status = Integer.parseInt(old.get("status").toString());
                category.setStatus(status);
            }
            if (category.getCategoryId() > 0) {
                iCategoryDao.updateObject(category);
                return;
            }
            iCategoryDao.saveObject(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    @Override
    public JSONObject saveCategoryProp(ItempropObj itempropObj) {
        try {
            boolean flag = iCategoryDao.checkCategoryPropCode(itempropObj.getCategoryId(), itempropObj.getPropId(), itempropObj.getPropCode());
            if (flag) {
                return CommonMethod.packagMsg("15");
            }

            flag = false;//新增
            TbItemprops itemprops = new TbItemprops();
            itemprops.setStatus(1);
            if (null != itempropObj.getPropId() && itempropObj.getPropId() > 0) {
                flag = true;
                itemprops = iCategoryDao.loadTbItempropsById(itempropObj.getPropId());
            }
            itemprops.setCategoryId(itempropObj.getCategoryId());
            itemprops.setCategoryType(0);
            itemprops.setIsShow(1);
            itemprops.setDataIndex(itempropObj.getDataIndex());
            itemprops.setIsSales(itempropObj.getIsSales());
            itemprops.setOuteId(itempropObj.getOuteId());
            itemprops.setPropImg(itempropObj.getPropImg());
            itemprops.setPropCode(itempropObj.getPropCode());
            itemprops.setPropId(itempropObj.getPropId() == null ? 0 : itempropObj.getPropId());
            itemprops.setPropName(itempropObj.getPropName());
            itemprops.setRemark(itempropObj.getRemark());
            itemprops.setShowView(itempropObj.getShowView());

            //属性值
            String valueIds = itempropObj.getValueIds();
            String outeIds = itempropObj.getOuteIds();
            String valueNames = itempropObj.getValueNames();
            String values = itempropObj.getValues();
            String dataIndexs = itempropObj.getDataIndexs();

            //删除不存在的属性值
            if (itempropObj.getPropId() != null && itempropObj.getPropId() > 0) {
                String[] deleteValueIds = valueIds.split("``");
                String strIds = "";
                for (int i = 0; i < deleteValueIds.length; i++) {
                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(deleteValueIds[i]) && !"0".equals(deleteValueIds[i])) {
                        strIds = strIds + deleteValueIds[i] + ",";
                    }
                }
                if (strIds.endsWith("``")) {
                    strIds = strIds.substring(0, strIds.length() - 2);
                }
                if (strIds.endsWith(",")) {
                    strIds = strIds.substring(0, strIds.length() - 1);
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(strIds)) {
                    iCategoryDao.deletePropValue(itempropObj.getPropId(), strIds);
                }
            }
            iCategoryDao.saveObject(itemprops);

            //保存属性值
            String[] valueIdsArray = valueIds.split("``");
            String[] outeIdsArray = outeIds.split("``");
            String[] valueNamesArray = valueNames.split("``");
            String[] valuesArray = values.split("``");
            String[] dataIndexArray = dataIndexs.split("``");
            boolean hasValueObj = false;
            for (int i = 0; i < valueNamesArray.length; i++) {
                TbPropsValue propsValue = new TbPropsValue();
                propsValue.setValueName(valueNamesArray[i]);
                if (StringUtils.isEmpty(valueNamesArray[i])) {
                    continue;
                }
                hasValueObj = true;
                propsValue.setValueId(StringUtils.isEmpty(valueIdsArray[i]) || "0".equals(valueIdsArray[i]) ? 0 : Integer.parseInt(valueIdsArray[i]));
                propsValue.setPropId(itemprops.getPropId());
                propsValue.setCategoryId(itemprops.getCategoryId());
                propsValue.setOuteId(StringUtils.isEmpty(outeIdsArray[i]) || "0".equals(outeIdsArray[i]) ? "" : outeIdsArray[i]);
                propsValue.setValue(valuesArray[i]);
                propsValue.setDataIndex(Integer.parseInt(dataIndexArray[i]));
                propsValue.setStatus(1);
                if (propsValue.getValueId() == 0) {
                    iCategoryDao.saveObject(propsValue);
                } else {
                    iCategoryDao.updateObject(propsValue);
                }
            }
            if (!hasValueObj && itempropObj.getPropId() != null) {
                iCategoryDao.deletePropValue(itempropObj.getPropId(), null);
            }

            JSONObject out = CommonMethod.packagMsg("6");
            if (flag) {
                out = CommonMethod.packagMsg("5");
            }
            return out;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
