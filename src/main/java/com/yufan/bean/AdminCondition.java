package com.yufan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 创建人: lirf
 * 创建时间:  2019/5/21 14:55
 * 功能介绍: 用户管理查询条件
 */
@Setter
@Getter
public class AdminCondition {

    private String userName;
    private String loginName;
    private String idcard;
    private Integer roleId;
    private String phone;
    private String shopName;
    private Integer status;

}
