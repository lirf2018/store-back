package com.yufan.service.complain.impl;

import com.yufan.bean.ComplainCondition;
import com.yufan.dao.complain.IComplainDao;
import com.yufan.pojo.TbBanner;
import com.yufan.service.complain.IComplainService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/10 9:31
 * 功能介绍:
 */
@Service
public class ComplainServiceImpl implements IComplainService {

    @Autowired
    private IComplainDao iComplainDao;


    @Override
    public PageInfo loadDataPage(int currePage, ComplainCondition complainCondition) {
        return iComplainDao.loadDataPage(currePage,complainCondition);
    }

    @Override
    public void updateComplainStatus(int complainId, int status) {
        iComplainDao.updateComplainStatus(complainId,status);
    }

    @Override
    public void updateComlpainIsRead(int complainId, int isRead) {
        iComplainDao.updateComlpainIsRead(complainId,isRead);
    }

    @Override
    public void updateAnswer(int complainId, String answer) {
        iComplainDao.updateAnswer(complainId,answer);
    }


}
