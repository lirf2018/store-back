package com.yufan.service.shop;

import com.yufan.bean.ShopCondition;
import com.yufan.pojo.TbImg;
import com.yufan.pojo.TbShop;
import com.yufan.utils.PageInfo;

import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/5/21 16:58
 * 功能介绍:
 */
public interface IShopService {

    List<TbShop> findShopAll();
    List<TbShop> findShopAll(int shopId);

    PageInfo loadDataPage(int currePage, ShopCondition shopCondition);

    int saveShop(TbShop shop, List<TbImg> listImg);

    void updateShopStatus(int shopId, int status);

    boolean checkShopCode(Integer shopId,String shopCode);

    TbShop loadShop(int shopId);
    TbShop loadShopBySecretKey(String secretKey);

}
