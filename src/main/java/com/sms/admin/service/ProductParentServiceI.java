package com.sms.admin.service;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.ProductParentSearch;

import javax.servlet.http.HttpServletRequest;

public interface ProductParentServiceI {
    ApiResult list(HttpServletRequest request, ProductParentSearch productParentSearch);

    ApiResult addProductParent(HttpServletRequest request, ProductParentSearch productParentSearch);

    ApiResult updateProductParentInfo(HttpServletRequest request, ProductParentSearch productParentSearch);

    ApiResult updateProductParentDeleted(HttpServletRequest request, ProductParentSearch productParentSearch);
}
