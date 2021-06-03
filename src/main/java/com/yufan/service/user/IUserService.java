package com.yufan.service.user;

import com.yufan.bean.AdminCondition;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbMemberId;
import com.yufan.pojo.TbUserRole;
import com.yufan.utils.PageInfo;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/22 15:47
 * 功能介绍:
 */
public interface IUserService {

    TbAdmin findByUserLoginName(String loginName);

    PageInfo loadPrivUserInfoPage(int currePage, AdminCondition adminCondition);

    TbAdmin finAdminById(int adminId);

    void updateAdminStatus(int adminId, int status);

    TbAdmin saveAdmin(TbAdmin admin,int roleId);

    /**
     * 查询系统用户角色
     *
     * @param adminId
     * @return
     */
    public TbUserRole loadUserRoleByAdminId(int adminId);

    /**
     * 修改登录用户密码
     * @param newPasswd
     * @param loginId
     */
    public void updateLoginPasswd(String newPasswd,int loginId);


    PageInfo loadMemberIdPage(int currePage, TbMemberId memberId);

    boolean checkMemberCode(String memberId);

    void saveObj(Object obj);

    void deleteMemberCode(Integer id);
}
