package com.tp.admin.service.implement;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenAppMiniTemplatemessageSendRequest;
import com.alipay.api.response.AlipayOpenAppMiniTemplatemessageSendResponse;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.TemplateDao;
import com.tp.admin.data.entity.AdminServiceInfo;
import com.tp.admin.data.entity.AdminTemplateInfo;
import com.tp.admin.data.search.AdminAutoSearch;
import com.tp.admin.data.search.AdminServiceSearch;
import com.tp.admin.data.search.TemplateSearch;
import com.tp.admin.enums.AdminTemplateInfoEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.AliMiniServiceI;
import com.tp.admin.utils.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliMiniServiceImpl implements AliMiniServiceI {

    private static final Logger logger = LoggerFactory.getLogger(AliMiniServiceImpl.class);

    @Autowired
    TemplateDao templateDao;

    @Autowired
    HttpHelperI httpHelper;

    @Override
    public ApiResult sendAliTemplate(TemplateSearch templateSearch) {
        if (templateSearch.getTouser() == null || templateSearch.getFormId() == null || templateSearch.getData() == null){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG,"参数");
        }
        String aliUrl = "https://openapi.alipay.com/gateway.do";


        AdminServiceSearch adminServiceSearch = new AdminServiceSearch();
        AdminAutoSearch adminAutoSearch = new AdminAutoSearch();
        adminAutoSearch.setType(3);
        List<AdminServiceInfo> list = templateDao.searchServiceInfoList(adminAutoSearch);
        for (AdminServiceInfo adminServiceInfo:list) {
            if (adminServiceInfo.getKey().equals("ALiMiniAppID")){
                adminServiceSearch.setAppId(adminServiceInfo.getValue());
            }else if (adminServiceInfo.getKey().equals("ALiMiniAppPublicKey")){
                adminServiceSearch.setAppPublicKey(adminServiceInfo.getValue());
            }else if (adminServiceInfo.getKey().equals("ALiMiniAppPrivateKey")){
                adminServiceSearch.setAppPrivateKey(adminServiceInfo.getValue());
            }
        }


        String aLiMiniTemplateId = "";
        if (templateSearch.getTemplateInfo() != null){
            AdminTemplateInfo adminTemplateInfo = templateDao.searchTemplateId(AdminTemplateInfoEnum.getByValue(templateSearch.getTemplateInfo()).getValue());
            aLiMiniTemplateId = adminTemplateInfo.getTemplateId();
            if (aLiMiniTemplateId.equals("")){
                throw new BaseException(ExceptionCode.PARAMETER_WRONG,"empty templateId");
            }
        }

        if (null == adminServiceSearch.getAppId() || null == adminServiceSearch.getAppPrivateKey() || null == adminServiceSearch.getAppPublicKey() || adminServiceSearch.getAppId().equals("") || adminServiceSearch.getAppPrivateKey().equals("") || adminServiceSearch.getAppPublicKey().equals("")){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("to_user_id",templateSearch.getTouser());
        jsonObject.put("form_id",templateSearch.getFormId());
        jsonObject.put("user_template_id",aLiMiniTemplateId);
        jsonObject.put("page",templateSearch.getPage());
        jsonObject.put("data",templateSearch.getData());

        AlipayClient alipayClient = new DefaultAlipayClient(aliUrl,adminServiceSearch.getAppId(),adminServiceSearch.getAppPrivateKey(),"json","GBK",adminServiceSearch.getAppPublicKey(),"RSA2");
        AlipayOpenAppMiniTemplatemessageSendRequest request1 = new AlipayOpenAppMiniTemplatemessageSendRequest();
        request1.setBizContent(jsonObject.toString());

        AlipayOpenAppMiniTemplatemessageSendResponse response = null;
        String aliRes = "";
        try {
            response = alipayClient.execute(request1);
            aliRes = response.getCode();
        } catch (AlipayApiException e) {
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        if (StringUtil.isEmpty(aliRes)){
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        if (aliRes.equals("10000")){
            logger.info("接收人：{} formId {}  发送成功",templateSearch.getTouser(),templateSearch.getFormId());
            return ApiResult.ok();
        }
        if (aliRes.equals("20000")){
            logger.error("接收人：{} formId {} ",templateSearch.getTouser(),templateSearch.getFormId(),"invalid service");
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION, "invalid service");
        }
        if (aliRes.equals("20001")) {
            logger.error("接收人：{} formId {} ", templateSearch.getTouser(),templateSearch.getFormId(), "no auth to payback");
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION, "no auth to payback");
        }
        if (aliRes.equals("40001")) {
            logger.error("接收人：{} formId {} ", templateSearch.getTouser(),templateSearch.getFormId(), "param miss");
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION, "param miss");
        }
        if (aliRes.equals("40002")) {
            logger.error("接收人：{} formId {} ", templateSearch.getTouser(),templateSearch.getFormId(), "illegal param");
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION, "illegal param");
        }
        if (aliRes.equals("40004")) {
            logger.error("接收人：{} formId {} ", templateSearch.getTouser(),templateSearch.getFormId(), "service fails");
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION, "service fails");
        }
        if (aliRes.equals("40006")) {
            logger.error("接收人：{} formId {} ", templateSearch.getTouser(),templateSearch.getFormId(), "another: no auth to payback");
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION, "another: no auth to payback");
        }
        return ApiResult.error(ExceptionCode.UNKNOWN_EXCEPTION);
    }
}