package com.yufan.dao.func.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.func.IMenuDao;
import com.yufan.pojo.TbActivity;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbFunctions;
import com.yufan.pojo.TbPageMenu;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/4/9 14:49
 * 功能介绍:
 */
@Repository
@Transactional
public class MenuDaoImpl implements IMenuDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadMenuPage(int currePage, TbFunctions func) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT function_id,function_code,function_name,function_action,data_index,status from tb_functions ");
        sql.append(" where 1=1 ");
        if (null != func) {
            if (null != func.getFunctionParentid() && -1 != func.getFunctionParentid()) {
                sql.append(" and function_parentid=").append(func.getFunctionParentid());
            }
            if (!StringUtils.isEmpty(func.getFunctionCode())) {
                sql.append(" and function_code='").append(func.getFunctionCode().trim()).append("' ");
            }
            if (!StringUtils.isEmpty(func.getFunctionName())) {
                sql.append(" and function_name like '%").append(func.getFunctionName().trim()).append("%' ");
            }
            if (null != func.getStatus() && -1 != func.getStatus()) {
                sql.append(" and status=").append(func.getStatus());
            }
        }
        sql.append(" ORDER BY data_index desc ");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void deleteMenuByParentId(int parentId) {
        String sql = " DELETE from tb_functions where function_parentid=? ";
        iGeneralDao.executeUpdateForSQL(sql, parentId);
    }

    @Override
    public void delRoleFun(int roleId) {
        String sql = " DELETE from tb_role_function where role_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, roleId);
    }

    @Override
    public void insertMoreBySQL(String values) {
        values = values.substring(0, values.length() - 1);
        StringBuffer sql = new StringBuffer();
        sql.append(" insert into tb_role_function(role_id,function_id,createtime) values ");
        sql.append(values);
        iGeneralDao.executeUpdateForSQL(sql.toString());
    }

    @Override
    public List<Map<String, Object>> loadRoleFunListMap(int roleId) {
        String sql = " SELECT role_id,function_id from tb_role_function where role_id=? ";
        return iGeneralDao.getBySQLListMap(sql, roleId);
    }

    @Override
    public PageInfo loadMenuWebPage(int currePage, TbPageMenu menu) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select id,menu_name,CONCAT('").append(Constants.IMG_WEB_URL).append("',menu_img) as menu_img,menu_sort,menu_type,rel_type,menu_url,status,leve1_ids,category_ids,DATE_FORMAT(createtime,'%Y-%m-%d %T') as createtime,shop_id from tb_page_menu ");
        sql.append(" where 1=1 ");

        if (null != menu) {
            if (null != menu.getMenuType() && -1 != menu.getMenuType()) {
                sql.append(" and menu_type=").append(menu.getMenuType());
            }
            if (null != menu.getStatus() && -1 != menu.getStatus()) {
                sql.append(" and status=").append(menu.getStatus());
            }
            if (null != menu.getShopId() && -1 != menu.getShopId()) {
                sql.append(" and shop_id=").append(menu.getShopId());
            }
            if(menu.getRelType() == 1){
                if (!StringUtils.isEmpty(menu.getLeve1Ids())) {
                    sql.append(" and CONCAT(leve1_ids,',') like '%").append(menu.getLeve1Ids()).append(",%' ");
                }
            }else if(menu.getRelType() == 2){
                if (!StringUtils.isEmpty(menu.getCategoryIds())) {
                    sql.append(" and CONCAT(category_ids,',') like '%").append(menu.getCategoryIds()).append(",%' ");
                }
            }
        }
        sql.append(" order by menu_sort desc,createtime desc ");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void updateMenuWebStatus(int id, int status) {
        String sql = " update tb_page_menu set `status`=? where id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, id);
    }

    @Override
    public TbPageMenu loadPageMenu(int id) {
        String hql = " from TbPageMenu where id = ?1 ";
        return iGeneralDao.queryUniqueByHql(hql,id);
    }

    @Override
    public void updatePageMenuData(TbPageMenu pageMenu) {
        iGeneralDao.saveOrUpdate(pageMenu);
    }

    @Override
    public void savePageMenuData(TbPageMenu pageMenu) {
        iGeneralDao.save(pageMenu);
    }
}
