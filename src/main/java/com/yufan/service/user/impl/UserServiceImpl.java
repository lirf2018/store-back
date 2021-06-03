package com.yufan.service.user.impl;

import com.yufan.bean.AdminCondition;
import com.yufan.dao.user.IUserDao;
import com.yufan.dao.user.IUserJpaDao;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbMemberId;
import com.yufan.pojo.TbUserRole;
import com.yufan.service.user.IUserService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/22 15:47
 * 功能介绍:
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserJpaDao iUserJpaDao;

    @Autowired
    private IUserDao iUserDao;

    @Override
    public TbAdmin findByUserLoginName(String loginName) {
        return iUserJpaDao.findByUserLoginName(loginName);
    }

    public Page<TbAdmin> loadPrivUserPage(String userName, int currePage, int pageSize) {
        Pageable pageable = PageRequest.of((currePage - 1), pageSize);//分页信息
        //条件
        Specification<TbAdmin> spec = new Specification<TbAdmin>() {
            @Override
            public Predicate toPredicate(Root<TbAdmin> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //构造条件
                Predicate nameCondition = criteriaBuilder.like(root.get("userName"), "%" + userName + "%");
                return nameCondition;
            }
        };
        return iUserJpaDao.findAll(spec, pageable);
    }

    @Override
    public PageInfo loadPrivUserInfoPage(int currePage, AdminCondition adminCondition) {
        return iUserDao.loadPrivUserInfoPage(currePage, adminCondition);
    }

    @Override
    public TbAdmin finAdminById(int adminId) {
        return iUserJpaDao.getOne(adminId);
    }

    @Override
    public void updateAdminStatus(int adminId, int status) {
        iUserDao.updateAdminStatus(adminId, status);
    }

    @Override
    @Transactional
    public TbAdmin saveAdmin(TbAdmin admin, int roleId) {
        try {
            //保存用户角色(先删除现保存)
            if (admin.getAdminId() != null && admin.getAdminId() > 0) {
                iUserDao.delUserRole(admin.getAdminId());
            }
            iUserJpaDao.save(admin);
            TbUserRole userRole = new TbUserRole();
            userRole.setAdminId(admin.getAdminId());
            userRole.setCreatetime(new Timestamp(new Date().getTime()));
            userRole.setRoleId(roleId);
            iUserDao.saveUserRole(userRole);
            return admin;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public TbUserRole loadUserRoleByAdminId(int adminId) {
        return iUserDao.loadUserRoleByAdminId(adminId);
    }

    @Override
    public void updateLoginPasswd(String newPasswd, int loginId) {

    }

    @Override
    public PageInfo loadMemberIdPage(int currePage, TbMemberId memberId) {
        return iUserDao.loadMemberIdPage(currePage, memberId);
    }

    @Override
    public boolean checkMemberCode(String memberId) {
        return iUserDao.checkMemberCode(memberId);
    }

    @Override
    public void saveObj(Object obj) {
        iUserDao.saveObj(obj);
    }

    @Override
    public void deleteMemberCode(Integer id) {
        iUserDao.deleteMemberCode(id);
    }
}
