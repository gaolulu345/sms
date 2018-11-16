package com.tp.admin.service;


public interface WxMiniServiceI {

    String getAccessToken(String appId , String appSecret);

    void sendTemplateMessage(String accessToken , String requestBody);

}
