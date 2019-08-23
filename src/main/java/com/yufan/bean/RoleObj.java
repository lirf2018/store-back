package com.yufan.bean;

import com.yufan.pojo.TbRole;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/5/23 14:59
 * 功能介绍:
 */
@Setter
@Getter
public class RoleObj {
    private Integer roleId;
    private String roleName;
    private Integer roleParentid;
    private String createman;
    private Timestamp createtime;
    private Integer status;
    private String remark;
    private List<RoleObj> childRole;
}
