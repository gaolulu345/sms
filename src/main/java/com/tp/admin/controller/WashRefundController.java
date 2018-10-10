package com.tp.admin.controller;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.OrderSearch;
import com.tp.admin.data.search.RefundSearch;
import com.tp.admin.service.WashRefundServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(WashRefundController.ROUTER_INDEX)
public class WashRefundController {

    public static final String ROUTER_INDEX = "/api/private/refund";

    @Autowired
    WashRefundServiceI refundService;

    @PostMapping(value = "/list")
    public ApiResult list(HttpServletRequest request , @RequestBody RefundSearch refundSearch){
        return refundService.list(request,refundSearch);
    }

    @PostMapping(value = "/approved")
    public ApiResult approved(HttpServletRequest request , @RequestBody RefundSearch refundSearch){
        return refundService.approved(request,refundSearch);
    }

    @PostMapping(value = "/back")
    ApiResult payBack(HttpServletRequest request ,@RequestBody  RefundSearch refundSearch){
        return refundService.payBack(request,refundSearch);
    }

    @GetMapping(value = "/list/exprot")
    public ResponseEntity<FileSystemResource> listExport(HttpServletRequest request, HttpServletResponse response,
                                                         @RequestParam(value = "st") String st,
                                                         @RequestParam(value = "et") String et,
                                                         @RequestParam(value = "status" , required = false) Integer status,
                                                         @RequestParam(value = "reason" , required = false) Integer reason
    ) {
        RefundSearch search = new RefundSearch();
        search.setStartTime(st);
        search.setEndTime(et);



        if (null != status) {
            search.setStatus(status);
        }
        if (null != reason) {
            search.setReason(reason);
        }
        return refundService.listExport(request, response, search);
    }

}
