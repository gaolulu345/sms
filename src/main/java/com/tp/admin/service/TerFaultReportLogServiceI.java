package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;

import javax.servlet.http.HttpServletRequest;

/**
 * 网点故障显示
 */
public interface TerFaultReportLogServiceI {

    ApiResult terFaultReportList(HttpServletRequest request);

}
