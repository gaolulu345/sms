package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminMerchantEmployeeDao;
import com.tp.admin.dao.AdminTerOperatingLogDao;
import com.tp.admin.dao.TerDao;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.entity.AdminMerchantEmployee;
import com.tp.admin.data.entity.AdminTerOperatingLog;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.enums.AdminTerOperatingLogSourceEnum;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WashSiteServiceI;
import com.tp.admin.service.WxMiniMerchantManageServiceI;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class WxMiniMerchantManageServiceImpl implements WxMiniMerchantManageServiceI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    AdminTerOperatingLogDao adminTerOperatingLogDao;

    @Autowired
    AdminMerchantEmployeeDao adminMerchantEmployeeDao;

    @Autowired
    WashSiteServiceI  washSiteService;

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TerDao terDao;

    @Override
    public ApiResult userInfo(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult moneyTotal(HttpServletRequest request) {
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
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        return ApiResult.ok(dto);
    }

    @Override
    public ApiResult siteStatus(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult siteOnline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty terId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        if (dto.isOnline()) {
            throw new BaseException(ExceptionCode.REPEAT_OPERATION , "该网点已经上线");
        }
        int res = terDao.updateOnline(wxMiniSearch.getTerId());
        if (res == 0) {
            buildTerOperationLog(dto , adminMerchantEmployee , WashTerOperatingLogTypeEnum.ONLINE,false);
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        buildTerOperationLog(dto , adminMerchantEmployee , WashTerOperatingLogTypeEnum.ONLINE,true);
        return ApiResult.ok();
    }

    @Override
    public ApiResult siteOffline(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId() || StringUtils.isBlank(wxMiniSearch.getMsg()) ) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        TerInfoDTO dto = washSiteService.terCheck(wxMiniSearch);
        if (!dto.isOnline()) {
            throw new BaseException(ExceptionCode.REPEAT_OPERATION , "该网点已经下线");
        }
        int res = terDao.updateOffline(wxMiniSearch.getTerId(),wxMiniSearch.getMsg());
        if (res == 0) {
            buildTerOperationLog(dto , adminMerchantEmployee , WashTerOperatingLogTypeEnum.NOT_ONLINE,false);
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        buildTerOperationLog(dto , adminMerchantEmployee , WashTerOperatingLogTypeEnum.NOT_ONLINE,true);
        return ApiResult.ok();
    }

    @Override
    public ApiResult orderListSearch(HttpServletRequest request) {
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
    public AdminMerchantEmployee check(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty openId");
        }
        AdminMerchantEmployee adminMerchantEmployee = adminMerchantEmployeeDao.findByWxMiniId(openId);
        if (null == adminMerchantEmployee) {
            throw new BaseException(ExceptionCode.NO_THIS_USER);
        }
        return adminMerchantEmployee;
    }

    @Override
    public void buildTerOperationLog(TerInfoDTO terInfoDTO, AdminMerchantEmployee adminMerchantEmployee,
                                     WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum, Boolean sucess) {
        String intros = adminMerchantEmployee.getName() + " 操作 " + terInfoDTO.getTitle() + washTerOperatingLogTypeEnum
                .getDesc();
        AdminTerOperatingLog adminTerOperatingLog = new
                AdminTerOperatingLog();
        adminTerOperatingLog.setEmployeeId(adminMerchantEmployee.getId());
        adminTerOperatingLog.setUsername(adminMerchantEmployee.getName());
        adminTerOperatingLog.setTerId(terInfoDTO.getId());
        adminTerOperatingLog.setTitle(washTerOperatingLogTypeEnum.getDesc());
        adminTerOperatingLog.setIntros(intros);
        adminTerOperatingLog.setType(washTerOperatingLogTypeEnum.getValue());
        adminTerOperatingLog.setOpSource(AdminTerOperatingLogSourceEnum.MERCHANT.getValue());
        adminTerOperatingLog.setSucess(sucess);
        int res = adminTerOperatingLogDao.insert(adminTerOperatingLog);
        if (res == 0) {
            log.error("维保人员操作日志存储失败 {} " + adminTerOperatingLog.toString());
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }
}
