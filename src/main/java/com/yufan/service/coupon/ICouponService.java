package com.yufan.service.coupon;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.CouponCondition;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.utils.PageInfo;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/13
 */
public interface ICouponService {

    public PageInfo loadCouponPage(int currePage, CouponCondition couponCondition);

    public TbCoupon loadCouponById(int id);

    public void updateStatus(int id, int status);

    public void updateIsPutAway(int id, int isPutAway);

    public JSONObject saveCouponData(TbCoupon coupon);

    public PageInfo loadCouponQrPage(int currePage, CouponCondition couponCondition);

    public JSONObject changeQrCode(String qrCoode,String checkCode);

}
