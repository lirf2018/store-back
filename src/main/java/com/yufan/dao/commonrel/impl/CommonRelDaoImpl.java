package com.yufan.dao.commonrel.impl;

import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.commonrel.ICommonRelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/9 10:21
 * 功能介绍:
 */
@Repository
@Transactional
public class CommonRelDaoImpl implements ICommonRelDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public List<Map<String, Object>> queryTableRelImg(Integer imgType, int relateId, Integer classifyType) {

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT img_id,img_url,img_type,img_classify,img_sort from tb_img ");
        sql.append(" where status=1 and relate_id=").append(relateId).append(" ");
        if (null != imgType) {
            sql.append(" and img_type=").append(imgType).append(" ");
        }
        if (null != classifyType) {
            sql.append(" and img_classify=").append(classifyType).append(" ");
        }
        sql.append(" ORDER BY img_sort desc,createtime DESC ");
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void deletRelImg(int relateId, Integer imgType, Integer classifyType) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tb_img set status=0 where status=1 and relate_id=").append(relateId).append(" ");
        if (null != imgType) {
            sql.append(" and img_type=").append(imgType).append(" ");
        }
        if (null != classifyType) {
            sql.append(" and img_classify=").append(classifyType).append(" ");
        }
        iGeneralDao.executeUpdateForSQL(sql.toString());
    }

    @Override
    public void saveObject(Object obj) {
        iGeneralDao.save(obj);
    }

    @Override
    public List<Map<String, Object>> queryShopGoodsRel(int shopId, int goodsId, int relType) {
        String sql = " SELECT id,goods_id,shop_id,rel_type,sale_price from tb_shop_goods_rel where shop_id=? and goods_id=? and rel_type=? ";
        return iGeneralDao.getBySQLListMap(sql, shopId, goodsId,relType);
    }

    @Override
    public List<Map<String, Object>> queryShopGoodsRel(int shopId, int goodsId) {
        String sql = " SELECT id,goods_id,shop_id,rel_type,sale_price from tb_shop_goods_rel where shop_id=? and goods_id=? ";
        return iGeneralDao.getBySQLListMap(sql, shopId, goodsId);
    }

    @Override
    public List<Map<String, Object>> queryShopGoodsRelByShopId(int shopId) {
        String sql = " SELECT id,goods_id,shop_id,rel_type,sale_price from tb_shop_goods_rel where shop_id=? ";
        return iGeneralDao.getBySQLListMap(sql, shopId);
    }

    @Override
    public List<Map<String, Object>> queryShopGoodsRelByGoodsId(int goodsId) {
        String sql = " SELECT id,goods_id,shop_id,rel_type,sale_price from tb_shop_goods_rel where goods_id=? ";
        return iGeneralDao.getBySQLListMap(sql, goodsId);
    }

    @Override
    public List<Map<String, Object>> queryShopGoodsRel() {
        String sql = " SELECT id,goods_id,shop_id,rel_type,sale_price from tb_shop_goods_rel ";
        return iGeneralDao.getBySQLListMap(sql);
    }
}
