package com.yufan.dao.banner;

import com.yufan.pojo.TbBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/8 11:59
 * 功能介绍:
 */
@Transactional
@Repository
public interface IBannerJpaDao extends JpaRepository<TbBanner,Integer> {
}
