package com.tp.admin.controller.merchant;


import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.MerchantEmployeeSearch;
import com.tp.admin.service.MerchantEmployeeServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(MerchantEmployeeController.ROUTER_INDEX)
public class MerchantEmployeeController {

    public static final String ROUTER_INDEX = "/api/private/merchant/employee";

    @Autowired
    MerchantEmployeeServiceI merchantEmployeeService;

    @PostMapping(value = "/list")
    public ApiResult list(HttpServletRequest request, MerchantEmployeeSearch merchantEmployeeSearch) {
        return merchantEmployeeService.list(request, merchantEmployeeSearch);
    }

    @PostMapping(value = "/update/delete")
    public ApiResult bachUpdateDeleted(HttpServletRequest request, MerchantEmployeeSearch merchantEmployeeSearch){
        return merchantEmployeeService.bachUpdateDeleted(request, merchantEmployeeSearch);
    }

    @PostMapping(value = "/update/enable")
    public ApiResult updateEnable(HttpServletRequest request, MerchantEmployeeSearch merchantEmployeeSearch){
        return merchantEmployeeService.updateEnable(request, merchantEmployeeSearch);
    }

}
