package com.yufan.service.second;

import com.yufan.pojo.TbSecondGoods;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/30 17:53
 * 功能介绍: 只提供简单浏览的简单商品
 */
public interface ISecondGoodsService {

    PageInfo loadDataPage(int currePage, TbSecondGoods secondGoods);


    public TbSecondGoods loadSecondGoods(int id);

    public void saveSecondGoods(TbSecondGoods secondGoods);

    public void updateSecondGoodsStatus(int id, int status);
}
