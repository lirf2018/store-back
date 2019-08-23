package com.yufan.dao.addr;

import com.yufan.bean.RegionCondition;
import com.yufan.pojo.TbPlatformAddr;
import com.yufan.pojo.TbRegion;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/8 11:31
 * 功能介绍:
 */
public interface IAddrDao {

    public PageInfo loadPlatformAddrPage(int currePage, TbPlatformAddr distributionAddr);

    public void updatePlatformAddrStatus(int addrId, int status);

    /**
     * 全国地址查询
     *
     * @param currePage
     * @param regionCondition
     * @return
     */
    public PageInfo loadGlobleAddrData(int currePage, RegionCondition regionCondition);

    /**
     * 更新地址
     *
     * @param regionId
     * @param status
     */
    public void updateGlobleAddrStatus(int regionId, int status);

    /**
     * 查询地址详情
     *
     * @param regionId
     * @return
     */
    public TbRegion loadTbRegionById(int regionId);

    /**
     * 查询地址列表
     *
     * @param status
     * @return
     */
    List<Map<String, Object>> loadRegionListMap(Integer status,String regionName,String regionCode);

    /**
     * 保存地址
     *
     * @param object
     */
    void saveObject(Object object);

    void updateObject(Object object);

    /**
     * 更新运费
     * @param regionId
     * @param freight
     */
    public void updateGlobleAddrFreight(int regionId,String freight);
}
