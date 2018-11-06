package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信小程序-维保授权
 */
public interface WxMiniMaintainAuthServiceI {

    /**
     * 微信授权
     * @param request
     * @return
     */
    ApiResult auth(HttpServletRequest request);

    /**
     * 授权登录
     * @param request
     * @return
     */
    ApiResult login(HttpServletRequest request);

    /**
     * 登录注册
     * @param request
     * @return
     */
    ApiResult register(HttpServletRequest request);

    /**
     * 注册检查
     * @param request
     * @return
     */
    ApiResult registerCheck(HttpServletRequest request);

}
