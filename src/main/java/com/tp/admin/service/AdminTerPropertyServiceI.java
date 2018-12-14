package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.data.dto.TerInfoDTO;
import com.tp.admin.data.entity.AdminMerchantEmployee;
import com.tp.admin.data.parameter.WxMiniSearch;
import com.tp.admin.data.search.TerPropertySearch;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;

import javax.servlet.http.HttpServletRequest;

public interface AdminTerPropertyServiceI {
    ApiResult allTerPropertyInfoList(HttpServletRequest request);

    ApiResult terPropertySearch(HttpServletRequest request);

    ApiResult onlineFreeStart(HttpServletRequest request);

    ApiResult updateTerProperty(HttpServletRequest request,AdminTerPropertyDTO adminTerPropertyDTO);

    Object check(String openId);

    void buildTerOperateLog(Object object, TerInfoDTO terInfoDTO,WashTerOperatingLogTypeEnum washTerOperatingLogTypeEnum, String img, Boolean
            sucess);

}
