package com.yufan.dao.addr.impl;

import com.yufan.bean.RegionCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.addr.IAddrDao;
import com.yufan.pojo.TbPlatformAddr;
import com.yufan.pojo.TbRegion;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/8 11:31
 * 功能介绍:
 */
@Repository
@Transactional
public class AddrDaoImpl implements IAddrDao {


    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadPlatformAddrPage(int currePage, TbPlatformAddr platformAddr) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select pf.id,pf.detail_addr,pf.responsible_man,pf.responsible_phone,CONCAT(pf.freight,'') as freight,pf.sort_char,pf.addr_prefix,pf.status,pf.remark,pf.addr_sort,pf.addr_type,pf.addr_desc,");
        sql.append(" pf.addr_name,pf.shop_id,date_format(pf.createtime,'%Y-%m-%d %T') as createtime,pf.addr_lng,pf.addr_lat ");
        sql.append(" ,md.store_name ");
        sql.append(" from tb_platform_addr pf  ");
        sql.append(" LEFT JOIN tb_mendian md on md.store_id=pf.store_id ");
        sql.append(" where 1=1 ");
        if (0 != platformAddr.getId()) {
            sql.append(" and pf.id = ").append(platformAddr.getId()).append(" ");
        }
        if (!StringUtils.isEmpty(platformAddr.getAddrPrefix())) {
            sql.append(" and pf.addr_prefix = '").append(platformAddr.getAddrPrefix()).append("' ");
        }
        if (!StringUtils.isEmpty(platformAddr.getDetailAddr())) {
            sql.append(" and pf.detail_addr like '%").append(platformAddr.getDetailAddr()).append("%' ");
        }
        if (!StringUtils.isEmpty(platformAddr.getResponsibleMan())) {
            sql.append(" and (pf.responsible_man = '").append(platformAddr.getResponsibleMan()).append("' or pf.responsible_phone = '").append(platformAddr.getResponsibleMan()).append("') ");
        }
        if (platformAddr.getAddrType() != null) {
            sql.append(" and pf.addr_type = ").append(platformAddr.getAddrType()).append(" ");
        }
        if (platformAddr.getShopId() != null) {
            sql.append(" and pf.shop_id=").append(platformAddr.getShopId()).append(" ");
        }
        if (null != platformAddr.getStatus() && -1 != platformAddr.getStatus()) {
            sql.append(" and pf.status=").append(platformAddr.getStatus()).append(" ");
        }
        if (null != platformAddr.getStoreId() && -1 != platformAddr.getStoreId()) {
            sql.append(" and pf.store_id=").append(platformAddr.getStoreId()).append(" ");
        }
        sql.append(" ORDER BY pf.sort_char,pf.addr_sort desc,pf.createtime desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void updatePlatformAddrStatus(int addrId, int status) {
        String sql = " update tb_platform_addr set status=? where id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, addrId);
    }

    @Override
    public PageInfo loadGlobleAddrData(int currePage, RegionCondition regionCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT region_id,region_code,region_name,region_shortname,parent_id,region_level,region_order,region_name_en,region_shortname_en,region_type, ");
        sql.append(" createman,createtime,status,remark,CONCAT('',freight) as freight ");
        sql.append(" from tb_region where 1=1 ");

        if (regionCondition.getStatus() != null && regionCondition.getStatus() != -1) {
            sql.append(" and `status`=" + regionCondition.getStatus() + " ");
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(regionCondition.getRegionCode())) {
            sql.append(" and region_code='" + regionCondition.getRegionCode().trim() + "' ");
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(regionCondition.getParentId())) {
            sql.append(" and parent_id='" + regionCondition.getParentId().trim() + "' ");
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(regionCondition.getRegionName())) {
            sql.append(" and region_name like '%" + regionCondition.getRegionName().trim() + "%' ");
        }
        if (!"-1".equals(regionCondition.getRegionType()) && null != regionCondition.getRegionType()) {
            sql.append(" and `region_type`=" + regionCondition.getRegionType() + " ");
        }
        if (!"-1".equals(regionCondition.getRegionLevel()) && null != regionCondition.getRegionLevel()) {
            sql.append(" and `region_level`=" + regionCondition.getRegionLevel() + " ");
        }
        sql.append(" ORDER BY parent_id, region_level,region_order desc ");

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public void updateGlobleAddrStatus(int regionId, int status) {
        String sql = " update tb_region set `status`=? where region_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, regionId);
    }

    @Override
    public TbRegion loadTbRegionById(int regionId) {
        String sql = " from TbRegion where regionId=?1 ";
        return iGeneralDao.queryUniqueByHql(sql, regionId);
    }

    @Override
    public List<Map<String, Object>> loadRegionListMap(Integer status, String regionName, String regionCode) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT region_id,region_code,region_name,region_shortname,parent_id,region_level,region_order,region_name_en,region_shortname_en,region_type, ");
        sql.append(" createman,createtime,status,remark,CONCAT('',freight) as freight ");
        sql.append(" from tb_region where 1=1 ");
        if (status != null) {
            sql.append(" and status=").append(status).append(" ");
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(regionName)) {
            sql.append(" and region_name like '%" + regionName.trim() + "%' ");
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(regionCode)) {
            sql.append(" and region_code like '%" + regionCode.trim() + "%' ");
        }
        sql.append(" ORDER BY parent_id, region_level,region_order desc ");
        if (StringUtils.isEmpty(regionName)) {
            sql.append(" limit 0,1000 ");
        }
        return iGeneralDao.getBySQLListMap(sql.toString());
    }

    @Override
    public void saveObject(Object object) {
        iGeneralDao.save(object);
    }

    @Override
    public void updateObject(Object object) {
        iGeneralDao.saveOrUpdate(object);
    }

    @Override
    public void updateGlobleAddrFreight(int regionId, String freight) {
        String sql = " update tb_region set freight=? where region_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, freight, regionId);
    }
}
