package com.yufan.dao.role;

import com.yufan.pojo.TbRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/29 10:27
 * 功能介绍:
 */
@Transactional
@Repository
public interface IRoleJpaDao extends JpaRepository<TbRole, Integer> {
}
