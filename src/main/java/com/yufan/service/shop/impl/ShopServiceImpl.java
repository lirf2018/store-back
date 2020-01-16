package com.yufan.service.shop.impl;

import com.yufan.bean.ShopCondition;
import com.yufan.dao.commonrel.ICommonRelDao;
import com.yufan.dao.shop.IShopDao;
import com.yufan.dao.shop.IShopJpaDao;
import com.yufan.pojo.TbImg;
import com.yufan.pojo.TbMendian;
import com.yufan.pojo.TbShop;
import com.yufan.service.shop.IShopService;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/5/21 16:58
 * 功能介绍:
 */
@Service
@Transactional
public class ShopServiceImpl implements IShopService {

    @Autowired
    private IShopJpaDao iShopJpaDao;

    @Autowired
    private IShopDao iShopDao;

    @Autowired
    private ICommonRelDao iCommonRelDao;

    @Override
    public List<TbShop> findShopAll() {
        return iShopJpaDao.findShopAll();
    }

    @Override
    public List<TbShop> findShopAll(int shopId) {
        return iShopJpaDao.findShopAll(shopId);
    }

    @Override
    public PageInfo loadDataPage(int currePage, ShopCondition shopCondition) {
        return iShopDao.loadDataPage(currePage, shopCondition);
    }

    @Transactional
    @Override
    public int saveShop(TbShop shop, List<TbImg> listImg) {
        try {
            Integer shopId = shop.getShopId();
            if (null != shopId && shopId > 0) {
                //删除已保存的图片
                iCommonRelDao.deletRelImg(shop.getShopId(), Constants.IMGTYPE_SHOP_INFO, Constants.CLASSIFY_SHOP);
            }
            //保存店铺
            shopId = iShopJpaDao.save(shop).getShopId();

            //保存图片
            for (int i = 0; i < listImg.size(); i++) {
                TbImg img = listImg.get(i);
                img.setImgType(Constants.IMGTYPE_SHOP_INFO);
                img.setImgClassify(Constants.CLASSIFY_SHOP);
                img.setRelateId(shopId);
                img.setCreatetime(new Timestamp(new Date().getTime()));
                img.setStatus(1);
                iCommonRelDao.saveObject(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void updateShopStatus(int shopId, int status) {
        iShopDao.updateShopStatus(shopId, status);
    }

    @Override
    public boolean checkShopCode(Integer shopId, String shopCode) {
        return iShopDao.checkShopCode(shopId, shopCode);
    }

    @Override
    public TbShop loadShop(int shopId) {
        return iShopJpaDao.getOne(shopId);
    }

    @Override
    public TbShop loadShopBySecretKey(String secretKey) {
        return iShopJpaDao.loadShopBySecretKey(secretKey);
    }

    @Override
    public List<TbMendian> loadMendian(int shopId) {
        return iShopDao.loadMendian(shopId);
    }


}
