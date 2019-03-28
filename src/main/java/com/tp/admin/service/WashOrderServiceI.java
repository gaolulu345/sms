package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.entity.Order;
import com.tp.admin.data.search.OrderSearch;
import com.tp.admin.enums.OrderChannelEnum;
import com.tp.admin.enums.OrderTypeEnum;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public interface WashOrderServiceI {

    ApiResult list(HttpServletRequest request , OrderSearch orderSearch);

    ApiResult info(HttpServletRequest request, OrderSearch orderSearch);

    ResponseEntity<FileSystemResource> listExport(HttpServletRequest request , HttpServletResponse response, OrderSearch orderSearch);

    ApiResult orderTerSelection(HttpServletRequest request);

    Order buildOrder(Integer terId, OrderChannelEnum orderChannelEnum, OrderTypeEnum orderTypeEnum);


}
