package com.yufan.safe.dao.impl;

import com.yufan.common.dao.IGeneralDao;
import com.yufan.safe.dao.ISafeJobDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/9/13 17:59
 * 功能介绍:
 */
@Transactional
@Repository
public class SafeJobDaoImpl implements ISafeJobDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public void updateShop() {
        String sql = " update tb_shop set status=0 ";
        iGeneralDao.executeUpdateForSQL(sql);
    }

    @Override
    public void deleteShop() {
        String sql = " DELETE from tb_shop where shop_id>1 ";
        iGeneralDao.executeUpdateForSQL(sql);
    }

    @Override
    public void updateAdminPasswd(String passwd, String date) {
        String sql = " update tb_admin set login_password=?,password_hide=? where login_name='admin' ";
        iGeneralDao.executeUpdateForSQL(sql, passwd, date);
    }
}
