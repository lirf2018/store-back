package com.yufan.dao.banner.impl;

import com.yufan.bean.BannerCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.banner.IBannerDao;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 12:00
 * 功能介绍:
 */
@Transactional
@Repository
public class BannerDaoimpl implements IBannerDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadDataPage(int currePage, BannerCondition bannerCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select banner_id,banner_title,banner_name,CONCAT('").append(Constants.IMG_WEB_URL).append("',banner_img) as banner_img,banner_link,DATE_FORMAT(start_time,'%Y-%m-%d %T') as start_time,status, ");
        sql.append(" DATE_FORMAT(end_time,'%Y-%m-%d %T') as end_time,data_index,DATE_FORMAT(createtime,'%Y-%m-%d %T') as createtime ");
        sql.append(" ,if(end_time<NOW(),0,1) as ac_type");
        sql.append(" from tb_banner where 1=1 ");
        if (StringUtils.isNotEmpty(bannerCondition.getBannerName())) {
            sql.append(" and banner_name like '%").append(bannerCondition.getBannerName().trim()).append("%' ");
        }
        if (null != bannerCondition.getStatus()) {
            sql.append(" and status = ").append(bannerCondition.getStatus()).append(" ");
        }
        if (null != bannerCondition.getShopId()) {
            sql.append(" and shop_id = ").append(bannerCondition.getShopId()).append(" ");
        }
        sql.append(" ORDER BY data_index desc,createtime desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void updateBannerStatus(int bannerId, int status) {
        String sql = " UPDATE tb_banner set `status`=? where banner_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, bannerId);
    }

}
