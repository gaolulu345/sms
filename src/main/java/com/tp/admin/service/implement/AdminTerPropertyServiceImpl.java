package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.ajax.ResultCode;
import com.tp.admin.dao.*;
import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.*;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.search.TerPropertySearch;
import com.tp.admin.enums.AdminTerOperatingLogSourceEnum;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.AdminTerPropertyServiceI;
import com.tp.admin.service.WashSiteServiceI;
import com.tp.admin.service.WxMiniMerchantManageServiceI;
import com.tp.admin.utils.SessionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminTerPropertyServiceImpl implements AdminTerPropertyServiceI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TerDao terDao;

    @Autowired
    AdminMaintionEmployeeDao adminMaintionEmployeeDao;

    @Autowired
    AdminMerchantEmployeeDao adminMerchantEmployeeDao;

    @Autowired
    PartnerDao partnerDao;

    @Autowired
    WashSiteServiceI washSiteServiceI;

    @Autowired
    WxMiniMerchantManageServiceI wxMiniMerchantManageServiceI;

    @Autowired
    AdminTerOperatingLogDao adminTerOperatingLogDao;

    @Override
    public ApiResult allTerPropertyInfoList(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TerPropertySearch terPropertySearch = new Gson().fromJson(body, TerPropertySearch.class);
        if (null == terPropertySearch) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        List<AdminTerPropertyDTO> list = terDao.findAllTerProperty(terPropertySearch);
        if (list == null) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        for (AdminTerPropertyDTO adminTerProperty:list) {
            adminTerProperty.build();
        }
        return ApiResult.ok(list);
    }


    @Override
    public ApiResult terPropertySearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TerPropertySearch terPropertySearch = new Gson().fromJson(body, TerPropertySearch.class);
        if (null == terPropertySearch.getId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "设备id为空");
        }

        AdminTerPropertyDTO adminTerPropertyDTO =  terDao.findTerStartInfo(terPropertySearch.getId());
        if (adminTerPropertyDTO == null){
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        adminTerPropertyDTO.build();
        return ApiResult.ok(adminTerPropertyDTO);
    }

    @Override
    public ApiResult onlineFreeStart(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TerPropertySearch terPropertySearch = new Gson().fromJson(body, TerPropertySearch.class);
        if (null == terPropertySearch.getId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "设备id为空");
        }

        Object ob = check(terPropertySearch.getOpenId());
        if (ob == null){
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        AdminTerPropertyDTO adminTerPropertyDTO =  terDao.findTerStartInfo(terPropertySearch.getId());
        if (adminTerPropertyDTO.getStartOnline() != 1){
            terDao.updateOnlineFreeStartState(terPropertySearch.getId());
            adminTerPropertyDTO.setStartOnline(1);
            //添加日志
            buildTerOperateLog(ob,adminTerPropertyDTO,WashTerOperatingLogTypeEnum.getByCode(4));

        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult updateTerProperty(HttpServletRequest request,AdminTerPropertyDTO adminTerPropertyDTO) {
        if (null == adminTerPropertyDTO.getId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty id");
        }
        int res = terDao.updateTerProperty(adminTerPropertyDTO);
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public Object check(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
        }
        AdminMaintionEmployee adminMaintionEmployee = adminMaintionEmployeeDao.findByWxMiniId(openId);

        AdminMerchantEmployee adminMerchantEmployee;
        if (null != adminMaintionEmployee && adminMaintionEmployee.isEnable() && !adminMaintionEmployee.isDeleted()) {
            return adminMaintionEmployee;
        }else {
            adminMerchantEmployee = adminMerchantEmployeeDao.findByWxMiniId(openId);
            if (null != adminMerchantEmployee && adminMerchantEmployee.isEnable() && !adminMerchantEmployee.isDeleted()) {
                return adminMerchantEmployee;
            }
        }
        return null;
    }

    @Override
    public void buildTerOperateLog(Object object,AdminTerPropertyDTO adminTerPropertyDTO,WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum) {
        AdminTerOperatingLog adminTerOperatingLog = new AdminTerOperatingLog();
        AdminMerchantEmployee adminMerchantEmployee;
        AdminMaintionEmployee adminMaintionEmployee;
        if (object instanceof AdminMerchantEmployee){
            adminMerchantEmployee = (AdminMerchantEmployee) object;
            String intros = adminMerchantEmployee.getName() + " 商户操作 " + adminTerPropertyDTO.getId() + "号设备" + washTerOperatingLogTypeEnum
                    .getDesc();
            adminTerOperatingLog.setMerchantId(adminMerchantEmployee.getId());
            adminTerOperatingLog.setUsername(adminMerchantEmployee.getName());
            adminTerOperatingLog.setIntros(intros);
            adminTerOperatingLog.setOpSource(AdminTerOperatingLogSourceEnum.MERCHANT.getValue());
        }else {
            adminMaintionEmployee = (AdminMaintionEmployee) object;
            String intros = adminMaintionEmployee.getName() + " 维保操作 " + adminTerPropertyDTO.getTerId() + "号设备" + washTerOperatingLogTypeEnum
                    .getDesc();
            adminTerOperatingLog.setMerchantId(adminMaintionEmployee.getId());
            adminTerOperatingLog.setUsername(adminMaintionEmployee.getName());
            adminTerOperatingLog.setIntros(intros);
            adminTerOperatingLog.setOpSource(AdminTerOperatingLogSourceEnum.MAINTAUN.getValue());
        }
        adminTerOperatingLog.setTerId(adminTerPropertyDTO.getId());
        adminTerOperatingLog.setTitle(washTerOperatingLogTypeEnum.getDesc());

        adminTerOperatingLog.setType(washTerOperatingLogTypeEnum.getValue());
        adminTerOperatingLog.setImgs("");
        int res = adminTerOperatingLogDao.insert(adminTerOperatingLog);
        if (res == 0) {
            log.error("操作日志存储失败 {} " + adminTerOperatingLog.toString());
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }


}
