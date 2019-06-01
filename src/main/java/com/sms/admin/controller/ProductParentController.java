package com.sms.admin.controller;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.dao.ProductParentDao;
import com.sms.admin.data.search.ProductParentSearch;
import com.sms.admin.service.ProductParentServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(ProductParentController.ROUTE_INDEX)
public class ProductParentController {

    public static final String ROUTE_INDEX = "/api/private/product/parent";

    @Autowired
    ProductParentServiceI productParentServiceI;

    @PostMapping(value = "/list")
    public ApiResult listProductParent(HttpServletRequest request, @RequestBody ProductParentSearch productParentSearch) {
        return productParentServiceI.list(request, productParentSearch);
    }

    
}
