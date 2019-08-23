package com.yufan.dao.shop.impl;

import com.yufan.bean.ShopCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.shop.IShopDao;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 11:30
 * 功能介绍:
 */
@Transactional
@Repository
public class ShopDaoImpl implements IShopDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    private String ImgUrl = Constants.IMG_URL;

    @Override
    public PageInfo loadDataPage(int currePage, ShopCondition shopCondition) {

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT s.shop_id,s.shop_name,concat('").append(ImgUrl).append("',s.shop_logo) as shop_logo,s.shop_tel1,s.shop_tel2,s.shop_lng,s.shop_lat,s.weight,s.introduce,s.toway,s.admin_id, ");
        sql.append(" s.address,s.area_id,CONCAT(s.deposit,'') as deposit,DATE_FORMAT(s.deposit_time,'%Y-%m-%d') as deposit_time,CONCAT('',s.shop_money) as shop_money,s.shop_code, ");
        sql.append(" DATE_FORMAT(s.createtime,'%Y-%m-%d %T') as createtime,s.status,s.remark,DATE_FORMAT(s.enter_start_time,'%Y-%m-%d') as enter_start_time,DATE_FORMAT(s.enter_end_time,'%Y-%m-%d') as enter_end_time,s.is_out_shop, ");
        sql.append(" if(s.is_out_shop = 1,'内部店铺','第三方店铺') as is_out_shop_name ");
        sql.append("  from tb_shop s where 1=1 ");
        if (StringUtils.isNotEmpty(shopCondition.getShopName())) {
            sql.append(" and s.shop_name like '%").append(shopCondition.getShopName().trim()).append("%' ");
        }
        if (null != shopCondition.getIsInOrOut() && shopCondition.getIsInOrOut() != -1) {
            sql.append(" and s.is_out_shop =").append(shopCondition.getIsInOrOut()).append(" ");
        }
        if (null != shopCondition.getStatus() && shopCondition.getStatus() != -1) {
            sql.append(" and s.status =").append(shopCondition.getStatus()).append(" ");
        }
        sql.append("ORDER BY weight desc,s.shop_id desc");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void updateShopStatus(int shopId, int status) {
        String sql = " update tb_shop set status=? where shop_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, shopId);
    }

    @Override
    public boolean checkShopCode(Integer shopId, String shopCode) {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" select shop_id,shop_code from tb_shop where shop_code=?  ");
            if (null != shopId && 0 != shopId) {
                sql.append(" and shop_id !=").append(shopId).append(" ");
            }
            List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql.toString(), shopCode);
            if (null != list && list.size() == 0) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
