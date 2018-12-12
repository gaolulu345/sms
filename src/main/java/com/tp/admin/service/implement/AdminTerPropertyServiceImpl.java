package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.ajax.ResultCode;
import com.tp.admin.dao.AdminMerchantEmployeeDao;
import com.tp.admin.dao.AdminTerOperatingLogDao;
import com.tp.admin.dao.PartnerDao;
import com.tp.admin.dao.TerDao;
import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.AdminMerchantEmployee;
import com.tp.admin.data.entity.AdminTerOperatingLog;
import com.tp.admin.data.entity.AdminTerProperty;
import com.tp.admin.data.entity.Partner;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.enums.AdminTerOperatingLogSourceEnum;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.AdminTerPropertyServiceI;
import com.tp.admin.service.WashSiteServiceI;
import com.tp.admin.service.WxMiniMerchantManageServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AdminTerPropertyServiceImpl implements AdminTerPropertyServiceI {

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TerDao terDao;

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
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getOpenId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
        }
        List<AdminTerProperty> list = terDao.findAllTerProperty();
        if (list != null){
            wxMiniSearch.setResult(list);
        }
        return ApiResult.ok(wxMiniSearch.getResult());
    }

    //微信小程序
    @Override
    public ApiResult terPropertySearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        checkTerAndPartner(wxMiniSearch.getTerId(),adminMerchantEmployee.getPartnerId());
        AdminTerPropertyDTO adminTerPropertyDTO =  terDao.findTerStartInfo(wxMiniSearch.getTerId());
        adminTerPropertyDTO.build();
        return ApiResult.ok(adminTerPropertyDTO);
    }

    @Override
    public ApiResult onlineFreeStart(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        AdminTerPropertyDTO adminTerPropertyDTO =  terDao.findTerStartInfo(wxMiniSearch.getTerId());
        if (adminTerPropertyDTO.getStartOnline() != 1){
            terDao.updateOnlineFreeStartState(wxMiniSearch.getTerId());
            adminTerPropertyDTO.setStartOnline(1);
            //添加日志
            TerInfoDTO terInfoDTO = washSiteServiceI.terCheck(wxMiniSearch);
            AdminTerOperatingLog adminTerOperatingLog = new AdminTerOperatingLog();
            adminTerOperatingLog.setTerId(wxMiniSearch.getTerId());
            adminTerOperatingLog.setMerchantId(adminMerchantEmployee.getId());
            adminTerOperatingLog.setUsername(adminMerchantEmployee.getName());
            adminTerOperatingLog.setOpSource(AdminTerOperatingLogSourceEnum.MERCHANT.getValue());
            adminTerOperatingLog.setTitle(WashTerOperatingLogTypeEnum.ONLINE_FREE_STARTED.getDesc());
            adminTerOperatingLog.setIntros(adminMerchantEmployee.getName() + " 操作" + terInfoDTO.getTitle() + WashTerOperatingLogTypeEnum.ONLINE_FREE_STARTED.getDesc());
            adminTerOperatingLog.setImgs("");
            adminTerOperatingLogDao.insert(adminTerOperatingLog);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult updateTerProperty(HttpServletRequest request,AdminTerPropertyDTO adminTerPropertyDTO) {
        if (null == adminTerPropertyDTO.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(adminTerPropertyDTO.getOpenId());
        checkTerAndPartner(adminTerPropertyDTO.getTerId(),adminMerchantEmployee.getPartnerId());
        int res = terDao.updateTerProperty(adminTerPropertyDTO);
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        return ApiResult.ok();
    }

    private final ApiResult buildApiResult(String result, TerInfoDTO dto, AdminMerchantEmployee
            adminMerchantEmployee, String img, WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum
    ) {
        ApiResult apiResult = null;
        try {
            apiResult = new Gson().fromJson(result, ApiResult.class);
            if (null == apiResult) {
                throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
            }
            if (apiResult.getCode().equals(ResultCode.SUCCESS.getCode())) {
                wxMiniMerchantManageServiceI.buildTerOperationLog(dto, adminMerchantEmployee, washTerOperatingLogTypeEnum, img, true);
            } else {
                wxMiniMerchantManageServiceI.buildTerOperationLog(dto, adminMerchantEmployee, washTerOperatingLogTypeEnum, img, false);
            }
        } catch (JsonSyntaxException ex) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        return apiResult;
    }
    @Override
    public AdminMerchantEmployee check(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
        }
        AdminMerchantEmployee adminMerchantEmployee = adminMerchantEmployeeDao.findByWxMiniId(openId);
        if (null == adminMerchantEmployee) {
            throw new BaseException(ExceptionCode.NO_THIS_USER);
        }
        if (!adminMerchantEmployee.isEnable()) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        if (adminMerchantEmployee.isDeleted()) {
            throw new BaseException(ExceptionCode.USER_DELETE_REGISTERED);
        }
        if (adminMerchantEmployee.getPartnerId() == 0) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        Partner partner = partnerDao.findById(adminMerchantEmployee.getPartnerId());
        if (null == partner) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        return adminMerchantEmployee;
    }

    @Override
    public void checkTerAndPartner(int terId, int partnerId) {
        List<Integer> terIds = terDao.findRelatedTerByPartnerId(partnerId);
        if (null != terIds && !terIds.isEmpty()){
            int flag = 0;
            for (int terIdc : terIds) {
                if (terId == terIdc){
                    flag = 1;
                    break;
                }
            }
            if (flag == 0){
                throw new BaseException(ExceptionCode.PARAMETER_WRONG,"terId mismatch partnerId");
            }
        }
    }


}
