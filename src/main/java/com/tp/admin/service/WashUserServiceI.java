package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.UserSearch;

import javax.servlet.http.HttpServletRequest;

public interface WashUserServiceI {

    ApiResult list(HttpServletRequest request , UserSearch userSearch);

    ApiResult listExport(HttpServletRequest request , UserSearch userSearch);

}
