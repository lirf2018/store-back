package com.yufan.service.second.impl;

import com.sun.org.apache.bcel.internal.generic.DADD;
import com.yufan.bean.GoodsCondition;
import com.yufan.dao.commonrel.ICommonRelDao;
import com.yufan.dao.second.ISecondGoodsDao;
import com.yufan.dao.second.ISecondGoodsJpaDao;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.service.second.ISecondGoodsService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/30 17:53
 * 功能介绍: 只提供简单浏览的简单商品
 */
@Service
public class SecondGoodsServiceImpl implements ISecondGoodsService {


    @Autowired
    private ISecondGoodsDao iSecondGoodsDao;

    @Autowired
    private ISecondGoodsJpaDao iSecondGoodsJpaDao;

    @Autowired
    private ICommonRelDao iCommonRelDao;


    @Override
    public PageInfo loadDataPage(int currePage, TbSecondGoods secondGoods) {
        return iSecondGoodsDao.loadDataPage(currePage, secondGoods);
    }

    @Override
    public TbSecondGoods loadSecondGoods(int goodsId) {
        return iSecondGoodsJpaDao.getOne(goodsId);
    }

    @Override
    public boolean saveSecondGoods(TbSecondGoods secondGoods) {
        secondGoods.setCreateTime(new Timestamp(new Date().getTime()));
        secondGoods.setLikeNum(0);
        secondGoods.setReadNum(0);
        secondGoods.setSuperLike(0);
        try {
            if (secondGoods.getGoodsId() > 0) {
                TbSecondGoods s = iSecondGoodsDao.loadTbSecondGoods(secondGoods.getGoodsId());
                secondGoods.setLikeNum(s.getLikeNum());
                secondGoods.setReadNum(s.getReadNum());
                secondGoods.setSuperLike(s.getSuperLike());
                secondGoods.setGoodsShopCode(s.getGoodsShopCode());
            }
            if (StringUtils.isEmpty(secondGoods.getGoodsShopCode())) {
                //生成商品店铺唯一码
                int nowGoodsShopCode = iSecondGoodsDao.getGoodsShopCodeMax();
                if (nowGoodsShopCode == 0) {
                    System.out.printf("-----查询失败---");
                    return false;
                }
                String goodsShopCode = goodsShopCode(String.valueOf((nowGoodsShopCode + 1)));
                secondGoods.setGoodsShopCode(goodsShopCode);
                if (StringUtils.isEmpty(secondGoods.getGoodsShopCode())) {
                    System.out.printf("-----------生成店铺码失败--------------");
                    return false;
                }
            }
            iSecondGoodsJpaDao.save(secondGoods);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.printf("---保存失败--");
        return false;
    }

    private String goodsShopCode(String str) {
        String goodsShopCode = "";
        int len = 10;//编码长度
        if (str.length() > len) {
            System.out.printf("------长度已超----");
            return "";
        }
        int strLen = str.length();
        int eq = len - strLen;
        String index = "";//补充0数
        for (int i = 0; i < eq; i++) {
            index = index + "0";
        }
        goodsShopCode = index + str;
        return goodsShopCode;

    }

    @Override
    public void updateSecondGoodsStatus(int goodsId, int status) {
        iSecondGoodsDao.updateSecondGoodsStatus(goodsId, status);
    }

    @Override
    public List<Map<String, Object>> queryShopGoodsRel(int shopId, int goodsId, int relType) {
        return iCommonRelDao.queryShopGoodsRel(shopId, goodsId, relType);
    }

    /************************手机端页面**********************************/
    /**
     * 更新浏览数
     *
     * @param goodsId
     */
    @Override
    public void UpdateSecondGoodsReadCount(int goodsId) {
        iSecondGoodsDao.UpdateSecondGoodsReadCount(goodsId);
    }


    /**
     * 查询商品列表
     *
     * @param
     * @return
     */
    @Override
    public PageInfo loadGoodsList(GoodsCondition condition) {
        return iSecondGoodsDao.loadGoodsList(condition);
    }
    /************************手机端页面**********************************/

}
