package com.tp.admin.controller;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.service.TerFaultReportLogServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 此控制层用于故障上报在后台显示，供后台管理员查看
 */
@RestController
@RequestMapping(TerFaultReportController.ROUTE_INDEX)
public class TerFaultReportController {

    public static final String ROUTE_INDEX = "/api/private/ter/fault";

    @Autowired
    TerFaultReportLogServiceI terFaultReportLogService;

    @PostMapping(value = "/list")
    public ApiResult reportList(HttpServletRequest request) {
        return terFaultReportLogService.terFaultReportList(request);
    }
}
