package com.yufan.dao.category.impl;

import com.yufan.bean.CategoryCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.category.ICategoryDao;
import com.yufan.pojo.TbCategory;
import com.yufan.pojo.TbItemprops;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/7 9:26
 * 功能介绍:
 */
@Repository
@Transactional
public class CategoryDaoImpl implements ICategoryDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    private String imgUrl = Constants.IMG_WEB_URL;

    @Override
    public PageInfo loadLevelPage(int currePage, CategoryCondition categoryCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT lev.level_id,lev.level_name,lev.level_code,CONCAT('").append(imgUrl).append("',lev.level_img) as level_img,lev.data_index,lev.`status`,DATE_FORMAT(lev.createtime,'%Y-%m-%d %T') as createtime ");
        sql.append(" from tb_category_level lev where 1=1 ");
        if (!StringUtils.isEmpty(categoryCondition.getLevelName())) {
            sql.append(" and lev.level_name like '%").append(categoryCondition.getLevelName().trim()).append("%' ");
        }
        if (!StringUtils.isEmpty(categoryCondition.getLevelCode())) {
            sql.append(" and lev.level_code ='").append(categoryCondition.getLevelCode().trim()).append("' ");
        }
        if (null != categoryCondition.getLevelStatus() && categoryCondition.getLevelStatus() != -1) {
            sql.append(" and lev.status=").append(categoryCondition.getLevelStatus()).append(" ");
        }
        if ((null != categoryCondition.getCategoryId() && categoryCondition.getCategoryId() != -1) || !StringUtils.isEmpty(categoryCondition.getCategoryName())) {
            sql.append(" and lev.level_id in ( ");
            sql.append(" SELECT rel.level_id from tb_category ca JOIN tb_level_category_rel rel on rel.category_id=ca.category_id ");
            sql.append(" where 1=1 ");

            if (!StringUtils.isEmpty(categoryCondition.getCategoryName())) {
                sql.append(" and ca.category_name like '%").append(categoryCondition.getCategoryName().trim()).append("%' ");
            }
            if (null != categoryCondition.getCategoryId() && categoryCondition.getCategoryId() != -1) {
                sql.append(" and ca.category_id=").append(categoryCondition.getCategoryId()).append(" ");
            }
            sql.append(" )  ");
        }
        sql.append(" ORDER BY data_index desc,level_id desc ");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    public List<Map<String, Object>> loadLevelCategoryData(Integer levelId, Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ca.category_id,ca.category_code,ca.category_name,ca.data_index,CONCAT('").append(imgUrl).append("',ca.category_img) as category_img,ca.`status` ");
        sql.append(" from tb_category ca ");
        if (null != levelId && levelId > 0) {
            sql.append(" left JOIN tb_level_category_rel rel on rel.category_id=ca.category_id  ");
        }
        sql.append(" where 1=1 ");
        if (null != levelId && levelId > 0) {
            sql.append(" and  rel.level_id=").append(levelId).append(" ");
        }
        if (null != status) {
            sql.append(" and ca.status =").append(status).append(" ");
        }

        sql.append(" ORDER BY ca.data_index desc,ca.category_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadCategoryListMap(Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT category_id,category_name,category_code from tb_category where 1=1 ");
        if (null != status) {
            sql.append(" and `status` =1 ");
        }
        sql.append(" ORDER BY data_index desc,category_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadLevelListMap(Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT level_id,level_code,level_name from tb_category_level where 1=1 ");
        if (null != status) {
            sql.append(" and `status` =1 ");
        }
        sql.append(" ORDER BY data_index desc,level_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public boolean checkLevelCode(String levelCode, Integer levelId) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT level_id,level_code from tb_category_level where level_code=? ");
            if (null != levelId && levelId > 0) {
                sql.append(" and level_id !=").append(levelId).append(" ");
            }
            List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql.toString(), levelCode);
            if (null != list && list.size() == 0) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void deleteLevelCategoryRelByLevelId(int levelId) {
        String sql = " DELETE from tb_level_category_rel where level_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, levelId);
    }

    @Override
    public void saveObject(Object obj) {
        iGeneralDao.save(obj);
    }

    @Override
    public void updateObject(Object obj) {
        iGeneralDao.saveOrUpdate(obj);
    }

    @Override
    public List<Map<String, Object>> loadGoodsAttr(int goodsId) {
        String sql = " SELECT attr_id,goods_id,prop_id,value_id from tb_goods_attribute where goods_id=? ";
        return iGeneralDao.getBySQLListMap(sql);
    }

    @Override
    public List<Map<String, Object>> loadCategoryAllRelListMap(Integer categoryId, Integer caStatus, Integer itStatus, Integer pvStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ");
        //类目
        sql.append(" ca.category_id as ca_category_id,ca.category_name as ca_category_name,ca.is_parent as ca_is_parent,ca.data_index as ca_data_index,ca.oute_id as ca_oute_id,CONCAT('").append(imgUrl).append("',ca.category_img) as ca_category_web_img,ca.category_img as ca_category_img,ca.category_code as ca_category_code,ca.is_show as ca_is_show,ca.status as ca_status,ca.remark as ca_remark ");
        //属性
        sql.append(" ,it.prop_id as it_prop_id,it.prop_name as it_prop_name,it.category_id as it_category_id,it.oute_id as it_oute_id,it.is_sales as it_is_sales,it.show_view as it_show_view,CONCAT('").append(imgUrl).append("',it.prop_img) as it_prop_web_img,it.prop_img as it_prop_img,it.prop_code as it_prop_code,it.is_show as it_is_show,it.data_index as it_data_index,it.status as it_status,it.remark as it_remark ");
        //属性值
        sql.append(" ,pv.value_id as pv_value_id,pv.prop_id as pv_prop_id,pv.value_name as pv_value_name,pv.category_id as pv_category_id,pv.oute_id as pv_oute_id,pv.value as pv_value,pv.data_index as pv_data_index,pv.status as pv_status,CONCAT('").append(imgUrl).append("',pv.value_img) as pv_value_web_img,pv.value_img as  pv_value_img ");
        sql.append(" from tb_category ca ");
        sql.append(" LEFT JOIN tb_itemprops it on it.category_id=ca.category_id ");
        if (null != itStatus) {
            sql.append("  and it.`status`=").append(itStatus).append("  ");
        }
        sql.append(" LEFT JOIN tb_props_value pv on pv.category_id=ca.category_id ");
        if (pvStatus != null) {
            sql.append(" and pv.`status`=").append(pvStatus).append(" ");
        }
        sql.append(" where 1=1 ");
        if (null != caStatus) {
            sql.append(" and ca.`status`=").append(caStatus).append(" ");
        }
        if (null != categoryId) {
            sql.append(" and ca.category_id=").append(categoryId).append(" ");
        }
        sql.append(" ORDER BY ca.data_index desc,it.data_index desc,pv.data_index desc ");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadCategoryItemRelListMap(Integer categoryId, Integer caStatus, Integer itStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ");
        //类目
        sql.append(" ca.category_id as ca_category_id,ca.category_name as ca_category_name,ca.is_parent as ca_is_parent,ca.data_index as ca_data_index,ca.oute_id as ca_oute_id,CONCAT('").append(imgUrl).append("',ca.category_img) as ca_category_web_img,ca.category_img as ca_category_img,ca.category_code as ca_category_code,ca.is_show as ca_is_show,ca.status as ca_status,ca.remark as ca_remark ");
        //属性
        sql.append(" ,it.prop_id as it_prop_id,it.prop_name as it_prop_name,it.category_id as it_category_id,it.oute_id as it_oute_id,it.is_sales as it_is_sales,it.show_view as it_show_view,CONCAT('").append(imgUrl).append("',it.prop_img) as it_prop_web_img,it.prop_img as it_prop_img,it.prop_code as it_prop_code,it.is_show as it_is_show,it.data_index as it_data_index,it.status as it_status,it.remark as it_remark ");
        sql.append(" from tb_category ca ");
        sql.append(" LEFT JOIN tb_itemprops it on it.category_id=ca.category_id ");
        if (null != itStatus) {
            sql.append("  and it.`status`=").append(itStatus).append("  ");
        }
        sql.append(" where 1=1 ");
        if (null != caStatus) {
            sql.append(" and ca.`status`=").append(caStatus).append(" ");
        }
        if (null != categoryId) {
            sql.append(" and ca.category_id=").append(categoryId).append(" ");
        }
        sql.append(" ORDER BY ca.data_index desc,it.data_index desc");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadItemAllRelListMap(Integer categoryId, Integer itStatus, Integer pvStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ");
        //属性
        sql.append(" it.prop_id as it_prop_id,it.prop_name as it_prop_name,it.category_id as it_category_id,it.oute_id as it_oute_id,it.is_sales as it_is_sales,it.show_view as it_show_view,CONCAT('").append(imgUrl).append("',it.prop_img) as it_prop_web_img,it.prop_img as it_prop_img,it.prop_code as it_prop_code,it.is_show as it_is_show,it.data_index as it_data_index,it.status as it_status,it.remark as it_remark ");
        //属性值
        sql.append(" ,pv.value_id as pv_value_id,pv.prop_id as pv_prop_id,pv.value_name as pv_value_name,pv.category_id as pv_category_id,pv.oute_id as pv_oute_id,pv.value as pv_value,pv.data_index as pv_data_index,pv.status as pv_status,CONCAT('").append(imgUrl).append("',pv.value_img) as pv_value_web_img,pv.value_img as pv_value_img ");
        sql.append(" from tb_itemprops it  ");
        sql.append(" LEFT JOIN tb_props_value pv on pv.prop_id=it.prop_id  ");
        if (pvStatus != null) {
            sql.append(" and pv.`status`=").append(pvStatus).append(" ");
        }
        sql.append(" where 1=1 ");
        if (null != categoryId) {
            sql.append(" and it.category_id=").append(categoryId).append(" ");
        }
        if (null != itStatus) {
            sql.append("  and it.`status`=").append(itStatus).append("  ");
        }
        sql.append(" ORDER BY it.data_index desc,pv.data_index desc ");

        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void updateLevelStatus(int levelId, int status) {
        String sql = " update tb_category_level set status=? where level_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, levelId);
    }

    @Override
    public List<Map<String, Object>> loadPropValueByCategoryId(int categoryId) {
        String sql = " SELECT prop_id,value_id,value_name from tb_props_value where status=1 and category_id=? ";
        return iGeneralDao.getBySQLListMap(sql, categoryId);
    }

    @Override
    public List<Map<String, Object>> loadItemPropListMap(Integer categoryId, Integer propId, Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT prop_id,prop_name,category_id,oute_id,is_sales,show_view,CONCAT('").append(imgUrl).append("',prop_img) as prop_img,prop_code,is_show,data_index,`status`,remark,category_type from tb_itemprops ");
        sql.append(" where 1=1 ");
        if (null != categoryId && categoryId > 0) {
            sql.append(" and category_id=").append(categoryId).append(" ");
        }
        if (null != propId && propId > 0) {
            sql.append(" and prop_id=").append(propId).append(" ");
        }
        if (null != status) {
            sql.append(" and status=").append(status).append(" ");
        }
        sql.append(" ORDER BY data_index desc,prop_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadItemPropValueListMap(Integer categoryId, Integer propId, Integer valueId, Integer status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT value_id, prop_id, value_name, category_id, oute_id, value, data_index,`status`,CONCAT('http', value_img) ");
        sql.append(" as value_img from tb_props_value ");
        sql.append(" where 1 = 1 ");
        if (null != valueId && valueId > 0) {
            sql.append(" and value_id=").append(valueId).append(" ");
        }
        if (null != propId && propId > 0) {
            sql.append(" and prop_id=").append(propId).append(" ");
        }
        if (null != categoryId && categoryId > 0) {
            sql.append(" and category_id=").append(categoryId).append(" ");
        }
        if (null != status) {
            sql.append(" and status=").append(status).append(" ");
        }
        sql.append(" ORDER BY data_index desc, value_id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadLeveCategoryRel(Integer levelId, Integer categoryId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT level_id,category_id from tb_level_category_rel where 1=1 ");
        if (null != levelId) {
            sql.append(" and level_id=").append(levelId).append(" ");
        }
        if (null != categoryId) {
            sql.append(" and category_id=").append(categoryId).append(" ");
        }
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void updateCategoryStatus(int categoryId, int status) {
        String sql = " update tb_category set status=? where category_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, categoryId);
    }

    @Override
    public void updateCategoryPropStatus(int propId, int status) {
        String sql = " update tb_itemprops set status=? where prop_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, propId);
    }

    @Override
    public boolean checkCategoryCode(Integer categoryId, String categoryCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT category_id,category_code from tb_category where category_code='").append(categoryCode.trim()).append("' ");
        if (null != categoryId && categoryId > 0) {
            sql.append(" and category_id !=").append(categoryId).append(" ");
        }
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql.toString());
        if (null != list && list.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public TbCategory loadTbCategoryById(int categoryId) {
        String sql = " from TbCategory where categoryId=?1 ";
        return iGeneralDao.queryUniqueByHql(sql, categoryId);
    }

    @Override
    public boolean checkCategoryPropCode(int categoryId, Integer propId, String propCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT prop_id,prop_code from tb_itemprops where prop_code='").append(propCode.trim()).append("' ");
        sql.append(" and category_id =").append(categoryId).append(" ");
        if (null != propId && propId > 0) {
            sql.append(" and prop_id != ").append(propId).append(" ");
        }
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql.toString());
        if (null != list && list.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public TbItemprops loadTbItempropsById(int propId) {
        String sql = " from TbItemprops where propId=?1 ";
        return iGeneralDao.queryUniqueByHql(sql, propId);
    }

    @Override
    public void deletePropValue(int propId, String valueIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_props_value set `status`=0 where  prop_id=").append(propId).append(" ");
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(valueIds)){
            sql.append(" and value_id not in (").append(valueIds).append(")  ");
        }

        iGeneralDao.executeUpdateForSQL(sql.toString());
    }

    @Override
    public Map<String, Object> loadCategoryMap(int categoryId) {
        String sql = " SELECT category_id,category_code,category_name,category_type,data_index,is_parent,oute_id,is_show,`status` from tb_category where category_id=? ";
        return iGeneralDao.getBySQLListMap(sql, categoryId).get(0);
    }

}
