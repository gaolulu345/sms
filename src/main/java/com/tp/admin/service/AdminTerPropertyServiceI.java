package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.entity.AdminMerchantEmployee;

import javax.servlet.http.HttpServletRequest;

public interface AdminTerPropertyServiceI {

    ApiResult terPropertySearch(HttpServletRequest request);

    ApiResult onlineFreeStart(HttpServletRequest request);

    AdminMerchantEmployee check(String openId);
}
