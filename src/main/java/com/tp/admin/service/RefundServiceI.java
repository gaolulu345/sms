package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.RefundSearch;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface RefundServiceI {

    ApiResult list(HttpServletRequest request , RefundSearch refundSearch);
}
