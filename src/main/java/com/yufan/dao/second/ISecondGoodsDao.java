package com.yufan.dao.second;

import com.yufan.pojo.TbSecondGoods;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/30 17:55
 * 功能介绍: 只提供简单浏览的简单商品
 */
public interface ISecondGoodsDao {


    PageInfo loadDataPage(int currePage, TbSecondGoods secondGoods);

    public void updateSecondGoodsStatus(int id, int status);

    public TbSecondGoods loadTbSecondGoods(int id);

}
