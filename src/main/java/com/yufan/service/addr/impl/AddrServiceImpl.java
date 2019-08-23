package com.yufan.service.addr.impl;

import com.yufan.bean.RegionCondition;
import com.yufan.dao.addr.IAddrDao;
import com.yufan.dao.addr.IAddrJpaDao;
import com.yufan.pojo.TbPlatformAddr;
import com.yufan.pojo.TbRegion;
import com.yufan.service.addr.IAddrService;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/8 11:29
 * 功能介绍:
 */
@Service
public class AddrServiceImpl implements IAddrService {

    @Autowired
    private IAddrDao iAddrDao;

    @Autowired
    private IAddrJpaDao iAddrJpaDao;

    @Override
    public PageInfo loadPlatformAddrPage(int currePage, TbPlatformAddr platformAddr) {
        return iAddrDao.loadPlatformAddrPage(currePage, platformAddr);
    }

    @Override
    public TbPlatformAddr loadPlatformAddrById(int addrId) {
        return iAddrJpaDao.getOne(addrId);
    }

    @Override
    public int savePlatformAddr(TbPlatformAddr platformAddr) {
        return iAddrJpaDao.save(platformAddr).getId();
    }

    @Override
    @Transactional
    public int savePlatformAddrList(List<TbPlatformAddr> addrs) {
        try {
            iAddrJpaDao.saveAll(addrs);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void updatePlatformAddrStatus(int addrId, int status) {
        iAddrDao.updatePlatformAddrStatus(addrId, status);
    }

    @Override
    public PageInfo loadGlobleAddrData(int currePage, RegionCondition regionCondition) {
        return iAddrDao.loadGlobleAddrData(currePage, regionCondition);
    }

    @Override
    public void updateGlobleAddrStatus(int regionId, int status) {
        iAddrDao.updateGlobleAddrStatus(regionId, status);
    }

    @Override
    public TbRegion loadTbRegionById(int regionId) {
        return iAddrDao.loadTbRegionById(regionId);
    }

    @Override
    public List<Map<String, Object>> loadRegionListMap(Integer status, String regionName, String regionCode) {
        return iAddrDao.loadRegionListMap(status, regionName, regionCode);
    }

    @Override
    public void saveRegion(TbRegion region) {
        if (region.getRegionId() == 0) {
            iAddrDao.saveObject(region);
        } else {
            iAddrDao.updateObject(region);
        }
    }

    @Override
    public void updateGlobleAddrFreight(int regionId, String freight) {
        iAddrDao.updateGlobleAddrFreight(regionId, freight);
    }
}
