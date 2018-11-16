package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.data.wx.WxAccessToken;
import com.tp.admin.service.WxMiniServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WxMiniServiceImpl implements WxMiniServiceI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String getAccessToken(String appId, String appSecret) {
        String query =     "?grant_type=client_credential"
                         + "&appid=" + appId
                         + "&secret=" + appSecret;
        RestTemplate rest = new RestTemplate();
        String result = rest.getForObject("https://api.weixin.qq.com/cgi-bin/token"+query,String.class);
        WxAccessToken wxAccessToken = null ;
        try {
            wxAccessToken = new Gson().fromJson(result, WxAccessToken.class);
        }catch (Exception e){
            log.error("微信令牌获取失败");
        }
        if (null == wxAccessToken.getErrcode()) {
            return wxAccessToken.getAccessToken();
        }else{
            return null;
        }
    }

    @Override
    public void sendTemplateMessage(String accessToken , String requestBody) {
        String query =  "?access_token=" + accessToken;
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);
        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> result = rest.postForEntity("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send"+query, httpEntity, String.class);
        log.info(result.getBody());
    }
}
