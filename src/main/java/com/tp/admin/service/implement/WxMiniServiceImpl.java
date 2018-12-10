package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.TemplateDao;
import com.tp.admin.data.entity.AdminTemplateInfo;
import com.tp.admin.data.search.TemplateSearch;
import com.tp.admin.data.wx.WxAccessToken;
import com.tp.admin.enums.AdminTemplateInfoEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.WxMiniServiceI;
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

    @Override
    public ApiResult sendWxTemplate(TemplateSearch templateSearch) {
        if (templateSearch.getFormId() == null || templateSearch.getTouser() == null || templateSearch.getData() == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"缺少参数");
        }
        MultiValueMap<String,Object> requestBody = new LinkedMultiValueMap<>();

        String templateId = "";
        if (templateSearch.getTemplateInfo() != null){
            AdminTemplateInfo adminTemplateInfo = templateDao.searchTemplateId(AdminTemplateInfoEnum.getByValue(templateSearch.getTemplateInfo()).getValue());
            templateId = adminTemplateInfo.getTemplateId();
            if (templateId.equals("")){
                throw new BaseException(ExceptionCode.PARAMETER_WRONG,"empty templateId");
            }
        }

        requestBody.add("form_id",templateSearch.getFormId());
        requestBody.add("touser",templateSearch.getTouser());
        requestBody.add("data",templateSearch.getData());
        requestBody.add("template_id",templateId);
        if (templateSearch.getPage() != null){
            requestBody.add("page",templateSearch.getPage());
        }
        String appId = templateDao.searchMasterplateTool("WXAPP_ID");
        String appSecret = templateDao.searchMasterplateTool("WXAPP_SECRET");
        String accessToken = getAccessToken(appId,appSecret);
        sendTemplateMessage(accessToken,requestBody.toString());
        return ApiResult.ok();
    }


}
