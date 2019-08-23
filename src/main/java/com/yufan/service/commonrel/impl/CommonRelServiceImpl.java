package com.yufan.service.commonrel.impl;

import com.yufan.dao.commonrel.ICommonRelDao;
import com.yufan.service.commonrel.ICommonRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/9 10:19
 * 功能介绍: 中间表
 */
@Service
public class CommonRelServiceImpl implements ICommonRelService {

    @Autowired
    private ICommonRelDao iCommonRelDao;

    @Override
    public List<Map<String, Object>> queryTableRelImg(Integer imgType, int relateId, Integer classifyType) {
        return iCommonRelDao.queryTableRelImg(imgType,relateId,classifyType);
    }
}
