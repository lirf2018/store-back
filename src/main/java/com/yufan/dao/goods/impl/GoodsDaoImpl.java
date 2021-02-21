package com.yufan.dao.goods.impl;

import com.yufan.bean.ActivityCondition;
import com.yufan.bean.GoodsCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.goods.IGoodsDao;
import com.yufan.pojo.TbGoodsSku;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 12:35
 * 功能介绍:
 */
@Repository
@Transactional
public class GoodsDaoImpl implements IGoodsDao {

    @Autowired
    private IGeneralDao iGeneralDao;


    @Override
    public void updateGoodsToTimeGoods(int goodsId, int isTimeGoods) {
        String sql = " update tb_goods set is_time_goods = ?,lastaltertime=now() where   goods_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, isTimeGoods, goodsId);
    }

    @Override
    public PageInfo loadDataPage(int currePage, GoodsCondition goodsCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT g.goods_id,g.goods_name,g.title,CONCAT(g.true_money,'') as true_money,CONCAT(g.now_money,'') as now_money,g.intro,g.shop_id,g.is_yuding,g.get_way, ");
        sql.append(" g.is_invoice,g.is_putaway,CONCAT('").append(Constants.IMG_WEB_URL).append("',g.goods_img) as goods_img,g.data_index,g.category_id,g.area_id,g.property, ");
        sql.append(" DATE_FORMAT(g.start_time,'%Y-%m-%d %T') as start_time,DATE_FORMAT(g.end_time,'%Y-%m-%d %T') as end_time, ");
        sql.append(" g.goods_code,g.goods_unit,g.is_single,if(g.is_single=1,g.goods_num,(select sum(sku.sku_num) from tb_goods_sku sku where sku.`status`=1 and sku.goods_id=g.goods_id)) as goods_num,g.is_return,g.coupon_id,DATE_FORMAT(g.createtime,'%Y-%m-%d %T') as createtime, ");
        sql.append(" g.status,g.remark,g.goods_type,g.is_pay_online,g.out_code,CONCAT(g.deposit_money,'') as deposit_money,g.peisong_zc_desc,g.peisong_pei_desc, ");
        sql.append(" CONCAT(g.purchase_price,'') as purchase_price,g.is_time_goods,g.limit_num,g.bar_code,g.bar_code_shop,g.sell_count, ");
        sql.append(" g.limit_way,DATE_FORMAT(g.limit_begin_time,'%Y-%m-%d %T') as limit_begin_time,g.level_id,CONCAT(g.advance_price,'') as advance_price ");
        sql.append(" ,cl.level_name,ca.category_name,IFNULL(tg.id,0) as time_goods_id,g.is_zi_yin ");
        sql.append(" from tb_goods g LEFT JOIN tb_category_level cl on cl.level_id=g.level_id LEFT JOIN tb_category ca on ca.category_id=g.category_id  ");
        sql.append(" LEFT JOIN tb_time_goods tg on tg.goods_id=g.goods_id and tg.`status`=1 ");
        sql.append(" where 1=1 ");

        if (null != goodsCondition.getGoodsId()) {
            sql.append(" and g.goods_id=").append(goodsCondition.getGoodsId()).append(" ");
        }
        if (StringUtils.isNotEmpty(goodsCondition.getGoodsName())) {
            sql.append(" and g.goods_name like '%").append(goodsCondition.getGoodsName().trim()).append("%' ");
        }
        if (StringUtils.isNotEmpty(goodsCondition.getGoodsCode())) {
            sql.append(" and g.goods_code='").append(goodsCondition.getGoodsCode().trim()).append("' ");
        }
        if (goodsCondition.getGoodsType() != null) {
            sql.append(" and g.goods_type=").append(goodsCondition.getGoodsType()).append(" ");
        }
        if (goodsCondition.getProperty() != null) {
            sql.append(" and g.property=").append(goodsCondition.getProperty()).append(" ");
        }
        if (goodsCondition.getIsPutaway() != null) {
            sql.append(" and g.is_putaway=").append(goodsCondition.getIsPutaway()).append(" ");
            if(goodsCondition.getIsPutaway() == 2){
                sql.append(" and NOW()>= g.start_time AND NOW()<= g.end_time ");
            }
        }
        if (goodsCondition.getStatus() != null) {
            sql.append(" and g.`status`=").append(goodsCondition.getStatus()).append(" ");
        }
        if (goodsCondition.getIsSingle() != null) {
            sql.append(" and g.is_single=").append(goodsCondition.getIsSingle()).append(" ");
        }
        if (goodsCondition.getIsTimeGoods() != null) {
            sql.append(" and g.is_time_goods=").append(goodsCondition.getIsTimeGoods()).append(" ");
        }
        if (goodsCondition.getIsPayOnline() != null) {
            sql.append(" and g.is_pay_online=").append(goodsCondition.getIsPayOnline()).append(" ");
        }
        if (goodsCondition.getGetWay() != null) {
            sql.append(" and g.get_way=").append(goodsCondition.getGetWay()).append(" ");
        }
        if (goodsCondition.getIsYuding() != null) {
            sql.append(" and g.is_yuding=").append(goodsCondition.getIsYuding()).append(" ");
        }
        if (goodsCondition.getLevelId() != null) {
            sql.append(" and g.level_id=").append(goodsCondition.getLevelId()).append(" ");
        }
        if (goodsCondition.getCategoryId() != null) {
            sql.append(" and g.category_id=").append(goodsCondition.getCategoryId()).append(" ");
        }
        if (goodsCondition.getCouponId() != null) {
            sql.append(" and g.coupon_id=").append(goodsCondition.getCouponId()).append(" ");
        }
        if (goodsCondition.getShopId() != null) {
            sql.append(" and g.shop_id=").append(goodsCondition.getShopId()).append(" ");
        }
        if (goodsCondition.getIsZiYin() != null&&goodsCondition.getIsZiYin()>-1) {
            sql.append(" and g.is_zi_yin=").append(goodsCondition.getIsZiYin()).append(" ");
        }
        sql.append(" ORDER BY g.data_index DESC,g.goods_id desc ");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public int saveObject(Object object) {
        return iGeneralDao.save(object);
    }

    @Override
    public void updateGoodsSku(TbGoodsSku goodsSku) {
        String sql = " update tb_goods_sku set goods_id =? ,sku_name=? ,true_money=? ,now_money=? ,sku_code=?,prop_code=?,prop_code_name=?,sku_num=?,sku_img=?,purchase_price=? where sku_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, goodsSku.getGoodsId(), goodsSku.getSkuName(), goodsSku.getTrueMoney(), goodsSku.getNowMoney(),
                goodsSku.getSkuCode(), goodsSku.getPropCode(),goodsSku.getPropCodeName(), goodsSku.getSkuNum(), goodsSku.getSkuImg(), goodsSku.getPurchasePrice(), goodsSku.getSkuId());
    }

    @Override
    public void updateGoodsSingle(int goodsId, int isSingle) {
        String sql = " update tb_goods set is_single=? ,lastaltertime=now() where  goods_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, isSingle, goodsId);
    }

    @Override
    public void updateGoodsStatus(int goodsId, int status) {
        String sql = " update tb_goods set `status`=?,lastaltertime=now()  where  goods_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, goodsId);
    }

    @Override
    public void updateObject(Object object) {
        iGeneralDao.saveOrUpdate(object);
    }


    @Override
    public List<Map<String, Object>> loadGoodsSkuListMap(int goodsId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT sku_id,goods_id,sku_name,CONCAT(true_money,'') as true_money,CONCAT(now_money,'') as now_money,sku_code,prop_code,sku_num, ");
        sql.append(" CONCAT('").append(Constants.IMG_WEB_URL).append("',sku_img) as sku_web_img,sku_img,CONCAT(purchase_price,'') as purchase_price,sell_count ");
        sql.append(" from tb_goods_sku ");
        sql.append(" where goods_id=? and status=1 ");
        return iGeneralDao.getBySQLListMap(sql.toString(), goodsId);
    }

    @Override
    public void updateGoodsOnSell(int goodsId, int isPutway) {
        String sql = " update tb_goods set is_putaway=?,lastaltertime=now()  where goods_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, isPutway, goodsId);
    }

    @Override
    public List<Map<String, Object>> loadGoodsAttribute(int goodsId) {
        String sql = " SELECT attr_id,goods_id,prop_id,value_id from tb_goods_attribute where goods_id=? ";
        return iGeneralDao.getBySQLListMap(sql, goodsId);
    }

    @Override
    public void updateGoodsSkuStatus(int goodsId, int status) {
        String sql = " update tb_goods_sku set status=? where goods_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, goodsId);
    }

    @Override
    public void updateGoodsSkuStatus(int goodsId, int skuId, int status) {
        String sql = " update tb_goods_sku set status=? where sku_id=? and goods_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, skuId, goodsId);
    }

    @Override
    public void updateGoodsSkuStatusNoinSkuId(int goodsId, String skuIds, int status) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_goods_sku set status=").append(status).append(" where sku_id not in (").append(skuIds).append(") and goods_id=").append(goodsId).append(" ");
        iGeneralDao.executeUpdateForSQL(sql.toString());
    }

    @Override
    public void deleteGoodsAttribute(int goodsId) {
        String sql = " delete from tb_goods_attribute where goods_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, goodsId);
    }

    @Override
    public Map<String, Object> getGoodsInfoMap(int goodsId) {
        String sql = " SELECT  goods_id,createman,createtime,sell_count,is_time_goods  from tb_goods where goods_id=? ";
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql, goodsId);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
