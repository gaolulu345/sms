package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.AdminEmployeeSearch;

import javax.servlet.http.HttpServletRequest;

public interface AdminEmployeeLogServiceI {

    ApiResult list(HttpServletRequest request, AdminEmployeeSearch adminEmployeeSearch);
}
