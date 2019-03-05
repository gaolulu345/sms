package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.TerFaultOperationDao;
import com.tp.admin.data.entity.TerFaultInfo;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.TerFaultReportLogServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class TerFaultReportLogServiceImpl implements TerFaultReportLogServiceI {

    @Autowired
    TerFaultOperationDao terFaultOperationDao;
    @Autowired
    HttpHelperI helper;

    @Override
    public ApiResult terFaultReportList(HttpServletRequest request) {
        String body = helper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        wxMiniSearch.builData();
        List<TerFaultInfo> list = terFaultOperationDao.terFaultReportList(wxMiniSearch);
        if (null != list && !list.isEmpty()) {
            int cnt = terFaultOperationDao.cntTerFaultReportList(wxMiniSearch);
            wxMiniSearch.setResult(list);
            wxMiniSearch.setTotalCnt(cnt);
        }else {
            wxMiniSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(wxMiniSearch));
    }
}
