package com.sms.admin.service;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.ProductSearch;

import javax.servlet.http.HttpServletRequest;

public interface ProductServiceI {

    ApiResult list(HttpServletRequest request, ProductSearch productSearch);

    ApiResult findProductOne(HttpServletRequest request, ProductSearch productSearch);

    ApiResult updateProductOnline(HttpServletRequest request, ProductSearch productSearch);

    ApiResult updateProduct(HttpServletRequest request, ProductSearch productSearch);
}
