package com.tp.admin.service.implement;

import com.google.gson.JsonObject;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.AdminEmployeeOperatingLogDao;
import com.tp.admin.dao.AdminMerchantEmployeeDao;
import com.tp.admin.dao.PartnerDao;
import com.tp.admin.data.entity.*;
import com.tp.admin.data.search.MerchantEmployeeSearch;
import com.tp.admin.data.table.ResultTable;
import com.tp.admin.data.wx.WxTemplateData;
import com.tp.admin.data.wx.WxTemplateMessage;
import com.tp.admin.enums.AdminEmployeeOperatingLogTypeEnum;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.service.MerchantEmployeeServiceI;
import com.tp.admin.service.WxMiniServiceI;
import com.tp.admin.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MerchantEmployeeServiceImpl implements MerchantEmployeeServiceI {

    @Autowired
    AdminMerchantEmployeeDao adminMerchantEmployeeDao;

    @Autowired
    PartnerDao partnerDao;

    @Autowired
    WxMiniServiceI wxMiniService;

    @Autowired
    AdminEmployeeOperatingLogDao adminEmployeeOperatingLogDao;

    @Override
    public ApiResult list(HttpServletRequest request, MerchantEmployeeSearch merchantEmployeeSearch) {
        merchantEmployeeSearch.builData();
        List<AdminMaintionEmployee> list = adminMerchantEmployeeDao.listBySearch(merchantEmployeeSearch);
        if (null != list && !list.isEmpty()) {
            Integer cnt = adminMerchantEmployeeDao.cntBySearch(merchantEmployeeSearch);
            merchantEmployeeSearch.setResult(list);
            merchantEmployeeSearch.setTotalCnt(cnt);
        }else{
            merchantEmployeeSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(merchantEmployeeSearch));
    }

    @Override
    public ApiResult bachUpdateDeleted(HttpServletRequest request, MerchantEmployeeSearch merchantEmployeeSearch) {
        if (null == merchantEmployeeSearch.getIds() || merchantEmployeeSearch.getIds().length == 0 || null == merchantEmployeeSearch.getDeleted()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        String employeeName = findOperateEmployeeUsername(merchantEmployeeSearch);
        int res = adminMerchantEmployeeDao.bachUpdateDeleted(merchantEmployeeSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        buildEmployeeOperateLog(request,merchantEmployeeSearch, AdminEmployeeOperatingLogTypeEnum.DISABLE_EMPLOYEE,employeeName);
        return ApiResult.ok();
    }

    @Override
    public ApiResult updateEnable(HttpServletRequest request, MerchantEmployeeSearch merchantEmployeeSearch) {
        if (null == merchantEmployeeSearch.getId() || null == merchantEmployeeSearch.getPartnerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        Partner partner = partnerDao.findById(merchantEmployeeSearch.getPartnerId());
        if (null == partner) {
            throw new BaseException(ExceptionCode.NOT_PARTNER);
        }
        AdminMerchantEmployee adminMerchantEmployee = adminMerchantEmployeeDao.findById(merchantEmployeeSearch.getId());
        if (null == adminMerchantEmployee) {
            throw new BaseException(ExceptionCode.NO_THIS_USER);
        }
        int res = adminMerchantEmployeeDao.updateEnable(merchantEmployeeSearch.getId(),merchantEmployeeSearch.getEnable(),
                merchantEmployeeSearch
                .getPartnerId());
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        if(merchantEmployeeSearch.getEnable()){
            String result = wxMiniService.getAccessToken(Constant.WxMiniMerchant.APP_ID,Constant.WxMiniMerchant.APP_SECRET);
            if (null != result) {
                List<WxTemplateData> params = new ArrayList<>();
                params.add(new WxTemplateData(adminMerchantEmployee.getName(),"#ffffff"));
                params.add(new WxTemplateData("申请审核","#ffffff"));
                params.add(new WxTemplateData("审核通过","#ffffff"));
                params.add(new WxTemplateData(adminMerchantEmployee.getCreateTime().toString(),"#ffffff"));
                params.add(new WxTemplateData(adminMerchantEmployee.getModifyTime().toString(),"#ffffff"));
                WxTemplateMessage wxTextMessage = new WxTemplateMessage(result , adminMerchantEmployee.getMiniWxId(),
                        Constant.WxMiniMerchant.TEMPLATE_ID,adminMerchantEmployee.getFormId(),
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

    public final String findOperateEmployeeUsername(MerchantEmployeeSearch merchantEmployeeSearch) {
        if (null == merchantEmployeeSearch.getIds() || merchantEmployeeSearch.getIds().length == 0 || null == merchantEmployeeSearch.getDeleted()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        List<AdminMerchantEmployee> list = adminMerchantEmployeeDao.findByIdsBach(merchantEmployeeSearch);
        if (list == null || list.size() == 0){
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        String string = "";
        for (AdminMerchantEmployee adminMerchantEmployee:list) {
            string += adminMerchantEmployee.getName() + " ";
        }
        return string;
    }

    public final void buildEmployeeOperateLog(HttpServletRequest request,MerchantEmployeeSearch merchantEmployeeSearch,AdminEmployeeOperatingLogTypeEnum adminEmployeeOperatingLogTypeEnum,String employeeNames) {
        AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
        AdminEmployeeOperatingLog adminEmployeeOperatingLog = new AdminEmployeeOperatingLog();
        adminEmployeeOperatingLog.setMerchantId(Arrays.toString(merchantEmployeeSearch.getIds()));
        adminEmployeeOperatingLog.setEmployeeName(employeeNames);
        adminEmployeeOperatingLog.setOperateName(adminAccount.getName());
        adminEmployeeOperatingLog.setTitle(adminEmployeeOperatingLogTypeEnum.getDesc());
        String intros = adminAccount.getName() + " " + adminEmployeeOperatingLogTypeEnum.getDesc() + "【" + employeeNames + "】";
        adminEmployeeOperatingLog.setIntros(intros);
        int res = adminEmployeeOperatingLogDao.insertMerchantEmployeeOperatingLog(adminEmployeeOperatingLog);
        if (res == 0){
            throw new BaseException(ExceptionCode.DB_ERR_EXCEPTION);
        }
    }
}
