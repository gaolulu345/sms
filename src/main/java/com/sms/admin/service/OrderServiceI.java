package com.sms.admin.service;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.OrderSearch;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface OrderServiceI {

    ApiResult orderList(HttpServletRequest request, OrderSearch orderSearch);

    ResponseEntity<FileSystemResource> orderExport(HttpServletRequest request, HttpServletResponse response, OrderSearch orderSearch);
}
