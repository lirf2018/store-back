package com.yufan.dao.second.impl;

import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.second.ISecondGoodsDao;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.utils.Constants;
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
        sql.append(" SELECT id,goods_name,CONCAT(true_price,'') as true_price,CONCAT(now_price,'') as now_price,CONCAT(purchase_price,'') as purchase_price,read_num,like_num,new_info,is_post, ");
        sql.append(" about_price,super_like,`status`,img4,img3,img2,img1,goods_code,goods_shop_code,data_index,concat('" + Constants.IMG_URL + "',goods_img) as goods_web_img, ");
        sql.append(" CONCAT('" + Constants.IMG_URL + "',img4) as web_img4,CONCAT('" + Constants.IMG_URL + "',img3) as web_img3,CONCAT('" + Constants.IMG_URL + "',img2) as web_img2,CONCAT('" + Constants.IMG_URL + "',img1) as web_img1 ");
        sql.append(" from tb_second_goods ");
        sql.append(" where status !=0 ");
        if (StringUtils.isNotEmpty(secondGoods.getGoodsName())) {
            sql.append("and goods_name like '%").append(secondGoods.getGoodsName().trim()).append("%' ");
        }
        if (-1 != secondGoods.getIsPost()) {
            sql.append("and is_post=").append(secondGoods.getIsPost()).append(" ");
        }
        if (-1 != secondGoods.getStatus()) {
            sql.append("and status=").append(secondGoods.getStatus()).append(" ");
        }
        if (-1 != secondGoods.getAboutPrice()) {
            sql.append("and about_price=").append(secondGoods.getAboutPrice()).append(" ");
        }
        if (StringUtils.isNotEmpty(secondGoods.getGoodsCode())) {
            sql.append(" and goods_code='").append(secondGoods.getGoodsCode().trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(secondGoods.getGoodsShopCode())) {
            sql.append(" and goods_shop_code='").append(secondGoods.getGoodsShopCode().trim()).append("' ");
        }
        if (-1 != secondGoods.getNewInfo()) {
            sql.append(" and new_info=").append(secondGoods.getNewInfo()).append(" ");
        }
        sql.append(" order by data_index desc,id desc ");

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

    @Override
    public TbSecondGoods loadTbSecondGoods(int id) {
        String hql = " from TbSecondGoods where id=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, id);
    }
}
