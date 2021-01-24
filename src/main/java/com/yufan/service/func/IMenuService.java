package com.yufan.service.func;

import com.yufan.pojo.TbFunctions;
import com.yufan.pojo.TbPageMenu;
import com.yufan.utils.PageInfo;

import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/25 15:47
 * 功能介绍:
 */
public interface IMenuService {

    List<TbFunctions> listUserPrivFunction();

    List<TbFunctions> listUserPrivFunctionParent(int parentId);

    List<TbFunctions> listUserPrivFunction(int userId);

    PageInfo loadMenuPage(int currePage, TbFunctions func);

    TbFunctions saveFuntion(TbFunctions func);

    void deleteMenu(int menuId, int parentId);

    void updateMenuStatus(int menuId, int status);

    PageInfo loadMenuWebPage(int currePage, TbPageMenu menu);

    void updateMenuWebStatus(int id, int status);

    TbPageMenu loadPageMenu(int id);

    void savePageMenuData(TbPageMenu pageMenu);
}
