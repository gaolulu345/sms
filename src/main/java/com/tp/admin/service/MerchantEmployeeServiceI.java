package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.MerchantEmployeeSearch;

import javax.servlet.http.HttpServletRequest;

public interface MerchantEmployeeServiceI {

    ApiResult list(HttpServletRequest request , MerchantEmployeeSearch merchantEmployeeSearch);

    ApiResult bachUpdateDeleted(HttpServletRequest request , MerchantEmployeeSearch merchantEmployeeSearch);

    ApiResult updateEnable(HttpServletRequest request , MerchantEmployeeSearch merchantEmployeeSearch);

}
