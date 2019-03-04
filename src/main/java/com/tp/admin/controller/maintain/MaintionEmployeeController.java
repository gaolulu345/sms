package com.tp.admin.controller.maintain;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.MaintionEmployeeSearch;
import com.tp.admin.service.MaintionEmployeeServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(MaintionEmployeeController.ROUTER_INDEX)
public class MaintionEmployeeController {

    public static final String ROUTER_INDEX = "/api/private/maintion/employee";

    @Autowired
    MaintionEmployeeServiceI maintionEmployeeService;

    @PostMapping(value = "/list")
    public ApiResult list(HttpServletRequest request, @RequestBody MaintionEmployeeSearch
            maintionEmployeeSearch) {
        return maintionEmployeeService.list(request, maintionEmployeeSearch);
    }

    @PostMapping(value = "/update/delete")
    public ApiResult bachUpdateDeleted(HttpServletRequest request, @RequestBody MaintionEmployeeSearch
            maintionEmployeeSearch) {
        return maintionEmployeeService.bachUpdateDeleted(request, maintionEmployeeSearch);
    }

    @PostMapping(value = "/update/enable")
    public ApiResult updateEnable(HttpServletRequest request, @RequestBody MaintionEmployeeSearch
            maintionEmployeeSearch) {
        return maintionEmployeeService.updateEnable(request, maintionEmployeeSearch);
    }

    @PostMapping(value = "/update/enable/sm")
    public ApiResult updateEnableSm(HttpServletRequest request, @RequestBody MaintionEmployeeSearch maintionEmployeeSearch) {
        return maintionEmployeeService.updateEnableSm(request, maintionEmployeeSearch);
    }

}
