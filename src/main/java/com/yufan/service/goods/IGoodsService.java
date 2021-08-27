package com.yufan.service.goods;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.BannerCondition;
import com.yufan.bean.GoodsCondition;
import com.yufan.bean.GoodsDataObj;
import com.yufan.pojo.TbBanner;
import com.yufan.pojo.TbGoods;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 12:34
 * 功能介绍:
 */
public interface IGoodsService {


    /**
     * 查询分页数据
     *
     * @param currePage
     * @param goodsCondition
     * @return
     */
    PageInfo loadDataPage(int currePage, GoodsCondition goodsCondition);

    /**
     * 保存数据
     *
     * @param goods
     * @return
     */
    JSONObject saveGoods(TbGoods goods, GoodsDataObj goodsDataObj);

    /**
     * 更新数据状态
     *
     * @param goodsId
     * @param status
     */
    void updateGoodsStatus(int goodsId, int status);


    TbGoods loadGoods(int goodsId);

    /**
     * 查询商品sku列表
     *
     * @param goodsId
     * @return
     */
    List<Map<String, Object>> loadGoodsSkuListMap(int goodsId);

    /**
     * 商品上下架
     * @param goodsId
     * @param isPutway 上架状态 0下架 1等待确认 2销售中
     */
    void updateGoodsOnSell(int goodsId, int isPutway);
    void updateGoodsOnSell(String goodsIds, int isPutway);


    /**
     * 取消抢购
     * @param goodsId
     */
    void deleteTimeGoods(int goodsId);

    /**
     * 查询商品销售和非销售属性关联
     * @param goodsId
     * @return
     */
    List<Map<String,Object>> loadGoodsAttribute(int goodsId);

}
