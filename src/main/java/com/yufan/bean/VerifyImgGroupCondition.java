package com.yufan.bean;

import lombok.Data;

/**
 * @description:
 * @author: lirf
 * @time: 2021/7/1
 */
@Data
public class VerifyImgGroupCondition {

    private String verifyCode;
    private Integer status;
    private Integer verifyType;
    private String imgUuid;
    private Integer similarType;
}
