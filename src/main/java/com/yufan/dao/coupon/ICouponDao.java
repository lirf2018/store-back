package com.yufan.dao.coupon;

import com.yufan.bean.CouponCondition;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/12
 */
public interface ICouponDao {


    PageInfo loadCouponPage(int currePage, CouponCondition couponCondition);

    TbCoupon loadCouponById(int id);

    void updateStatus(int id, int status);

    void updateIsPutAway(int id, int isPutAway);

    void updateCouponData(TbCoupon coupon);

    void saveCouponData(TbCoupon coupon);

    Map<String, Object> loadCouponDataMap(int couponId);

    PageInfo loadCouponQrPage(int currePage, CouponCondition couponCondition);

    TbCouponDownQr loadCouponDownQr(String qrCoode, Integer qrStatus);

    PageInfo giveCouponListData(int currePage, CouponCondition couponCondition);

    void deleteGiveCouponData(int id);

    List<Map<String, Object>> findUserGiveCouponListByStatus(int status, String userPhones, int couponId);

}
