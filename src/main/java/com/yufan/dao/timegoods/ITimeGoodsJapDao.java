package com.yufan.dao.timegoods;

import com.yufan.pojo.TbTimeGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/11 11:24
 * 功能介绍:
 */
@Transactional
@Repository
public interface ITimeGoodsJapDao extends JpaRepository<TbTimeGoods,Integer> {
}
