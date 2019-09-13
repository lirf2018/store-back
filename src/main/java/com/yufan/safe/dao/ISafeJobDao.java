package com.yufan.safe.dao;

/**
 * 创建人: lirf
 * 创建时间:  2019/9/13 17:58
 * 功能介绍:
 */
public interface ISafeJobDao {

    void updateShop();


    void deleteShop();

    void updateAdminPasswd(String passwd,String date);

}
