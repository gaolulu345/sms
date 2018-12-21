package com.tp.admin.manage.impl;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.TemplateDao;
import com.tp.admin.data.entity.AdminServiceInfo;
import com.tp.admin.data.parameter.WxMiniAuthDTO;
import com.tp.admin.data.search.AdminAutoSearch;
import com.tp.admin.data.search.AdminServiceSearch;
import com.tp.admin.data.wx.WxJscodeSessionResult;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.manage.MiniAutoServiceI;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class MiniAutoServiceImpl implements MiniAutoServiceI {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TemplateDao templateDao;


    @Override
    public ApiResult miniWxAuto(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniAuthDTO wxMiniAuthDTO = new Gson().fromJson(body, WxMiniAuthDTO.class);
        if (StringUtils.isBlank(wxMiniAuthDTO.getCode())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty code");
        }

        int authType = check(request);
        if (authType == 0){
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        AdminAutoSearch adminAutoSearch = new AdminAutoSearch();
        adminAutoSearch.setType(authType);

        AdminServiceSearch adminServiceSearch = new AdminServiceSearch();

        List<AdminServiceInfo> list = templateDao.searchServiceInfoList(adminAutoSearch);
        for (AdminServiceInfo adminServiceInfo:list) {
            if (adminServiceInfo.getKey().equals("WX_APP_ID")){
                adminServiceSearch.setAppId(adminServiceInfo.getValue());
            }
            if (adminServiceInfo.getKey().equals("WX_APP_SECRET")){
                adminServiceSearch.setAppSecret(adminServiceInfo.getValue());
            }
        }

        String query =    "?appid=" + adminServiceSearch.getAppId()
                + "&secret=" + adminServiceSearch.getAppSecret()
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
    public int check(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (null == uri || uri.equals("")){
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        if (uri.indexOf("/api/open/wx/mini/merchant") != -1){
            return 1;
        }else if (uri.indexOf("/api/open/wx/mini/maintain") != -1){
            return 2;
        }
        return 0;
    }


}
