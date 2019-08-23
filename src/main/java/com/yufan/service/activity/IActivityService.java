package com.yufan.service.activity;

import com.yufan.bean.ActivityCondition;
import com.yufan.pojo.TbActivity;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/10 12:30
 * 功能介绍:
 */
public interface IActivityService {

    /**
     * 查询分页数据
     * @param currePage
     * @param activityCondition
     * @return
     */
    PageInfo loadDataPage(int currePage, ActivityCondition activityCondition);

    /**
     * 保存数据
     * @param activity
     * @return
     */
    int saveActivity(TbActivity activity);

    /**
     * 更新数据状态
     * @param activityId
     * @param status
     */
    void updateActivityStatus(int activityId, int status);


    /**
     * 查询对象数据
     * @param activityId
     * @return
     */
    TbActivity loadActivity(int activityId);


}
