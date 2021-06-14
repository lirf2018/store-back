package com.yufan.dao.coupon;

import com.yufan.bean.CouponCondition;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.utils.PageInfo;

import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/12
 */
public interface ICouponDao {


    public PageInfo loadCouponPage(int currePage, CouponCondition couponCondition);

    public TbCoupon loadCouponById(int id);

    public void updateStatus(int id, int status);

    public void updateIsPutAway(int id, int isPutAway);

    public void updateCouponData(TbCoupon coupon);

    public void saveCouponData(TbCoupon coupon);

    public Map<String, Object> loadCouponDataMap(int couponId);

    public PageInfo loadCouponQrPage(int currePage, CouponCondition couponCondition);

    public TbCouponDownQr loadCouponDownQr(String qrCoode, Integer qrStatus);

    public void updateCouponDownQr(int id, int recodeState);

}
