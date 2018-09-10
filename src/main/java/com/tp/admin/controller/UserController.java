package com.tp.admin.controller;


import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.service.AccountServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(UserController.ROUTER_INDEX)
public class UserController {

    public static final String ROUTER_INDEX = "/api/user";

    @Autowired
    AccountServiceI accountServiceI;

    @PostMapping(value = "/api/user/login")
    public ApiResult login(HttpServletRequest request, @RequestBody AdminAccount adminAccount) {
        return accountServiceI.login(request,adminAccount);
    }

    @PostMapping(value = "/api/user/logout")
    public ApiResult logout(HttpServletRequest request) {
        return accountServiceI.logout(request);
    }



}
