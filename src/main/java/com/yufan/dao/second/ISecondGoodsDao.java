package com.yufan.dao.second;

import com.yufan.bean.GoodsCondition;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/30 17:55
 * 功能介绍: 只提供简单浏览的简单商品
 */
public interface ISecondGoodsDao {


    PageInfo loadDataPage(int currePage, TbSecondGoods secondGoods);

    public void updateSecondGoodsStatus(int goodsId, int status);

    public TbSecondGoods loadTbSecondGoods(int goodsId);

    /**
     * 获取最大编码
     *
     * @return
     */
    public int getGoodsShopCodeMax();


    /************************手机端页面**********************************/
    /**
     * 更新浏览数
     *
     * @param goodsId
     */
    public void UpdateSecondGoodsReadCount(int goodsId);


    /**
     * 查询商品列表
     *
     * @param
     * @return
     */
    public PageInfo loadGoodsList(GoodsCondition condition);
    /************************手机端页面**********************************/


}
