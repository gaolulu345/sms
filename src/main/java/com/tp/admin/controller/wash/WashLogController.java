package com.tp.admin.wash.controller;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.WashLogSearch;
import com.tp.admin.service.WashLogServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(WashLogController.ROUTER_INDEX)
public class WashLogController {

    @Autowired
    WashLogServiceI washLogService;

    public static final String ROUTER_INDEX = "/api/private/wash/order/log";

    @PostMapping(value = "/list")
    public ApiResult list(HttpServletRequest request, @RequestBody WashLogSearch washLogSearch) {
        return washLogService.list(request, washLogSearch);
    }

}
