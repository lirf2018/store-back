package com.yufan.service.second.impl;

import com.yufan.dao.second.ISecondGoodsDao;
import com.yufan.dao.second.ISecondGoodsJpaDao;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.service.second.ISecondGoodsService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/30 17:53
 * 功能介绍: 只提供简单浏览的简单商品
 */
@Service
public class SecondGoodsServiceImpl implements ISecondGoodsService {


    @Autowired
    private ISecondGoodsDao iSecondGoodsDao;

    private ISecondGoodsJpaDao iSecondGoodsJpaDao;


    @Override
    public PageInfo loadDataPage(int currePage, TbSecondGoods secondGoods) {
        return null;
    }

    @Override
    public TbSecondGoods loadSecondGoods(int id) {
        return null;
    }

    @Override
    public void saveSecondGoods(TbSecondGoods secondGoods) {

    }

    @Override
    public void updateSecondGoodsStatus(int id, int status) {

    }
}
