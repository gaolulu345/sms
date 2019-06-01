package com.sms.admin.controller;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.ProductSearch;
import com.sms.admin.service.ProductServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(ProductController.ROUTE_INDEX)
public class ProductController {

    public static final String ROUTE_INDEX = "/api/private/product";

    @Autowired
    ProductServiceI productService;

    @PostMapping(value = "/list")
    public ApiResult listProduct(HttpServletRequest request, @RequestBody ProductSearch productSearch) {
        return productService.list(request, productSearch);
    }

    @PostMapping(value = "/find/one")
    public ApiResult findById(HttpServletRequest request, @RequestBody ProductSearch productSearch) {
        return productService.findProductOne(request, productSearch);
    }

    @PostMapping(value = "/update/online")
    public ApiResult updateProductOnline(HttpServletRequest request, @RequestBody ProductSearch productSearch) {
        return productService.updateProductOnline(request, productSearch);
    }

    @PostMapping(value = "/update/product")
    public ApiResult updateProduct(HttpServletRequest request, @RequestBody ProductSearch productSearch) {
        return productService.updateProduct(request, productSearch);
    }
}
