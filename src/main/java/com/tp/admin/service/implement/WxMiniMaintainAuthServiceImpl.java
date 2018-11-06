package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.AdminMaintionEmployeeDao;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.entity.FileUploadLog;
import com.tp.admin.data.parameter.WxMiniAuthDTO;
import com.tp.admin.data.parameter.WxMiniRegisterDTO;
import com.tp.admin.data.result.WxJscodeSessionResult;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WxMiniMaintainAuthServiceI;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Service
public class WxMiniMaintainAuthServiceImpl implements WxMiniMaintainAuthServiceI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    AdminMaintionEmployeeDao adminMaintionEmployeeDao;

    @Override
    public ApiResult login(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniAuthDTO wxMiniAuthDTO = new Gson().fromJson(body, WxMiniAuthDTO.class);
        if (StringUtils.isBlank(wxMiniAuthDTO.getOpenId())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty openId");
        }
        AdminMaintionEmployee adminMaintionEmployee = adminMaintionEmployeeDao.findByWxMiniId(wxMiniAuthDTO.getOpenId());
        if (null == adminMaintionEmployee) {
            throw new BaseException(ExceptionCode.NO_THIS_USER);
        }
        if (!adminMaintionEmployee.isEnable()) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        return ApiResult.ok(adminMaintionEmployee);
    }

    @Override
    public ApiResult auth(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniAuthDTO wxMiniAuthDTO = new Gson().fromJson(body, WxMiniAuthDTO.class);
        if (StringUtils.isBlank(wxMiniAuthDTO.getCode())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty code");
        }
        String query = "?appid=" + Constant.WxMiniMaintain.APP_ID
                        + "&secret=" + Constant.WxMiniMaintain.APP_SECRET
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
    public ApiResult register(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniRegisterDTO wxMiniRegisterDTO = new Gson().fromJson(body, WxMiniRegisterDTO.class);
        if (StringUtils.isBlank(wxMiniRegisterDTO.getOpenId()) ||
                StringUtils.isBlank(wxMiniRegisterDTO.getName()) ||
                StringUtils.isBlank(wxMiniRegisterDTO.getPhone())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminMaintionEmployee adminMaintionEmployee = adminMaintionEmployeeDao.findByWxMiniId(wxMiniRegisterDTO.getOpenId());
        if (null != adminMaintionEmployee) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        adminMaintionEmployee = adminMaintionEmployeeDao.findByPhone(wxMiniRegisterDTO.getPhone());
        if (null != adminMaintionEmployee) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"该手机号已经申请注册,请尝试其它手机号。或联系系统管理员");
        }
        adminMaintionEmployee = new AdminMaintionEmployee();
        adminMaintionEmployee.setMiniWxId(wxMiniRegisterDTO.getOpenId());
        adminMaintionEmployee.setWxUnionId("");
        adminMaintionEmployee.setName(wxMiniRegisterDTO.getName());
        adminMaintionEmployee.setPhone(wxMiniRegisterDTO.getPhone());
        adminMaintionEmployee.setEnable(false);
        int res = adminMaintionEmployeeDao.insert(adminMaintionEmployee);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult registerCheck(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniRegisterDTO wxMiniRegisterDTO = new Gson().fromJson(body, WxMiniRegisterDTO.class);
        if (StringUtils.isBlank(wxMiniRegisterDTO.getOpenId())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminMaintionEmployee adminMaintionEmployee = adminMaintionEmployeeDao.findByWxMiniId(wxMiniRegisterDTO.getOpenId());
        if (null == adminMaintionEmployee ) {
            throw new BaseException(ExceptionCode.NO_THIS_USER);
        }
        if (!adminMaintionEmployee.isEnable()) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        return ApiResult.ok();
    }
}
