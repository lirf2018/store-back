package com.yufan.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/13 17:12
 * 功能介绍: 属性
 */
@Setter
@Getter
public class ItempropObj {
    
    private Integer propId;//属性ID
    private Integer isSales;//是否销售属性:0不是销售属性1是销售属性
    private String showView;//显示类型:checkbox：多选 select：下拉列表',
    private String propName;//属性名
    private Integer categoryId;
    private String outeId;
    private String propImg;
    private String propWebImg;
    private String propCode;
    private Integer isShow;
    private Integer dataIndex;
    private Integer status;
    private String remark;
    private Integer shopId;
    private Integer categoryType;

    //属性值
    private String valueIds;
    private String outeIds;
    private String valueNames;
    private String values;
    private String dataIndexs;



    private List<PropValueObj> propValueObjList;//属性值列表
}
