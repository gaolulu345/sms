package com.tp.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping
public class IndexController {

    /**
     * 首页
     * @return
     */
    @GetMapping(value = {"","/pages/index"})
    public String index(HttpServletRequest request , Model model) {
        return "index";
    }

    /**
     * 登录页面
     * @return
     */
    @GetMapping(value = {"/login"})
    public String login(HttpServletRequest request, Model model ) {
        return "login";
    }

    /**
     * 系统错误
     * @return
     */
    @GetMapping(value = "/error")
    public String error() { return "error"; }

    /**
     * 用户
     * @return
     */
    @GetMapping(value = {"/pages/user"})
    public String user(HttpServletRequest request, Model model ) {
        return "user";
    }

    /**
     * 订单列表
     * @return
     */
    @RequestMapping(value = {"/pages/order"})
    public String order(HttpServletRequest request, Model model ) {
        return "order";
    }

    /**
     * 退款
     * @return
     */
    @GetMapping(value = {"/pages/refund"})
    public String refund(HttpServletRequest request, Model model ) {
        return "refund";
    }

    /**
     * 上传
     * @return
     */
    @GetMapping(value = {"/pages/upload"})
    public String upload(HttpServletRequest request, Model model ) {
        return "upload";
    }

    /**
     * 系统管理
     * @return
     */
    @GetMapping(value = "/pages/system")
    public String system(HttpServletRequest request, Model model ) {
        return "system";
    }

    /**
     * 角色管理
     * @param request
     * @return
     */
    @GetMapping(value = "/pages/roles")
    public String roles(HttpServletRequest request, Model model ) {
        return "roles";
    }

    /**
     * 权限
     * @param request
     * @return
     */
    @GetMapping(value = "/pages/permission")
    public String permission(HttpServletRequest request, Model model ) {
        return "permission";
    }

    /**
     * 员工
     * @param request
     * @return
     */
    @GetMapping(value = "/pages/admin")
    public String admin(HttpServletRequest request, Model model ) {
        return "admin";
    }

}
