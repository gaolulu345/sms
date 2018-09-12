package com.tp.admin.controller;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.RefundSearch;
import com.tp.admin.service.RefundServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(RefundController.ROUTER_INDEX)
public class RefundController {

    public static final String ROUTER_INDEX = "/api/private/refund";

    @Autowired
    RefundServiceI refundService;

    @PostMapping(value = "/list")
    public ApiResult list(HttpServletRequest request , @RequestBody RefundSearch refundSearch){
        return refundService.list(request,refundSearch);
    }


}
