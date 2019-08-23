package com.yufan.dao.category;

import com.yufan.pojo.TbCategoryLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/7 9:26
 * 功能介绍:
 */
@Transactional
@Repository
public interface ICategoryJpaDao extends JpaRepository<TbCategoryLevel, Integer> {


}
