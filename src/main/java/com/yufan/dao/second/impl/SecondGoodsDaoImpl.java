package com.yufan.dao.second.impl;

import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.second.ISecondGoodsDao;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/30 17:55
 * 功能介绍: 只提供简单浏览的简单商品
 */
@Transactional
@Repository
public class SecondGoodsDaoImpl implements ISecondGoodsDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadDataPage(int currePage, TbSecondGoods secondGoods) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT id,goods_name,CONCAT(true_price,'') as true_price,CONCAT(now_price,'') as now_price,CONCAT(purchase_price,'') as purchase_price,read_num,like_num,new_info,is_post ");
        sql.append("about_price,super_like,`status`,img4,img3,img2,img1,goods_code,goods_shop_code, ");
        sql.append("CONCAT('',img4) as web_img4,CONCAT('',img3) as web_img3,CONCAT('',img2) as web_img2,CONCAT('',img1) as web_img1 ");
        sql.append("from tb_second_goods ");
        sql.append("where 1=1 ");
        if(StringUtils.isNotEmpty(secondGoods.getGoodsName())){
            sql.append("and goods_name like '%").append(secondGoods.getGoodsName().trim()).append("%' ");
        }
        sql.append("and is_post=1 ");
        sql.append("and status=1 ");
        sql.append("and about_price=1 ");
        sql.append(" and goods_code='' ");
        sql.append(" and goods_shop_code='' ");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void updateSecondGoodsStatus(int id, int status) {
        String sql = " update tb_second_goods set `status`=? where id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, id);
    }
}
