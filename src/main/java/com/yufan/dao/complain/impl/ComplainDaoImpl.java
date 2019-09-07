package com.yufan.dao.complain.impl;

import com.yufan.bean.ComplainCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.complain.IComplainDao;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/10 9:32
 * 功能介绍:
 */
@Transactional
@Repository
public class ComplainDaoImpl implements IComplainDao {

    @Autowired
    private IGeneralDao iGeneralDao;


    @Override
    public PageInfo loadDataPage(int currePage, ComplainCondition complainCondition) {

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT id,user_id,information,contents,`status`,is_read,answer, ");
        sql.append(" DATE_FORMAT(createtime,'%Y-%m-%d %T') as createtime,DATE_FORMAT(lastaltertime,'%Y-%m-%d %T') as lastaltertime ");
        sql.append(" from tb_complain where 1=1 ");
        if (null != complainCondition.getUserId()) {
            sql.append(" and user_id=").append(complainCondition.getUserId()).append(" ");
        }
        if (StringUtils.isNotEmpty(complainCondition.getInformation())) {
            sql.append(" and information like '%").append(complainCondition.getInformation().trim()).append("%' ");
        }
        if (StringUtils.isNotEmpty(complainCondition.getContents())) {
            sql.append(" and contents like '%").append(complainCondition.getContents().trim()).append("%' ");
        }
        if (null != complainCondition.getStatus() && -1 != complainCondition.getStatus()) {
            sql.append(" and `status`=").append(complainCondition.getStatus()).append(" ");
        }
        if (null != complainCondition.getIsRead() && -1 != complainCondition.getIsRead()) {
            sql.append(" and is_read = ").append(complainCondition.getIsRead()).append(" ");
        }
        if (null != complainCondition.getComplainType() && -1 != complainCondition.getComplainType()) {
            sql.append(" and complain_type=").append(complainCondition.getComplainType()).append(" ");
        }
        if (complainCondition.getShopId() != null) {
            sql.append(" and shop_id=").append(complainCondition.getShopId()).append(" ");
        }
        sql.append(" ORDER BY id desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void updateComplainStatus(int complainId, int status) {
        String sql = " update tb_complain set status=?,lastaltertime=now() where  id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, complainId);
    }

    @Override
    public void updateComlpainIsRead(int complainId, int isRead) {
        String sql = " update tb_complain set is_read=?,lastaltertime=now() where  id=? ";
        iGeneralDao.executeUpdateForSQL(sql, isRead, complainId);
    }

    @Override
    public void updateAnswer(int complainId, String answer) {
        String sql = " update tb_complain set answer=? ,is_read=1,lastaltertime=NOW()  WHERE id=? ";
        iGeneralDao.executeUpdateForSQL(sql, answer, complainId);
    }
}
