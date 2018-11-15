package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.AdminMerchantEmployeeDao;
import com.tp.admin.dao.PartnerDao;
import com.tp.admin.data.dto.AdminMerchantEmployeeInfoDTO;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.entity.AdminMerchantEmployee;
import com.tp.admin.data.entity.Partner;
import com.tp.admin.data.parameter.WxMiniAuthDTO;
import com.tp.admin.data.parameter.WxMiniRegisterDTO;
import com.tp.admin.data.result.WxJscodeSessionResult;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WxMiniAuthServiceI;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Service(value = "wxMiniMerchantAuthService")
public class WxMiniMerchantAuthServiceImpl implements WxMiniAuthServiceI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    PartnerDao partnerDao;

    @Autowired
    AdminMerchantEmployeeDao adminMerchantEmployeeDao;

    @Override
    public ApiResult auth(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniAuthDTO wxMiniAuthDTO = new Gson().fromJson(body, WxMiniAuthDTO.class);
        if (StringUtils.isBlank(wxMiniAuthDTO.getCode())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty code");
        }
        String query =    "?appid=" + Constant.WxMiniMerchant.APP_ID
                + "&secret=" + Constant.WxMiniMerchant.APP_SECRET
                + "&js_code=" + wxMiniAuthDTO.getCode()
                + "&grant_type=authorization_code";
        RestTemplate rest = new RestTemplate();
        String result = rest.getForObject("https://api.weixin.qq.com/sns/jscode2session"+query,String.class);
        WxJscodeSessionResult wxJscodeSessionResult = null;
        try {
            wxJscodeSessionResult = new Gson().fromJson(result, WxJscodeSessionResult.class);
        }catch (Exception e){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "微信授权失败" );
        }
        if (StringUtils.isBlank(wxJscodeSessionResult.getOpenId())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , result );
        }
        log.info(wxJscodeSessionResult.toString());
        return ApiResult.ok(wxJscodeSessionResult);
    }

    @Override
    public ApiResult login(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniAuthDTO wxMiniAuthDTO = new Gson().fromJson(body, WxMiniAuthDTO.class);
        if (StringUtils.isBlank(wxMiniAuthDTO.getOpenId())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty openId");
        }
        AdminMerchantEmployee adminMerchantEmployee = adminMerchantEmployeeDao.findByWxMiniId(wxMiniAuthDTO.getOpenId());
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
        AdminMerchantEmployeeInfoDTO adminMerchantEmployeeInfoDTO = new AdminMerchantEmployeeInfoDTO();
        adminMerchantEmployeeInfoDTO.setId(adminMerchantEmployee.getId());
        adminMerchantEmployeeInfoDTO.setOpenId(adminMerchantEmployee.getMiniWxId());
        adminMerchantEmployeeInfoDTO.setPartnerTitle(partner.getTitle());
        adminMerchantEmployeeInfoDTO.setUsername(adminMerchantEmployee.getName());
        return ApiResult.ok(adminMerchantEmployeeInfoDTO);
    }

    @Override
    public ApiResult register(HttpServletRequest request , String body) {
//        String body = httpHelper.jsonBody(request);
        WxMiniRegisterDTO wxMiniRegisterDTO = new Gson().fromJson(body, WxMiniRegisterDTO.class);
        if (StringUtils.isBlank(wxMiniRegisterDTO.getOpenId()) ||
                StringUtils.isBlank(wxMiniRegisterDTO.getName()) ||
                StringUtils.isBlank(wxMiniRegisterDTO.getPhone())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminMerchantEmployee adminMerchantEmployee = adminMerchantEmployeeDao.findByWxMiniId(wxMiniRegisterDTO.getOpenId());
        if (null != adminMerchantEmployee) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        adminMerchantEmployee = adminMerchantEmployeeDao.findByPhone(wxMiniRegisterDTO.getPhone());
        if (null != adminMerchantEmployee) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"该手机号已经申请注册,请尝试其它手机号。或联系系统管理员");
        }
        adminMerchantEmployee = new AdminMerchantEmployee();
        adminMerchantEmployee.setMiniWxId(wxMiniRegisterDTO.getOpenId());
        adminMerchantEmployee.setWxUnionId("");
        adminMerchantEmployee.setName(wxMiniRegisterDTO.getName());
        adminMerchantEmployee.setPhone(wxMiniRegisterDTO.getPhone());
        adminMerchantEmployee.setEnable(false);
        int res = adminMerchantEmployeeDao.insert(adminMerchantEmployee);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult registerCheck(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniRegisterDTO wxMiniRegisterDTO = new Gson().fromJson(body, WxMiniRegisterDTO.class);
        if (StringUtils.isBlank(wxMiniRegisterDTO.getPhone())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminMerchantEmployee adminMerchantEmployee = adminMerchantEmployeeDao.findByPhone(wxMiniRegisterDTO.getPhone());
        if (null != adminMerchantEmployee ) {
            throw new BaseException(ExceptionCode.USER_PHONE_HAS_REGISTERED);
        }
        return ApiResult.ok();
    }


}
