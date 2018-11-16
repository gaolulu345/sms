package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.AdminSearch;
import com.tp.admin.data.search.MaintionEmployeeSearch;

import javax.servlet.http.HttpServletRequest;

public interface MaintionEmployeeServiceI {

    ApiResult list(HttpServletRequest request , MaintionEmployeeSearch maintionEmployeeSearch);

    ApiResult bachUpdateDeleted(HttpServletRequest request , MaintionEmployeeSearch maintionEmployeeSearch);

    ApiResult updateEnable(HttpServletRequest request , MaintionEmployeeSearch maintionEmployeeSearch);

}
