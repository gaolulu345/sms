package com.tp.admin.controller;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.SupplySearch;
import com.tp.admin.service.SupplyServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(SupplyController.ROUTE_INDEX)
public class SupplyController {
    public static final String ROUTE_INDEX = "/api/private/supply";

    @Autowired
    SupplyServiceI supplyService;

    @PostMapping(value = "/list")
    public ApiResult supplyList(HttpServletRequest request, @RequestBody SupplySearch supplySearch) {
        return supplyService.supplyList(request,supplySearch);
    }


    @PostMapping(value = "/detail")
    public ApiResult supplyDetail(HttpServletRequest request, @RequestBody SupplySearch supplySearch) {
        return supplyService.supplyDetail(request, supplySearch);
    }

    @PostMapping(value = "/update/detail")
    public ApiResult updateSuplyDetail(HttpServletRequest request, @RequestBody SupplySearch supplySearch) {
        return supplyService.updateSupplyDetail(request, supplySearch);
    }

    @PostMapping(value = "/update/deleted")
    public ApiResult updateSupplyDeleted(HttpServletRequest request, @RequestBody SupplySearch supplySearch){
        return supplyService.updateSupplyDeleted(request, supplySearch);
    }

    @PostMapping(value = "/add")
    public ApiResult addSupply(HttpServletRequest request, @RequestBody SupplySearch supplySearch){
        return supplyService.addSupply(request, supplySearch);
    }

}
