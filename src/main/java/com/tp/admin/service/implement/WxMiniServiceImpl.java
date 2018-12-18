package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.TemplateDao;
import com.tp.admin.data.entity.AdminTemplateInfo;
import com.tp.admin.data.search.AdminAutoSearch;
import com.tp.admin.data.search.TemplateSearch;
import com.tp.admin.data.wx.WxAccessToken;
import com.tp.admin.enums.AdminTemplateInfoEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WxMiniServiceI;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Service
public class WxMiniServiceImpl implements WxMiniServiceI {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    TemplateDao templateDao;

    @Autowired
    HttpHelperI httpHelper;

    @Override
    public String getAccessToken(String appId, String appSecret) {
        String query =     "?grant_type=client_credential"
                         + "&appid=" + appId
                         + "&secret=" + appSecret;
        RestTemplate rest = new RestTemplate();
        String result = rest.getForObject("https://api.weixin.qq.com/cgi-bin/token"+query,String.class);
        WxAccessToken wxAccessToken = null;
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
        String errcode = JSONObject.fromObject(result.getBody()).getString("errcode");
        if (errcode.equals("0")){
            log.info(result.getBody());
            return;
        }else {
            log.error(result.getBody());
        }
    }

    @Override
    public void sendWxTemplate(TemplateSearch templateSearch,AdminTemplateInfo adminTemplateInfo) {
        if (templateSearch.getFormId() == null || templateSearch.getTouser() == null || templateSearch.getData() == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"缺少参数");
        }
        JSONObject requestBody = new JSONObject();

        requestBody.put("form_id",templateSearch.getFormId());
        requestBody.put("touser",templateSearch.getTouser());
        requestBody.put("data",templateSearch.getData());
        requestBody.put("template_id",adminTemplateInfo.getTemplateId());
        if (templateSearch.getPage() != null){
            requestBody.put("page",templateSearch.getPage());
        }
        AdminAutoSearch adminAutoSearch = new AdminAutoSearch();
        adminAutoSearch.setType(adminTemplateInfo.getServiceType());
        adminAutoSearch.setKey("WX_APP_ID");
        String appId = templateDao.searchMasterplateTool(adminAutoSearch);
        adminAutoSearch.setKey("WX_APP_SECRET");
        String appSecret = templateDao.searchMasterplateTool(adminAutoSearch);
        String accessToken = getAccessToken(appId,appSecret);
        requestBody.put("access_token",accessToken);
        sendTemplateMessage(accessToken,requestBody.toString());
    }


}
