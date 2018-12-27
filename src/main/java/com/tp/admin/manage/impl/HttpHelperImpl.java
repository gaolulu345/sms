package com.tp.admin.manage.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tp.admin.common.Constant;
import com.tp.admin.config.AdminProperties;
import com.tp.admin.data.wash.TerDeviceRequest;
import com.tp.admin.data.wash.WashSiteRequest;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.utils.SecurityUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;

@Service
public class HttpHelperImpl implements HttpHelperI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    AdminProperties adminProperties;

    @Override
    public String jsonBody(HttpServletRequest request) {
        String body = null;
        try {
            JsonRequestWrapperImpl jsonRequestWrapper = new JsonRequestWrapperImpl(request);
            body = jsonRequestWrapper.getBody();
            if (!validate(body)){
                log.error(" 请求参数 json 解析错误 请求参数如下 {} ",body);
                throw new BaseException(ExceptionCode.PARAMETER_WRONG);
            }
            return body;
        } catch (IOException e) {
            log.error(" 请求参数异常 {} ",e.getMessage());
            e.printStackTrace();
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
    }

    @Override
    public String sendPostByJsonData(String url , String requestBody) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);
        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> result = rest.postForEntity(url, httpEntity, String.class);
        if (result.getStatusCode() != HttpStatus.OK) {
            throw new BaseException(ExceptionCode.SIGN_ERROR_FOR_REMOTE_OP);
        }
        String resultStr = result.getBody();
        log.info(resultStr);
        return resultStr;
    }

    @Override
    public WashSiteRequest signInfo(Integer deviceId, String orderId, String msg) {
        JsonObject jsonObject = new JsonObject();
        String key = adminProperties.getWashManageKey();
        Long timestamp = System.currentTimeMillis();
        jsonObject.addProperty("deviceId", deviceId);
        try {
            int order = Integer.valueOf(orderId);
            jsonObject.addProperty("orderId", order);
        } catch (NumberFormatException e) {
            jsonObject.addProperty("orderId", orderId);
        }
        jsonObject.addProperty("msg", msg);
        jsonObject.addProperty("key", key);
        jsonObject.addProperty("time", timestamp);
        String sign = "";
        try {
            RSAPublicKey publicKey = SecurityUtil.RsaUtil.getPublicKey(Constant.RemoteTer.PUBLIC_KEY);
            sign = SecurityUtil.RsaUtil.encrypt(jsonObject.toString(), publicKey);
        } catch (Exception e) {
            throw new BaseException(ExceptionCode.SIGN_FAILURE_FOR_REMOTE_TER);
        }
        if (StringUtils.isEmpty(sign) || sign.length() < 2) {
            throw new BaseException(ExceptionCode.SIGN_ERROR_FOR_REMOTE_TER);
        }
        WashSiteRequest washSiteRequest = new WashSiteRequest();
        washSiteRequest.setDeviceId(deviceId);
        washSiteRequest.setOrderId(orderId);
        washSiteRequest.setMsg(msg);
        washSiteRequest.setKey(key);
        washSiteRequest.setTime(timestamp);
        washSiteRequest.setSign(sign);
        return washSiteRequest;
    }

    @Override
    public TerDeviceRequest signTerInfo(String frpIp, List<String> pictures, String msg, String frpPort) {
        JsonObject jsonObject = new JsonObject();
        String key = adminProperties.getWashManageKey();
        Long timestamp = System.currentTimeMillis();
        jsonObject.addProperty("frpIp", frpIp);
        jsonObject.addProperty("frpPort",frpPort);
        if (pictures == null){
            jsonObject.addProperty("picture", "");
        }else {
            jsonObject.addProperty("picture", Arrays.toString(pictures.toArray()));
        }
        jsonObject.addProperty("msg",msg);
        jsonObject.addProperty("key", key);
        jsonObject.addProperty("time", timestamp);
        String sign = "";
        try {
            RSAPublicKey publicKey = SecurityUtil.RsaUtil.getPublicKey(Constant.RemoteTer.PUBLIC_KEY);
            sign = SecurityUtil.RsaUtil.encrypt(jsonObject.toString(), publicKey);
        } catch (Exception e) {
            throw new BaseException(ExceptionCode.SIGN_FAILURE_FOR_REMOTE_TER);
        }
        if (StringUtils.isEmpty(sign) || sign.length() < 2) {
            throw new BaseException(ExceptionCode.SIGN_ERROR_FOR_REMOTE_TER);
        }
        TerDeviceRequest terDeviceRequest = new TerDeviceRequest();
        terDeviceRequest.setFrpIp(frpIp);
        terDeviceRequest.setPictures(pictures);
        terDeviceRequest.setFrpPort(frpPort);
        terDeviceRequest.setMsg(msg);
        terDeviceRequest.setKey(key);
        terDeviceRequest.setTime(timestamp);
        terDeviceRequest.setSign(sign);
        return terDeviceRequest;
    }

    private boolean validate(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return false;
        }
        JsonElement jsonElement = null;
        try {
            jsonElement = new JsonParser().parse(jsonStr);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement.isJsonObject()) {
            return true;
        }
        return false;
    }
}
