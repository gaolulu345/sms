package com.tp.admin.controller;


import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.OrderSearch;
import com.tp.admin.data.search.RefundSearch;
import com.tp.admin.service.WashOrderServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(WashOrderController.ROUTER_INDEX)
public class WashOrderController {

    public static final String ROUTER_INDEX = "/api/private/order";

    @Autowired
    WashOrderServiceI orderService;

    @PostMapping(value = "/ter/selection")
    public ApiResult orderTerSelection(HttpServletRequest request){
        return orderService.orderTerSelection(request);
    }

    @PostMapping(value = "/list")
    public ApiResult list(HttpServletRequest request , @RequestBody OrderSearch orderSearch){
        return orderService.list(request,orderSearch);
    }



}
