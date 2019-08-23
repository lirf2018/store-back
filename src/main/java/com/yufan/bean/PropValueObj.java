package com.yufan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/13 17:14
 * 功能介绍: 属性值
 */
@Setter
@Getter
public class PropValueObj {
    private Integer valueId;
    private String valueName;
    private String value;
    private Integer relGoods;//是否与商品关联 1 关联

    private Integer propId;
    private Integer categoryId;
    private String outeId;
    private Integer dataIndex;
    private Integer status;
    private String remark;
    private String valueImg;
    private Integer shopId;
}
