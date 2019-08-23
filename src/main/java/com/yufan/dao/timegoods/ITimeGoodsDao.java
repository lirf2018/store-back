package com.yufan.dao.timegoods;

import com.yufan.bean.TimeGoodsCondition;
import com.yufan.pojo.TbTimeGoods;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 11:23
 * 功能介绍:
 */
public interface ITimeGoodsDao {

    /**
     * 查询分页数据
     *
     * @param currePage
     * @param timeGoodsCondition
     * @return
     */
    PageInfo loadDataPage(int currePage, TimeGoodsCondition timeGoodsCondition);

    /**
     * 更新数据状态
     *
     * @param timeGoodsId
     * @param status
     */
    void updateTimeGoodsStatus(int timeGoodsId, int status);


    /**
     * 同一个商品只能有一个抢购
     * @param goodsId
     * @param status
     */
    void deleteTimeGoods(int goodsId,int status);


}
