package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.AdminTerPropertyDTO;
import com.tp.admin.data.entity.AdminMerchantEmployee;
import com.tp.admin.data.parameter.WxMiniSearch;

import javax.servlet.http.HttpServletRequest;

public interface AdminTerPropertyServiceI {

    ApiResult terPropertySearch(HttpServletRequest request);

    ApiResult onlineFreeStart(HttpServletRequest request);

    ApiResult updateTerProperty(HttpServletRequest request,AdminTerPropertyDTO adminTerPropertyDTO);

    AdminMerchantEmployee check(String openId);

    /**
     * 检验该登陆者是否有该网点操作权限
     */
    void checkTerAndPartner(int terId,int partnerId);


}
