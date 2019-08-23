package com.yufan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 14:28
 * 功能介绍:
 */
@Setter
@Getter
public class ShopCondition {

    private String shopName;//店铺名称
    private Integer isInOrOut;
    private Integer status;
}
