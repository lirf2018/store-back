package com.yufan.service.activity.impl;

import com.yufan.bean.ActivityCondition;
import com.yufan.dao.activity.IActivityDao;
import com.yufan.dao.activity.IActivityJpaDao;
import com.yufan.pojo.TbActivity;
import com.yufan.pojo.TbBanner;
import com.yufan.service.activity.IActivityService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/10 12:31
 * 功能介绍:
 */
@Service
public class ActivityServiceImpl implements IActivityService {

    @Autowired
    private IActivityJpaDao iActivityJpaDao;

    @Autowired
    private IActivityDao iActivityDao;

    @Override
    public PageInfo loadDataPage(int currePage, ActivityCondition activityCondition) {
        return iActivityDao.loadDataPage(currePage,activityCondition);
    }

    @Override
    public int saveActivity(TbActivity activity) {
        return iActivityJpaDao.save(activity).getActivityId();
    }

    @Override
    public void updateActivityStatus(int activityId, int status) {
        iActivityDao.updateActivityStatus(activityId,status);
    }

    @Override
    public TbActivity loadActivity(int activityId) {
        return iActivityJpaDao.getOne(activityId);
    }
}
