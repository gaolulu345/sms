package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;

import javax.servlet.http.HttpServletRequest;

public interface AdminTemplateInfoServiceI {
    ApiResult sendTemplate(HttpServletRequest request);
}
