package com.yufan.service.second.impl;

import com.sun.org.apache.bcel.internal.generic.DADD;
import com.yufan.dao.second.ISecondGoodsDao;
import com.yufan.dao.second.ISecondGoodsJpaDao;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.service.second.ISecondGoodsService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

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


    @Override
    public PageInfo loadDataPage(int currePage, TbSecondGoods secondGoods) {
        return iSecondGoodsDao.loadDataPage(currePage, secondGoods);
    }

    @Override
    public TbSecondGoods loadSecondGoods(int id) {
        return iSecondGoodsJpaDao.getOne(id);
    }

    @Override
    public void saveSecondGoods(TbSecondGoods secondGoods) {
        secondGoods.setCreateTime(new Timestamp(new Date().getTime()));
        secondGoods.setLikeNum(0);
        secondGoods.setReadNum(0);
        secondGoods.setSuperLike(0);
        if (secondGoods.getId() > 0) {
            TbSecondGoods s = iSecondGoodsDao.loadTbSecondGoods(secondGoods.getId());
            secondGoods.setLikeNum(s.getLikeNum());
            secondGoods.setReadNum(s.getReadNum());
            secondGoods.setSuperLike(s.getSuperLike());
        }
        iSecondGoodsJpaDao.save(secondGoods);
    }

    @Override
    public void updateSecondGoodsStatus(int id, int status) {
        iSecondGoodsDao.updateSecondGoodsStatus(id, status);
    }
}
