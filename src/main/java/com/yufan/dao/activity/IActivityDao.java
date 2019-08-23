package com.yufan.dao.activity;

import com.yufan.bean.ActivityCondition;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/10 12:31
 * 功能介绍:
 */
public interface IActivityDao {

    /**
     * 查询分页数据
     * @param currePage
     * @param activityCondition
     * @return
     */
    PageInfo loadDataPage(int currePage, ActivityCondition activityCondition);

    /**
     * 更新数据状态
     * @param activityId
     * @param status
     */
    void updateActivityStatus(int activityId, int status);



}
