package com.sms.admin.service;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.SupplySearch;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SupplyServiceI {

    ApiResult supplyList(HttpServletRequest request, SupplySearch supplySearch);

    ApiResult supplyDetail(HttpServletRequest request, SupplySearch supplySearch);

    ApiResult updateSupplyDetail(HttpServletRequest request, SupplySearch supplySearch);

    ApiResult updateSupplyDeleted(HttpServletRequest request, SupplySearch supplySearch);

    ApiResult addSupply(HttpServletRequest request, SupplySearch supplySearch);

    ResponseEntity<FileSystemResource> supplyExport(HttpServletRequest request, HttpServletResponse response, SupplySearch supplySearch);
}
