package com.yufan.dao.activity.impl;

import com.yufan.bean.ActivityCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.activity.IActivityDao;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/10 12:32
 * 功能介绍:
 */
@Transactional
@Repository
public class ActivityDaoImpl implements IActivityDao {

    @Autowired
    private IGeneralDao iGeneralDao;
    private String imgUrl = Constants.IMG_WEB_URL;


    @Override
    public PageInfo loadDataPage(int currePage, ActivityCondition activityCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT activity_id,activity_title,activity_name,activity_link,CONCAT('").append(imgUrl).append("',activity_img) as activity_img,data_index,status, ");
        sql.append(" DATE_FORMAT(start_time,'%Y-%m-%d %T') as start_time,DATE_FORMAT(end_time,'%Y-%m-%d %T') as end_time, ");
        sql.append(" DATE_FORMAT(createtime,'%Y-%m-%d %T') as createtime ");
        sql.append(" from tb_activity where 1=1 ");
        if (!StringUtils.isEmpty(activityCondition.getTitle())) {
            sql.append(" and activity_title like '%").append(activityCondition.getTitle().trim()).append("%' ");
        }
        if (null != activityCondition.getStatus() && activityCondition.getStatus() != -1) {
            sql.append(" and status = ").append(activityCondition.getStatus()).append(" ");
        }
        if (activityCondition.getShopId() != null) {
            sql.append(" and shop_id=").append(activityCondition.getShopId()).append(" ");
        }
        sql.append(" ORDER BY data_index desc,activity_id desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void updateActivityStatus(int activityId, int status) {
        String sql = " update tb_activity set `status`=? where activity_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, activityId);
    }
}
