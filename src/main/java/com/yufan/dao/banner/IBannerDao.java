package com.yufan.dao.banner;

import com.yufan.bean.BannerCondition;
import com.yufan.pojo.TbBanner;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 11:59
 * 功能介绍:
 */
public interface IBannerDao {

    /**
     * 查询分页数据
     * @param currePage
     * @param bannerCondition
     * @return
     */
    PageInfo loadDataPage(int currePage, BannerCondition bannerCondition);

    /**
     * 更新数据状态
     * @param bannerId
     * @param status
     */
    void updateBannerStatus(int bannerId, int status);

}
