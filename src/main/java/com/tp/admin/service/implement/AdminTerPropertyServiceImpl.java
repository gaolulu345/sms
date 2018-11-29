package com.tp.admin.service.implement;

import com.google.gson.Gson;
import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminMerchantEmployeeDao;
import com.tp.admin.dao.PartnerDao;
import com.tp.admin.dao.TerDao;
import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.data.entity.AdminMerchantEmployee;
import com.tp.admin.data.entity.Partner;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.HttpHelperI;
import com.tp.admin.service.AdminTerPropertyServiceI;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AdminTerPropertyServiceImpl implements AdminTerPropertyServiceI {

    @Autowired
    HttpHelperI httpHelper;

    @Autowired
    TerDao terDao;

    @Autowired
    AdminMerchantEmployeeDao adminMerchantEmployeeDao;

    @Autowired
    PartnerDao partnerDao;

    @Override
    public ApiResult terPropertySearch(HttpServletRequest request) {
        String body = httpHelper.jsonBody(request);
        WxMiniSearch wxMiniSearch = new Gson().fromJson(body, WxMiniSearch.class);
        if (null == wxMiniSearch.getTerId()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty terId");
        }
        AdminMerchantEmployee adminMerchantEmployee = check(wxMiniSearch.getOpenId());
        List<Integer> terIds = terDao.findRelatedTerByPartnerId(adminMerchantEmployee.getPartnerId());
        if (null != terIds && !terIds.isEmpty()){
            int flag = 0;
            for (int terId : terIds) {
                if (wxMiniSearch.getTerId() == terId){
                    flag = 1;
                }
            }
            if (flag == 0){
                throw new BaseException(ExceptionCode.PARAMETER_WRONG,"terId mismatch partnerId");
            }
        }
        AdminTerPropertyDTO adminTerPropertyDTO =  terDao.findTerStartInfo(wxMiniSearch.getTerId());
        adminTerPropertyDTO.build();
        return ApiResult.ok(adminTerPropertyDTO);
    }

    @Override
    public AdminMerchantEmployee check(String openId) {
        if (StringUtils.isBlank(openId)) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG, "empty openId");
        }
        AdminMerchantEmployee adminMerchantEmployee = adminMerchantEmployeeDao.findByWxMiniId(openId);
        if (null == adminMerchantEmployee) {
            throw new BaseException(ExceptionCode.NO_THIS_USER);
        }
        if (!adminMerchantEmployee.isEnable()) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        if (adminMerchantEmployee.isDeleted()) {
            throw new BaseException(ExceptionCode.USER_DELETE_REGISTERED);
        }
        if (adminMerchantEmployee.getPartnerId() == 0) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        Partner partner = partnerDao.findById(adminMerchantEmployee.getPartnerId());
        if (null == partner) {
            throw new BaseException(ExceptionCode.USER_NOT_PERMISSION);
        }
        return adminMerchantEmployee;
    }
}
