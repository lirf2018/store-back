package com.yufan.service.func.impl;

import com.yufan.dao.func.IMenuDao;
import com.yufan.dao.func.IMenuJpaDao;
import com.yufan.pojo.TbFunctions;
import com.yufan.service.func.IMenuService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/25 15:47
 * 功能介绍:
 */
@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private IMenuJpaDao iMenuJpaDao;

    @Autowired
    private IMenuDao iMenuDao;

    @Override
    public List<TbFunctions> listUserPrivFunction() {
        return iMenuJpaDao.listUserPrivFunction();
    }

    @Override
    public List<TbFunctions> listUserPrivFunctionParent(int parentId) {
        return iMenuJpaDao.listUserPrivFunctionParent(parentId);
    }

    @Override
    public List<TbFunctions> listUserPrivFunction(int userId) {
        return iMenuJpaDao.listUserPrivFunction(userId);
    }

    @Override
    public PageInfo loadMenuPage(int currePage, TbFunctions func) {
        return iMenuDao.loadMenuPage(currePage, func);
    }

    @Override
    public TbFunctions saveFuntion(TbFunctions func) {
        return iMenuJpaDao.save(func);
    }

    @Override
    @Transactional
    public void deleteMenu(int menuId, int parentId) {
        TbFunctions func = new TbFunctions();
        func.setFunctionId(menuId);
        iMenuJpaDao.delete(func);
        if (parentId == 0) {
            //删除子菜单
            iMenuDao.deleteMenuByParentId(menuId);
        }
    }

    @Override
    public void updateMenuStatus(int menuId, int status) {
        TbFunctions f = iMenuJpaDao.getOne(menuId);
        f.setStatus(status);
        iMenuJpaDao.save(f);
    }
}
