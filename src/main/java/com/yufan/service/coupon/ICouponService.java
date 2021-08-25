package com.yufan.service.coupon;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.CouponCondition;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/13
 */
public interface ICouponService {

    PageInfo loadCouponPage(int currePage, CouponCondition couponCondition);

    TbCoupon loadCouponById(int id);

    void updateStatus(int id, int status);

    void updateIsPutAway(int id, int isPutAway);

    JSONObject saveCouponData(TbCoupon coupon);

    PageInfo loadCouponQrPage(int currePage, CouponCondition couponCondition);

    JSONObject changeQrCode(String qrCoode, String checkCode);

    PageInfo giveCouponListData(int currePage, CouponCondition couponCondition);

    void deleteGiveCouponData(int id);

    List<Map<String, Object>> findUserGiveCouponListByStatus(int status, String userPhones, int couponId);

}
