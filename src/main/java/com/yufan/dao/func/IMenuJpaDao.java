package com.yufan.dao.func;

import com.yufan.pojo.TbFunctions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/25 15:47
 * 功能介绍:
 */
@Transactional
@Repository
public interface IMenuJpaDao extends JpaRepository<TbFunctions, Integer> {

    @Query(value = "SELECT * from tb_functions f JOIN tb_role_function fr on fr.function_id=f.function_id   " +
            "where fr.role_id in (SELECT ur.role_id from tb_user_role ur where ur.admin_id=?1) ORDER BY f.data_index desc", nativeQuery = true)
    List<TbFunctions> listUserPrivFunction(Integer userId);

    @Query(value = "SELECT * from tb_functions f  ORDER BY f.data_index desc", nativeQuery = true)
    List<TbFunctions> listUserPrivFunction();

    @Query(value = "SELECT * from tb_functions f where f.function_parentid=?1  ORDER BY f.data_index desc", nativeQuery = true)
    List<TbFunctions> listUserPrivFunctionParent(int parentId);

}
