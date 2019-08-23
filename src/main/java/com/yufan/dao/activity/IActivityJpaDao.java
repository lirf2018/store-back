package com.yufan.dao.activity;

import com.yufan.pojo.TbActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/8/10 12:32
 * 功能介绍:
 */
@Transactional
@Repository
public interface IActivityJpaDao extends JpaRepository<TbActivity,Integer> {

}
