package com.yufan.dao.shop;

import com.yufan.bean.ShopCondition;
import com.yufan.pojo.TbMendian;
import com.yufan.pojo.TbShop;
import com.yufan.utils.PageInfo;

import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 11:29
 * 功能介绍:
 */
public interface IShopDao {

    public PageInfo loadDataPage(int currePage, ShopCondition shopCondition);


    public void updateShopStatus(int shopId, int status);

    boolean checkShopCode(Integer shopId,String shopCode);

    /**
     * 查询门店列表
     * @return
     */
    public List<TbMendian> loadMendian(int shopId);
}
