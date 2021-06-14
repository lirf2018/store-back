package com.yufan.service.coupon.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.CouponCondition;
import com.yufan.dao.coupon.ICouponDao;
import com.yufan.dao.coupon.ICouponJapDao;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbCouponDownQr;
import com.yufan.service.coupon.ICouponService;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/13
 */
@Service
public class CouponServiceImpl implements ICouponService {

    @Autowired
    private ICouponDao iCouponDao;


    @Autowired
    private ICouponJapDao iCouponJapDao;

    @Override
    public PageInfo loadCouponPage(int currePage, CouponCondition couponCondition) {
        return iCouponDao.loadCouponPage(currePage, couponCondition);
    }

    @Override
    public TbCoupon loadCouponById(int id) {
        return iCouponDao.loadCouponById(id);
    }

    @Override
    public void updateStatus(int id, int status) {
        iCouponDao.updateStatus(id, status);
    }

    @Override
    public void updateIsPutAway(int id, int isPutAway) {
        iCouponDao.updateIsPutAway(id, isPutAway);
    }

    @Override
    public JSONObject saveCouponData(TbCoupon coupon) {
        JSONObject result = CommonMethod.packagMsg("1");
        if (coupon.getCouponId() > 0) {
            Map<String, Object> map = iCouponDao.loadCouponDataMap(coupon.getCouponId());
            if (map.get("createtime") != null) {
                String createtime = map.get("createtime").toString();
                try {
                    coupon.setCreatetime(new Timestamp(DatetimeUtil.convertStrToDate(createtime, DatetimeUtil.DEFAULT_DATE_FORMAT_STRING).getTime()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            coupon.setCreateman(String.valueOf(map.get("createman")));
            iCouponDao.updateCouponData(coupon);
        } else {
            iCouponDao.saveCouponData(coupon);
        }
        return result;

    }

    @Override
    public PageInfo loadCouponQrPage(int currePage, CouponCondition couponCondition) {
        return iCouponDao.loadCouponQrPage(currePage, couponCondition);
    }

    @Override
    public JSONObject changeQrCode(String qrCoode, String checkCode) {
        JSONObject out = CommonMethod.packagMsg("1");
        TbCouponDownQr qr = iCouponDao.loadCouponDownQr(qrCoode.trim(), 1);
        try {
            if (qr != null) {
                out = CommonMethod.packagMsg("25");
                return out;
            }
            if (!checkCode.equals(qr.getCheckCode())) {
                out = CommonMethod.packagMsg("26");
                return out;
            }
            if (qr.getRecodeState() != 1) {
                out = CommonMethod.packagMsg("27");
                return out;
            }
            // 二维码每次生成的有效时间
            long outTime = qr.getQrCodeOuttime().getTime();
            long nowTime = System.currentTimeMillis();
            if (nowTime > outTime) {
                out = CommonMethod.packagMsg("28");
                return out;
            }
            iCouponDao.updateCouponDownQr(qr.getId(), 2);
        } catch (Exception e) {
            throw new RuntimeException("兑换异常");
        }
        return out;
    }
}
