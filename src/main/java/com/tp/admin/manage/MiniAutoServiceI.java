package com.tp.admin.manage;

import com.tp.admin.ajax.ApiResult;

import javax.servlet.http.HttpServletRequest;

public interface MiniAutoServiceI {

    ApiResult miniWxAuto(HttpServletRequest request);

    int check(HttpServletRequest request);
}
