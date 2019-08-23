package com.yufan.dao.shop;

import com.yufan.pojo.TbShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/5/21 16:59
 * 功能介绍:
 */
@Transactional
@Repository
public interface IShopJpaDao extends JpaRepository<TbShop, Integer> {

    @Query(value = "SELECT * from tb_shop s  where s.status=1", nativeQuery = true)
    List<TbShop> findShopAll();
}
