package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.GoodsCondition;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.service.second.ISecondGoodsService;
import com.yufan.utils.Constants;
import com.yufan.utils.PageInfo;
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
public class XiancaiGoodsController {

    @Autowired
    private ISecondGoodsService iSecondGoodsService;

    /**
     * 闲菜列表页面
     *
     * @return
     */
    @RequestMapping("xcPage")
    public ModelAndView toXiancaiPage(HttpServletRequest request, HttpServletResponse response, String goodsName, Integer currePage) {
        ModelAndView modelAndView = new ModelAndView();


        modelAndView.addObject("goodsName", goodsName);
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
    public void loadXcPageData(HttpServletRequest request, HttpServletResponse response, String goodsName, Integer currePage) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();

            GoodsCondition goodsCondition = new GoodsCondition();
            goodsCondition.setGoodsName(goodsName);
            goodsCondition.setCurrePage((currePage == null || currePage == 0) ? 1 : currePage);
            PageInfo page = iSecondGoodsService.loadGoodsList(goodsCondition);
            List<Map<String, Object>> outList = new ArrayList<>();
            List<Map<String, Object>> listData = page.getResultListMap();
            for (int i = 0; i < listData.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("goods_id", Integer.parseInt(listData.get(i).get("id").toString()));
                map.put("goods_name", listData.get(i).get("goods_name"));
                map.put("goods_img", Constants.IMG_URL + listData.get(i).get("goods_img"));
                map.put("now_price", listData.get(i).get("now_price").toString());
                map.put("read_num", Integer.parseInt(listData.get(i).get("read_num").toString()));
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
    public ModelAndView toXiancaiDetailPage(HttpServletRequest request, HttpServletResponse response, Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        //更新访问数
        iSecondGoodsService.UpdateSecondGoodsReadCount(id);

        TbSecondGoods secondGoods = iSecondGoodsService.loadSecondGoods(id);

        modelAndView.addObject("goods_id", secondGoods.getId());
        modelAndView.addObject("goods_name", secondGoods.getGoodsName());
        modelAndView.addObject("goods_img", Constants.IMG_URL + secondGoods.getGoodsImg());
        modelAndView.addObject("true_price", secondGoods.getTruePrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        modelAndView.addObject("now_price", secondGoods.getNowPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
        modelAndView.addObject("new_info", secondGoods.getNewInfo());
        modelAndView.addObject("is_post", secondGoods.getIsPost());
        modelAndView.addObject("about_price", secondGoods.getAboutPrice());
        modelAndView.addObject("goods_info", secondGoods.getGoodsInfo());
        modelAndView.addObject("goods_shop_code", secondGoods.getGoodsShopCode());
        String img1 = StringUtils.isEmpty(secondGoods.getImg1()) ? "" : Constants.IMG_URL + secondGoods.getImg1();
        String img2 = StringUtils.isEmpty(secondGoods.getImg2()) ? "" : Constants.IMG_URL + secondGoods.getImg2();
        String img3 = StringUtils.isEmpty(secondGoods.getImg3()) ? "" : Constants.IMG_URL + secondGoods.getImg3();
        String img4 = StringUtils.isEmpty(secondGoods.getImg4()) ? "" : Constants.IMG_URL + secondGoods.getImg4();
        modelAndView.addObject("img1", img1);
        modelAndView.addObject("img2", img2);
        modelAndView.addObject("img3", img3);
        modelAndView.addObject("img4", img4);
        modelAndView.setViewName("xc-detail");
        return modelAndView;

    }


}
