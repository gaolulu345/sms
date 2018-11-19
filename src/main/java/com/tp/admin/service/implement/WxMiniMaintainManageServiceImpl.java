package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.ajax.ResultCode;
import com.tp.admin.common.Constant;
import com.tp.admin.config.TpProperties;
import com.tp.admin.dao.*;
import com.tp.admin.data.dto.AdminTerOperatingLogDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.entity.AdminMaintionEmployeeLogTerOperating;
import com.tp.admin.data.entity.AdminTerOperatingLog;
import com.tp.admin.data.entity.TerLog;
import com.tp.admin.data.parameter.WxMiniAuthDTO;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.data.wash.WashSiteRequest;
import com.tp.admin.enums.AdminTerOperatingLogSourceEnum;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import com.tp.admin.enums.WashTerStateEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WashSiteServiceI;
import com.tp.admin.service.WxMiniAuthServiceI;
import com.tp.admin.service.WxMiniMaintainManageServiceI;
import com.tp.admin.utils.SecurityUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.sql.Timestamp;
import java.util.List;

@Service
public class WxMiniMaintainManageServiceImpl implements WxMiniMaintainManageServiceI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    AdminMaintionEmployeeDao adminMaintionEmployeeDao;

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TerLogDao terLogDao;

    @Autowired
    TerDao terDao;

    @Autowired
    AdminTerOperatingLogDao adminTerOperatingLogDao;

    @Autowired
    WxMiniAuthServiceI wxMiniMaintainAuthService;

    @Autowired
    WashSiteServiceI washSiteService;

    @Autowired
    TpProperties tpProperties;

    @Override
    public ApiResult region(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniAuthDTO wxMiniAuthDTO = new Gson().fromJson(body, WxMiniAuthDTO.class);
        if (StringUtils.isBlank(wxMiniAuthDTO.getOpenId())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
        }
        check(wxMiniAuthDTO.getOpenId());
        List<Integer> cityIds = terDao.listTerCityId();
        return ApiResult.ok(cityIds);
    }

    @Override
    public ApiResult siteListSearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getCityCode()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty cityCode");
        }
        check(wxMiniSearch.getOpenId());
        wxMiniSearch.builData();
        List<TerInfoDTO> results = terDao.terInfoSearch(wxMiniSearch);
        if (null != results && !results.isEmpty()) {
            for (TerInfoDTO t : results) {
                t.build();
            }
            int cnt = terDao.cntTerInfoSearch(wxMiniSearch);
            wxMiniSearch.setTotalCnt(cnt);
        } else {
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
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        return ApiResult.ok(dto);
    }

    @Override
    public ApiResult siteOnline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        check(wxMiniSearch.getOpenId());
        AdminMaintionEmployee adminMaintionEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        WashSiteRequest washSiteRequest = httpHelper.signInfo(wxMiniSearch.getTerId(), "", "");
        String jsonBody = new Gson().toJson(washSiteRequest);
        String result = httpHelper.sendPostByJsonData(tpProperties.getWashManageServer() + Constant.RemoteTer
                        .SITE_ONLINE,jsonBody);
        return buildApiResult(result,dto,adminMaintionEmployee,WashTerOperatingLogTypeEnum.ONLINE);
    }

    @Override
    public ApiResult siteOffline(HttpServletRequest request, String body) {
//        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId() || StringUtils.isBlank(wxMiniSearch.getMsg())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        check(wxMiniSearch.getOpenId());
        AdminMaintionEmployee adminMaintionEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        WashSiteRequest washSiteRequest = httpHelper.signInfo(wxMiniSearch.getTerId(), "", "");
        String jsonBody = new Gson().toJson(washSiteRequest);
        String result = httpHelper.sendPostByJsonData(tpProperties.getWashManageServer() + Constant.RemoteTer
                        .SITE_OFFLINE,jsonBody);
        return buildApiResult(result,dto,adminMaintionEmployee,WashTerOperatingLogTypeEnum.NOT_ONLINE);
    }

    @Override
    public ApiResult siteDeviceReset(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        check(wxMiniSearch.getOpenId());
        AdminMaintionEmployee adminMaintionEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        WashSiteRequest washSiteRequest = httpHelper.signInfo(wxMiniSearch.getTerId(), "", "");
        String jsonBody = new Gson().toJson(washSiteRequest);
        String result = httpHelper.sendPostByJsonData(tpProperties.getWashManageServer() + Constant.RemoteTer
                .SITE_RESET, jsonBody);
        return buildApiResult(result,dto,adminMaintionEmployee,WashTerOperatingLogTypeEnum.TER_RESET);
    }

    @Override
    public ApiResult siteStatusReset(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        AdminMaintionEmployee adminMaintionEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        WashSiteRequest washSiteRequest = httpHelper.signInfo(wxMiniSearch.getTerId(), "", "");
        String jsonBody = new Gson().toJson(washSiteRequest);
        String result = httpHelper.sendPostByJsonData(tpProperties.getWashManageServer() + Constant.RemoteTer
                .SITE_STATUS_RESET, jsonBody);
        return buildApiResult(result,dto,adminMaintionEmployee,WashTerOperatingLogTypeEnum.TER_RESET_STATE);
    }

    @Override
    public ApiResult siteOperationLog(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        wxMiniSearch.builData();
        check(wxMiniSearch.getOpenId());
        return washSiteService.siteOperationLog(wxMiniSearch);
    }

    @Override
    public AdminMaintionEmployee check(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
        }
        AdminMaintionEmployee adminMaintionEmployee = adminMaintionEmployeeDao.findByWxMiniId(openId);
        if (null == adminMaintionEmployee) {
            throw new BaseException(ExceptionCode.NO_THIS_USER);
        }
        if (null == adminMaintionEmployee) {
            throw new BaseException(ExceptionCode.NO_THIS_USER);
        }
        if (!adminMaintionEmployee.isEnable()) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        if (adminMaintionEmployee.isDeleted()) {
            throw new BaseException(ExceptionCode.USER_DELETE_REGISTERED);
        }
        return adminMaintionEmployee;
    }

    @Override
    public void buildTerOperationLog(TerInfoDTO terInfoDTO,
                                     AdminMaintionEmployee adminMaintionEmployee,
                                     WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum,
                                     Boolean sucess
    ) {
        String intros = adminMaintionEmployee.getName() + " 操作 " + terInfoDTO.getTitle() + washTerOperatingLogTypeEnum
                .getDesc();
        AdminTerOperatingLog adminTerOperatingLog = new
                AdminTerOperatingLog();
        adminTerOperatingLog.setMaintionId(adminMaintionEmployee.getId());
        adminTerOperatingLog.setUsername(adminMaintionEmployee.getName());
        adminTerOperatingLog.setTerId(terInfoDTO.getId());
        adminTerOperatingLog.setTitle(washTerOperatingLogTypeEnum.getDesc());
        adminTerOperatingLog.setIntros(intros);
        adminTerOperatingLog.setType(washTerOperatingLogTypeEnum.getValue());
        adminTerOperatingLog.setOpSource(AdminTerOperatingLogSourceEnum.MAINTAUN.getValue());
        adminTerOperatingLog.setSucess(sucess);
        int res = adminTerOperatingLogDao.insert(adminTerOperatingLog);
        if (res == 0) {
            log.error("维保人员操作日志存储失败 {} " + adminTerOperatingLog.toString());
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }

    private final ApiResult buildApiResult(String result, TerInfoDTO dto, AdminMaintionEmployee
            adminMaintionEmployee, WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum
    ) {
        ApiResult apiResult = null;
        try {
            apiResult = new Gson().fromJson(result, ApiResult.class);
            if (null == apiResult) {
                throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
            }
            if (apiResult.getCode() == ResultCode.SUCCESS.getCode()) {
                buildTerOperationLog(dto, adminMaintionEmployee, washTerOperatingLogTypeEnum, true);
            } else {
                buildTerOperationLog(dto, adminMaintionEmployee, washTerOperatingLogTypeEnum, false);
            }
        } catch (JsonSyntaxException ex) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        return apiResult;
    }
}
