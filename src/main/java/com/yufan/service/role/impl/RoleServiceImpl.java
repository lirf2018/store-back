package com.yufan.service.role.impl;

import com.yufan.dao.func.IMenuDao;
import com.yufan.dao.role.IRoleJpaDao;
import com.yufan.pojo.TbRole;
import com.yufan.service.role.IRoleService;
import com.yufan.utils.DatetimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/29 10:27
 * 功能介绍:
 */
@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private IRoleJpaDao iRoleJpaDao;

    @Autowired
    private IMenuDao iMenuDao;

    @Override
    public Page<TbRole> loadTbPrivRoleListPage(int currentPage, int pagesize) {
        //jpa的Page currePage是从0开始
        Sort sort = new Sort(Sort.Direction.DESC, "roleId");
        Pageable pageable = PageRequest.of((currentPage - 1), pagesize, sort);
        return iRoleJpaDao.findAll(pageable);
    }

    @Override
    public List<TbRole> findRoleAll() {
        return iRoleJpaDao.findAll();
    }

    @Override
    public void updateRoleStatus(int roleId, int status) {
        TbRole role = iRoleJpaDao.getOne(roleId);
        role.setStatus(status);
        role.setCreatetime(new Timestamp(new Date().getTime()));
        iRoleJpaDao.save(role);
    }

    @Override
    public TbRole saveRole(TbRole role) {
        return iRoleJpaDao.save(role);
    }

    @Transactional
    @Override
    public int saveRoleFun(int roleId, String checkValues) {
        try {

            iMenuDao.delRoleFun(roleId);
            //删除角色功能
            if ("".equals(checkValues)) {
                return 1;
            }
            String[] checkValuesArray = checkValues.split(",");
            //(role_id,function_id,createtime)
            String dateTime = DatetimeUtil.getNow();
            StringBuffer valuesString = new StringBuffer();
            for (int i = 0; i < checkValuesArray.length; i++) {
                if (!StringUtils.isEmpty(checkValuesArray[i])) {
                    valuesString.append("(").append(roleId).append(",").append(checkValuesArray[i]).append(",'").append(dateTime).append("'),");
                }
            }
            iMenuDao.insertMoreBySQL(valuesString.toString());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public List<Map<String, Object>> loadRoleFunListMap(int roleId) {
        return iMenuDao.loadRoleFunListMap(roleId);
    }
}
