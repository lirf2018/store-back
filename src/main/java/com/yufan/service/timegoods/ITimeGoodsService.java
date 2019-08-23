package com.yufan.service.timegoods;

import com.yufan.bean.TimeGoodsCondition;
import com.yufan.pojo.TbTimeGoods;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 11:25
 * 功能介绍:
 */
public interface ITimeGoodsService {


    /**
     * 查询分页数据
     *
     * @param currePage
     * @param timeGoodsCondition
     * @return
     */
    PageInfo loadDataPage(int currePage, TimeGoodsCondition timeGoodsCondition);

    /**
     * 保存数据
     *
     * @param timeGoods
     * @return
     */
    int saveTimeGoods(TbTimeGoods timeGoods);

    /**
     * 更新数据状态
     *
     * @param timeGoodsId
     * @param status
     */
    void updateTimeGoodsStatus(int goodsId,int timeGoodsId, int status);


    /**
     * 查询对象数据
     *
     * @param timeGoodsId
     * @return
     */
    TbTimeGoods loadTimeGoods(int timeGoodsId);


}
