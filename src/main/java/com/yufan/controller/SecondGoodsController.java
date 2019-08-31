package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbSecondGoods;
import com.yufan.service.second.ISecondGoodsService;
import com.yufan.utils.CommonMethod;
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

/**
 * 创建人: lirf
 * 创建时间:  2019/8/30 18:02
 * 功能介绍:  只提供简单浏览的简单商品（闲菜）
 */
@Controller
@RequestMapping(value = "/second/")
public class SecondGoodsController {

    @Autowired
    private ISecondGoodsService iSecondGoodsService;


    @RequestMapping("secondGoodsPage")
    public ModelAndView secondGoodsPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("second-goods-list");
        return modelAndView;
    }

    /**
     * 加载分页数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("loadSecondGoodsPageData")
    public void loadPageData(TbSecondGoods secondGoods, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页

            pageInfo = iSecondGoodsService.loadDataPage(currePage, secondGoods);
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
     * 增加
     *
     * @return
     */
    @RequestMapping("addSecondGoodsPage")
    public ModelAndView addSecondGoodsPage(HttpServletRequest request, HttpServletResponse response, Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        TbSecondGoods goods = new TbSecondGoods();
        goods.setDataIndex(0);
        goods.setTruePrice(new BigDecimal(0));
        goods.setPurchasePrice(new BigDecimal(0));
        goods.setNowPrice(new BigDecimal(0));
        if (id != null && id > 0) {
            goods = iSecondGoodsService.loadSecondGoods(id);
            modelAndView.addObject("img4", goods.getImg4());
            modelAndView.addObject("img3", goods.getImg3());
            modelAndView.addObject("img2", goods.getImg2());
            modelAndView.addObject("img1", goods.getImg1());
        }
        modelAndView.addObject("webImg", Constants.IMG_URL);
        modelAndView.addObject("goods", goods);
        modelAndView.setViewName("add-second-goods");
        return modelAndView;
    }


    /**
     * 新增加数据
     *
     * @param request
     * @param response
     * @param secondGoods
     */
    @RequestMapping("saveSecondGoods")
    public void saveSecondGoods(HttpServletRequest request, HttpServletResponse response, TbSecondGoods secondGoods) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = secondGoods.getId() == 0 ? CommonMethod.packagMsg("6") : CommonMethod.packagMsg("5");
            boolean flag = iSecondGoodsService.saveSecondGoods(secondGoods);
            if (!flag) {
                out = CommonMethod.packagMsg("0");
            }
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改数据
     *
     * @param request
     * @param response
     */
    @RequestMapping("updateSecondGoodsStatus")
    public void updateSecondGoodsStatus(HttpServletRequest request, HttpServletResponse response, Integer id, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = CommonMethod.packagMsg("1");
            iSecondGoodsService.updateSecondGoodsStatus(id, status);
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

    /*
   CREATE TABLE `tb_second_goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goods_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `goods_img` varchar(50) DEFAULT NULL COMMENT '商品主图',
  `true_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品原价',
  `now_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品现价',
  `purchase_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品进货价',
  `read_num` int(11) DEFAULT '0' COMMENT '浏览数',
  `like_num` int(11) DEFAULT '0' COMMENT '意向(想要的人数)',
  `new_info` int(11) DEFAULT '0' COMMENT '新旧 0全新 1到9成新 ',
  `is_post` int(11) DEFAULT '0' COMMENT '是否邮寄 1 邮寄  0 不邮寄',
  `about_price` int(11) DEFAULT '0' COMMENT '是否议价0不议价 1议价',
  `super_like` int(11) DEFAULT '0' COMMENT '超赞数()',
  `goods_info` text COMMENT '详情',
  `status` int(11) DEFAULT '2' COMMENT '状态 0已删除 1上架 2 下架 3 备货中',
  `data_index` int(11) DEFAULT '0' COMMENT '排序,数值越大越靠前',
  `create_time` datetime DEFAULT NULL,
  `img4` varchar(50) DEFAULT NULL COMMENT '图片4(排第1位)',
  `img3` varchar(50) DEFAULT NULL COMMENT '图片3(排第2位)',
  `img2` varchar(50) DEFAULT NULL COMMENT '图片2(排第3位)',
  `img1` varchar(50) DEFAULT NULL COMMENT '图片1(排第4位)',
  `goods_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品编码',
  `goods_shop_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '商品店铺编码(唯一)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_goods_shop_code` (`goods_shop_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
    * */