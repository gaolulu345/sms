package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.AdminMaintionEmployeeDao;
import com.tp.admin.data.wx.WxTemplateData;
import com.tp.admin.data.wx.WxTemplateMessage;
import com.tp.admin.data.wx.WxTextMessage;
import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.search.MaintionEmployeeSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.service.MaintionEmployeeServiceI;
import com.tp.admin.service.WxMiniServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaintionEmployeeServiceImpl implements MaintionEmployeeServiceI {

    @Autowired
    AdminMaintionEmployeeDao adminMaintionEmployeeDao;

    @Autowired
    WxMiniServiceI wxMiniService;

    @Override
    public ApiResult list(HttpServletRequest request, MaintionEmployeeSearch maintionEmployeeSearch) {
        maintionEmployeeSearch.builData();
        List<AdminMaintionEmployee> list = adminMaintionEmployeeDao.listBySearch(maintionEmployeeSearch);
        if (null != list && !list.isEmpty()) {
            Integer cnt = adminMaintionEmployeeDao.cntBySearch(maintionEmployeeSearch);
            maintionEmployeeSearch.setResult(list);
            maintionEmployeeSearch.setTotalCnt(cnt);
        }else{
            maintionEmployeeSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(maintionEmployeeSearch));
    }

    @Override
    public ApiResult bachUpdateDeleted(HttpServletRequest request, MaintionEmployeeSearch maintionEmployeeSearch) {
        if (null == maintionEmployeeSearch.getIds() || maintionEmployeeSearch.getIds().length == 0 || null == maintionEmployeeSearch.getDeleted()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        int res = adminMaintionEmployeeDao.bachUpdateDeleted(maintionEmployeeSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult updateEnable(HttpServletRequest request, MaintionEmployeeSearch maintionEmployeeSearch) {
        if (null == maintionEmployeeSearch.getId() || null == maintionEmployeeSearch.getEnable()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminMaintionEmployee adminMaintionEmployee = adminMaintionEmployeeDao.findById(maintionEmployeeSearch.getId());
        if (null == adminMaintionEmployee) {
            throw new BaseException(ExceptionCode.NO_THIS_USER);
        }
        int res = adminMaintionEmployeeDao.updateEnable(maintionEmployeeSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        if(maintionEmployeeSearch.getEnable()){
            String result = wxMiniService.getAccessToken(Constant.WxMiniMaintain.APP_ID,Constant.WxMiniMaintain.APP_SECRET);
            if (null != result) {
                List<WxTemplateData> params = new ArrayList<>();
                params.add(new WxTemplateData(adminMaintionEmployee.getName(),"#ffffff"));
                params.add(new WxTemplateData("申请审核","#ffffff"));
                params.add(new WxTemplateData("审核通过","#ffffff"));
                params.add(new WxTemplateData(adminMaintionEmployee.getCreateTime().toString(),"#ffffff"));
                params.add(new WxTemplateData(adminMaintionEmployee.getModifyTime().toString(),"#ffffff"));
                WxTemplateMessage wxTextMessage = new WxTemplateMessage(result , adminMaintionEmployee.getMiniWxId(),
                        Constant.WxMiniMaintain.TEMPLATE_ID,adminMaintionEmployee.getFormId(),
                        "pages/index/index?t="+System.currentTimeMillis());
                JsonObject body = new JsonObject();
                body.addProperty("access_token", wxTextMessage.getAccessToken());
                body.addProperty("touser", wxTextMessage.getTouser());
                body.addProperty("template_id", wxTextMessage.getTemplateId());
                body.addProperty("form_id", wxTextMessage.getFormId());
                body.addProperty("page", wxTextMessage.getPage());
                JsonObject bodyData =  new JsonObject();
                JsonObject bodyDataParams =  null;
                for (int i = 0 ; i < params.size() ; i++){
                    bodyDataParams = new JsonObject();
                    bodyDataParams.addProperty("value",params.get(i).getValue());
                    bodyDataParams.addProperty("color",params.get(i).getColor());
                    bodyData.add("keyword"+(i + 1),bodyDataParams);
                }
                body.add("data",bodyData);
                wxMiniService.sendTemplateMessage(result , body.toString());
            }
        }
        return ApiResult.ok();
    }

}
