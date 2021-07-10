package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.anno.ClassAnnotation;
import com.yufan.bean.TimeGoodsCondition;
import com.yufan.pojo.*;
import com.yufan.service.goods.IGoodsService;
import com.yufan.service.param.IParamCodeService;
import com.yufan.service.shop.IShopService;
import com.yufan.service.timegoods.ITimeGoodsService;

import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.HelpCommon;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 11:27
 * 功能介绍:
 */
@Controller
@RequestMapping(value = "timeGoods")
@ClassAnnotation(name = "timeGoods", desc = "抢购管理")
public class TimeGoodsController {

    @Autowired
    private ITimeGoodsService iTimeGoodsService;

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private IParamCodeService iParamCodeService;

    @Autowired
    private IShopService iShopService;

    /**
     * 管理页面
     *
     * @return
     */
    @RequestMapping("timeGoodsPage")
    public ModelAndView toTimeGoodsPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if("admin".equals(user.getLoginName())){
            shopList = iShopService.findShopAll();
        }else{
            shopList = iShopService.findShopAll(user.getShopId());
        }
        modelAndView.addObject("shopList",shopList);
        modelAndView.setViewName("timegoods-list");
        return modelAndView;
    }

    /**
     * 加载分页数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("loadTimeGoodsPageData")
    public void loadPageData(HttpServletRequest request, HttpServletResponse response, TimeGoodsCondition timeGoodsCondition) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页

            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            if (!"admin".equals(user.getLoginName())) {
                int shopId = user.getShopId();
                timeGoodsCondition.setShopId(shopId);
            }

            pageInfo = iTimeGoodsService.loadDataPage(currePage, timeGoodsCondition);
            //处理数据
            //查询iParamCodeService
            Map<String, String> limitWapMap = new HashMap<>();
            List<TbParam> listParam = iParamCodeService.loadTbParamCodeList(Constants.DATA_STATUS_YX, Constants.PARAM_CODE_LIMIT_WAY);//查询限购方式
            for (int i = 0; i < listParam.size(); i++) {
                TbParam param = listParam.get(i);
                limitWapMap.put("limitWay" + param.getParamKey(), param.getParamValue());
            }

            List<Map<String, Object>> dataList = pageInfo.getResultListMap();
            List<Map<String, Object>> outList = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> map = dataList.get(i);
                map.put("limit_way_name", limitWapMap.get("limitWay" + map.get("time_way")));
                outList.add(map);
            }
            //输出参数
            int recordSum = pageInfo.getRecordSum();
            JSONObject dataJson = new JSONObject();
            dataJson.put("draw", Integer.parseInt(request.getParameter("draw")));
            dataJson.put("recordsTotal", recordSum);
            dataJson.put("recordsFiltered", recordSum);
            dataJson.put("data", outList);
            writer.print(dataJson);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改数据状态
     */
    @RequestMapping("updateTimeGoodsStatus")
    public void updateDataStatus(HttpServletRequest request, HttpServletResponse response, Integer timeGoodsId, Integer status, Integer goodsId) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? HelpCommon.packagMsg("3") : HelpCommon.packagMsg("4");
            iTimeGoodsService.updateTimeGoodsStatus(goodsId, timeGoodsId, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 新设置抢购商品页面
     */
    @RequestMapping("addTimeGoodsPage")
    public ModelAndView addDataPage(HttpServletRequest request, HttpServletResponse response, Integer timeGoodsId, Integer goodsId, Integer fromPage) {
        ModelAndView modelAndView = new ModelAndView();



        TbGoods goods = iGoodsService.loadGoods(goodsId);

        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if(!"admin".equals(user.getLoginName())&&user.getShopId()!= goods.getShopId()){
            modelAndView.setViewName("404");
            return modelAndView;
        }

        //查询商品是否有设置抢购
        TbTimeGoods timeGoods = new TbTimeGoods();
        timeGoods.setWeight(0);
        timeGoods.setLimitNum(0);
        timeGoods.setGoodsStore(0);
        if (null != timeGoodsId && timeGoodsId > 0) {
            timeGoods = iTimeGoodsService.loadTimeGoods(timeGoodsId);
            modelAndView.addObject("timeWay",String.valueOf(timeGoods.getTimeWay()));
        }
        //查询iParamCodeService
        List<TbParam> listParam = iParamCodeService.loadTbParamCodeList(Constants.DATA_STATUS_YX, Constants.PARAM_CODE_LIMIT_WAY);//查询限购方式


        modelAndView.addObject("webImg", Constants.IMG_WEB_URL);
        modelAndView.addObject("nowDate", DatetimeUtil.getNow("yyyy-MM-dd"));
        modelAndView.addObject("timeGoods", timeGoods);
        modelAndView.addObject("goods", goods);
        modelAndView.addObject("listParam", listParam);
        modelAndView.addObject("fromPage", fromPage == null ? 0 : fromPage);
        modelAndView.addObject("webImgPath", StringUtils.isEmpty(goods.getGoodsImg()) ? "" : (Constants.IMG_WEB_URL + goods.getGoodsImg()));
        modelAndView.setViewName("add-timegoods");
        return modelAndView;
    }

    /**
     * 保存数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("saveTimeGoodsData")
    public void saveData(HttpServletRequest request, HttpServletResponse response, TbTimeGoods timeGoods) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg("6");
            if (timeGoods.getId() > 0) {
                out = HelpCommon.packagMsg("5");
            }
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            timeGoods.setCreatetime(new Timestamp(new Date().getTime()));
            timeGoods.setCreateman(user.getLoginName());
            timeGoods.setLastalterman("");
            timeGoods.setLastaltertime(new Timestamp(new Date().getTime()));
            String limitBeginTimeStr = request.getParameter("limitBeginTimeStr");
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(limitBeginTimeStr)) {
                timeGoods.setLimitBeginTime(new Timestamp(DatetimeUtil.convertStrToDate(limitBeginTimeStr, DatetimeUtil.DEFAULT_DATE_FORMAT_STRING).getTime()));
            }
            iTimeGoodsService.saveTimeGoods(timeGoods);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
