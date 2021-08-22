package com.yufan.dao.param.impl;

import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.param.IParamCodeDao;
import com.yufan.pojo.TbFunctions;
import com.yufan.pojo.TbParam;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/5 22:22
 * 功能介绍:
 */
@Repository
@Transactional
public class ParamCodeDaoImpl implements IParamCodeDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadParamCodePage(int currePage, TbParam param) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT param_id,param_name,param_code,param_key,param_value,param_value1 ,param_value2,param_value3,param_value4,param_value5,param_value6,remark,DATE_FORMAT(createtime,'%Y-%m-%d %T') as createtime from tb_param ");
        sql.append(" where status=1 ");
        if(!StringUtils.isEmpty(param.getParamCode())){
            sql.append(" and param_code like '%").append(param.getParamCode().trim()).append("%' ");
        }
        if(!StringUtils.isEmpty(param.getParamKey())){
            sql.append(" and param_key like '%").append(param.getParamKey().trim()).append("%' ");
        }
        if(!StringUtils.isEmpty(param.getParamValue())){
            sql.append(" and param_value like '%").append(param.getParamValue().trim()).append("%' ");
        }
        if(!StringUtils.isEmpty(param.getParamName())){
            sql.append(" and param_name='").append(param.getParamName().trim()).append("' ");
        }
        sql.append(" ORDER BY  param_code,data_index desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void updateParamCodeStatus(int paramId, int status) {
        String sql = " update tb_param set `status`=? where param_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, paramId);
    }

    @Override
    public List<Map<String, Object>> loadParamGroupName() {
        String sql = " SELECT param_name,param_code from tb_param where `status`=1 GROUP BY param_name,param_code order by data_index desc ";
        return iGeneralDao.getBySQLListMap(sql);
    }

    @Override
    public TbParam loadTbParamCodeById(int id) {
        String hql = " from TbParam where paramId=?1 ";
        return iGeneralDao.queryUniqueByHql(hql,id);
    }
}
