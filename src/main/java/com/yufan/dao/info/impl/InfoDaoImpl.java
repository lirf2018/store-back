package com.yufan.dao.info.impl;

import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.info.IInfoDao;
import com.yufan.pojo.TbInfo;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 创建人: lirf
 * 创建时间:  2020/1/16 14:34
 * 功能介绍:
 */
@Repository
@Transactional
public class InfoDaoImpl implements IInfoDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadDataPage(int currePage, TbInfo info) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT info_id,info_title,CONCAT('").append(Constants.IMG_WEB_URL).append("',info_img) as info_img,`status`,DATE_FORMAT(create_time,'%Y-%m-%d %T') as create_time ");
        sql.append(" ,info_index ");
        sql.append(" from tb_info where 1=1 ");
        if (null != info.getStatus() && -1 != info.getStatus()) {
            sql.append(" and status=").append(info.getStatus()).append(" ");
        }
        if (!StringUtils.isEmpty(info.getInfoTitle())) {
            sql.append(" and info_title like '%").append(info.getInfoTitle()).append("%' ");
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public TbInfo loadInfo(int id) {
        String hql = " from TbInfo where infoId=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, id);
    }

    @Override
    public int updateInfoStatus(int id, int status) {
        String sql = " update tb_info set status=? where info_id=?  ";
        return iGeneralDao.executeUpdateForSQL(sql, status, id);
    }

    @Override
    public int saveInfo(TbInfo info) {
        return iGeneralDao.save(info);
    }

    @Override
    public void updateInfo(TbInfo info) {
        String sql = " update tb_info set info_title=?,info_img=?,info_url=?,info_content=?,status=?,shop_id=?,info_index=?,update_time=now() where info_id=?  ";
        iGeneralDao.executeUpdateForSQL(sql, info.getInfoTitle(), info.getInfoImg(), info.getInfoUrl(), info.getInfoContent(), info.getStatus(), info.getShopId(), info.getInfoIndex(), info.getInfoId());
    }
}
