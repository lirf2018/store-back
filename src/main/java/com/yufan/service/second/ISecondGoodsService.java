package com.yufan.service.second;

import com.yufan.bean.GoodsCondition;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/30 17:53
 * 功能介绍: 只提供简单浏览的简单商品
 */
public interface ISecondGoodsService {

    PageInfo loadDataPage(int currePage, TbSecondGoods secondGoods);


    public TbSecondGoods loadSecondGoods(int goodsId);

    public boolean saveSecondGoods(TbSecondGoods secondGoods);

    public void updateSecondGoodsStatus(int goodsId, int status);

    public List<Map<String, Object>> queryShopGoodsRel(int shopId,int goodsId,int relType);

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
