package com.tp.admin.manage.impl;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.TemplateDao;
import com.tp.admin.data.entity.AdminServiceInfo;
import com.tp.admin.data.parameter.WxMiniAuthDTO;
import com.tp.admin.data.search.AdminAutoSearch;
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
    public ApiResult miniAuto(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniAuthDTO wxMiniAuthDTO = new Gson().fromJson(body, WxMiniAuthDTO.class);
        if (StringUtils.isBlank(wxMiniAuthDTO.getCode())) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG , "empty code");
        }

        AdminAutoSearch adminAutoSearch = new AdminAutoSearch();
        adminAutoSearch.setType(wxMiniAuthDTO.getType());
        String appId = "";
        String appSecret = "";
        if (wxMiniAuthDTO.getType() != null){
            List<AdminServiceInfo> list = templateDao.searchTemplateList(adminAutoSearch);
            for (AdminServiceInfo adminServiceInfo:list) {
                if (adminServiceInfo.getKey().equals("WX_APP_ID")){
                    appId = adminServiceInfo.getValue();
                }
                if (adminServiceInfo.getKey().equals("WX_APP_SECRET")){
                    appSecret = adminServiceInfo.getValue();
                }
            }
        }

        String query =    "?appid=" + appId
                + "&secret=" + appSecret
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
}
