package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.bean.AdminCondition;
import com.yufan.pojo.*;
import com.yufan.service.role.IRoleService;
import com.yufan.service.shop.IShopService;
import com.yufan.service.user.IUserService;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.MD5;
import com.yufan.utils.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/25 17:26
 * 功能介绍: 用户管理
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    private Logger LOG = Logger.getLogger(UserController.class);

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IShopService iShopService;

    /**
     * 用户管理列表
     *
     * @return
     */
    @RequestMapping("userPage")
    public ModelAndView toUserPage(HttpServletRequest request, HttpServletResponse response) {
        //查询角色列表
        List<TbRole> roleList = iRoleService.findRoleAll();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("roleList", roleList);
        modelAndView.setViewName("user-list");
        return modelAndView;
    }

    /**
     * 查询用户列表数据
     */
    @RequestMapping("userListData")
    public void loadUserListData(HttpServletRequest request, HttpServletResponse response, AdminCondition adminCondition) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页
            pageInfo = iUserService.loadPrivUserInfoPage(currePage, adminCondition);
            //处理数据
            int recordSum = pageInfo.getRecordSum();

            //输出参数
            JSONObject dataJson = new JSONObject();
            dataJson.put("draw", Integer.parseInt(request.getParameter("draw")));
            dataJson.put("recordsTotal", recordSum);
            dataJson.put("recordsFiltered", recordSum);
            dataJson.put("data", pageInfo.getResultListMap());
            writer.print(dataJson);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到新增加页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("addAdminPage")
    public ModelAndView toAddAdminPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        //查询店铺列表
        List<TbShop> shopList = iShopService.findShopAll();
        //查询角色列表
        List<TbRole> roleList = iRoleService.findRoleAll();

        TbAdmin admin = new TbAdmin();
        admin.setSex("1");
        admin.setStatus(1);
        int roleId = -1;
        if (!StringUtils.isEmpty(request.getParameter("adminId")) && !"0".equals(request.getParameter("adminId"))) {
            int adminId = Integer.parseInt(request.getParameter("adminId"));
            admin = iUserService.finAdminById(adminId);
            TbUserRole userRole = iUserService.loadUserRoleByAdminId(adminId);
            if (null != userRole) {
                modelAndView.addObject("userRoleId", userRole.getRoleId());
            }
        }

        modelAndView.addObject("shopList", shopList);
        modelAndView.addObject("roleList", roleList);
        modelAndView.addObject("roleId", roleId);
        modelAndView.addObject("admin", admin);
        modelAndView.setViewName("add-admin");
        return modelAndView;
    }


    /**
     * 更新用户状态
     *
     * @param request
     * @param response
     * @param adminId
     * @param status
     */
    @PostMapping("updateAdminStatus")
    public void updateAdminStatus(HttpServletRequest request, HttpServletResponse response, Integer adminId, Integer status) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = CommonMethod.packagMsg("1");
            iUserService.updateAdminStatus(adminId, status);
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存用户
     *
     * @param request
     * @param response
     * @param admin
     */
    @PostMapping("saveAdmin")
    public void saveAdmin(HttpServletRequest request, HttpServletResponse response, TbAdmin admin, Integer roleId) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = CommonMethod.packagMsg("1");
            if (!StringUtils.isEmpty(request.getParameter("admin.adminId"))) {
                admin.setAdminId(Integer.parseInt(request.getParameter("admin.adminId")));
            }
            Integer shopId = Integer.parseInt(request.getParameter("admin.shopId"));
            admin.setShopId(shopId);
            String loginName = request.getParameter("admin.loginName");
            admin.setLoginName(loginName);
            Integer memberType = Integer.parseInt(request.getParameter("admin.shopMenberType"));
            admin.setShopMenberType(memberType);
            String userName = request.getParameter("admin.userName");
            admin.setUserName(userName);
            String idcard = request.getParameter("admin.idcard");
            admin.setIdcard(idcard);
            String phone = request.getParameter("admin.phone");
            admin.setPhone(phone);

            String password = request.getParameter("admin.loginPassword");
            String loginPasswdMd5 = MD5.enCodeStandard(loginName + password);//MD5加密的密码
            admin.setLoginPassword(loginPasswdMd5);

            String sDate = request.getParameter("admin.memberBegintime");
            admin.setMemberBegintime(new Timestamp(DatetimeUtil.convertStrToDate(sDate).getTime()));
            String eDate = request.getParameter("admin.memberEndtime");
            admin.setMemberEndtime(new Timestamp(DatetimeUtil.convertStrToDate(eDate).getTime()));

            String remark = request.getParameter("admin.remark");
            admin.setRemark(remark);
            String sex = request.getParameter("admin.sex");
            admin.setSex(sex);
            Integer status = Integer.parseInt(request.getParameter("admin.status"));
            admin.setStatus(status);

            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            admin.setCreateman(user.getLoginName());
            admin.setCreatetime(new Timestamp(new Date().getTime()));
            admin.setIsMakeSure(0);
            if (null != admin.getAdminId() && admin.getAdminId() > 0) {
                //修改
                TbAdmin a = iUserService.finAdminById(admin.getAdminId());
                if (a.getLoginPassword().equals(password)) {
                    admin.setLoginPassword(a.getLoginPassword());
                }
                admin.setCreateman(a.getCreateman());
                admin.setCreatetime(a.getCreatetime());
                admin.setLastaltertime(new Timestamp(new Date().getTime()));
                admin.setLastalterman(user.getLoginName());
            }
            iUserService.saveAdmin(admin, roleId);
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改登录密码
     *
     * @return
     */
    @RequestMapping("updatePasswdPage")
    public ModelAndView updatePasswdPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("update_passwd");
        return modelAndView;
    }


    @RequestMapping("updatePasswd")
    public void updatePasswd(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject out = null;

            TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
            String nowPasswd = user.getLoginPassword();//当前密码
            String loginName = user.getLoginName();

            String oldPasswd = request.getParameter("oldPasswd");
            //判断旧密码是否正确
            String oldPasswdMd5 = MD5.enCodeStandard(loginName + oldPasswd);
            if (!oldPasswdMd5.equals(nowPasswd)) {
                out = CommonMethod.packagMsg("16");
                writer.print(out);
                writer.close();
                return;
            }
            String newPasswd = request.getParameter("newPasswd");
            String newPasswdMd5 = MD5.enCodeStandard(loginName + newPasswd);
            iUserService.updateLoginPasswd(newPasswdMd5, user.getAdminId());
            out = CommonMethod.packagMsg("5");
            writer.print(out);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 用户会员号管理列表(商城)
     *
     * @return
     */
    @RequestMapping("toMemberCodePage")
    public ModelAndView toMemberCodePage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("membercode-list");
        return modelAndView;
    }

    /**
     * 查询用户会员号列表数据(商城)
     */
    @RequestMapping("loadMemberCodeListData")
    public void loadMemberCodeListData(HttpServletRequest request, HttpServletResponse response, TbMemberId memberId) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            PageInfo pageInfo = new PageInfo();
            int pageSize = pageInfo.getPageSize();
            int start = Integer.parseInt(request.getParameter("start"));//第一条数据的起始位置，比如0代表第一条数据
            int currePage = start / pageSize + 1; //当前页
            pageInfo = iUserService.loadMemberIdPage(currePage, memberId);
            //处理数据
            int recordSum = pageInfo.getRecordSum();

            //输出参数
            JSONObject dataJson = new JSONObject();
            dataJson.put("draw", Integer.parseInt(request.getParameter("draw")));
            dataJson.put("recordsTotal", recordSum);
            dataJson.put("recordsFiltered", recordSum);
            dataJson.put("data", pageInfo.getResultListMap());
            writer.print(dataJson);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增加会员号
     *
     * @param request
     * @param response
     */
    @PostMapping("addMemberCode")
    public void addMemberCode(HttpServletRequest request, HttpServletResponse response, TbMemberId memberId) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = CommonMethod.packagMsg("1");
            memberId.setCreateTime(new Timestamp(System.currentTimeMillis()));
            memberId.setMemberType(0);
            // check
            if (iUserService.checkMemberCode(memberId.getMemberId())) {
                result = CommonMethod.packagMsg("24", memberId.getMemberId());
            } else {
                iUserService.saveObj(memberId);
            }
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除会员号
     *
     * @param request
     * @param response
     */
    @PostMapping("delMemberCode")
    public void delMemberCode(HttpServletRequest request, HttpServletResponse response, Integer id) {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            JSONObject result = CommonMethod.packagMsg("1");
            iUserService.deleteMemberCode(id);
            writer.print(result);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
