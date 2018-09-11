package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.entity.AdminMenu;
import com.tp.admin.data.entity.AdminOperations;
import com.tp.admin.data.entity.AdminRoles;
import com.tp.admin.data.search.SystemSearch;

import javax.servlet.http.HttpServletRequest;

public interface SystemServiceI {

    ApiResult findOperationsMaps(HttpServletRequest request);

    ApiResult findMenuMaps(HttpServletRequest request);

    void verify(AdminMenu adminMenu);

    ApiResult insert(HttpServletRequest request, AdminMenu adminMenu);

    ApiResult update(HttpServletRequest request, AdminMenu adminMenu);

    ApiResult listSysMenuBySearch(HttpServletRequest request, SystemSearch systemSearch);

    ApiResult bachUpdateSysMenuEnable(HttpServletRequest request, SystemSearch systemSearch);

    ApiResult bachUpdateSysMenuDeleted(HttpServletRequest request, SystemSearch systemSearch);

    void verify(AdminOperations adminOperations);

    ApiResult insert(HttpServletRequest request, AdminOperations adminOperations);

    ApiResult update(HttpServletRequest request, AdminOperations adminOperations);

    ApiResult listSysOperationsBySearch(HttpServletRequest request, SystemSearch systemSearch);

    ApiResult bachUpdateSysOperationsEnable(HttpServletRequest request, SystemSearch systemSearch);

    ApiResult bachUpdateSysOperationsDeleted(HttpServletRequest request, SystemSearch systemSearch);

    void verify(AdminRoles adminRoles);

    ApiResult insert(HttpServletRequest request, AdminRoles adminRoles);

    ApiResult update(HttpServletRequest request, AdminRoles adminRoles);

    ApiResult listSysRolesBySearch(HttpServletRequest request, SystemSearch systemSearch);

    ApiResult bachUpdateSysRolesEnable(HttpServletRequest request, SystemSearch systemSearch);

    ApiResult bachUpdateSysRolesDeleted(HttpServletRequest request, SystemSearch systemSearch);

    ApiResult findAllPermission(HttpServletRequest request, SystemSearch systemSearch);
}
