package com.sms.admin.controller;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.OrderSearch;
import com.sms.admin.service.OrderServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(OrderController.ROUTE_INDEX)
public class OrderController {
    public static final String ROUTE_INDEX = "/api/private/order";

    @Autowired
    OrderServiceI orderService;

    @PostMapping(value = "/add")
    public ApiResult orderAdd(HttpServletRequest request, @RequestBody OrderSearch orderSearch) {
        return orderService.addOrder(request, orderSearch);
    }

    @PostMapping("/list")
    public ApiResult orderList(HttpServletRequest request, @RequestBody OrderSearch orderSearch){
        return orderService.orderList(request, orderSearch);
    }

    @PostMapping(value = "/detail")
    public ApiResult orderDetail(HttpServletRequest request, @RequestBody OrderSearch orderSearch) {
        return orderService.orderDetail(request, orderSearch);
    }

    @PostMapping(value = "/update")
    public ApiResult updateOrder(HttpServletRequest request, @RequestBody OrderSearch orderSearch) {
        return orderService.updateOrder(request, orderSearch);
    }

    @PostMapping(value = "/update/deleted")
    public ApiResult updateDeleted(HttpServletRequest request, @RequestBody OrderSearch orderSearch) {
        return orderService.updateDeleted(request, orderSearch);
    }

    @PostMapping(value = "/list/all/supply")
    public ApiResult listAllSupply(HttpServletRequest request){
        return orderService.listAllSupply(request);
    }

    @GetMapping(value = "/export")
    public ResponseEntity<FileSystemResource> export(HttpServletRequest request, HttpServletResponse response,
                                                     @RequestParam(value = "goodName", required = false) String goodName,
                                                     @RequestParam(value = "supplyId", required = false) Integer supplyId,
                                                     @RequestParam(value = "status", required = false) Integer status,
                                                     @RequestParam(value = "startTime", required = false) String startTime,
                                                     @RequestParam(value = "endTime", required = false) String endTime
    ){
        OrderSearch orderSearch = new OrderSearch();
        if (null != goodName){
            orderSearch.setGoodName(goodName);
        }
        if (null != supplyId){
            orderSearch.setSupplyId(supplyId);
        }
        if (null != status){
            orderSearch.setStatus(status);
        }
        if (null != startTime){
            orderSearch.setStartTime(startTime);
        }
        if (null != endTime) {
            orderSearch.setEndTime(endTime);
        }
        return orderService.orderExport(request, response, orderSearch);
    }


}
