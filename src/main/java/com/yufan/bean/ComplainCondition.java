package com.yufan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/9 23:18
 * 功能介绍: 投诉建议
 */
@Setter
@Getter
public class ComplainCondition {

    private Integer userId;
    private String information;
    private String contents;
    private Integer status;
    private Integer isRead;
    private Integer complainType; //类型 0 投诉  1建议
}
