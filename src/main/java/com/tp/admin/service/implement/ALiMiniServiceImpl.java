package com.tp.admin.service.implement;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOpenAppMiniTemplatemessageSendRequest;
import com.alipay.api.response.AlipayOpenAppMiniTemplatemessageSendResponse;
import com.google.gson.Gson;

import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.TemplateDao;
import com.tp.admin.data.search.TemplateSearch;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.ALiMiniServiceI;
import com.tp.admin.utils.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
@Service
public class ALiMiniServiceImpl implements ALiMiniServiceI {

    private static final Logger logger = LoggerFactory.getLogger(ALiMiniServiceImpl.class);

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
        String appId = templateDao.searchMasterplateTool("ALiMiniAppID");
        String aLiMiniAppPublicKey = templateDao.searchMasterplateTool("ALiMiniAppPublicKey");
        String aLiMiniAppPrivateKey = templateDao.searchMasterplateTool("ALiMiniAppPrivateKey");
        String aLiMiniTemplateId = templateDao.searchMasterplateTool("ALiMiniTemplateId");
        if (null == appId || null == aLiMiniAppPrivateKey || null == aLiMiniAppPublicKey || appId.equals("") || aLiMiniAppPrivateKey.equals("") || aLiMiniAppPublicKey.equals("")){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("to_user_id",templateSearch.getTouser());
        jsonObject.put("form_id",templateSearch.getFormId());
        jsonObject.put("user_template_id",aLiMiniTemplateId);
        jsonObject.put("page",templateSearch.getPage());
        jsonObject.put("data",templateSearch.getData());

        AlipayClient alipayClient = new DefaultAlipayClient(aliUrl,appId,aLiMiniAppPrivateKey,"json","GBK",aLiMiniAppPublicKey,"RSA2");
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
