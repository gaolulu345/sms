package com.tp.admin.controller.wash;


import com.tp.admin.ajax.ApiResult;
import com.tp.admin.service.WashDeviceLogServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(WashDeviceLogController.ROUTER_INDEX)
public class WashDeviceLogController {

    public static final String ROUTER_INDEX = "/api/private/device";

    @Autowired
    WashDeviceLogServiceI washDeviceLogService;

    @PostMapping(value = "/operation/log/list")
    public ApiResult deviceOperationLog(HttpServletRequest request){
        return washDeviceLogService.deviceOperationLog(request);
    }


}
