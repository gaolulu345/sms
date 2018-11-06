package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WxMiniMaintainManageServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class WxMiniMaintainManageServiceImpl implements WxMiniMaintainManageServiceI {

    @Autowired
    HttpHelperI httpHelper;

    @Override
    public ApiResult region(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult siteListSearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult siteInfo(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult siteOnline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult siteOffline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult siteDeviceReset(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult siteOperationLog(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }
}
