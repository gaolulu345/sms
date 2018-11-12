package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.data.parameter.WxMiniAuthDTO;
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
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult register(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }

    @Override
    public ApiResult registerCheck(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        return ApiResult.ok(body);
    }


}
