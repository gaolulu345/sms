package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.RefundSearch;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WashRefundServiceI {

    ApiResult list(HttpServletRequest request , RefundSearch refundSearch);

    ResponseEntity<FileSystemResource> listExport(HttpServletRequest request, HttpServletResponse response , RefundSearch refundSearch);

    ApiResult approved(HttpServletRequest request , RefundSearch refundSearch);

    ApiResult payBack(HttpServletRequest request , RefundSearch refundSearch);

}
