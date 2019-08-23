package com.yufan.bean;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/19 16:27
 * 功能介绍:
 */
@Setter
@Getter
public class CategoryObj {
    private int categoryId;
    private String categoryName;
    private Integer isParent;
    private Integer dataIndex;
    private String outeId;
    private String categoryImg;
    private String categoryWebImg;
    private String categoryCode;
    private Integer isShow;
    private String createman;
    private Timestamp createtime;
    private Timestamp lastaltertime;
    private String lastalterman;
    private Integer status;
    private String remark;
    private Integer shopId;
    private List<ItempropObj> itempropObjList;
}
