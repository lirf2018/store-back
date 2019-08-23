package com.yufan.dao.user;

import com.yufan.pojo.TbAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/22 15:48
 * 功能介绍:
 */
@Transactional
@Repository
public interface IUserJpaDao extends JpaRepository<TbAdmin, Integer>,JpaSpecificationExecutor<TbAdmin> {

    @Query("select t from TbAdmin t where t.loginName = :name")
    TbAdmin findByUserLoginName(@Param("name") String name);
}
