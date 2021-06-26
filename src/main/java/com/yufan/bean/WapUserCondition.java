package com.yufan.bean;

import lombok.Data;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/12
 */
@Data
public class WapUserCondition {

    private Integer id;
    private String userMobile;
    private String memberId;
    private Integer userId;
    private Integer userStatus;
    private String nickName;

    // 私人定制
    private String privateCode;
    private String getTime;
    private Integer status;
    private Integer postWay;
    private Integer flowStatus;
    private Integer goodsId;
    private String goodsName;
}
