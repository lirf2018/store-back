package com.yufan.dao.func;

import com.yufan.pojo.TbFunctions;
import com.yufan.pojo.TbPageMenu;
import com.yufan.utils.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2019/4/9 14:49
 * 功能介绍:
 */
public interface IMenuDao {

    public PageInfo loadMenuPage(int currePage, TbFunctions func);

    public void deleteMenuByParentId(int parentId);

    public void delRoleFun(int roleId);

    public void insertMoreBySQL(String values);

    public List<Map<String, Object>> loadRoleFunListMap(int roleId);

    PageInfo loadMenuWebPage(int currePage, TbPageMenu menu);

    void updateMenuWebStatus(int id, int status);

    TbPageMenu loadPageMenu(int id);
}
