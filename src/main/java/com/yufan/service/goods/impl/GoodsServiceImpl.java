package com.yufan.service.goods.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.GoodsCondition;
import com.yufan.bean.GoodsDataObj;
import com.yufan.dao.category.ICategoryDao;
import com.yufan.dao.commonrel.ICommonRelDao;
import com.yufan.dao.goods.IGoodsDao;
import com.yufan.dao.goods.IGoodsJapDao;
import com.yufan.dao.timegoods.ITimeGoodsDao;
import com.yufan.pojo.TbGoods;
import com.yufan.pojo.TbGoodsAttribute;
import com.yufan.pojo.TbGoodsSku;
import com.yufan.pojo.TbImg;
import com.yufan.service.goods.IGoodsService;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 12:34
 * 功能介绍:
 */
@Service
public class GoodsServiceImpl implements IGoodsService {

    private Logger LOG = Logger.getLogger(GoodsServiceImpl.class);

    @Autowired
    private IGoodsDao iGoodsDao;

    @Autowired
    private IGoodsJapDao iGoodsJapDao;

    @Autowired
    private ITimeGoodsDao iTimeGoodsDao;

    @Autowired
    private ICommonRelDao iCommonRelDao;

    @Autowired
    private ICategoryDao iCategoryDao;


    @Override
    public TbGoods loadGoods(int goodsId) {
        return iGoodsJapDao.getOne(goodsId);
    }

    @Override
    public PageInfo loadDataPage(int currePage, GoodsCondition goodsCondition) {
        return iGoodsDao.loadDataPage(currePage, goodsCondition);
    }

    @Transactional
    @Override
    public JSONObject saveGoods(TbGoods goods, GoodsDataObj goodsDataObj) {
        JSONObject out = CommonMethod.packagMsg("0");
        try {
            boolean flag = goods.getGoodsId() > 0 ? true : false;
            //默认值
            goods.setIsSingle(1);
            goods.setIsTimeGoods(0);
            goods.setAreaId(0);
            goods.setSellCount(0);
            if (goods.getGoodsId() > 0) {
                //删除商品已存在的图片
                iCommonRelDao.deletRelImg(goods.getGoodsId(), null, 0);
                //删除商品属性
                iGoodsDao.deleteGoodsAttribute(goods.getGoodsId());
                goods.setLastaltertime(new Timestamp(new Date().getTime()));
                goods.setLastalterman(goodsDataObj.getCreateman());
            }
            List<TbImg> listImg = new ArrayList<>();
            //bannel图片
            String img5 = goodsDataObj.getImg5();
            if (StringUtils.isNotEmpty(img5)) {
                TbImg img = new TbImg();
                img.setImgSort(5);
                img.setImgUrl(img5);
                img.setImgType(Constants.IMGTYPE_GOODS_BANNER);
                listImg.add(img);
            }
            String img6 = goodsDataObj.getImg6();
            if (StringUtils.isNotEmpty(img6)) {
                TbImg img = new TbImg();
                img.setImgSort(6);
                img.setImgUrl(img6);
                img.setImgType(Constants.IMGTYPE_GOODS_BANNER);
                listImg.add(img);
            }
            String img7 = goodsDataObj.getImg7();
            if (StringUtils.isNotEmpty(img7)) {
                TbImg img = new TbImg();
                img.setImgSort(7);
                img.setImgUrl(img7);
                img.setImgType(Constants.IMGTYPE_GOODS_BANNER);
                listImg.add(img);
            }
            String img8 = goodsDataObj.getImg8();
            if (StringUtils.isNotEmpty(img8)) {
                TbImg img = new TbImg();
                img.setImgSort(8);
                img.setImgUrl(img8);
                img.setImgType(Constants.IMGTYPE_GOODS_BANNER);
                listImg.add(img);
            }
            //介绍图片
            String img9 = goodsDataObj.getImg9();
            if (StringUtils.isNotEmpty(img9)) {
                TbImg img = new TbImg();
                img.setImgSort(9);
                img.setImgUrl(img9);
                img.setImgType(Constants.IMGTYPE_GOODS_INFO);
                listImg.add(img);
            }
            String img10 = goodsDataObj.getImg10();
            if (StringUtils.isNotEmpty(img10)) {
                TbImg img = new TbImg();
                img.setImgSort(10);
                img.setImgUrl(img10);
                img.setImgType(Constants.IMGTYPE_GOODS_INFO);
                listImg.add(img);
            }
            String img11 = goodsDataObj.getImg11();
            if (StringUtils.isNotEmpty(img11)) {
                TbImg img = new TbImg();
                img.setImgSort(11);
                img.setImgUrl(img11);
                img.setImgType(Constants.IMGTYPE_GOODS_INFO);
                listImg.add(img);
            }
            String img12 = goodsDataObj.getImg12();
            if (StringUtils.isNotEmpty(img12)) {
                TbImg img = new TbImg();
                img.setImgSort(12);
                img.setImgUrl(img12);
                img.setImgType(Constants.IMGTYPE_GOODS_INFO);
                listImg.add(img);
            }
            //保存商品
            if (goods.getGoodsId() > 0) {
                //更新
                TbGoods old = iGoodsJapDao.getOne(goods.getGoodsId());
                goods.setCreatetime(old.getCreatetime());
                goods.setCreateman(old.getCreateman());
                goods.setSellCount(old.getSellCount() == null ? 0 : old.getSellCount());
                goods.setLastalterman(goodsDataObj.getCreateman());
                goods.setLastaltertime(new Timestamp(new Date().getTime()));
                goods.setIsTimeGoods(old.getIsTimeGoods());
            } else {
                //增加
                goods.setCreateman(goodsDataObj.getCreateman());
                goods.setCreatetime(new Timestamp(new Date().getTime()));
                goods.setLastalterman(goodsDataObj.getCreateman());
                goods.setLastaltertime(new Timestamp(new Date().getTime()));
            }
            iGoodsJapDao.save(goods);

            //保存商品图片
            for (int i = 0; i < listImg.size(); i++) {
                TbImg img = listImg.get(i);
                img.setImgClassify(Constants.CLASSIFY_GOODS);
                img.setRelateId(goods.getGoodsId());
                img.setCreatetime(new Timestamp(new Date().getTime()));
                img.setStatus(1);
                iCommonRelDao.saveObject(img);
            }


            //查询类目属性值
            List<Map<String, Object>> listPropValue = iCategoryDao.loadPropValueByCategoryId(goods.getCategoryId());
            Map<String, String> mapValueProp = new HashMap<>();
            for (int i = 0; i < listPropValue.size(); i++) {
                String key = listPropValue.get(i).get("value_id").toString();
                String value = listPropValue.get(i).get("prop_id").toString();
                mapValueProp.put(key, value);
            }
            //商品非销售属性
            String unsellProp = goodsDataObj.getUnsellPropId();
            String[] unsellPropArray = unsellProp.split("``");
            for (int i = 0; i < unsellPropArray.length; i++) {
                if (StringUtils.isNotEmpty(unsellPropArray[i])) {
                    TbGoodsAttribute attribute = new TbGoodsAttribute();
                    attribute.setCreatetime(new Timestamp(new Date().getTime()));
                    attribute.setGoodsId(goods.getGoodsId());

                    String propId = mapValueProp.get(unsellPropArray[i]);
                    if (StringUtils.isEmpty(propId)) {
                        LOG.info("------异常1----------");
                        out = CommonMethod.packagMsg("goods_exception_01");
                        throw new RuntimeException();
                    }
                    attribute.setPropId(Integer.parseInt(propId));
                    attribute.setValueId(Integer.parseInt(unsellPropArray[i]));
                    iGoodsDao.saveObject(attribute);
                }
            }

            LOG.info("----商品销售属性---");
            //商品销售属性(商品sku)
            String skuCodes = goodsDataObj.getSkuCode();
            String skuPurchasePrices = goodsDataObj.getSkuPurchasePrice();
            String skuTrueMoneys = goodsDataObj.getSkuTrueMoney();
            String skuNowMoneys = goodsDataObj.getSkuNowMoney();
            String skuNums = goodsDataObj.getSkuNum();
            String skuImgs = goodsDataObj.getSkuImg();
            String skuIds = goodsDataObj.getSkuId();
            String skuNames = goodsDataObj.getSkuName();
            String skuPropCodes = goodsDataObj.getSkuPropCode();

            String[] skuCodeArray = skuCodes.split("``");
            String[] skuPurchasePriceArray = skuPurchasePrices.split("``");
            String[] skuTrueMoneyArray = skuTrueMoneys.split("``");
            String[] skuNowMoneyArray = skuNowMoneys.split("``");
            String[] skuNumArray = skuNums.split("``");
            String[] skuImgArray = skuImgs.split("``");
            String[] skuIdArray = skuIds.split("``");
            String[] skuNameArray = skuNames.split("``");
            String[] skuPropCodeArray = skuPropCodes.split("``");
            //删除商品失去关联的sku
            if (flag) {
                String[] deleteSkuIds = skuIds.split("``");
                String strIds = "";
                for (int i = 0; i < deleteSkuIds.length; i++) {
                    if (StringUtils.isNotEmpty(deleteSkuIds[i])) {
                        strIds = strIds + deleteSkuIds[i] + ",";
                    }
                }
                if (strIds.endsWith("``")) {
                    strIds = strIds.substring(0, strIds.length() - 2);
                }
                if (strIds.endsWith(",")) {
                    strIds = strIds.substring(0, strIds.length() - 1);
                }
                if (StringUtils.isNotEmpty(strIds)) {
                    iGoodsDao.updateGoodsSkuStatusNoinSkuId(goods.getGoodsId(), strIds, Constants.DATA_STATUS_WX);
                }
            }

            boolean isSingle = false;
            Map<String, String> mapValueIds = new HashMap<>();
            out = CommonMethod.packagMsg("goods_exception_02");
            for (int i = 0; i < skuPurchasePriceArray.length; i++) {
                if (StringUtils.isEmpty(skuPurchasePriceArray[i])) {
                    continue;
                }
                isSingle = true;//单品0
                String skuCode = skuCodeArray[i];
                String skuPurchasePrice = skuPurchasePriceArray[i];
                String skuTrueMoney = skuTrueMoneyArray[i];
                String skuNowMoney = skuNowMoneyArray[i];
                String skuNum = skuNumArray[i];
                String skuImg = skuImgArray[i];
                String skuId = skuIdArray[i];
                String skuName = skuNameArray[i];
                String propCode = skuPropCodeArray[i];
                String valueIds[] = propCode.split(";");
                //商品属性表
                for (int j = 0; j < valueIds.length; j++) {
                    mapValueIds.put(valueIds[j], valueIds[j]);
                }
                TbGoodsSku sku = new TbGoodsSku();
                sku.setGoodsId(goods.getGoodsId());
                sku.setSkuName(skuName);
                sku.setPropCode(propCode);//属性值标识
                sku.setTrueMoney(new BigDecimal(skuTrueMoney));
                sku.setNowMoney(new BigDecimal(skuNowMoney));
                sku.setSkuCode("0".equals(skuCode) ? "" : skuCode);
                sku.setSkuNum(Integer.parseInt(skuNum));
                sku.setSkuImg("0".equals(skuImg) ? "" : skuImg);
                sku.setPurchasePrice(new BigDecimal(skuPurchasePrice));
                sku.setSellCount(0);
                sku.setSkuId(Integer.parseInt(StringUtils.isEmpty(skuId) ? "0" : skuId));
                sku.setStatus(1);
                if (sku.getSkuId() > 0) {
                    iGoodsDao.updateGoodsSku(sku);
                } else {
                    iGoodsDao.saveObject(sku);
                }
            }


            if (isSingle) {
                //非单品
                iGoodsDao.updateGoodsSingle(goods.getGoodsId(), 0);

            } else {
                //单品
                iGoodsDao.updateGoodsSkuStatus(goods.getGoodsId(), 0);
            }

            //保存商品属性
            for (Map.Entry<String, String> map : mapValueIds.entrySet()) {
                String valueId = map.getKey();
                TbGoodsAttribute attribute = new TbGoodsAttribute();
                attribute.setCreatetime(new Timestamp(new Date().getTime()));
                attribute.setGoodsId(goods.getGoodsId());

                String propId = mapValueProp.get(valueId);
                if (StringUtils.isEmpty(propId)) {
                    LOG.info("------异常2----------");
                    out = CommonMethod.packagMsg("goods_exception_01");
                    throw new RuntimeException();
                }
                attribute.setPropId(Integer.parseInt(propId));
                attribute.setValueId(Integer.parseInt(valueId));
                iGoodsDao.saveObject(attribute);
            }

            out = CommonMethod.packagMsg("6");
            if (flag) {
                out = CommonMethod.packagMsg("5");
            }

            return out;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return out;
        }
    }

    @Override
    public void updateGoodsStatus(int goodsId, int status) {
        iGoodsDao.updateGoodsStatus(goodsId, status);
    }

    @Override
    public List<Map<String, Object>> loadGoodsSkuListMap(int goodsId) {
        return iGoodsDao.loadGoodsSkuListMap(goodsId);
    }

    @Override
    public void updateGoodsOnSell(int goodsId, int isPutway) {
        iGoodsDao.updateGoodsOnSell(goodsId, isPutway);
    }

    @Transactional
    @Override
    public void deleteTimeGoods(int goodsId) {
        try {
            iTimeGoodsDao.deleteTimeGoods(goodsId, 0);
            iGoodsDao.updateGoodsToTimeGoods(goodsId, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Map<String, Object>> loadGoodsAttribute(int goodsId) {
        return iGoodsDao.loadGoodsAttribute(goodsId);
    }

}
