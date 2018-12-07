package com.tp.admin.service;


import com.tp.admin.ajax.ApiResult;

import javax.servlet.http.HttpServletRequest;

public interface WxMiniServiceI {

    String getAccessToken(String appId , String appSecret);

    void sendTemplateMessage(String accessToken , String requestBody);

    ApiResult sendWxTemplate(HttpServletRequest request);


}
