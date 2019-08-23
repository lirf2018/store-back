package com.yufan.dao.goods;

import com.yufan.bean.GoodsCondition;
import com.yufan.pojo.TbGoods;
import com.yufan.pojo.TbGoodsSku;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 12:35
 * 功能介绍:
 */
public interface IGoodsDao {


    /**
     * 更新商品字段 isTimeGoods
     *
     * @param goodsId
     * @param isTimeGoods 是否抢购商品 0不是抢购商品 1抢购商品
     */
    public void updateGoodsToTimeGoods(int goodsId, int isTimeGoods);

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
     * @param object
     * @return
     */
    int saveObject(Object object);

    /**
     * 更新数据状态
     *
     * @param goodsId
     * @param status
     */
    void updateGoodsStatus(int goodsId, int status);


    /**
     * 查询商品sku列表
     *
     * @param goodsId
     * @return
     */
    List<Map<String, Object>> loadGoodsSkuListMap(int goodsId);

    /**
     * 商品上下架
     *
     * @param goodsId
     * @param isPutway 上架状态 0下架 1等待确认 2销售中
     */
    void updateGoodsOnSell(int goodsId, int isPutway);

    /**
     * 查询商品销售和非销售属性关联
     *
     * @param goodsId
     * @return
     */
    List<Map<String, Object>> loadGoodsAttribute(int goodsId);

    /**
     * 更新对象
     * @param object
     */
    void updateObject(Object object);

    void updateGoodsSku(TbGoodsSku goodsSku);

    void updateGoodsSingle(int goodsId,int isSingle);

    /**
     * 删除商品sku
     *
     * @param goodsId
     * @param status
     */
    void updateGoodsSkuStatus(int goodsId, int status);

    void updateGoodsSkuStatus(int goodsId, int skuId, int status);

    void updateGoodsSkuStatusNoinSkuId(int goodsId, String skuIds, int status);

    /**
     * 删除商品属性
     * @param goodsId
     */
    void deleteGoodsAttribute(int goodsId);
}
