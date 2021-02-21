package com.yufan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/21 18:01
 * 功能介绍:
 */
@Getter
@Setter
public class RegionCondition {

    private Integer status;
    private Integer regionType;
    private Integer regionLevel;
    private String regionCode;
    private String parentId;
    private String regionName;
    private String regionNameStr;// 多条件查询用分号分开
}
