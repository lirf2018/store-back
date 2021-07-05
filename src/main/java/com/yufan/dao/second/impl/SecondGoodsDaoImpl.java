package com.yufan.dao.second.impl;

import com.yufan.bean.GoodsCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.second.ISecondGoodsDao;
import com.yufan.pojo.TbSecondGoods;
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
        sql.append(" SELECT goods_id,goods_name,CONCAT(true_price,'') as true_price,CONCAT(now_price,'') as now_price,CONCAT(purchase_price,'') as purchase_price,read_num,like_num,new_info,is_post, ");
        sql.append(" about_price,super_like,`status`,img4,img3,img2,img1,goods_code,goods_shop_code,data_index,concat('" + Constants.IMG_WEB_URL + "',goods_img) as goods_web_img, ");
        sql.append(" CONCAT('" + Constants.IMG_WEB_URL + "',img4) as web_img4,CONCAT('" + Constants.IMG_WEB_URL + "',img3) as web_img3,CONCAT('" + Constants.IMG_WEB_URL + "',img2) as web_img2,CONCAT('" + Constants.IMG_WEB_URL + "',img1) as web_img1 ");
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
        if (secondGoods.getShopId() != null) {
            sql.append(" and shop_id=").append(secondGoods.getShopId()).append(" ");
        }
        sql.append(" order by data_index desc,read_num desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void updateSecondGoodsStatus(int goodsId, int status) {
        String sql = " update tb_second_goods set `status`=? where goods_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, goodsId);
    }

    @Override
    public TbSecondGoods loadTbSecondGoods(int goodsId) {
        String hql = " from TbSecondGoods where goodsId=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, goodsId);
    }

    @Override
    public int getGoodsShopCodeMax() {
        String sql = " SELECT goods_shop_code from tb_second_goods ORDER BY goods_shop_code desc LIMIT 1 ";
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql);
        if (null != list && list.size() > 0) {
            String goodsShopCode = list.get(0).get("goods_shop_code").toString();
            return Integer.parseInt(goodsShopCode);
        }
        if (null != list && list.size() == 0) {
            return 1;
        }
        return 0;
    }

    /************************手机端页面**********************************/
    @Override
    public void UpdateSecondGoodsReadCount(int goodsId) {
        String sql = " UPDATE tb_second_goods set read_num=read_num+1 where goods_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, goodsId);
    }

    @Override
    public PageInfo loadGoodsList(GoodsCondition condition) {
        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT t.goods_id,t.goods_name,t.goods_img,t.now_price,t.read_num,t.shop_id,t.data_index from ( ");

        sql.append(" SELECT sg.goods_id,sg.goods_name,sg.goods_img,sg.now_price,sg.read_num,sg.shop_id,sg.data_index  ");
        sql.append(" from tb_second_goods sg JOIN tb_shop s on s.shop_id=sg.shop_id where sg.status=1 and s.`status`=1 and  s.secret_key='").append(condition.getSecretKey()).append("' ");
        if (StringUtils.isNotEmpty(condition.getGoodsName())) {
            sql.append(" and sg.goods_name like '%").append(condition.getGoodsName().trim()).append("%' ");
        }
        sql.append(" UNION ");

        sql.append(" SELECT sg.goods_id,sg.goods_name,sg.goods_img,rel.sale_price as now_price ,sg.read_num,sg.shop_id,rel.data_index from tb_shop_goods_rel rel  ");
        sql.append(" JOIN tb_second_goods sg  on rel.goods_id=sg.goods_id where rel.rel_type=0 and rel.shop_id=(SELECT shop_id from tb_shop where `status`=1 and secret_key='").append(condition.getSecretKey()).append("') ");
        if (StringUtils.isNotEmpty(condition.getGoodsName())) {
            sql.append(" and sg.goods_name like '%").append(condition.getGoodsName().trim()).append("%' ");
        }
        sql.append(" ) t ");
        sql.append(" ORDER BY t.data_index desc,t.read_num desc ");


        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(condition.getCurrePage());
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }
/************************手机端页面**********************************/

}
