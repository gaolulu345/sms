package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.OrderSearch;

import javax.servlet.http.HttpServletRequest;

public interface OrderServiceI {

    ApiResult list(HttpServletRequest request , OrderSearch orderSearch);

    ApiResult orderTerSelection(HttpServletRequest request);
}
