package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.anno.ClassAnnotation;
import com.yufan.bean.GoodsCondition;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.pojo.TbShop;
import com.yufan.service.second.ISecondGoodsService;
import com.yufan.service.shop.IShopService;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/9/1 18:31
 * 功能介绍: 闲菜商品
 */
@Controller
@RequestMapping(value = "/xc/")
@ClassAnnotation(name = "xc", desc = "闲菜商品")
public class XiancaiGoodsController {

    private Logger LOG = Logger.getLogger(XiancaiGoodsController.class);

    @Autowired
    private ISecondGoodsService iSecondGoodsService;

    @Autowired
    private IShopService iShopService;

    /**
     * 闲菜列表页面
     *
     * @return
     */
    @RequestMapping("xcPage")
    public ModelAndView toXiancaiPage(HttpServletRequest request, HttpServletResponse response, String goodsName, Integer currePage, String secretKey) {
        ModelAndView modelAndView = new ModelAndView();


        modelAndView.addObject("goodsName", goodsName);
        modelAndView.addObject("secretKey", secretKey);
        modelAndView.setViewName("xc-list");
        return modelAndView;

    }

    /**
     * 加载闲菜分页数据
     *
     * @param request
     * @param response
     * @param goodsName
     */
    @RequestMapping("xcPageData")
    public void loadXcPageData(HttpServletRequest request, HttpServletResponse response, String goodsName, Integer currePage, String secretKey) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            TbShop shop = iShopService.loadShopBySecretKey(secretKey);
            LOG.info("-------secretKey--------" + secretKey);
            if (StringUtils.isEmpty(secretKey) || shop.getStatus() != 1) {
                JSONObject data = new JSONObject();
                data.put("has_next", false);
                data.put("curre_page", (currePage == null || currePage == 0) ? 1 : currePage);
                data.put("page_size", 20);
                data.put("goods_list", new ArrayList<>());


                JSONObject out = new JSONObject();
                out.put("code", 1);
                out.put("data", data);
                writer.println(out);
                writer.close();
                if (shop.getStatus() != 1) {
                    LOG.info("--------店铺无效---------");
                }
                return;
            }


            GoodsCondition goodsCondition = new GoodsCondition();
            goodsCondition.setGoodsName(goodsName);
            goodsCondition.setShopId(shop.getShopId());
            goodsCondition.setSecretKey(secretKey);
            goodsCondition.setCurrePage((currePage == null || currePage == 0) ? 1 : currePage);
            PageInfo page = iSecondGoodsService.loadGoodsList(goodsCondition);
            List<Map<String, Object>> outList = new ArrayList<>();
            List<Map<String, Object>> listData = page.getResultListMap();
            for (int i = 0; i < listData.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("goods_id", Integer.parseInt(listData.get(i).get("goods_id").toString()));
                map.put("goods_name", listData.get(i).get("goods_name"));
                map.put("goods_img", Constants.IMG_WEB_URL + listData.get(i).get("goods_img"));
                map.put("now_price", listData.get(i).get("now_price").toString());
                map.put("read_num", Integer.parseInt(listData.get(i).get("read_num").toString()));
                map.put("secret_key", secretKey);
                outList.add(map);
            }

            JSONObject data = new JSONObject();
            data.put("has_next", page.isHasNext());
            data.put("curre_page", page.getCurrePage());
            data.put("page_size", page.getPageSize());
            data.put("goods_list", outList);


            JSONObject out = new JSONObject();
            out.put("code", 1);
            out.put("data", data);
            writer.println(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 闲菜详情页面
     *
     * @return
     */
    @RequestMapping("xcDetailPage")
    public ModelAndView toXiancaiDetailPage(HttpServletRequest request, HttpServletResponse response, Integer goodsId, String secretKey) {
        ModelAndView modelAndView = new ModelAndView();
        LOG.info("-------secretKey--------" + secretKey);
        if (org.apache.commons.lang3.StringUtils.isEmpty(secretKey)) {
            //没权限访问
            modelAndView.setViewName("404");
            return modelAndView;
        }

        TbSecondGoods secondGoods = iSecondGoodsService.loadSecondGoods(goodsId);
        //查询店铺（商品对应的店铺）
        TbShop shop = iShopService.loadShop(secondGoods.getShopId());
        boolean flag = true;
        if (shop.getStatus() != 1) {
            LOG.info("--------店铺无效---------");
            flag = false;
        }
        //如果商品店铺秘钥与传的秘钥一致,则为非分销链接,否则为分销商品
        if (!shop.getSecretKey().equals(secretKey)) {
            //访问地址秘钥与商品实际对应的店铺秘钥不一致,考虑是否为分销商品
            //则要查看访问地址秘钥是否有权限查看商品详情
            TbShop shop_ = iShopService.loadShopBySecretKey(secretKey);//查询链接秘钥所对应的实际店铺信息
            List<Map<String, Object>> mapList = iSecondGoodsService.queryShopGoodsRel(shop_.getShopId(), goodsId, 0);//根据访问地址店铺查询是否存在分销情况
            if (null == mapList || mapList.size() == 0) {
                flag = false;
                LOG.info("--------非分销商品-------链接店铺-secretKey:" + secretKey + " 商品对应店铺 secretKey:" + shop.getSecretKey());
            }
        }
        if (!flag) {
            //没权限访问
            modelAndView.setViewName("404");
            return modelAndView;
        }


        //更新访问数
        iSecondGoodsService.UpdateSecondGoodsReadCount(goodsId);

        modelAndView.addObject("goods_id", secondGoods.getGoodsId());
        modelAndView.addObject("goods_name", secondGoods.getGoodsName());
        modelAndView.addObject("goods_img", Constants.IMG_WEB_URL + secondGoods.getGoodsImg());
        modelAndView.addObject("true_price", secondGoods.getTruePrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        modelAndView.addObject("now_price", secondGoods.getNowPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        modelAndView.addObject("new_info", secondGoods.getNewInfo());
        modelAndView.addObject("is_post", secondGoods.getIsPost());
        modelAndView.addObject("about_price", secondGoods.getAboutPrice());
        modelAndView.addObject("goods_info", secondGoods.getGoodsInfo());
        modelAndView.addObject("goods_shop_code", secondGoods.getGoodsShopCode());
        String img1 = StringUtils.isEmpty(secondGoods.getImg1()) ? "" : Constants.IMG_WEB_URL + secondGoods.getImg1();
        String img2 = StringUtils.isEmpty(secondGoods.getImg2()) ? "" : Constants.IMG_WEB_URL + secondGoods.getImg2();
        String img3 = StringUtils.isEmpty(secondGoods.getImg3()) ? "" : Constants.IMG_WEB_URL + secondGoods.getImg3();
        String img4 = StringUtils.isEmpty(secondGoods.getImg4()) ? "" : Constants.IMG_WEB_URL + secondGoods.getImg4();
        modelAndView.addObject("img1", img1);
        modelAndView.addObject("img2", img2);
        modelAndView.addObject("img3", img3);
        modelAndView.addObject("img4", img4);
        modelAndView.setViewName("xc-detail");
        return modelAndView;

    }


}
