package com.yufan.dao.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.OrderCondition;
import com.yufan.common.dao.IGeneralDao;
import com.yufan.dao.order.IOrderDao;
import com.yufan.pojo.TbOrder;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/22 13:43
 * 功能介绍:
 */
@Repository
@Transactional
public class OrderDaoImpl implements IOrderDao {

    @Autowired
    private IGeneralDao iGeneralDao;

    @Override
    public PageInfo loadOrderPage(int currePage, OrderCondition orderCondition) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT o.order_id,o.user_id,o.order_no,o.order_from,o.order_count,CONCAT(o.order_price,'') as order_price,o.real_price,CONCAT(o.advance_price,'') as advance_price,o.needpay_price,o.user_name,o.user_phone,  ");
        sql.append(" o.user_addr,o.user_addr_id,o.advance_pay_way,o.advance_pay_code,DATE_FORMAT(o.advance_pay_time,'%Y-%m-%d %T') as advance_pay_time,o.pay_way,o.pay_code,DATE_FORMAT(o.pay_time,'%Y-%m-%d %T') as pay_time,o.trade_channel,CONCAT(o.post_price,'') as post_price, ");
        sql.append(" o.post_way,o.company_code,o.post_no,o.user_remark,o.service_remark,o.order_status,DATE_FORMAT(o.order_time,'%Y-%m-%d %T') as order_time,DATE_FORMAT(o.post_time,'%Y-%m-%d %T') as post_time,o.business_type,o.discounts_id,o.discounts_price, ");
        sql.append(" o.discounts_remark,o.trade_no,o.refund_price,o.refund_remark, o.lastaltertime,o.lastalterman,o.remark,o.post_phone,o.post_man,o.status_opration,o.shop_id ");
        sql.append(" from tb_order o where 1=1 ");
        if (!StringUtils.isEmpty(orderCondition.getOrderNo())) {
            sql.append(" and o.order_no like '%").append(orderCondition.getOrderNo()).append("%' ");
        }
        if (!StringUtils.isEmpty(orderCondition.getUserPhone())) {
            sql.append("  and o.user_phone like '%").append(orderCondition.getUserPhone()).append("%' ");
        }
        if (!StringUtils.isEmpty(orderCondition.getUserName())) {
            sql.append(" and o.user_name  like '%").append(orderCondition.getUserName()).append("%' ");
        }
        if (null != orderCondition.getOrderStatus()) {
            sql.append("  and o.order_status = ").append(orderCondition.getOrderStatus()).append(" ");
        }
        if (null != orderCondition.getUserId()) {
            sql.append(" and o.user_id = ").append(orderCondition.getUserId()).append(" ");
        }
        if (null != orderCondition.getPostWay()) {
            sql.append(" and o.post_way = ").append(orderCondition.getPostWay()).append(" ");
        }
        if (null != orderCondition.getBusinessType()) {
            sql.append(" and o.business_type = ").append(orderCondition.getBusinessType()).append(" ");
        }
        if (!StringUtils.isEmpty(orderCondition.getStartOrderDate())) {
            sql.append(" and o.order_time >= '").append(orderCondition.getStartOrderDate()).append(" 00:00:00' ");
        }
        if (!StringUtils.isEmpty(orderCondition.getEndOrderDate())) {
            sql.append(" and o.order_time <= '").append(orderCondition.getEndOrderDate()).append(" 23:59:59' ");
        }
        if (!StringUtils.isEmpty(orderCondition.getStartPayDate())) {
            sql.append(" and o.pay_time >= '").append(orderCondition.getStartPayDate()).append(" 00:00:00' ");
        }
        if (!StringUtils.isEmpty(orderCondition.getEndPayDate())) {
            sql.append(" and o.pay_time <= '").append(orderCondition.getEndPayDate()).append(" 23:59:59' ");
        }
        if (null != orderCondition.getOrderId()) {
            sql.append(" and o.order_id=").append(orderCondition.getOrderId()).append(" ");
        }
        if (null != orderCondition.getShopId()) {
            sql.append(" and o.shop_id = ").append(orderCondition.getShopId()).append(" ");
        }
        if (!StringUtils.isEmpty(orderCondition.getGoodsName()) || null != orderCondition.getGoodsId() || null != orderCondition.getSkuId()
                || null != orderCondition.getTimeGoodsId()) {
            sql.append(" and o.order_id in (select dd.order_id from tb_order_detail dd where 1=1 ");
            if (!StringUtils.isEmpty(orderCondition.getGoodsName())) {
                sql.append(" and dd.goods_name like '%").append(orderCondition.getGoodsName().trim()).append("%' ");
            }
            if (null != orderCondition.getGoodsId()) {
                sql.append(" and dd.goods_id =").append(orderCondition.getGoodsId()).append(" ");
            }
            if (null != orderCondition.getSkuId()) {
                sql.append(" and dd.sku_id =").append(orderCondition.getSkuId()).append(" ");
            }
            if (null != orderCondition.getTimeGoodsId()) {
                sql.append(" and dd.time_goods_id =").append(orderCondition.getTimeGoodsId()).append(" ");
            }
            sql.append(" ) ");
        }
        if (orderCondition.getGoodsYuding() != null && orderCondition.getGoodsYuding() == 1) {
            sql.append(" and o.order_id in (select pp.order_id from tb_order_detail_property pp where 1=1 and  pp.property_type=0 and pp.property_key='is_yuding' and pp.property_value='1') ");
        }
        if (orderCondition.getGoodsYuding() != null && orderCondition.getGoodsYuding() == 0) {
            sql.append(" and o.order_id not in (select pp.order_id from tb_order_detail_property pp where 1=1 and  pp.property_type=0 and pp.property_key='is_yuding' and pp.property_value='1') ");
        }
        sql.append(" ORDER BY o.order_id desc ");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrePage(currePage);
        pageInfo.setSqlQuery(sql.toString());
        pageInfo = iGeneralDao.loadPageInfoSQLListMap(pageInfo);
        return pageInfo;
    }

    @Override
    public List<Map<String, Object>> queryOrderDetailById(int orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT d.order_id,d.detail_id,d.goods_id,d.goods_name,d.goods_spec_name,d.goods_count,CONCAT(d.sale_money,'') as sale_money,CONCAT(d.time_price,'') as time_price,CONCAT(d.deposit_price,'') as deposit_price,d.get_addr_name, ");
        sql.append(" DATE_FORMAT(d.get_time,'%Y-%m-%d %T') as get_time,d.back_addr_name,DATE_FORMAT(d.back_time,'%Y-%m-%d %T') as back_time,p.param_value as detail_status_name,if(d.is_coupon=1,'是','否') as is_coupon,d.detail_status ");
        sql.append(" ,d.goods_spec,CONCAT(d.goods_true_money,'') as goods_true_money,CONCAT(d.goods_purchase_price,'') as goods_purchase_price,d.time_goods_id,if(d.time_goods_id>0,'是','否') as is_time_goods ");
        sql.append(" ,d.rent_pay_type,d.rent_day,DATE_FORMAT(d.rent_end_time,'%Y-%m-%d') as rent_end_time ");
        sql.append("  from tb_order_detail d ");
        sql.append(" LEFT JOIN tb_param p on p.param_code='detail_status' and p.param_key=d.detail_status and p.`status`=1 where order_id=? ");
        return iGeneralDao.getBySQLListMap(sql.toString(), orderId);
    }

    @Override
    public List<Map<String, Object>> queryOrderDetailPropByOrderId(int orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT p.order_id,p.detail_id,p.property_key,p.property_value,pa.param_value as remark_name from tb_order_detail_property p ");
        sql.append(" LEFT JOIN tb_param pa on  pa.param_code='detail_property_desc' and pa.param_key=p.remark and pa.`status`=1 ");
        sql.append(" where p.order_id=? ");
        return iGeneralDao.getBySQLListMap(sql.toString(), orderId);
    }

    @Override
    public List<Map<String, Object>> queryOrderDetailPropByDetailId(int detailId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT p.order_id,p.detail_id,p.property_key,p.property_value,pa.param_value as remark_name from tb_order_detail_property p ");
        sql.append(" LEFT JOIN tb_param pa on  pa.param_code='detail_property_desc' and pa.param_key=p.remark and pa.`status`=1 ");
        sql.append(" where p.detail_id=? ");
        return iGeneralDao.getBySQLListMap(sql.toString(), detailId);
    }

    @Override
    public List<Map<String, Object>> queryOrderStatusLogByOrderNo(String orderNo) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DATE_FORMAT(ro.createtime,'%Y-%m-%d %T') as createtime,ro.lastalterman,ro.remark,p.param_value as status_name ");
        sql.append(" from tb_order_status_recode ro LEFT JOIN tb_param p on p.param_code='order_status' and p.param_key=ro.order_status and p.`status`=1 where order_no =? ORDER BY ro.id desc ");
        return iGeneralDao.getBySQLListMap(sql.toString(), orderNo);
    }

    @Override
    public void updateOrderDetailStatus(int detailId, int status) {
        String sql = " update tb_order_detail set detail_status=?,lastaltertime=NOW() where detail_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, status, detailId);
    }

    @Override
    public void updateOrderStatus(int orderId, int status, String serviceRemark, String lastalterman) {
        String sql = " update tb_order set order_status=?,lastaltertime=NOW(),service_remark=?,lastalterman=?,user_read_mark=1 where order_id=? ";
        if (Constants.ORDER_STATUS_6 == status) {
            sql = " update tb_order set order_status=?,lastaltertime=NOW(),service_remark=?,finish_time=NOW(),lastalterman=?,user_read_mark=1 where order_id=? ";
        }
        iGeneralDao.executeUpdateForSQL(sql, status, serviceRemark, lastalterman, orderId);
    }

    @Override
    public TbOrder loadOrder(String orderNo) {
        String hql = " from TbOrder where orderNo=?1 ";
        return iGeneralDao.queryUniqueByHql(hql, orderNo);
    }

    @Override
    public void updateOrderInfo(JSONObject orderData) {
        String lastalterman = orderData.getString("lastalterman");
        int orderStatus = orderData.getInteger("orderStatus");
        BigDecimal refundPrice = orderData.getBigDecimal("refundPrice");
        String refundRemark = orderData.getString("refundRemark");
        String serviceRemark = orderData.getString("serviceRemark");
        String postMan = orderData.getString("postMan");
        String postPhone = orderData.getString("postPhone");
        int orderId = orderData.getInteger("orderId");
        String sql = " update tb_order set lastalterman=?,lastaltertime=NOW(),order_status=?,refund_price=?,refund_remark=?,service_remark=?,post_man=?,post_phone=?,finish_time=NOW(),user_read_mark=1 where order_id=? ";
        iGeneralDao.executeUpdateForSQL(sql, lastalterman, orderStatus, refundPrice, refundRemark, serviceRemark, postMan, postPhone, orderId);
    }

    @Override
    public void updateDetailInfo(Integer orderId, Integer detailId, String rentEntTime, Integer status) {
        String sql = " update tb_order_detail set detail_status=" + status + ",lastaltertime=now() ";
        if (!StringUtils.isEmpty(rentEntTime)) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate now = LocalDate.now();
            LocalDate endDate = LocalDate.parse(rentEntTime, format);
            int rentDay = Period.between(now, endDate).getDays() + 1;
            sql = sql + " ,rent_day=" + rentDay + ",rent_end_time='" + rentEntTime + "' ";
        }else{
            sql = sql + " ,rent_day=0,rent_end_time=null ";
        }
        if (detailId == -1) {
            // 更新全部
            sql = sql + " where order_id=" + orderId + " ";
        } else {
            sql = sql + " where detail_id=" + detailId + " ";
        }
        iGeneralDao.executeUpdateForSQL(sql);
    }
}
