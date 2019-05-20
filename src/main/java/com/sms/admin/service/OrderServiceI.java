package com.sms.admin.service;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.OrderSearch;
import com.sms.admin.data.search.RangeSearch;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface OrderServiceI {

    ApiResult addOrder(HttpServletRequest request, OrderSearch orderSearch);

    ApiResult orderList(HttpServletRequest request, OrderSearch orderSearch);

    ApiResult orderDetail(HttpServletRequest request, OrderSearch orderSearch);

    ApiResult listAllSupply(HttpServletRequest request);

    ApiResult updateOrder(HttpServletRequest request, OrderSearch orderSearch);

    ApiResult updateDeleted(HttpServletRequest request, OrderSearch orderSearch);

    ResponseEntity<FileSystemResource> orderExport(HttpServletRequest request, HttpServletResponse response, OrderSearch orderSearch);

    ApiResult orderRangeSumTotal(HttpServletRequest request, RangeSearch rangeSearch);

    ApiResult orderNumTotal(HttpServletRequest request, RangeSearch rangeSearch);
}
