package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.OrderSearch;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public interface WashOrderServiceI {

    ApiResult list(HttpServletRequest request , OrderSearch orderSearch);

    ResponseEntity<FileSystemResource> listExport(HttpServletRequest request , HttpServletResponse response, OrderSearch orderSearch);

    ApiResult orderTerSelection(HttpServletRequest request);


}
