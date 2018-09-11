package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.security.AutoResource;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface AccountServiceI {

    ApiResult login(HttpServletRequest request, AdminAccount adminAccount);

    ApiResult logout(HttpServletRequest request);

    AdminAccount findByUsername(String username);

    int updateLastLoginTime(int id);

    Set<AutoResource> findAdminAutoResource(int id);

}
