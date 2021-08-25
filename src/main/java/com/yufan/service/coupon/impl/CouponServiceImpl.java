package com.yufan.service.coupon.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.CouponCondition;
import com.yufan.bean.RequestData;
import com.yufan.bean.ResponeData;
import com.yufan.dao.coupon.ICouponDao;
import com.yufan.dao.coupon.ICouponJapDao;
import com.yufan.dao.user.IInfAccountJpaDao;
import com.yufan.pojo.TbCoupon;
import com.yufan.pojo.TbInfAccount;
import com.yufan.service.coupon.ICouponService;
import com.yufan.utils.*;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
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

    @Autowired
    private IInfAccountJpaDao iInfAccountJpaDao;

    @Value("${info.service}")
    private String SERVICE;

    @Value("${info.account}")
    private String ACCOUNT;

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
        JSONObject result = HelpCommon.packagMsg("1");
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
        JSONObject out = HelpCommon.packagMsg("0");
        try {
            TbInfAccount account = iInfAccountJpaDao.findByAccount(ACCOUNT);
            if (null == account) {
                out = HelpCommon.packagMsg("33");
                return out;
            }
            String reqType = Constants.BUSINESS_TYPE_1;
            String sid = account.getSid();
            String appsecret = account.getSecretKey();
            JSONObject data = new JSONObject();
            data.put("qrCode", qrCoode);
            RequestData requestData = new RequestData(reqType, sid, appsecret, data);
            String param = requestData.requestParam().toJSONString();
            String result = HttpRequest.httpPost(SERVICE, param);
            if (StringUtils.isNotEmpty(result)) {
                JSONObject obj = JSONObject.parseObject(result);
                ResponeData responeData = JSONObject.toJavaObject(obj, ResponeData.class);
                if (responeData.getRespCode() == 1) {
                    out = HelpCommon.packagMsg("1");
                } else {
                    out = HelpCommon.packagMsg("-100", responeData.getRespDesc());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public PageInfo giveCouponListData(int currePage, CouponCondition couponCondition) {
        return iCouponDao.giveCouponListData(currePage, couponCondition);
    }

    @Override
    public void deleteGiveCouponData(int id) {
        iCouponDao.deleteGiveCouponData(id);
    }

    @Override
    public List<Map<String, Object>> findUserGiveCouponListByStatus(int status, String userPhones, int couponId) {
        return iCouponDao.findUserGiveCouponListByStatus(status, userPhones, couponId);
    }
}
