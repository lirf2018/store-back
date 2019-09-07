package com.yufan.dao.timegoods.impl;

import com.yufan.bean.TimeGoodsCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.timegoods.ITimeGoodsDao;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 11:24
 * 功能介绍:
 */
@Transactional
@Repository
public class TimeGoodsDaoImpl implements ITimeGoodsDao {


    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadDataPage(int currePage, TimeGoodsCondition timeGoodsCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT tg.id,tg.goods_id,DATE_FORMAT(tg.begin_time,'%Y-%m-%d %T') as begin_time,DATE_FORMAT(tg.end_time,'%Y-%m-%d %T') as end_time,CONCAT(tg.time_price,'') as time_price,tg.goods_store,tg.limit_num,tg.time_way,tg.weight,tg.`status`,DATE_FORMAT(tg.createtime,'%Y-%m-%d %T') as createtime,DATE_FORMAT(tg.limit_begin_time,'%Y-%m-%d %T') as limit_begin_time ");
        sql.append(" ,g.goods_name,CONCAT(g.now_money,'') as now_money ");
        sql.append(" from tb_time_goods tg  JOIN tb_goods g on g.goods_id=tg.goods_id where 1=1 ");

        if (!StringUtils.isEmpty(timeGoodsCondition.getGoodsName())) {
            sql.append(" and g.goods_name like '%").append(timeGoodsCondition.getGoodsName().trim()).append("%' ");
        }
        if (timeGoodsCondition.getGoodsId() != null && timeGoodsCondition.getGoodsId() != -1) {
            sql.append(" and tg.goods_id=").append(timeGoodsCondition.getGoodsId()).append(" ");
        }
        if (timeGoodsCondition.getStatus() != null && timeGoodsCondition.getStatus() != -1) {
            sql.append(" and tg.`status`=").append(timeGoodsCondition.getStatus()).append(" ");
        }
        if (timeGoodsCondition.getShopId() != null) {
            sql.append(" and g.shop_id=").append(timeGoodsCondition.getShopId()).append(" ");
        }
        sql.append(" ORDER BY tg.weight desc,tg.status desc,g.data_index desc,tg.createtime desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void updateTimeGoodsStatus(int timeGoodsId, int status) {
        String sql = " update tb_time_goods set `status`=? where id = ? ";
        iGeneralDao.executeUpdateForSQL(sql, status, timeGoodsId);
    }

    @Override
    public void deleteTimeGoods(int goodsId, int status) {
        String sql = " update tb_time_goods set status=? where goods_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, goodsId);
    }
}
