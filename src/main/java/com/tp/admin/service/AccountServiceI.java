package com.tp.admin.service;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.LoginDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.security.AutoResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

public interface AccountServiceI {

    ApiResult login(HttpServletRequest request, LoginDTO loginDTO);

    ApiResult logout(HttpServletRequest request , HttpServletResponse response);

    AdminAccount findByUsername(String username);

    int updateLastLoginTime(int id);

}
