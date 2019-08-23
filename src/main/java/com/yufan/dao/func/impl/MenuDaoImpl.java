package com.yufan.dao.func.impl;

import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.func.IMenuDao;
import com.yufan.pojo.TbFunctions;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
        return iGeneralDao.getBySQLListMap(sql,roleId);
    }
}
