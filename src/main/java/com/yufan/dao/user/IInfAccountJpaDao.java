package com.yufan.dao.user;

import com.yufan.pojo.TbInfAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description:
 * @author: lirf
 * @time: 2021/6/19
 */
@Transactional
@Repository
public interface IInfAccountJpaDao extends JpaRepository<TbInfAccount, Integer>, JpaSpecificationExecutor<TbInfAccount> {

    @Query("select t from TbInfAccount t where t.sid = :account")
    TbInfAccount findByAccount(@Param("account") String account);

}
