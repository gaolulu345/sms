package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
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
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        return ApiResult.ok(dto);
    }

    @Override
    public ApiResult siteOnline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        AdminMaintionEmployee adminMaintionEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
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
        AdminMaintionEmployee adminMaintionEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
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
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        return ApiResult.error(ExceptionCode.NOT_PERMISSION_ERROR);
    }

    @Override
    public ApiResult siteStatusReset(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        AdminMaintionEmployee adminMaintionEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        if (StringUtils.isBlank(dto.getCode())) {
            throw new BaseException(ExceptionCode.NOT_TER);
        }
        if (dto.getStatus() != WashTerStateEnum.PAUSED.ordinal() && dto.getStatus() != WashTerStateEnum.ERROR.ordinal()) {
            TerLog terLog = terLogDao.latestRecordByCode(dto.getCode());
            if (null != terLog) {
                Long latestRecordTime = terLog.getCreateTime().getTime();
                Long currentTime = System.currentTimeMillis();
                long time = currentTime - latestRecordTime;
                log.info("latestRecordTime {} , currentTime {} time {}" , latestRecordTime , currentTime , time);
                if (time <= 1200000) {
                    throw new BaseException(ExceptionCode.TER_STATUS_RESERT_ERROR, "该网点不需要重置状态,若执意操作。请直接联系软件部相关人员");
                }
                if (dto.getStatus() == WashTerStateEnum.DEFAULT.getValue()) {
                    throw new BaseException(ExceptionCode.TER_STATUS_RESERT_ERROR, "该网点状态已经被重置");
                }
            }
        }
        int res = terDao.updateStateDefault(wxMiniSearch.getTerId());
        if (res == 0) {
            buildTerOperationLog(dto , adminMaintionEmployee , WashTerOperatingLogTypeEnum.TER_RESET_STATE,false);
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        buildTerOperationLog(dto , adminMaintionEmployee , WashTerOperatingLogTypeEnum.TER_RESET_STATE,true);
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteOperationLog(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        wxMiniSearch.builData();
        check(wxMiniSearch.getOpenId());
        return washSiteService.siteOperationLog(wxMiniSearch);
    }

    @Override
    public AdminMaintionEmployee check(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty openId");
        }
        AdminMaintionEmployee adminMaintionEmployee = adminMaintionEmployeeDao.findByWxMiniId(openId);
        if (null == adminMaintionEmployee) {
            throw new BaseException(ExceptionCode.NO_THIS_USER);
        }
        return adminMaintionEmployee;
    }

    @Override
    public void buildTerOperationLog(TerInfoDTO terInfoDTO ,
                                     AdminMaintionEmployee adminMaintionEmployee,
                                     WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum ,
                                     Boolean sucess
                                                      ) {
        String intros = adminMaintionEmployee.getName() + " 操作 " + terInfoDTO.getTitle() + washTerOperatingLogTypeEnum
                .getDesc();
        AdminTerOperatingLog adminTerOperatingLog = new
                AdminTerOperatingLog();
        adminTerOperatingLog.setEmployeeId(adminMaintionEmployee.getId());
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

    private final JsonObject signOrderInfo(int terId, String orderId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", terId);
        jsonObject.addProperty("key", "TPAuto@2015");
        try {
            int order = Integer.valueOf(orderId);
            jsonObject.addProperty("orderId", order);
        } catch (NumberFormatException e) {
            jsonObject.addProperty("orderId", orderId);
        }
        jsonObject.addProperty("time", new Timestamp(System.currentTimeMillis()).toString());
        String sign = new String();
        try {
            sign = SecurityUtil.RsaUtil.sign(jsonObject.toString(), Constant.RemoteTer.PRIVATE_KEY, true);
        } catch (Exception e) {
            throw new BaseException(ExceptionCode.SIGN_FAILURE_FOR_REMOTE_TER);
        }
        if (StringUtils.isEmpty(sign) || sign.length() < 2) {
            throw new BaseException(ExceptionCode.SIGN_ERROR_FOR_REMOTE_TER);
        }
        jsonObject.addProperty("sign", sign);
        return jsonObject;
    }
}
