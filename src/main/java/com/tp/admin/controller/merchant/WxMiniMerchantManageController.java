package com.tp.admin.controller.merchant;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.service.WxMiniMerchantManageServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(WxMiniMerchantManageController.ROUTER_INDEX)
public class WxMiniMerchantManageController {

    public static final String ROUTER_INDEX = "/api/open/wx/mini/merchant/manage";

    @Autowired
    WxMiniMerchantManageServiceI wxMiniMerchantManageService;

    /**
     * 站点金额统计
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/money/total")
    public ApiResult moneyTotal(HttpServletRequest request) {
        return wxMiniMerchantManageService.moneyTotal(request);
    }

    /**
     * 站点列表查询
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/site/list")
    public ApiResult siteListSearch(HttpServletRequest request) {
        return wxMiniMerchantManageService.siteListSearch(request);
    }

    /**
     * 站点信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/site/info")
    public ApiResult siteInfo(HttpServletRequest request) {
        return wxMiniMerchantManageService.siteInfo(request);
    }

    /**
     * 站点洗车机运行状态
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/site/status")
    public ApiResult siteStatus(HttpServletRequest request) {
        return wxMiniMerchantManageService.siteStatus(request);
    }

    /**
     * 站点上线
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/site/online")
    public ApiResult siteOnline(HttpServletRequest request) {
        return wxMiniMerchantManageService.siteOnline(request);
    }

    /**
     * 站点下线
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/site/offline")
    public ApiResult siteOffline(HttpServletRequest request, @RequestBody String body) {
        return wxMiniMerchantManageService.siteOffline(request, body);
    }

    /**
     * 网点设备复位
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/site/device/reset")
    public ApiResult siteDeviceReset(HttpServletRequest request, @RequestBody String body) {
        return wxMiniMerchantManageService.siteDeviceReset(request, body);
    }

    /**
     * 网点设备状态重置
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/site/status/reset")
    public ApiResult siteStatusReset(HttpServletRequest request) {
        return wxMiniMerchantManageService.siteStatusReset(request);
    }

    /**
     * 订单列表查询
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/site/order/list")
    public ApiResult orderListSearch(HttpServletRequest request) {
        return wxMiniMerchantManageService.orderListSearch(request);
    }

    /**
     * 站点操作日志
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/site/operation/log/list")
    public ApiResult siteOperationLog(HttpServletRequest request) {
        return wxMiniMerchantManageService.siteOperationLog(request);
    }


}
