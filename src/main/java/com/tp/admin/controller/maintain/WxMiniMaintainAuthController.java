package com.tp.admin.controller.maintain;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.service.WxMiniAuthServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信小程序-维保授权接口
 */
@RestController
@RequestMapping(WxMiniMaintainAuthController.ROUTER_INDEX)
public class WxMiniMaintainAuthController {

    public static final String ROUTER_INDEX = "/api/open/wx/mini/maintain";

    @Autowired
    @Qualifier(value = "wxMiniMaintainAuthService")
    WxMiniAuthServiceI wxMiniMaintainAuthService;

    /**
     * 微信授权
     * @param request
     * @return
     */
    @PostMapping(value = "/auth")
    public ApiResult auth(HttpServletRequest request){
        return wxMiniMaintainAuthService.auth(request );
    }

    /**
     * 授权登录
     * @param request
     * @return
     */
    @PostMapping(value = "/login")
    public ApiResult login(HttpServletRequest request){
        return wxMiniMaintainAuthService.login(request);
    }

    /**
     * 登录注册
     * @param request
     * @return
     */
    @PostMapping(value = "/register")
    public ApiResult register(HttpServletRequest request , @RequestBody String body){
        return wxMiniMaintainAuthService.register(request , body);
    }

    /**
     * 注册检查
     * @param request
     * @return
     */
    @PostMapping(value = "/register/check")
    public ApiResult registerCheck(HttpServletRequest request){
        return wxMiniMaintainAuthService.registerCheck(request);
    }


}
