package com.yufan.service.banner.impl;

import com.yufan.bean.BannerCondition;
import com.yufan.dao.banner.IBannerDao;
import com.yufan.dao.banner.IBannerJpaDao;
import com.yufan.pojo.TbBanner;
import com.yufan.service.banner.IBannerService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 12:01
 * 功能介绍:
 */
@Service
public class BannerServiceImpl implements IBannerService {

    @Autowired
    private IBannerJpaDao iBannerJpaDao;

    @Autowired
    private IBannerDao iBannerDao;


    @Override
    public PageInfo loadDataPage(int currePage, BannerCondition bannerCondition) {
        return iBannerDao.loadDataPage(currePage,bannerCondition);
    }

    @Override
    public int saveBanner(TbBanner banner) {
        return iBannerJpaDao.save(banner).getBannerId();
    }

    @Override
    public void updateBannerStatus(int bannerId, int status) {
        iBannerDao.updateBannerStatus(bannerId,status);
    }

    @Override
    public TbBanner loadBanner(int bannerId) {
        return iBannerJpaDao.getOne(bannerId);
    }
}
