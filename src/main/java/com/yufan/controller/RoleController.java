package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.anno.ClassAnnotation;
import com.yufan.bean.MenuObj;
import com.yufan.bean.RoleObj;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbFunctions;
import com.yufan.pojo.TbRole;
import com.yufan.service.func.IMenuService;
import com.yufan.service.role.IRoleService;

import com.yufan.utils.HelpCommon;
import com.yufan.utils.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/29 10:19
 * 功能介绍: 角色管理
 */
@Controller
@RequestMapping("/role/")
@ClassAnnotation(name = "role", desc = "角色管理")
public class RoleController {

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IMenuService iMenuService;

    /**
     * 跳转到角色管理页面
     *
     * @return
     */
    @RequestMapping("rolePage")
    public ModelAndView toRolePage() {
        //查询角色列表 // 原始的数据
        List<TbRole> roleList = iRoleService.findRoleAll();
        List<RoleObj> rootRoleTree = new ArrayList<RoleObj>();
        for (TbRole role : roleList) {
            if (role.getStatus() == 1) {
                RoleObj roleObj = new RoleObj();
                roleObj = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(role)), RoleObj.class);
                rootRoleTree.add(roleObj);
            }
        }

        // 最后的结果
        List<RoleObj> roleTreeList = new ArrayList<RoleObj>();
        // 先找到所有的一级角色
        for (RoleObj role : rootRoleTree) {
            if (role.getStatus() == 1 && role.getRoleParentid() == 0) {
                roleTreeList.add(role);
            }
        }
        // 为一级角色设置子角色, getChild是递归调用的
        for (RoleObj role : roleTreeList) {
            role.setChildRole(getChild(role.getRoleId(), rootRoleTree));
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roleList", roleList);
        modelAndView.addObject("roleTreeList", roleTreeList);
        modelAndView.setViewName("role-list2");
        return modelAndView;
    }

    private List<RoleObj> getChild(int roleId, List<RoleObj> rootRoleList) {
        // 子角色
        List<RoleObj> childList = new ArrayList<RoleObj>();
        for (RoleObj role : rootRoleList) {
            // 遍历所有节点，将父角色id与传过来的id比较
            if (roleId == role.getRoleParentid()) {
                childList.add(role);
            }
        }
        // 把子角色的子角色再循环一遍
        for (RoleObj role : childList) {
            role.setChildRole(getChild(role.getRoleId(), rootRoleList));
        }
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

    /**
     * 查询角色
     *
     * @param request
     * @param response
     */
    @PostMapping("loadRoleListData")
    private void loadRoleListData(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer;
        try {
            writer = response.getWriter();
            //分页查询
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页(为了页面统一,currePage都是从1开始)
            Page<TbRole> page = iRoleService.loadTbPrivRoleListPage(currePage, pageInfo.getPageSize());


            //输出参数
            JSONObject dataJson = new JSONObject();
            dataJson.put("draw", Integer.parseInt(request.getParameter("draw")));
            dataJson.put("recordsTotal", page.getTotalElements());
            dataJson.put("recordsFiltered", page.getTotalElements());
            dataJson.put("data", page.getContent());
            writer.print(dataJson);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改角色状态
     *
     * @param request
     * @param response
     * @param roleId
     * @param status
     */
    @PostMapping("updateRoleStatus")
    public void updateRoleStatus(HttpServletRequest request, HttpServletResponse response, Integer roleId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = status == 0 ? HelpCommon.packagMsg("3") : HelpCommon.packagMsg("4");
            iRoleService.updateRoleStatus(roleId, status);
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增加或者修改角色
     *
     * @param request
     * @param response
     * @param roleId
     * @param status
     */
    @PostMapping("saveRole")
    public void saveRole(HttpServletRequest request, HttpServletResponse response, Integer roleId, Integer status, Integer parentRoleId) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = roleId != 0 ? HelpCommon.packagMsg("5") : HelpCommon.packagMsg("6");
            TbRole role = new TbRole();
            if (roleId != 0) {
                role.setRoleId(roleId);
            }
            role.setRoleName(request.getParameter("roleName"));
            role.setStatus(status);
            role.setRoleParentid(parentRoleId);
            role.setRemark(request.getParameter("remark"));
            role.setCreatetime(new Timestamp(new Date().getTime()));
            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            role.setCreateman(user.getLoginName());
            iRoleService.saveRole(role);
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到增加角色功能页面
     *
     * @return
     */
    @RequestMapping("roleFunPage")
    public ModelAndView toAddRoleFunPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        //查询角色列表 // 原始的数据
        List<TbRole> roleList = iRoleService.findRoleAll();
        List<RoleObj> rootRoleTree = new ArrayList<RoleObj>();
        for (TbRole role : roleList) {
            if (role.getStatus() == 1) {
                RoleObj roleObj = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(role)), RoleObj.class);
                rootRoleTree.add(roleObj);
            }
        }

        // 最后的结果
        List<RoleObj> roleTreeList = new ArrayList<RoleObj>();
        // 先找到所有的一级角色
        for (RoleObj role : rootRoleTree) {
            if (role.getStatus() == 1 && role.getRoleParentid() == 0) {
                roleTreeList.add(role);
            }
        }
        // 为一级角色设置子角色, getChild是递归调用的
        for (RoleObj role : roleTreeList) {
            role.setChildRole(getChild(role.getRoleId(), rootRoleTree));
        }

        //查询角色功能
        //list角色功能
        List<String> roleFunStrList = new ArrayList<>();
        int roleId = StringUtils.isEmpty(request.getParameter("roleId")) ? 0 : Integer.parseInt(request.getParameter("roleId"));
        List<Map<String, Object>> roleFunList = iRoleService.loadRoleFunListMap(roleId);
        if (null != roleFunList && roleFunList.size() > 0) {
            for (int i = 0; i < roleFunList.size(); i++) {
                String funId = roleFunList.get(i).get("function_id").toString();
                roleFunStrList.add(funId);
            }
        }

        //查询功能列表
        List<MenuObj> menuTreeList = new ArrayList<>();
        //查询菜单
        List<TbFunctions> functions = iMenuService.listUserPrivFunction();
        for (TbFunctions f : functions) {
            if (f.getStatus() != 1) {
                continue;
            }
            if (f.getFunctionParentid() == 0) {
                MenuObj menu = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(f)), MenuObj.class);
                menuTreeList.add(menu);
            }
        }
        for (MenuObj m : menuTreeList) {
            List<TbFunctions> sun = new ArrayList<>();
            for (TbFunctions f : functions) {
                if (f.getStatus() != 1) {
                    continue;
                }
                if (f.getFunctionParentid() == m.getFunctionId()) {
                    sun.add(f);
                }
            }
            m.setChildMenu(sun);
        }

        modelAndView.addObject("menuTreeList", menuTreeList);

        modelAndView.addObject("roleList", roleList);
        modelAndView.addObject("roleTreeList", roleTreeList);

        modelAndView.addObject("roleId", roleId);
        modelAndView.addObject("roleFunStrList", roleFunStrList);
        modelAndView.addObject("selectRoleId", roleId);

        modelAndView.setViewName("add-role-fun");
        return modelAndView;
    }

    /**
     * 保存用户角色
     */
    @PostMapping("addRoleFun")
    public void saveRoleFun(HttpServletRequest request, HttpServletResponse response, String checkValues, Integer roleId) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = HelpCommon.packagMsg("7");
            int index = iRoleService.saveRoleFun(roleId, checkValues);
            if (index > 0) {
                result = HelpCommon.packagMsg("6");
            }
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
