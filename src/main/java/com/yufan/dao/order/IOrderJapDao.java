package com.yufan.dao.order;

import com.yufan.pojo.TbOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/22 13:41
 * 功能介绍:
 */
@Transactional
@Repository
public interface IOrderJapDao extends JpaRepository<TbOrder, Integer> {

    @Query("select t from TbOrder t where t.orderNo = :orderNo")
    public TbOrder loadOrderByOrderno(String orderNo);

}
