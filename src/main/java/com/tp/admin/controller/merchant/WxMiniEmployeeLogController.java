package com.tp.admin.controller.merchant;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.AdminEmployeeSearch;
import com.tp.admin.service.AdminEmployeeLogServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(WxMiniEmployeeLogController.ROUTER_INDEX)
public class WxMiniEmployeeLogController {

    public static final String ROUTER_INDEX = "/api/private/wx/employee/log";

    @Autowired
    AdminEmployeeLogServiceI adminEmployeeLogServiceI;

    @PostMapping("/list")
    public ApiResult list(HttpServletRequest request, @RequestBody AdminEmployeeSearch adminEmployeeSearch){
        return adminEmployeeLogServiceI.list(request,adminEmployeeSearch);
    }
}
