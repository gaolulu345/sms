package com.sms.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PurchaseOrderController.ROUTE_INDEX)
public class PurchaseOrderController {
    public static final String ROUTE_INDEX = "/api/private/purchase/order";


}
