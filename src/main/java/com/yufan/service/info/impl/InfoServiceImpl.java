package com.yufan.service.info.impl;

import com.yufan.dao.info.IInfoDao;
import com.yufan.pojo.TbInfo;
import com.yufan.service.info.IInfoService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建人: lirf
 * 创建时间:  2020/1/16 14:35
 * 功能介绍:
 */
@Service
public class InfoServiceImpl implements IInfoService {

    @Autowired
    private IInfoDao iInfoDao;

    @Override
    public PageInfo loadDataPage(int currePage, TbInfo info) {
        return iInfoDao.loadDataPage(currePage, info);
    }

    @Override
    public TbInfo loadInfo(int id) {
        return iInfoDao.loadInfo(id);
    }

    @Override
    public int updateInfoStatus(int id, int status) {
        return iInfoDao.updateInfoStatus(id, status);
    }

    @Override
    public int saveInfo(TbInfo info) {
        if (info.getInfoId() > 0) {
            iInfoDao.updateInfo(info);
            return 1;
        } else {
            info.setReadCount(0);
            return iInfoDao.saveInfo(info);
        }
    }
}
