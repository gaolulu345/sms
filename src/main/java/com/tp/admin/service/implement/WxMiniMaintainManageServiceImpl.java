package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.TerDao;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.parameter.WxMiniAuthDTO;
import com.tp.admin.data.parameter.WxMiniTerSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WxMiniMaintainManageServiceI;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class WxMiniMaintainManageServiceImpl implements WxMiniMaintainManageServiceI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TerDao terDao;

    @Override
    public ApiResult region(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniAuthDTO wxMiniAuthDTO = new Gson().fromJson(body, WxMiniAuthDTO.class);
        if (StringUtils.isBlank(wxMiniAuthDTO.getOpenId())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty openId");
        }
        List<Integer> cityIds = terDao.listTerCityId();
        return ApiResult.ok(cityIds);
    }

    @Override
    public ApiResult siteListSearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniTerSearch wxMiniTerSearch = new Gson().fromJson(body, WxMiniTerSearch.class);
        if (null == wxMiniTerSearch.getCityCode()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty cityCode");
        }
        List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniTerSearch);
        if (null != results && !results.isEmpty()) {
            for (TerInfoDTO t : results){
                t.build();
            }
            int cnt = terDao.cntTerInfoSearch(wxMiniTerSearch);
            wxMiniTerSearch.setTotalCnt(cnt);
        }else {
            wxMiniTerSearch.setTotalCnt(0);
        }
        wxMiniTerSearch.setResult(results);
        return ApiResult.ok(new ResultTable(wxMiniTerSearch));
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
