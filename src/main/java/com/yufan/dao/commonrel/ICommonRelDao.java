package com.yufan.dao.commonrel;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/9 10:20
 * 功能介绍: 中间表
 */
public interface ICommonRelDao {

    /**
     * 查询关联图片
     *
     * @param imgType      1:商品banner 2:商品图片介绍 3:店铺banner 4:店铺介绍图片 5:卡券banner 6:卡券介绍图片
     * @param relateId     店铺标识/商品标识/卡券标识
     * @param classifyType 0.商品图片1.卡券图片2.店铺图片
     * @return
     */
    public List<Map<String, Object>> queryTableRelImg(Integer imgType, int relateId, Integer classifyType);

    /**
     * 删除关联图片
     *
     * @param relateId     店铺标识/商品标识/卡券标识
     * @param imgType      1:商品banner 2:商品图片介绍 3:店铺banner 4:店铺介绍图片 5:卡券banner 6:卡券介绍图片
     * @param classifyType 0.商品图片1.卡券图片2.店铺图片
     */
    public void deletRelImg(int relateId, Integer imgType, Integer classifyType);


    public void saveObject(Object obj);

    /**
     * @param shopId
     * @param goodsId
     * @param relType 0 闲菜商品  1 商城商品
     * @return
     */
    public List<Map<String, Object>> queryShopGoodsRel(int shopId, int goodsId, int relType);

    public List<Map<String, Object>> queryShopGoodsRel(int shopId, int goodsId);

    public List<Map<String, Object>> queryShopGoodsRelByShopId(int shopId);

    public List<Map<String, Object>> queryShopGoodsRelByGoodsId(int goodsId);

    public List<Map<String, Object>> queryShopGoodsRel();

}
