package com.yufan.service.banner;

import com.yufan.bean.BannerCondition;
import com.yufan.pojo.TbBanner;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 12:00
 * 功能介绍:
 */
public interface IBannerService {

    /**
     * 查询分页数据
     * @param currePage
     * @param bannerCondition
     * @return
     */
    PageInfo loadDataPage(int currePage, BannerCondition bannerCondition);

    /**
     * 保存数据
     * @param banner
     * @return
     */
    int saveBanner(TbBanner banner);

    /**
     * 更新数据状态
     * @param bannerId
     * @param status
     */
    void updateBannerStatus(int bannerId, int status);


    /**
     * 查询对象数据
     * @param bannerId
     * @return
     */
    TbBanner loadBanner(int bannerId);




}
