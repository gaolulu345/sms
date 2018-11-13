package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.PartnerSearch;

import javax.servlet.http.HttpServletRequest;

public interface PartnerServiceI {

    ApiResult list(HttpServletRequest request, PartnerSearch partnerSearch);
}
