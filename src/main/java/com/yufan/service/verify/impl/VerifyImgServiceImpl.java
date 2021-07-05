package com.yufan.service.verify.impl;

import com.yufan.bean.VerifyImgGroupCondition;
import com.yufan.dao.verify.IVerifyImgDao;
import com.yufan.pojo.TbVerifyImg;
import com.yufan.pojo.TbVerifyImgGroup;
import com.yufan.service.verify.IVerifyImgService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/28
 */
@Service
public class VerifyImgServiceImpl implements IVerifyImgService {

    @Autowired
    private IVerifyImgDao iVerifyImgDao;

    @Override
    public PageInfo loadDataPage(int currePage, VerifyImgGroupCondition condition) {
        return iVerifyImgDao.loadDataPage(currePage, condition);
    }

    @Override
    public TbVerifyImgGroup findVerifyImgGroup(TbVerifyImgGroup imgGroup) {
        return iVerifyImgDao.findVerifyImgGroup(imgGroup);
    }

    @Override
    public List<Map<String, Object>> loadVerifyImgList(String verifyCode, Integer status) {
        return iVerifyImgDao.loadVerifyImgList(verifyCode, status);
    }

    @Override
    public void updateVerifyGroupStatus(int id, int status) {
        iVerifyImgDao.updateVerifyGroupStatus(id, status);
    }

    @Override
    public void updateVerifyImgStatus(int id, int status) {
        iVerifyImgDao.updateVerifyImgStatus(id, status);
    }

    @Override
    public boolean checkVerifyImgCode(Integer id, String verifyImgCode) {
        return iVerifyImgDao.checkVerifyImgCode(id, verifyImgCode);
    }

    @Override
    public void updateVerifyGroup(TbVerifyImgGroup imgGroup, String oldCode) {
        iVerifyImgDao.updateVerifyGroup(imgGroup, oldCode);
    }

    @Override
    public void addVerifyImg(TbVerifyImg verifyImg) {
        if (verifyImg.getId() > 0) {
            iVerifyImgDao.updateVerifyImg(verifyImg);
        } else {
            iVerifyImgDao.addVerifyImg(verifyImg);
        }
    }

    @Override
    public void updateBackImg(int id, String img) {
        iVerifyImgDao.updateBackImg(id, img);
    }
}
