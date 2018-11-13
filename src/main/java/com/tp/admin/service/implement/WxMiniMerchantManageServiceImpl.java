package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminTerOperatingLogDao;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.AdminMerchantEmployee;
import com.tp.admin.data.entity.AdminTerOperatingLog;
import com.tp.admin.enums.AdminTerOperatingLogSourceEnum;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WxMiniMerchantManageServiceI;
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
    HttpHelperI httpHelper;

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
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult siteStatus(HttpServletRequest request) {
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
    public ApiResult orderListSearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult siteOperationLog(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
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
