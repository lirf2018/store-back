package com.yufan.service.timegoods.impl;

import com.yufan.bean.TimeGoodsCondition;
import com.yufan.dao.goods.IGoodsDao;
import com.yufan.dao.timegoods.ITimeGoodsDao;
import com.yufan.dao.timegoods.ITimeGoodsJapDao;
import com.yufan.pojo.TbTimeGoods;
import com.yufan.service.timegoods.ITimeGoodsService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 11:26
 * 功能介绍:
 */
@Service
public class TimeGoodsServiceImpl implements ITimeGoodsService {

    @Autowired
    private ITimeGoodsDao iTimeGoodsDao;

    @Autowired
    private ITimeGoodsJapDao iTimeGoodsJapDao;

    @Autowired
    private IGoodsDao iGoodsDao;

    @Override
    public PageInfo loadDataPage(int currePage, TimeGoodsCondition timeGoodsCondition) {
        return iTimeGoodsDao.loadDataPage(currePage, timeGoodsCondition);
    }

    @Transactional
    @Override
    public int saveTimeGoods(TbTimeGoods timeGoods) {
        try {
            //设置商品为抢购商品
            iGoodsDao.updateGoodsToTimeGoods(timeGoods.getGoodsId(),1 );
            //对同一个商品只能设置 一条有效的抢购
            if (timeGoods.getId() == 0) {
                iTimeGoodsDao.deleteTimeGoods(timeGoods.getGoodsId(), 0);
            }
            return iTimeGoodsJapDao.save(timeGoods).getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Transactional
    @Override
    public void updateTimeGoodsStatus(int goodsId, int timeGoodsId, int status) {
        try {
            if (status == 0) {
                //删除商品为抢购商品
                iGoodsDao.updateGoodsToTimeGoods(0, goodsId);
            }
            iTimeGoodsDao.updateTimeGoodsStatus(timeGoodsId, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public TbTimeGoods loadTimeGoods(int timeGoodsId) {
        return iTimeGoodsJapDao.getOne(timeGoodsId);
    }
}
