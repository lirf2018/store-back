package com.yufan.service.param.impl;

import com.yufan.dao.param.IParamCodeDao;
import com.yufan.dao.param.IParamCodeJpaDao;
import com.yufan.pojo.TbParam;
import com.yufan.service.param.IParamCodeService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/22 17:37
 * 功能介绍:
 */
@Service
public class ParamCodeServiceImpl implements IParamCodeService {

    @Autowired
    private IParamCodeJpaDao iParamCodeJpaDao;

    @Autowired
    private IParamCodeDao iParamCodeDao;

    public TbParam loadTbParamCodeById(int id) {
        return iParamCodeJpaDao.getOne(id);
    }

    @Override
    public List<TbParam> loadTbParamCodeList() {
        return iParamCodeJpaDao.findAll();
    }

    @Override
    public List<TbParam> loadTbParamCodeList(int status) {
        return iParamCodeJpaDao.loadTbParamCodeList(status);
    }

    @Override
    public List<TbParam> loadTbParamCodeList(int status, String paramCode) {
        return iParamCodeJpaDao.loadTbParamCodeList(status,paramCode);
    }

    @Override
    public List<TbParam> loadTbParamCodeList(int status, String paramCode, String paramKey) {
        return iParamCodeJpaDao.loadTbParamCodeList(status,paramCode,paramKey);
    }

    @Override
    public PageInfo loadParamCodePage(int currePage, TbParam param) {
        return iParamCodeDao.loadParamCodePage(currePage, param);
    }

    @Override
    public int saveParamCode(TbParam param) {
        return iParamCodeJpaDao.save(param).getParamId();
    }

    @Override
    public void updateParamCodeStatus(int paramId, int status) {
        iParamCodeDao.updateParamCodeStatus(paramId,status);
    }

    @Override
    public List<Map<String, Object>> loadParamGroupName() {
        return iParamCodeDao.loadParamGroupName();
    }
}
