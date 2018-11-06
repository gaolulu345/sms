package com.tp.admin.controller.wash;


import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.AdminSearch;
import com.tp.admin.data.search.OrderSearch;
import com.tp.admin.data.search.RefundSearch;
import com.tp.admin.service.WashOrderServiceI;
import com.tp.admin.utils.StringUtil;
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

    public static final String ROUTER_INDEX = "/api/private/wash/order";

    @Autowired
    WashOrderServiceI washOrderService;

    @PostMapping(value = "/ter/selection")
    public ApiResult orderTerSelection(HttpServletRequest request) {
        return washOrderService.orderTerSelection(request);
    }

    @PostMapping(value = "/info")
    public ApiResult info(HttpServletRequest request, @RequestBody OrderSearch orderSearch){
        return washOrderService.info(request,orderSearch);
    }

    @PostMapping(value = "/list")
    public ApiResult list(HttpServletRequest request, @RequestBody OrderSearch orderSearch) {
        return washOrderService.list(request, orderSearch);
    }

    @GetMapping(value = "/list/exprot")
    public ResponseEntity<FileSystemResource> listExport(HttpServletRequest request, HttpServletResponse response,
                                                         @RequestParam(value = "st") String st,
                                                         @RequestParam(value = "et") String et,
                                                         @RequestParam(value = "status" , required = false) Integer status,
                                                         @RequestParam(value = "type" , required = false) Integer type,
                                                         @RequestParam(value = "terId" , required = false) Integer terId
                                                         ) {
        OrderSearch search = new OrderSearch();
        search.setStartTime(st);
        search.setEndTime(et);
        if (null != status) {
            search.setStatus(status);
        }
        if (null != type) {
            search.setType(type);
        }
        if (null != terId) {
            search.setTerIds(new int[]{terId});
        }
        return washOrderService.listExport(request, response, search);
    }

}
