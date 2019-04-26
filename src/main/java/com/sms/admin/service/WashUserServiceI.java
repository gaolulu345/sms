package com.sms.admin.service;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.UserSearch;

import javax.servlet.http.HttpServletRequest;

public interface WashUserServiceI {

    ApiResult list(HttpServletRequest request , UserSearch userSearch);

    ApiResult listExport(HttpServletRequest request , UserSearch userSearch);

}
