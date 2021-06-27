package com.yufan.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 21:16
 * 功能介绍:
 */
@Setter
@Getter
public class GoodsCondition {

    private Integer goodsId;
    private String goodsName;
    private Integer isYuding;
    private Integer getWay;
    private Integer isPutaway;
    private Integer categoryId;
    private Integer property;
    private String goodsCode;
    private Integer isSingle;
    private Integer couponId;
    private Integer status;
    private Integer goodsType;
    private Integer isPayOnline;
    private Integer isTimeGoods;
    private Integer levelId;
    private Integer shopId;
    private String secretKey;
    private Integer isZiYin;


    @JSONField(name = "curre_page")
    private Integer currePage;

    @JSONField(name = "page_size")
    private Integer pageSize;

     private Integer yuding;

}