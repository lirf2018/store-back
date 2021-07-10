package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.anno.ClassAnnotation;
import com.yufan.bean.GoodsCondition;
import com.yufan.bean.GoodsDataObj;
import com.yufan.bean.ItempropObj;
import com.yufan.bean.PropValueObj;
import com.yufan.dao.shop.IShopDao;
import com.yufan.exception.ApplicationException;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbGoods;
import com.yufan.pojo.TbParam;
import com.yufan.pojo.TbShop;
import com.yufan.service.category.ICategoryService;
import com.yufan.service.commonrel.ICommonRelService;
import com.yufan.service.goods.IGoodsService;
import com.yufan.service.param.IParamCodeService;
import com.yufan.service.shop.IShopService;

import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.HelpCommon;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 21:13
 * 功能介绍:
 */
@Controller
@RequestMapping(value = "goods")
@ClassAnnotation(name = "goods", desc = "商品管理")
public class GoodsController {


    @Autowired
    private IParamCodeService iParamCodeService;

    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private IShopService iShopService;

    @Autowired
    private ICommonRelService iCommonRelService;


    /**
     * 管理页面
     *
     * @return
     */
    @RequestMapping("goodsPage")
    public ModelAndView toBannerPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        //查询参数列表
        List<TbParam> listParam = iParamCodeService.loadTbParamCodeList(1);
        //查询一级分类
        List<Map<String, Object>> listLevel = iCategoryService.loadLevelListMap(Constants.DATA_STATUS_YX);

        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if ("admin".equals(user.getLoginName())) {
            shopList = iShopService.findShopAll();
        } else {
            shopList = iShopService.findShopAll(user.getShopId());
        }

        modelAndView.addObject("listLevel", listLevel);
        modelAndView.addObject("listParam", listParam);
        modelAndView.addObject("shopList", shopList);
        modelAndView.setViewName("goods-list");
        return modelAndView;
    }


    /**
     * 加载分页数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("loadGoodsPageData")
    public void loadPageData(HttpServletRequest request, HttpServletResponse response, GoodsCondition goodsCondition) {
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
                goodsCondition.setShopId(shopId);
            }

            pageInfo = iGoodsService.loadDataPage(currePage, goodsCondition);
            //处理数据
            Map<String, String> nameMap = new HashMap<>();
            List<TbParam> listParam = iParamCodeService.loadTbParamCodeList(1);
            for (int i = 0; i < listParam.size(); i++) {
                String key = listParam.get(i).getParamCode() + listParam.get(i).getParamKey();
                String value = listParam.get(i).getParamValue();
                nameMap.put(key, value);
            }
            List<Map<String, Object>> dataList = pageInfo.getResultListMap();
            List<Map<String, Object>> outDataList = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> map = dataList.get(i);
                String property = map.get("property").toString();
                map.put("property_name", nameMap.get("property" + property));
                //'属性 0虚拟商品1实体商品',
                map.put("property_name", "虚拟商品");
                if ("1".equals(property)) {
                    map.put("property_name", "实体商品");
                }
                String goodsType = map.get("goods_type").toString();
                map.put("goods_type_name", nameMap.get("goods_type" + goodsType));
                String getWay = map.get("get_way").toString();
                map.put("get_way_name", nameMap.get("get_way" + getWay));
                String status = map.get("status").toString();
                map.put("status_name", "无效");
                if (status.equals("1")) {
                    map.put("status_name", "有效");
                }
                String isSingle = map.get("is_single").toString();
                map.put("is_single_name", "否");
                if (isSingle.equals("1")) {
                    map.put("is_single_name", "是");
                }
                String isTimeGoods = map.get("is_time_goods").toString();
                map.put("is_time_goods_name", "否");
                if ("1".equals(isTimeGoods)) {
                    map.put("is_time_goods_name", "是");
                }
                String goodsUnit = map.get("goods_unit").toString();
                map.put("goods_unit_name", nameMap.get("goods_unit" + goodsUnit));

                outDataList.add(map);
            }


            //输出参数
            int recordSum = pageInfo.getRecordSum();
            JSONObject dataJson = new JSONObject();
            dataJson.put("draw", Integer.parseInt(request.getParameter("draw")));
            dataJson.put("recordsTotal", recordSum);
            dataJson.put("recordsFiltered", recordSum);
            dataJson.put("data", outDataList);
            writer.print(dataJson);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询商品sku详情
     */
    @RequestMapping("loadGoodsSku")
    public void loadGoodsSkuList(HttpServletRequest request, HttpServletResponse response, Integer goodsId) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            List<Map<String, Object>> skuList = iGoodsService.loadGoodsSkuListMap(goodsId);
            //输出参数
            JSONObject dataJson = new JSONObject();
            dataJson.put("data", skuList);
            writer.print(dataJson);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改数据状态
     */
    @RequestMapping("updateGoodsStatus")
    public void updateDataStatus(HttpServletRequest request, HttpServletResponse response, Integer goodsId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = status == 0 ? HelpCommon.packagMsg("3") : HelpCommon.packagMsg("4");
            iGoodsService.updateGoodsStatus(goodsId, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改数据销售状态
     *
     * @param request
     * @param response
     * @param goodsId
     * @param isPutway 上架状态 0下架  2销售中
     */
    @RequestMapping("updateGoodsSellStatus")
    public void updateDataSellStatus(HttpServletRequest request, HttpServletResponse response, Integer goodsId, Integer isPutway) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = isPutway == 2 ? HelpCommon.packagMsg("20") : HelpCommon.packagMsg("19");
            iGoodsService.updateGoodsOnSell(goodsId, isPutway);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消抢购
     *
     * @param request
     * @param response
     * @param goodsId
     */
    @RequestMapping("deleteTimeGoods")
    public void deleteTimeGoods(HttpServletRequest request, HttpServletResponse response, Integer goodsId) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = HelpCommon.packagMsg("21");
            iGoodsService.deleteTimeGoods(goodsId);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 新增加商品页面
     */
    @RequestMapping("addGoodsPage")
    public ModelAndView addGoodsPage(HttpServletRequest request, HttpServletResponse response, Integer goodsId, Integer editorFlag) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        //查询参数列表
        List<TbParam> listParam = iParamCodeService.loadTbParamCodeList(Constants.DATA_STATUS_YX);
        //查询一级分类
        List<Map<String, Object>> listLevel = iCategoryService.loadLevelListMap(Constants.DATA_STATUS_YX);

        //查询店铺
        List<TbShop> shopList = new ArrayList<>();
        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        if ("admin".equals(user.getLoginName())) {
            shopList = iShopService.findShopAll();
        } else {
            shopList = iShopService.findShopAll(user.getShopId());
        }

        //商品skuList
        List<Map<String, Object>> skuList = new ArrayList<>();

        TbGoods goods = new TbGoods();
        goods.setDataIndex(0);
        goods.setGoodsNum(0);
        goods.setLimitNum(0);
        goods.setAdvancePrice(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
        goods.setDepositMoney(new BigDecimal("0").setScale(2, BigDecimal.ROUND_HALF_UP));
        goods.setLimitWay(4);
        goods.setGetWay(6);
        goods.setGoodsType(0);
        goods.setLimitBeginTime(new Timestamp(new Date().getTime()));
        goods.setGoodsNum(1);
        goods.setGoodsUnit("7");// 默认促销
        goods.setStartTime(new Timestamp(new Date().getTime()));
        goods.setEndTime(new Timestamp(DatetimeUtil.convertStrToDate(DatetimeUtil.addYearTime(DatetimeUtil.getNow(), 1, DatetimeUtil.DEFAULT_DATE_FORMAT_STRING), DatetimeUtil.DEFAULT_DATE_FORMAT_STRING).getTime()));
        if (null != shopList && shopList.size() == 1) {
            goods.setShopId(shopList.get(0).getShopId());
        }
        if (null != goodsId && goodsId > 0) {
            goods = iGoodsService.loadGoods(goodsId);
            //查询关联图片
            List<Map<String, Object>> listImg = iCommonRelService.queryTableRelImg(null, goodsId, 0);
            if (listImg.size() > 0) {
                for (int i = 0; i < listImg.size(); i++) {
                    String imgUrl = listImg.get(i).get("img_url") == null ? "" : listImg.get(i).get("img_url").toString();
                    int imgSort = Integer.parseInt(listImg.get(i).get("img_sort").toString());
                    modelAndView.addObject("img" + imgSort, imgUrl);
                }
            }
            //查询商品sku列表
            if (goods.getIsSingle() == 0) {
                skuList = iGoodsService.loadGoodsSkuListMap(goodsId);
                int storeCount = 0;
                for (int i = 0; i < skuList.size(); i++) {
                    storeCount = storeCount + Integer.parseInt(skuList.get(i).get("sku_num").toString());
                }
                goods.setGoodsNum(storeCount);
            }

            if (!"admin".equals(user.getLoginName()) && user.getShopId() != goods.getShopId()) {
                modelAndView.setViewName("404");
                return modelAndView;
            }
        }

        modelAndView.addObject("webImg", Constants.IMG_WEB_URL);
        modelAndView.addObject("nowDate", DatetimeUtil.getNow("yyyy-MM-dd"));
        modelAndView.addObject("goods", goods);
        modelAndView.addObject("listParam", listParam);
        modelAndView.addObject("listLevel", listLevel);
        modelAndView.addObject("shopList", shopList);
        modelAndView.addObject("skuList", skuList);
        modelAndView.addObject("editorFlag",editorFlag );
        modelAndView.setViewName("add-goods");
        return modelAndView;
    }

    /**
     * 保存数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("saveGoodsData")
    public void saveData(HttpServletRequest request, HttpServletResponse response, TbGoods goods, GoodsDataObj goodsDataObj) {
        PrintWriter writer = null;
        JSONObject out = new JSONObject();
        try {
            writer = response.getWriter();
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            goodsDataObj.setCreateman(user.getLoginName());
            out = iGoodsService.saveGoods(goods, goodsDataObj);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            ApplicationException applicationException = (ApplicationException) e;
            writer.print(applicationException.getOut());
            writer.close();
        }
    }

    /**
     * 查询类目属性关联
     */
    @RequestMapping("loadCategoryRel")
    public void loadCategoryRel(HttpServletRequest request, HttpServletResponse response, Integer goodsId, Integer categoryId) {
        PrintWriter writer;
        try {
            writer = response.getWriter();

            //查询商品销售和非销售属性
            Map<Integer, Integer> goodsPropValueMap = new HashMap<>();
            if (null != goodsId && goodsId > 0) {
                List<Map<String, Object>> goodsAttributeList = iGoodsService.loadGoodsAttribute(goodsId);
                for (int i = 0; i < goodsAttributeList.size(); i++) {
                    Map<String, Object> map = goodsAttributeList.get(i);
                    int value = Integer.parseInt(map.get("value_id").toString());
                    goodsPropValueMap.put(value, value);
                }
            }
            //查询属性和属性值
            List<Map<String, Object>> listItemPropRelAll = iCategoryService.loadItemAllRelListMap(categoryId, 1, 1);

            //属性
            Map<Integer, Integer> repeatMap = new HashMap<>();
            List<ItempropObj> outItempropList = new ArrayList<>();
            for (int i = 0; i < listItemPropRelAll.size(); i++) {
                if (null == listItemPropRelAll.get(i).get("it_prop_id")) {
                    continue;
                }
                Integer propId = Integer.parseInt(listItemPropRelAll.get(i).get("it_prop_id").toString());//属性ID
                String propName = listItemPropRelAll.get(i).get("it_prop_name").toString();//属性名
                Integer isSales = Integer.parseInt(listItemPropRelAll.get(i).get("it_is_sales").toString());//是否销售属性:0不是销售属性1是销售属性
                String showView = listItemPropRelAll.get(i).get("it_show_view").toString();//显示类型:checkbox：多选 select：下拉列表',
                if (repeatMap.get(propId) != null) {
                    continue;
                }
                ItempropObj obj = new ItempropObj();
                obj.setPropId(propId);
                obj.setPropName(propName);
                obj.setIsSales(isSales);
                obj.setShowView(showView);
                outItempropList.add(obj);
                repeatMap.put(propId, propId);
            }
            //属性值
            repeatMap.clear();
            for (int i = 0; i < outItempropList.size(); i++) {
                ItempropObj obj = outItempropList.get(i);
                List<PropValueObj> propValueObjList = new ArrayList<>();
                for (int j = 0; j < listItemPropRelAll.size(); j++) {
                    if (null == listItemPropRelAll.get(j).get("pv_prop_id")) {
                        continue;
                    }
                    Integer propId = Integer.parseInt(listItemPropRelAll.get(j).get("pv_prop_id").toString());
                    Integer valueId = Integer.parseInt(listItemPropRelAll.get(j).get("pv_value_id").toString());
                    String valueName = String.valueOf(listItemPropRelAll.get(j).get("pv_value_name"));
                    String value = String.valueOf(listItemPropRelAll.get(j).get("pv_value"));
                    if (propId == obj.getPropId() && null == repeatMap.get(valueId)) {
                        PropValueObj propValueObj = new PropValueObj();
                        propValueObj.setValue(value);
                        propValueObj.setValueId(valueId);
                        propValueObj.setValueName(valueName);
                        //是否与商品关联
                        propValueObj.setRelGoods(0);//不关联
                        if (goodsPropValueMap.get(propValueObj.getValueId()) != null) {
                            propValueObj.setRelGoods(1);//关联
                        }
                        propValueObjList.add(propValueObj);
                        repeatMap.put(valueId, valueId);
                    }
                }
                obj.setPropValueObjList(propValueObjList);
            }
            JSONObject out = new JSONObject();
            out.put("list", outItempropList);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
