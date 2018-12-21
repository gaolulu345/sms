package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.TemplateDao;
import com.tp.admin.data.entity.AdminTemplateInfo;
import com.tp.admin.data.search.TemplateSearch;
import com.tp.admin.enums.AdminTemplateInfoEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.AdminTemplateInfoServiceI;
import com.tp.admin.service.AliMiniServiceI;
import com.tp.admin.service.WxMiniServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
@Service
public class AdminTemplateInfoServiceImpl implements AdminTemplateInfoServiceI {
    @Autowired
    WxMiniServiceI wxMiniServiceI;

    @Autowired
    AliMiniServiceI aliMiniServiceI;

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TemplateDao templateDao;

    @Override
    public ApiResult sendTemplate(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        TemplateSearch templateSearch = new Gson().fromJson(body, TemplateSearch.class);

        AdminTemplateInfo adminTemplateInfo = templateDao.searchTemplateId(templateSearch.getTemplateId());
        if (adminTemplateInfo == null){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
        if (adminTemplateInfo.getType() == 3){
            wxMiniServiceI.sendWxTemplate(templateSearch,adminTemplateInfo);
        }else if (adminTemplateInfo.getType() == 4){
            aliMiniServiceI.sendAliTemplate(templateSearch,adminTemplateInfo);
        }
        return ApiResult.ok();
    }
}
