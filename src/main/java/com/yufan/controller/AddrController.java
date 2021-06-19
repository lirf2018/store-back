package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.RegionCondition;
import com.yufan.pojo.*;
import com.yufan.service.addr.IAddrService;
import com.yufan.service.shop.IShopService;

import com.yufan.utils.Constants;
import com.yufan.utils.HelpCommon;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/8 9:40
 * 功能介绍: 地址管理
 */
@RestController
@RequestMapping(value = "/addr/")
public class AddrController {

    @Autowired
    private IAddrService iAddrService;

    @Autowired
    private IShopService iShopService;


    /**
     * 跳转到管理页面
     *
     * @return
     */
    @RequestMapping("platformAddrPage")
    public ModelAndView toPlatformAddrPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        //查询门店列表
        List<TbMendian> mendianList = new ArrayList<>();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if ("admin".equals(user.getLoginName())) {
            shopList = iShopService.findShopAll();
            mendianList = iShopService.loadMendian();
        } else {
            shopList = iShopService.findShopAll(user.getShopId());
            mendianList = iShopService.loadMendian(user.getShopId());
        }

        modelAndView.addObject("mendianList", mendianList);
        modelAndView.addObject("shopList", shopList);
        modelAndView.addObject("loginName", user.getLoginName());
        modelAndView.setViewName("platform-addr-list");
        return modelAndView;
    }

    /**
     * 查询数据
     *
     * @param request
     * @param response
     */
    @PostMapping("loadPlatformAddrData")
    public void loadPlatformAddrData(HttpServletRequest request, HttpServletResponse response, TbPlatformAddr addr) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if (!"admin".equals(user.getLoginName())) {
                int shopId = user.getShopId();
                addr.setShopId(shopId);
            }
            //条件处理
            if (null != request.getParameter("addrId") && !StringUtils.isEmpty(request.getParameter("addrId").trim())) {
                addr.setId(Integer.parseInt(request.getParameter("addrId").trim()));
            }
            addr.setAddrPrefix(request.getParameter("addrPrefix").trim());
            addr.setDetailAddr(request.getParameter("addrDetail").trim());
            if (null != request.getParameter("addrType") && !StringUtils.isEmpty(request.getParameter("addrType"))) {
                addr.setAddrType(Integer.parseInt(request.getParameter("addrType")));
            }
            addr.setResponsibleMan(request.getParameter("responsible").trim());
            addr.setResponsiblePhone(request.getParameter("responsible").trim());

            pageInfo = iAddrService.loadPlatformAddrPage(currePage, addr);
            //处理数据
            int recordSum = pageInfo.getRecordSum();
            //输出参数
            JSONObject dataJson = new JSONObject();
            dataJson.put("draw", Integer.parseInt(request.getParameter("draw")));
            dataJson.put("recordsTotal", recordSum);
            dataJson.put("recordsFiltered", recordSum);
            dataJson.put("data", pageInfo.getResultListMap());
            writer.print(dataJson);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到增加页面
     *
     * @param id
     * @return
     */
    @RequestMapping("addPlatformAddrPage")
    public ModelAndView toAddPlatformAddrPage(HttpServletRequest request, HttpServletResponse response, Integer id) {

        ModelAndView modelAndView = new ModelAndView();
        TbPlatformAddr distributionAddr = new TbPlatformAddr();
        if (null != id && id > 0) {
            distributionAddr = iAddrService.loadPlatformAddrById(id);
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if (!"admin".equals(user.getLoginName()) && distributionAddr.getShopId() != user.getShopId()) {
                modelAndView.setViewName("404");
                return modelAndView;
            }
        }

        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        //查询门店列表
        List<TbMendian> mendianList = new ArrayList<>();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if ("admin".equals(user.getLoginName())) {
            shopList = iShopService.findShopAll();
            mendianList = iShopService.loadMendian();
        } else {
            shopList = iShopService.findShopAll(user.getShopId());
            mendianList = iShopService.loadMendian(user.getShopId());
        }
        modelAndView.addObject("mendianList", mendianList);
        modelAndView.addObject("shopList", shopList);
        modelAndView.addObject("addr", distributionAddr);
        modelAndView.setViewName("add-platform-addr");
        return modelAndView;
    }

    /**
     * 保存地址
     *
     * @param request
     * @param response
     */
    @PostMapping("savePlatformAddrData")
    public void savePlatformAddrData(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");

            String addrTypes = request.getParameter("addrTypes");
            String[] addrTypesArray = addrTypes.split(",");

            List<TbPlatformAddr> addrslist = new ArrayList<>();
            for (int i = 0; i < addrTypesArray.length; i++) {
                if (!StringUtils.isEmpty(addrTypesArray[i])) {
                    TbPlatformAddr addr = new TbPlatformAddr();
                    addr.setCreateman(user.getLoginName());
                    addr.setCreatetime(new Timestamp(new Date().getTime()));

                    if (!StringUtils.isEmpty(request.getParameter("addr.id"))) {
                        addr.setId(Integer.parseInt(request.getParameter("addr.id")));
                    }
                    String detailAddr = request.getParameter("addr.detailAddr");
                    addr.setDetailAddr(detailAddr);
                    String responsibleMan = request.getParameter("addr.responsibleMan");
                    addr.setResponsibleMan(responsibleMan);
                    String responsiblePhone = request.getParameter("addr.responsiblePhone");
                    addr.setResponsiblePhone(responsiblePhone);
                    BigDecimal freight = new BigDecimal(StringUtils.isEmpty(request.getParameter("addr.freight")) ? "0" : request.getParameter("addr.freight"));
                    addr.setFreight(freight);
                    String sortChar = request.getParameter("addr.sortChar");
                    addr.setSortChar(sortChar);
                    String addrPrefix = request.getParameter("addr.addrPrefix");
                    addr.setAddrPrefix(addrPrefix);
                    addr.setStatus(1);
                    Integer addrShort = StringUtils.isEmpty(request.getParameter("addr.addrSort")) ? 0 : Integer.parseInt(request.getParameter("addr.addrSort"));
                    addr.setAddrSort(addrShort);
                    String addrDesc = request.getParameter("addr.addrDesc");
                    addr.setAddrDesc(addrDesc);
                    String addrName = request.getParameter("addr.addrName");
                    addr.setAddrName(addrName);
                    String remark = request.getParameter("addr.remark");
                    addr.setRemark(remark);
                    String addrLng = request.getParameter("addr.addrLng");
                    addr.setAddrLng(addrLng);
                    String addrLat = request.getParameter("addr.addrLat");
                    addr.setAddrLat(addrLat);
                    addr.setAddrType(Integer.parseInt(addrTypesArray[i]));
                    Integer shopId = StringUtils.isEmpty(request.getParameter("shopId")) ? 0 : Integer.parseInt(request.getParameter("shopId"));
                    addr.setShopId(shopId);
                    Integer storeId = StringUtils.isEmpty(request.getParameter("storeId")) ? 0 : Integer.parseInt(request.getParameter("storeId"));
                    addr.setStoreId(storeId);
                    addrslist.add(addr);
                }
            }
            iAddrService.savePlatformAddrList(addrslist);
            result = HelpCommon.packagMsg(1);
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改状态
     *
     * @param request
     * @param response
     * @param id
     * @param status
     */
    @PostMapping("updatePlatformStatus")
    public void updatePlatformStatus(HttpServletRequest request, HttpServletResponse response, Integer id, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = HelpCommon.packagMsg(1);
            iAddrService.updatePlatformAddrStatus(id, status);
            writer.print(result);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //**********************************************全国地址管理*******************************************************

    @RequestMapping("globleAddrPage")
    public ModelAndView globleAddrPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();

        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        modelAndView.addObject("loginName", user.getLoginName());

        modelAndView.setViewName("globleaddr-list");
        return modelAndView;

    }

    /**
     * 查询全国地址数据
     *
     * @param request
     * @param response
     * @param regionCondition
     */
    @RequestMapping("loadGlobleAddrData")
    public void loadGlobleAddrData(HttpServletRequest request, HttpServletResponse response, RegionCondition regionCondition) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页


            //条件处理
            String regionCode = regionCondition.getRegionCode();
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(regionCode)) {
                //补充位数达到12位
                regionCode = regionCode.trim();
                if (regionCode.length() == 1) {
                    regionCode = regionCode + "00000000000";
                } else if (regionCode.length() == 2) {
                    regionCode = regionCode + "0000000000";
                } else if (regionCode.length() == 3) {
                    regionCode = regionCode + "000000000";
                } else if (regionCode.length() == 4) {
                    regionCode = regionCode + "00000000";
                } else if (regionCode.length() == 5) {
                    regionCode = regionCode + "0000000";
                } else if (regionCode.length() == 6) {
                    regionCode = regionCode + "000000";
                } else if (regionCode.length() == 7) {
                    regionCode = regionCode + "00000";
                } else if (regionCode.length() == 8) {
                    regionCode = regionCode + "0000";
                } else if (regionCode.length() == 9) {
                    regionCode = regionCode + "000";
                } else if (regionCode.length() == 10) {
                    regionCode = regionCode + "00";
                } else if (regionCode.length() == 11) {
                    regionCode = regionCode + "0";
                }
                regionCondition.setRegionCode(regionCode);
            }

            String parentId = regionCondition.getParentId();
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(parentId)) {
                //补充位数达到12位
                parentId = parentId.trim();
                if (parentId.length() == 1) {
                    parentId = parentId + "00000000000";
                } else if (parentId.length() == 2) {
                    parentId = parentId + "0000000000";
                } else if (parentId.length() == 3) {
                    parentId = parentId + "000000000";
                } else if (parentId.length() == 4) {
                    parentId = parentId + "00000000";
                } else if (parentId.length() == 5) {
                    parentId = parentId + "0000000";
                } else if (parentId.length() == 6) {
                    parentId = parentId + "000000";
                } else if (parentId.length() == 7) {
                    parentId = parentId + "00000";
                } else if (parentId.length() == 8) {
                    parentId = parentId + "0000";
                } else if (parentId.length() == 9) {
                    parentId = parentId + "000";
                } else if (parentId.length() == 10) {
                    parentId = parentId + "00";
                } else if (parentId.length() == 11) {
                    parentId = parentId + "0";
                }
                regionCondition.setParentId(parentId);
            }

            pageInfo = iAddrService.loadGlobleAddrData(currePage, regionCondition);
            //处理数据
            int recordSum = pageInfo.getRecordSum();
            //输出参数
            JSONObject dataJson = new JSONObject();
            dataJson.put("draw", Integer.parseInt(request.getParameter("draw")));
            dataJson.put("recordsTotal", recordSum);
            dataJson.put("recordsFiltered", recordSum);
            dataJson.put("data", pageInfo.getResultListMap());
            writer.print(dataJson);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改全国地址状态
     *
     * @param request
     * @param response
     * @param id
     * @param status
     */
    @RequestMapping("updateGlobleAddrStatus")
    public void updateGlobleAddrStatus(HttpServletRequest request, HttpServletResponse response, Integer id, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? HelpCommon.packagMsg(3) : HelpCommon.packagMsg(4);
            iAddrService.updateGlobleAddrStatus(id, status);
            writer.print(out);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加地址页面
     *
     * @param request
     * @param response
     * @param regionId
     * @return
     */
    @RequestMapping("addGloblePage")
    public ModelAndView addGlobleAddrPage(HttpServletRequest request, HttpServletResponse response, Integer regionId) {
        ModelAndView modelAndView = new ModelAndView();

        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if (!"admin".equals(user.getLoginName())) {
            modelAndView.setViewName("404");
            return modelAndView;
        }

        TbRegion region = new TbRegion();
        region.setRegionOrder(0);
        region.setFreight(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
        if (null != regionId && regionId > 0) {
            region = iAddrService.loadTbRegionById(regionId);
        }
        List<Map<String, Object>> list = iAddrService.loadRegionListMap(null, null, null);
        modelAndView.addObject("region", region);
        modelAndView.addObject("list", list);
        modelAndView.setViewName("add-globleaddr");
        return modelAndView;
    }

    /**
     * 查询地址列表
     *
     * @param request
     * @param response
     * @param regionName
     */
    @RequestMapping("loadRegionListMap")
    public void loadRegionListMap(HttpServletRequest request, HttpServletResponse response, String regionName, String regionCode) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = new JSONObject();
            List<Map<String, Object>> list = iAddrService.loadRegionListMap(null, regionName, regionCode);
            out.put("data", list);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存地址数据
     *
     * @param request
     * @param response
     */
    @PostMapping("saveGlobleAddr")
    public void saveGlobleAddr(HttpServletRequest request, HttpServletResponse response, TbRegion region) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            region.setCreateman(user.getLoginName());
            region.setCreatetime(new Timestamp(new Date().getTime()));
            // 检验是否在
            List<Map<String, Object>>  list = iAddrService.findCheckGlobleAddrRegionCode(region.getRegionId(), region.getRegionCode());
            if(!CollectionUtils.isEmpty(list)){
                result = HelpCommon.packagMsg(24,"【行政区划代码】"+region.getRegionCode());
            }else{
                iAddrService.saveRegion(region);
                result = HelpCommon.packagMsg(1);
            }
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存地址数据
     *
     * @param request
     * @param response
     */
    @PostMapping("updateGlobleAddrFreight")
    public void updateGlobleAddrFreight(HttpServletRequest request, HttpServletResponse response, Integer regionId, String freight) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = new JSONObject();
            iAddrService.updateGlobleAddrFreight(regionId, freight);
            result = HelpCommon.packagMsg(1);
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
