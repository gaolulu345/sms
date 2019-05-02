package com.sms.admin.service;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.dto.LoginDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AccountServiceI {

    ApiResult login(HttpServletRequest request,HttpServletResponse response, LoginDTO loginDTO);

    ApiResult logout(HttpServletRequest request , HttpServletResponse response);

    int updateLastLoginTime(int id);

}
