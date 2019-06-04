package com.sms.admin.controller;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.OrderSearch;
import com.sms.admin.data.search.PurchaseOrderSearch;
import com.sms.admin.service.PurchaseOrderServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(PurchaseOrderController.ROUTE_INDEX)
public class PurchaseOrderController {
    public static final String ROUTE_INDEX = "/api/private/purchase/order";

    @Autowired
    PurchaseOrderServiceI purchaseOrderService;

    @PostMapping(value = "/list")
    public ApiResult listPurchaseOrder(HttpServletRequest request, @RequestBody PurchaseOrderSearch purchaseOrderSearch) {
        return purchaseOrderService.listPurchaseOrder(request, purchaseOrderSearch);
    }

    @PostMapping(value = "/add")
    public ApiResult addPurchaseOrder(HttpServletRequest request, @RequestBody PurchaseOrderSearch purchaseOrderSearch) {
        return purchaseOrderService.addPurchaseOrder(request, purchaseOrderSearch);
    }

    @GetMapping(value = "/export")
    public ResponseEntity<FileSystemResource> purchaseOrderExport(HttpServletRequest request, HttpServletResponse response,
                                                     @RequestParam(value = "proType", required = false) Integer proType,
                                                     @RequestParam(value = "proId", required = false) Integer proId,
                                                     @RequestParam(value = "startTime", required = false) String startTime,
                                                     @RequestParam(value = "endTime", required = false) String endTime
    ){
        PurchaseOrderSearch purchaseOrderSearch = new PurchaseOrderSearch();
        if (null != proType){
            purchaseOrderSearch.setProType(proType);
        }
        if (null != proId){
            purchaseOrderSearch.setProId(proId);
        }
        if (null != startTime){
            startTime = startTime + " 00:00:00";
            purchaseOrderSearch.setStartTime(startTime);
        }
        if (null != endTime) {
            endTime = endTime + " 23:59:59";
            purchaseOrderSearch.setEndTime(endTime);
        }
        return purchaseOrderService.purchaseOrderExport(request, response, purchaseOrderSearch);
    }


}
