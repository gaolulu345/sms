package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WashDeviceLogServiceI;
import com.tp.admin.service.WashSiteServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class WashDeviceLogServiceImpl implements WashDeviceLogServiceI {

    @Autowired
    WashSiteServiceI washSiteService;

    @Autowired
    HttpHelperI httpHelper;

    @Override
    public ApiResult deviceOperationLog(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        wxMiniSearch.builData();
        return washSiteService.siteOperationLog(wxMiniSearch);
    }
}
