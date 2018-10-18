package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.WashLogSearch;

import javax.servlet.http.HttpServletRequest;

public interface WashLogServiceI {

    ApiResult list(HttpServletRequest request, WashLogSearch washLogSearch);
}
