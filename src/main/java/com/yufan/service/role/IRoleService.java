package com.yufan.service.role;


import com.yufan.pojo.TbRole;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/29 10:27
 * 功能介绍:
 */
public interface IRoleService {

    Page<TbRole> loadTbPrivRoleListPage(int currentPage, int pagesize);

    List<TbRole> findRoleAll();

    void updateRoleStatus(int roleId, int status);

    TbRole saveRole(TbRole role);

    int saveRoleFun(int roleId,String checkValues);

    List<Map<String,Object>> loadRoleFunListMap(int roleId);

}
