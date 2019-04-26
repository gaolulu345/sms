package com.sms.admin.service;

import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.search.SupplySearch;

import javax.servlet.http.HttpServletRequest;

public interface SupplyServiceI {

    ApiResult supplyList(HttpServletRequest request, SupplySearch supplySearch);

    ApiResult supplyDetail(HttpServletRequest request, SupplySearch supplySearch);

    ApiResult updateSupplyDetail(HttpServletRequest request, SupplySearch supplySearch);

    ApiResult updateSupplyDeleted(HttpServletRequest request, SupplySearch supplySearch);

    ApiResult addSupply(HttpServletRequest request, SupplySearch supplySearch);
}
