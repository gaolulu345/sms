package com.sms.admin.controller;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.SupplySearch;
import com.sms.admin.service.SupplyServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @GetMapping(value = "/export")
    public ResponseEntity<FileSystemResource> export(HttpServletRequest request, HttpServletResponse response,
                                                     @RequestParam(value = "supplyCode", required = false) String supplyCode,
                                                     @RequestParam(value = "supplyName", required = false) String supplyName,
                                                     @RequestParam(value = "contactPhone", required = false) String contactPhone,
                                                     @RequestParam(value = "fax", required = false) String fax,
                                                     @RequestParam(value = "startTime", required = false) String startTime,
                                                     @RequestParam(value = "endTime", required = false) String endTime
                                                     ){
        SupplySearch supplySearch = new SupplySearch();
        if (null != supplyCode){
            supplySearch.setSupplyCode(supplyCode);
        }
        if (null != supplyName){
            supplySearch.setSupplyName(supplyName);
        }
        if (null != contactPhone){
            supplySearch.setContactPhone(contactPhone);
        }
        if (null != fax){
            supplySearch.setFax(fax);
        }
        if (null != startTime){
            supplySearch.setStartTime(startTime);
        }
        if (null != endTime) {
            supplySearch.setEndTime(endTime);
        }
        return supplyService.supplyExport(request, response, supplySearch);
    }

}
