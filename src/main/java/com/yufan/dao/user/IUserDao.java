package com.yufan.dao.user;


import com.yufan.bean.AdminCondition;
import com.yufan.bean.WapUserCondition;
import com.yufan.pojo.TbMemberId;
import com.yufan.pojo.TbUserRole;
import com.yufan.pojo.TbUserSns;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/4/9 14:03
 * 功能介绍:
 */
public interface IUserDao {


    /**
     * 分页查询用户列表
     *
     * @param adminCondition
     * @param currePage
     * @return
     */
    public PageInfo loadPrivUserInfoPage(int currePage, AdminCondition adminCondition);

    /**
     * 更新系统用户状态
     *
     * @param adminId
     * @param status
     */
    public void updateAdminStatus(int adminId, int status);

    /**
     * 查询系统用户角色
     *
     * @param adminId
     * @return
     */
    public TbUserRole loadUserRoleByAdminId(int adminId);

    /**
     * 删除用户角色
     *
     * @param adminId
     */
    public void delUserRole(int adminId);

    /**
     * 保存
     *
     * @param object
     * @return
     */
    public int saveUserRole(Object object);

    /**
     * 修改登录用户密码
     *
     * @param newPasswd
     * @param loginId
     */
    public void updateLoginPasswd(String newPasswd, int loginId);

    PageInfo loadMemberIdPage(int currePage, TbMemberId memberId);

    boolean checkMemberCode(String memberId);

    void saveObj(Object object);

    void deleteMemberCode(Integer id);

    public PageInfo loadWapUserInfoPage(int currePage, WapUserCondition wapUserCondition);

    public List<TbUserSns> loaduserSns(int userId);

    List<Map<String, Object>> loadSnsBangList(int userId);

    PageInfo loadWapUserPrivatePage(int currePage, WapUserCondition wapUserCondition);

    public void updateFlowStatus(int id, int flowStatus);

    List<Map<String, Object>> findUserListMapByPhones(String phones);
}
