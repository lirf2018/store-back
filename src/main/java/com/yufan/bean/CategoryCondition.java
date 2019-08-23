package com.yufan.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/7 9:58
 * 功能介绍:
 */
@Setter
@Getter
public class CategoryCondition {

    private String levelName;//一级分类名称
    private String levelCode;//一级分类编码
    private Integer levelStatus;//一级状态
    private Integer categoryId;//类目标识
    private String categoryName;//类目名称
    private Integer categoryType;//类型

}
