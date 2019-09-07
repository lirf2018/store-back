package com.yufan.controller;

import com.alibaba.fastjson.JSONObject;
import com.yufan.pojo.TbAdmin;
import com.yufan.pojo.TbFunctions;
import com.yufan.service.func.IMenuService;
import com.yufan.service.user.IUserService;
import com.yufan.utils.CommonMethod;
import com.yufan.utils.Constants;
import com.yufan.utils.DatetimeUtil;
import com.yufan.utils.MD5;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2019/3/21 17:35
 * 功能介绍: 登录
 */
@Controller
@RequestMapping(value = "/login/")
public class LoginController {

    private Logger LOG = Logger.getLogger(LoginController.class);

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IMenuService iMenuService;

    /**
     * 跳转到登录页面
     *
     * @return
     */
    @RequestMapping(value = "userLoginPage")
    public String toLoginPage() {
        return "login";
    }


    /**
     * 登录检验
     *
     * @param request
     * @param response
     */
    @PostMapping(value = "checkLogin")
    public void checkLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            JSONObject obj = new JSONObject();
            String loginName = request.getParameter("loginName").trim();
            String loginPasswd = request.getParameter("loginPasswd").trim();

            //查询登录用户
            TbAdmin admin = iUserService.findByUserLoginName(loginName);
            //如果是系统管理员,则密码固定规则(密码-实时时间)
            /*if ("admin".equals(loginName)) {
                if (loginPasswd.length() < 13) {
                    obj = CommonMethod.packagMsg("10");
                    writer.print(obj);
                    writer.close();
                    return;
                }
                String sysAdminPasswd = Constants.ADMINPASSWORD + DatetimeUtil.getNow("yyyyMMddHHmm");
                String psMD5 = MD5.enCodeStandard("admin" + loginPasswd.substring(0, loginPasswd.length() - 11)) + loginPasswd.substring(loginPasswd.length() - 12, loginPasswd.length());
                if (!sysAdminPasswd.equals(psMD5)) {
                    obj = CommonMethod.packagMsg("10");
                    writer.print(obj);
                    writer.close();
                    return;
                }
            } else {
                String loginPasswdMd5 = MD5.enCodeStandard(loginName + loginPasswd);
                if (null == admin || !loginPasswdMd5.equals(admin.getLoginPassword())) {
                    obj = CommonMethod.packagMsg("10");
                    writer.print(obj);
                    writer.close();
                    return;
                }
            }*/
            String loginPasswdMd5 = MD5.enCodeStandard(loginName + loginPasswd);
            if (null == admin || !loginPasswdMd5.equals(admin.getLoginPassword())) {
                obj = CommonMethod.packagMsg("10");
                writer.print(obj);
                writer.close();
                return;
            }

            //测试
            //TbAdmin admin = iUserService.findByUserLoginName("admin");

            request.getSession().setAttribute("user", admin);

            obj = CommonMethod.packagMsg("9");
            writer.print(obj);
            writer.close();
            return;
        } catch (Exception e) {
            LOG.info("登录异常", e);
        }
    }

    /**
     * 加载用户菜单
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping(value = "indexPage")
    public ModelAndView indexPage(HttpServletRequest request, HttpServletResponse response) {

        TbAdmin user = (TbAdmin) request.getSession().getAttribute("user");
        //查询用户菜单
        List<TbFunctions> privFunctions = iMenuService.listUserPrivFunction(user.getAdminId());
        //处理菜单
        List<TbFunctions> funParent = new ArrayList<>();
        List<TbFunctions> funPSun = new ArrayList<>();
        for (int i = 0; i < privFunctions.size(); i++) {
            int funType = privFunctions.get(i).getFunctionParentid();
            int status = privFunctions.get(i).getStatus();
            if (status == 0) {
                continue;
            }
            if (funType == 0) {
                funParent.add(privFunctions.get(i));
            } else {
                funPSun.add(privFunctions.get(i));
            }
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("funParent", funParent);
        modelAndView.addObject("funPSun", funPSun);
        modelAndView.setViewName("index");

        return modelAndView;
    }

    /**
     * 退出
     *
     * @param request
     * @param response
     */
    @GetMapping(value = "userExit")
    public void userExit(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login/userLoginPage");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "welcomePage")
    public ModelAndView welcomePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("welcome-page");
        return modelAndView;
    }

    public static void main(String[] args) {
        String str = "admin123456";
        System.out.printf(MD5.enCodeStandard(str));
    }


}
