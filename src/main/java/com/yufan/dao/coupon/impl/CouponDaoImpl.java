package com.yufan.dao.coupon.impl;

import com.yufan.bean.CouponCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.coupon.ICouponDao;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/12
 */
@Repository
@Transactional
public class CouponDaoImpl implements ICouponDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadCouponPage(int currePage, CouponCondition couponCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select c.coupon_id,c.coupon_name,c.title,CONCAT('").append(Constants.IMG_WEB_URL).append("',c.coupon_img) as coupon_img,c.intro,c.shop_id,c.weight,c.classify_id,c.area_id, ");
        sql.append(" c.is_show,c.coupon_type,DATE_FORMAT(c.start_time,'%Y-%m-%d')  as start_time,DATE_FORMAT(c.end_time,'%Y-%m-%d')  as end_time,c.out_date,c.createman, ");
        sql.append(" DATE_FORMAT(c.createtime,'%Y-%m-%d %T')  as createtime,c.lastaltertime,c.lastalterman,c.status,c.remark,c.need_know,c.count_get, ");
        sql.append(" c.coupon_num,c.is_putaway,c.limit_num,c.limit_way,c.leve1_id,c.appoint_type,DATE_FORMAT(c.appoint_date,'%Y-%m-%d')  as appoint_date,c.limit_begin_time,c.get_type,c.coupon_price, ");
        sql.append(" if(c.end_time<NOW(),0,1) as ac_type ,l.level_name,ca.category_name ");
        sql.append(" from tb_coupon c  LEFT JOIN tb_category_level l on l.level_id=c.leve1_id and l.status=1 LEFT JOIN tb_category ca on ca.category_id=c.classify_id and ca.status=1 where 1=1 ");
        if (couponCondition.getCouponId() != null) {
            sql.append(" and c.coupon_id = ").append(couponCondition.getCouponId()).append(" ");
        }
        if (StringUtils.isNotEmpty(couponCondition.getCouponName())) {
            sql.append(" and c.coupon_name like '%").append(couponCondition.getCouponName().trim()).append("%' ");
        }
        if (couponCondition.getIsPutaway() != null) {
            sql.append(" and c.is_putaway = ").append(couponCondition.getIsPutaway()).append(" ");
        }
        if (couponCondition.getLeve1Id() != null) {
            sql.append(" and c.leve1_id = ").append(couponCondition.getLeve1Id()).append(" ");
        }
        if (couponCondition.getClassifyId() != null) {
            sql.append(" and c.classify_id = ").append(couponCondition.getClassifyId()).append(" ");
        }
        if (couponCondition.getIsShow() != null) {
            sql.append(" and c.is_show = ").append(couponCondition.getIsShow()).append(" ");
        }
        if (couponCondition.getCouponType() != null) {
            sql.append(" and c.coupon_type = ").append(couponCondition.getCouponType()).append(" ");
        }
        if (couponCondition.getGetType() != null) {
            sql.append(" and c.get_type = ").append(couponCondition.getGetType()).append(" ");
        }
        if (couponCondition.getAppointType() != null) {
            sql.append(" and c.appoint_type = ").append(couponCondition.getAppointType()).append(" ");
        }
        if (couponCondition.getStatus() != null) {
            sql.append(" and c.status = ").append(couponCondition.getStatus()).append(" ");
        }
        if (couponCondition.getShopId() != null) {
            sql.append(" and c.shop_id = ").append(couponCondition.getShopId()).append(" ");
        }
        sql.append(" ORDER BY c.coupon_id desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public TbCoupon loadCouponById(int id) {
        String hql = " from TbCoupon where   couponId=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, id);
    }

    @Override
    public void updateStatus(int id, int status) {
        String sql = " update tb_coupon set status=?,lastaltertime=now() where coupon_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, id);
    }

    @Override
    public void updateIsPutAway(int id, int isPutAway) {
        String sql = " update tb_coupon set is_putaway=?,lastaltertime=now() where couponId=? ";
        iGeneralDao.executeUpdateForSQL(sql, isPutAway, id);
    }

    @Override
    public void updateCouponData(TbCoupon coupon) {
        iGeneralDao.saveOrUpdate(coupon);
    }

    @Override
    public void saveCouponData(TbCoupon coupon) {
        iGeneralDao.save(coupon);
    }

    @Override
    public Map<String, Object> loadCouponDataMap(int couponId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select coupon_id,createman,DATE_FORMAT(createtime,'%Y-%m-%d %T') as createtime from tb_coupon where coupon_id=? ");
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql.toString(), couponId);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return new HashMap<>();
    }

    @Override
    public PageInfo loadCouponQrPage(int currePage, CouponCondition couponCondition) {
        StringBuffer sql = new StringBuffer();

        sql.append(" select  q.id,q.coupon_id,q.coupon_name,q.user_id,q.channel_id,q.qr_img,q.change_code,q.check_code,q.content,DATE_FORMAT(q.change_out_date,'%Y-%m-%d') as change_out_date,q.recode_state,q.appoint_type, ");
        sql.append(" q.change_user_id,q.change_partners_id,q.status,q.remark,q.coupon_type,q.coupon_value,q.shop_id,q.qr_desc,q.get_type,q.coupon_price,DATE_FORMAT(q.createtime,'%Y-%m-%d %T') as createtime ");
        sql.append(" ,DATE_FORMAT(q.change_date,'%Y-%m-%d %T') as change_date,u.user_mobile as phone  ");
        sql.append(" from tb_coupon_down_qr q JOIN tb_user_info u on u.user_id=q.user_id where 1=1 ");
        if (couponCondition.getId() != null) {
            sql.append("  and q.id=").append(couponCondition.getId()).append(" ");
        }
        if (couponCondition.getCouponId() != null) {
            sql.append("  and q.coupon_id=").append(couponCondition.getCouponId()).append(" ");
        }
        if (couponCondition.getCouponType() != null) {
            sql.append("  and q.coupon_type=").append(couponCondition.getCouponType()).append(" ");
        }
        if (couponCondition.getGetType() != null) {
            sql.append("  and q.get_type=").append(couponCondition.getGetType()).append(" ");
        }
        if (couponCondition.getAppointType() != null) {
            sql.append("  and q.appoint_type=").append(couponCondition.getAppointType()).append(" ");
        }
        if (couponCondition.getRecodeState() != null) {
            sql.append("  and q.recode_state=").append(couponCondition.getRecodeState()).append(" ");
        }
        if (StringUtils.isNotEmpty(couponCondition.getCouponName())) {
            sql.append(" and q.coupon_name like '%").append(couponCondition.getCouponName().trim()).append("%' ");
        }
        if (StringUtils.isNotEmpty(couponCondition.getChangeCode())) {
            sql.append(" and q.change_code like '%").append(couponCondition.getChangeCode().trim()).append("%' ");
        }
        if(StringUtils.isNotEmpty(couponCondition.getPhone())){
            sql.append(" and u.user_mobile = '").append(couponCondition.getPhone().trim()).append("' ");
        }

        sql.append(" ORDER BY q.createtime desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public TbCouponDownQr loadCouponDownQr(String qrCoode, Integer qrStatus) {
        String hql = " from TbCouponDownQr where changeCode=?1 and recodeState=?2 ";
        return iGeneralDao.queryUniqueByHql(hql, qrCoode, qrStatus);
    }

}
