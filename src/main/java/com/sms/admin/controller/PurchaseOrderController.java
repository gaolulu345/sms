package com.sms.admin.controller;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.PurchaseOrderSearch;
import com.sms.admin.service.PurchaseOrderServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(PurchaseOrderController.ROUTE_INDEX)
public class PurchaseOrderController {
    public static final String ROUTE_INDEX = "/api/private/purchase/order";

    @Autowired
    PurchaseOrderServiceI purchaseOrderService;

    /*@PostMapping(value = "/list")
    public ApiResult listPurchaseOrder(HttpServletRequest request, @RequestBody PurchaseOrderSearch purchaseOrderSearch) {
        return
    }*/
}
