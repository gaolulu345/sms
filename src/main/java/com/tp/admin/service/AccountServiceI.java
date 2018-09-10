package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.entity.AdminAccount;

import javax.servlet.http.HttpServletRequest;

public interface AccountServiceI {

    ApiResult login(HttpServletRequest request, AdminAccount adminAccount);

    ApiResult logout(HttpServletRequest request);
}
