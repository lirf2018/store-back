package com.yufan.dao.addr;

import com.yufan.pojo.TbPlatformAddr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/7/8 11:30
 * 功能介绍:
 */
@Transactional
@Repository
public interface IAddrJpaDao extends JpaRepository<TbPlatformAddr, Integer> {

}
