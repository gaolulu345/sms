package com.sms.admin.controller;

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
     * 洗车用户
     * @return
     */
    @GetMapping(value = {"/pages/user"})
    public String user(HttpServletRequest request, Model model ) {
        return "user";
    }

    /**
     * 洗车订单列表
     * @return
     */
    @RequestMapping(value = {"/pages/order"})
    public String order(HttpServletRequest request, Model model ) {
        return "order";
    }

    /**
     * 维保人员
     * @return
     */
    @GetMapping(value = {"/pages/maintion/employee"})
    public String maintionEmployee(HttpServletRequest request, Model model ) {
        return "maintion_employee";
    }

    /**
     * 商户人员
     * @return
     */
    @GetMapping(value = {"/pages/merchant/employee"})
    public String merchantEmployee(HttpServletRequest request, Model model ) {
        return "merchant_employee";
    }

    /**
     * 洗车退款
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
     * 设备操作日志
     * @param request
     * @return
     */
    @GetMapping(value = "/pages/device/log")
    public String deviceLog(HttpServletRequest request, Model model ) {
        return "device_log";
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

    /**
     * 设备
     * @param request
     * @param model
     * @return
     */
    @GetMapping(value = "/pages/device")
    public String device(HttpServletRequest request,Model model){
        return "device";
    };

    /**
     * 设备详情
     * @param request
     * @param model
     * @return
     */
    @GetMapping(value = "/pages/detail")
    public String deviceInfo(HttpServletRequest request,Model model){
        return "device_detail";
    };

    /**
     * 设备广告
     * @param request
     * @param model
     * @return
     */
    @GetMapping(value = "/pages/advertising")
    public String deviceAdvertising(HttpServletRequest request,Model model){
        return "device_advertising";
    };
}
