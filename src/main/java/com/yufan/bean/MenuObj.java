package com.yufan.bean;

import com.yufan.pojo.TbFunctions;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/5/23 18:21
 * 功能介绍:
 */
@Data
public class MenuObj {
    private Integer functionId;
    private String functionCode;
    private String functionName;
    private Integer functionParentid;
    private Integer functionType;
    private Integer dataIndex;
    private String functionAction;
    private String createman;
    private Timestamp createtime;
    private Integer status;
    private String remark;
    private List<TbFunctions> childMenu;
}
