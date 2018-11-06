package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.service.WxMiniMaintainManageServiceI;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class WxMiniMaintainManageServiceImpl implements WxMiniMaintainManageServiceI {

    @Override
    public ApiResult region(HttpServletRequest request) {
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteListSearch(HttpServletRequest request) {
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteInfo(HttpServletRequest request) {
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteOnline(HttpServletRequest request) {
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteOffline(HttpServletRequest request) {
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteDeviceReset(HttpServletRequest request) {
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteOperationLog(HttpServletRequest request) {
        return ApiResult.ok();
    }
}
