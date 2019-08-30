package com.yufan.dao.second;

import com.yufan.pojo.TbSecondGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/30 17:55
 * 功能介绍: 只提供简单浏览的简单商品
 */
@Transactional
@Repository
public interface ISecondGoodsJpaDao extends JpaRepository<TbSecondGoods,Integer> {
}
