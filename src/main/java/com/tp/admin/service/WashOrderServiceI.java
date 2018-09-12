package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.OrderSearch;

import javax.servlet.http.HttpServletRequest;

public interface WashOrderServiceI {

    ApiResult list(HttpServletRequest request , OrderSearch orderSearch);

    ApiResult listExport(HttpServletRequest request , OrderSearch orderSearch);

    ApiResult orderTerSelection(HttpServletRequest request);


}
