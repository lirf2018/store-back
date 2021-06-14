package com.yufan.dao.coupon;

import com.yufan.pojo.TbCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/13
 */
@Transactional
@Repository
public interface ICouponJapDao extends JpaRepository<TbCoupon,Integer> {
}
