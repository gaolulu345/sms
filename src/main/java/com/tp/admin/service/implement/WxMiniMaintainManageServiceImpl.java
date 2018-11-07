package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminMaintionEmployeeDao;
import com.tp.admin.dao.AdminMaintionEmployeeLogTerOperatingDao;
import com.tp.admin.dao.TerDao;
import com.tp.admin.data.dto.AdminMaintionEmployeeLogTerOperatingDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.entity.AdminMaintionEmployeeLogTerOperating;
import com.tp.admin.data.parameter.WxMiniAuthDTO;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WxMiniMaintainAuthServiceI;
import com.tp.admin.service.WxMiniMaintainManageServiceI;
import org.apache.commons.lang.StringUtils;
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
    AdminMaintionEmployeeDao adminMaintionEmployeeDao;

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TerDao terDao;

    @Autowired
    AdminMaintionEmployeeLogTerOperatingDao adminMaintionEmployeeLogTerOperatingDao;

    @Autowired
    WxMiniMaintainAuthServiceI wxMiniMaintainAuthService;

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
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getCityCode()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty cityCode");
        }
        wxMiniSearch.builData();
        List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniSearch);
        if (null != results && !results.isEmpty()) {
            for (TerInfoDTO t : results){
                t.build();
            }
            int cnt = terDao.cntTerInfoSearch(wxMiniSearch);
            wxMiniSearch.setTotalCnt(cnt);
        }else {
            wxMiniSearch.setTotalCnt(0);
        }
        wxMiniSearch.setResult(results);
        return ApiResult.ok(new ResultTable(wxMiniSearch));
    }

    @Override
    public ApiResult siteInfo(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniSearch);
        TerInfoDTO dto = null;
        if (null != results && !results.isEmpty()) {
            dto = results.get(0);
            dto.build();
        }else{
            throw new BaseException(ExceptionCode.NOT_TER);
        }
        return ApiResult.ok(dto);
    }

    @Override
    public ApiResult siteOnline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        AdminMaintionEmployee adminMaintionEmployee = wxMiniMaintainAuthService.check(wxMiniSearch.getOpenId());
        List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniSearch);
        TerInfoDTO dto = null;
        if (null != results && !results.isEmpty()) {
            dto = results.get(0);
            dto.build();
        }else {
            throw new BaseException(ExceptionCode.NOT_TER);
        }
        if (dto.isOnline()) {
            throw new BaseException(ExceptionCode.REPEAT_OPERATION , "该网点已经上线");
        }
        int res = terDao.updateOnline(wxMiniSearch.getTerId());
        if (res == 0) {
            buildTerOperationLog(dto , adminMaintionEmployee , WashTerOperatingLogTypeEnum.ONLINE,false);
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        buildTerOperationLog(dto , adminMaintionEmployee , WashTerOperatingLogTypeEnum.ONLINE,true);
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteOffline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId() || StringUtils.isBlank(wxMiniSearch.getMsg()) ) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminMaintionEmployee adminMaintionEmployee = wxMiniMaintainAuthService.check(wxMiniSearch.getOpenId());
        List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniSearch);
        TerInfoDTO dto = null;
        if (null != results && !results.isEmpty()) {
            dto = results.get(0);
            dto.build();
        }
        if (null == dto) {
            throw new BaseException(ExceptionCode.NOT_TER);
        }
        if (!dto.isOnline()) {
            throw new BaseException(ExceptionCode.REPEAT_OPERATION , "该网点已经下线");
        }
        int res = terDao.updateOffline(wxMiniSearch.getTerId(),wxMiniSearch.getMsg());
        if (res == 0) {
            buildTerOperationLog(dto , adminMaintionEmployee , WashTerOperatingLogTypeEnum.NOT_ONLINE,false);
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        buildTerOperationLog(dto , adminMaintionEmployee , WashTerOperatingLogTypeEnum.NOT_ONLINE,true);
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteDeviceReset(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);


        return ApiResult.ok(body);
    }

    @Override
    public ApiResult siteOperationLog(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        wxMiniSearch.builData();
        wxMiniMaintainAuthService.check(wxMiniSearch.getOpenId());
        List<AdminMaintionEmployeeLogTerOperatingDTO> results = adminMaintionEmployeeLogTerOperatingDao.listBySearch
                (wxMiniSearch);
        if (null != results && !results.isEmpty()) {
            for (AdminMaintionEmployeeLogTerOperatingDTO dto : results){
                dto.build();
            }
            int cnt = adminMaintionEmployeeLogTerOperatingDao.cntBySearch(wxMiniSearch);
            wxMiniSearch.setTotalCnt(cnt);
        }else {
            wxMiniSearch.setTotalCnt(0);
        }
        return ApiResult.ok(results);
    }

    @Override
    public void buildTerOperationLog(TerInfoDTO terInfoDTO ,
                                     AdminMaintionEmployee adminMaintionEmployee,
                                     WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum ,
                                     Boolean sucess
                                                      ) {

        String intros = adminMaintionEmployee.getName() + " 操作 " + terInfoDTO.getTitle() + washTerOperatingLogTypeEnum
                .getDesc();
        AdminMaintionEmployeeLogTerOperating adminMaintionEmployeeLogTerOperating = new
                AdminMaintionEmployeeLogTerOperating();
        adminMaintionEmployeeLogTerOperating.setEmployeeId(adminMaintionEmployee.getId());
        adminMaintionEmployeeLogTerOperating.setUsername(adminMaintionEmployee.getName());
        adminMaintionEmployeeLogTerOperating.setTerId(terInfoDTO.getId());
        adminMaintionEmployeeLogTerOperating.setTitle(washTerOperatingLogTypeEnum.getDesc());
        adminMaintionEmployeeLogTerOperating.setIntros(intros);
        adminMaintionEmployeeLogTerOperating.setType(washTerOperatingLogTypeEnum.getValue());
        adminMaintionEmployeeLogTerOperating.setSucess(sucess);
        int res = adminMaintionEmployeeLogTerOperatingDao.insert(adminMaintionEmployeeLogTerOperating);
        if (res == 0) {
            log.error("维保人员操作日志存储失败 {} " + adminMaintionEmployeeLogTerOperating.toString());
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }
}
