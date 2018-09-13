package com.tp.admin.controller;


import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.AdminSearch;
import com.tp.admin.data.search.OrderSearch;
import com.tp.admin.data.search.RefundSearch;
import com.tp.admin.service.WashOrderServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(WashOrderController.ROUTER_INDEX)
public class WashOrderController {

    public static final String ROUTER_INDEX = "/api/private/order";

    @Autowired
    WashOrderServiceI washOrderService;

    @PostMapping(value = "/ter/selection")
    public ApiResult orderTerSelection(HttpServletRequest request) {
        return washOrderService.orderTerSelection(request);
    }

    @PostMapping(value = "/list")
    public ApiResult list(HttpServletRequest request, @RequestBody OrderSearch orderSearch) {
        return washOrderService.list(request, orderSearch);
    }

    @PostMapping(value = "/list/exprot")
    public ResponseEntity<FileSystemResource> listExport(HttpServletRequest request, HttpServletResponse response,
                                                         @RequestBody OrderSearch orderSearch
    ) {
        return washOrderService.listExport(request, response, orderSearch);
    }

}
