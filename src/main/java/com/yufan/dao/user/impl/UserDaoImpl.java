package com.yufan.dao.user.impl;

import com.yufan.bean.AdminCondition;
import com.yufan.bean.WapUserCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.user.IUserDao;
import com.yufan.pojo.TbMemberId;
import com.yufan.pojo.TbUserRole;
import com.yufan.pojo.TbUserSns;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/4/9 14:04
 * 功能介绍:
 */
@Repository
@Transactional
public class UserDaoImpl implements IUserDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadPrivUserInfoPage(int currePage, AdminCondition adminCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT u.admin_id,u.login_name,u.user_name,u.phone,u.idcard,DATE_FORMAT(u.member_begintime,'%Y-%m-%d') as member_begintime,DATE_FORMAT(u.member_endtime,'%Y-%m-%d') as member_endtime, ");
        sql.append(" u.shop_id,u.shop_menber_type,r.role_name,s.shop_name,u.`status` ");
        sql.append(" from tb_admin u LEFT JOIN tb_user_role ur on ur.admin_id=u.admin_id LEFT JOIN tb_role r on r.role_id=ur.role_id ");
        sql.append(" LEFT JOIN tb_shop s on s.shop_id=u.shop_id ");
        sql.append(" where 1=1 ");
        if (!StringUtils.isEmpty(adminCondition.getUserName().trim())) {
            sql.append(" and u.user_name='").append(adminCondition.getUserName().trim()).append("' ");
        }

        if (!StringUtils.isEmpty(adminCondition.getLoginName().trim())) {
            sql.append(" and u.login_name='").append(adminCondition.getLoginName().trim()).append("' ");
        }

        if (!StringUtils.isEmpty(adminCondition.getIdcard().trim())) {
            sql.append(" and u.idcard like '%").append(adminCondition.getIdcard().trim()).append("%' ");
        }

        if (null != adminCondition.getRoleId() && adminCondition.getRoleId() != -1) {
            sql.append(" and ur.role_id=").append(adminCondition.getRoleId());
        }

        if (!StringUtils.isEmpty(adminCondition.getPhone().trim())) {
            sql.append("  and u.phone='").append(adminCondition.getPhone().trim()).append("' ");
        }

        if (!StringUtils.isEmpty(adminCondition.getShopName().trim())) {
            sql.append(" and s.shop_name like '%").append(adminCondition.getShopName().trim()).append("%' ");
        }

        if (null != adminCondition.getStatus() && adminCondition.getStatus() != -1) {
            sql.append(" and u.`status`=").append(adminCondition.getStatus());
        }
        sql.append(" ORDER BY u.admin_id desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void updateAdminStatus(int adminId, int status) {
        String sql = " update tb_admin set status=? where admin_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, adminId);
    }

    @Override
    public TbUserRole loadUserRoleByAdminId(int adminId) {
        String hql = "from TbUserRole where adminId=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, adminId);
    }

    @Override
    public void delUserRole(int adminId) {
        String sql = "delete from tb_user_role where admin_id=?";
        iGeneralDao.executeUpdateForSQL(sql, adminId);
    }

    @Override
    public int saveUserRole(Object object) {
        return iGeneralDao.save(object);
    }

    @Override
    public void updateLoginPasswd(String newPasswd, int loginId) {
        String sql = " update tb_admin set login_password=? where admin_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, newPasswd, loginId);
    }

    @Override
    public PageInfo loadMemberIdPage(int currePage, TbMemberId memberId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select id,member_id,user_phone,DATE_FORMAT(create_time,'%Y-%m-%d %T') as create_time  from tb_member_id where 1=1  ");
        if (!StringUtils.isEmpty(memberId.getUserPhone())) {
            sql.append(" and user_phone ='").append(memberId.getUserPhone().trim()).append("' ");
        }
        if (!StringUtils.isEmpty(memberId.getMemberId())) {
            sql.append("  and member_id='").append(memberId.getMemberId().trim()).append("' ");
        }
        sql.append(" ORDER BY create_time desc ");


        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public boolean checkMemberCode(String memberId) {
        String sql = " select member_id,id from tb_member_id where member_id=? ";
        List<Map<String, Object>> list = iGeneralDao.getBySQLListMap(sql, memberId);
        if (list.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void saveObj(Object object) {
        iGeneralDao.save(object);
    }

    @Override
    public void deleteMemberCode(Integer id) {
        String sql = " delete from tb_member_id where id=? ";
        iGeneralDao.executeUpdateForSQL(sql, id);
    }

    @Override
    public PageInfo loadWapUserInfoPage(int currePage, WapUserCondition wapUserCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select user_id,login_name,nick_name,user_email,email_valite,user_mobile,mobile_valite,log_count,user_state,  ");
        sql.append(" DATE_FORMAT(createtime,'%Y-%m-%d %T') as createtime,DATE_FORMAT(lastlogintime,'%Y-%m-%d %T') as lastlogintime,DATE_FORMAT(lastaltertime,'%Y-%m-%d %T') as lastaltertime,  ");
        sql.append(" CONCAT('").append(Constants.IMG_WEB_URL).append("',user_img) as user_img,  ");
        sql.append(" shop_id,member_id,money,inviter_num,inviter_jf,inviter_money,jifen,  ");
        sql.append(" DATE_FORMAT(start_time,'%Y-%m-%d') as start_time,DATE_FORMAT(end_time,'%Y-%m-%d') as end_time from tb_user_info where 1=1  ");
        if (!StringUtils.isEmpty(wapUserCondition.getUserMobile())) {
            sql.append(" and user_mobile like '%").append(wapUserCondition.getUserMobile()).append("%'  ");
        }
        if (!StringUtils.isEmpty(wapUserCondition.getMemberId())) {
            sql.append(" and member_id='").append(wapUserCondition.getMemberId()).append("'  ");
        }
        if (!StringUtils.isEmpty(wapUserCondition.getNickName())) {
            sql.append(" and nick_name like '%").append(wapUserCondition.getNickName()).append("%'  ");
        }
        if (wapUserCondition.getUserId() != null && wapUserCondition.getUserId() > 0) {
            sql.append(" and user_id=").append(wapUserCondition.getUserId()).append("  ");
        }
        if (wapUserCondition.getUserStatus() != null && wapUserCondition.getUserStatus() > -1) {
            sql.append(" and user_state=").append(wapUserCondition.getUserStatus()).append("  ");
        }

        sql.append(" order by createtime desc  ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public List<TbUserSns> loaduserSns(int userId) {
        String hql = " from TbUserSns where userId=?1 order by createtime desc ";
        return (List<TbUserSns>)iGeneralDao.queryListByHql(hql, userId);
    }

    @Override
    public List<Map<String, Object>> loadSnsBangList(int userId) {
        return null;
    }
}
