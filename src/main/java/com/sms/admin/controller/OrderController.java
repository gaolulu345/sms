package com.sms.admin.controller;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.OrderSearch;
import com.sms.admin.data.search.RangeSearch;
import com.sms.admin.service.OrderServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 最近七天的总的订单数
     * @param request
     * @param rangeSearch
     * @return
     */
    @PostMapping(value = "/range/sum/total")
    public ApiResult orderRangeSumTotal(HttpServletRequest request, @RequestBody RangeSearch rangeSearch) {
        return orderService.orderRangeSumTotal(request, rangeSearch);
    }

    /**
     * 最近30天的每个供应商的商品数量
     * @param request
     * @param rangeSearch
     * @return
     */
    @PostMapping(value = "/range/num/total")
    public ApiResult orderNumTotal(HttpServletRequest request, @RequestBody RangeSearch rangeSearch) {
        return orderService.orderNumTotal(request, rangeSearch);
    }

    @PostMapping(value = "/data/total")
    public ApiResult dataTotal(HttpServletRequest request, @RequestBody RangeSearch rangeSearch) {
        return orderService.dataTotal(request, rangeSearch);
    }

    @PostMapping(value = "/picture/upload")
    public ApiResult uploadPicture(HttpServletRequest request, @RequestPart("file") MultipartFile multipartFile) {
        return orderService.uploadPicture(request, multipartFile);
    }


}
