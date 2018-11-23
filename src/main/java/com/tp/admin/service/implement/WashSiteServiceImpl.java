package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminTerOperatingLogDao;
import com.tp.admin.dao.TerDao;
import com.tp.admin.data.dto.AdminTerOperatingLogDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.service.WashSiteServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WashSiteServiceImpl implements WashSiteServiceI {

    @Autowired
    TerDao terDao;

    @Autowired
    AdminTerOperatingLogDao adminTerOperatingLogDao;

    @Override
    public ApiResult siteOperationLog(WxMiniSearch wxMiniSearch) {
        List<AdminTerOperatingLogDTO> results = adminTerOperatingLogDao.listLogBySearch(wxMiniSearch);
        if (null != results && !results.isEmpty()) {
            for (AdminTerOperatingLogDTO dto : results){
                dto.build();
            }
            int cnt = adminTerOperatingLogDao.cntBySearch(wxMiniSearch);
            wxMiniSearch.setResult(results);
            wxMiniSearch.setTotalCnt(cnt);
        }else {
            wxMiniSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(wxMiniSearch));
    }

    @Override
    public TerInfoDTO terCheck(WxMiniSearch wxMiniSearch) {
        if (null == wxMiniSearch) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        wxMiniSearch.builData();
        List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniSearch);
        TerInfoDTO dto = null;
        if (null != results && !results.isEmpty()) {
            dto = results.get(0);
            dto.build();
        }else {
            throw new BaseException(ExceptionCode.NOT_TER);
        }
        return dto;
    }
}
