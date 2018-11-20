package com.tp.admin.controller.maintain;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.service.WxMiniMaintainManageServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信小程序-维保管理
 */
@RestController
@RequestMapping(WxMiniMaintainManageController.ROUTER_INDEX)
public class WxMiniMaintainManageController {

    public static final String ROUTER_INDEX = "/api/open/wx/mini/maintain/manage";

    @Autowired
    WxMiniMaintainManageServiceI wxMiniMaintainManageService;

    /**
     * 区域（地市级）
     * @param request
     * @return
     */
    @PostMapping(value = "/region")
    public ApiResult region(HttpServletRequest request){
        return wxMiniMaintainManageService.region(request);
    }

    /**
     * 地区站点列表
     * @param request
     * @return
     */
    @PostMapping(value = "/site/list")
    public ApiResult siteListSearch(HttpServletRequest request){
        return wxMiniMaintainManageService.siteListSearch(request);
    }

    /**
     * 站点信息
     * @param request
     * @return
     */
    @PostMapping(value = "/site/info")
    public ApiResult siteInfo(HttpServletRequest request){
        return wxMiniMaintainManageService.siteInfo(request);
    }

    /**
     * 站点上线
     * @param request
     * @return
     */
    @PostMapping(value = "/site/online")
    public  ApiResult siteOnline(HttpServletRequest request){
        return wxMiniMaintainManageService.siteOnline(request);
    }

    /**
     * 站点下线
     * @param request
     * @return
     */
    @PostMapping(value = "/site/offline")
    public ApiResult siteOffline(HttpServletRequest request , @RequestBody String body){
        return wxMiniMaintainManageService.siteOffline(request , body);
    }

    /**
     * 网点设备复位
     * @param request
     * @return
     */
    @PostMapping(value = "/site/device/reset")
    public ApiResult siteDeviceReset(HttpServletRequest request){
        return wxMiniMaintainManageService.siteDeviceReset(request);
    }

    /**
     * 网点状态重置
     * @param request
     * @return
     */
    @PostMapping(value = "/site/status/reset")
    public ApiResult siteStatusReset(HttpServletRequest request){
        return wxMiniMaintainManageService.siteStatusReset(request);
    }

    /**
     * 站点操作日志
     * @param request
     * @return
     */
    @PostMapping(value = "/site/operation/log/list")
    public ApiResult siteOperationLog(HttpServletRequest request){
        return wxMiniMaintainManageService.siteOperationLog(request);
    }

    /**
     * 上传站点图片
     * @param request
     * @param file
     * @return
     */
    @PostMapping(value = "/site/upload/reset/photo")
    public ApiResult uploadSitePhoto(HttpServletRequest request, @RequestPart("file") MultipartFile file ,
                                     @RequestParam(value = "openId") String
            openId) {
        return wxMiniMaintainManageService.uploadSitePhoto(request, file , openId);
    }


}
