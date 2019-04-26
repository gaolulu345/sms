package com.sms.admin.manage.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.manage.HttpHelperI;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class HttpHelperImpl implements HttpHelperI {

    private Logger log = LoggerFactory.getLogger(getClass());

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
