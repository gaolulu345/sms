package com.tp.admin.service;


import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.TemplateSearch;

import javax.servlet.http.HttpServletRequest;

public interface WxMiniServiceI {

    String getAccessToken(String appId , String appSecret);

    void sendTemplateMessage(String accessToken , String requestBody);

    void sendWxTemplate(TemplateSearch templateSearch);


}
