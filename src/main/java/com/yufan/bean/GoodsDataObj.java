package com.yufan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/15 15:39
 * 功能介绍: 商品参数
 */
@Setter
@Getter
public class GoodsDataObj {

    //bannel图片
    private String img5;
    private String img6;
    private String img7;
    private String img8;
    //介绍图片
    private String img9;
    private String img10;
    private String img11;
    private String img12;
    //商品销售属性
    private String skuCode;
    private String skuPurchasePrice;
    private String skuTrueMoney;
    private String skuNowMoney;
    private String skuNum;
    private String skuImg;
    private String skuId;
    private String skuName;
    private String skuPropCode;
    //商品非销售属性
    private String unsellPropId;


    private String createman;
}
