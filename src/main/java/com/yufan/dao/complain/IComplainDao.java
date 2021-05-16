package com.yufan.dao.complain;

import com.yufan.bean.ComplainCondition;
import com.yufan.pojo.TbBanner;
import com.yufan.pojo.TbComplain;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/10 9:31
 * 功能介绍:
 */
public interface IComplainDao {


    /**
     * 查询分页数据
     *
     * @param currePage
     * @param complainCondition
     * @return
     */
    PageInfo loadDataPage(int currePage, ComplainCondition complainCondition);


    /**
     * 更新数据状态
     *
     * @param complainId
     * @param status
     */
    void updateComplainStatus(int complainId, int status);


    /**
     * 更新已读
     *
     * @param complainId
     * @param isRead
     */
    void updateComlpainIsRead(int complainId, int isRead);

    /**
     * 回复
     *
     * @param complainId
     * @param answer
     */
    void updateAnswer(int complainId, String answer);


    TbComplain loadComplain(int id);

}
