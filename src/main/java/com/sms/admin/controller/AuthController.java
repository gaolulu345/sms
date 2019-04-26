package com.sms.admin.controller;


import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.dto.LoginDTO;
import com.sms.admin.service.AccountServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(AuthController.ROUTER_INDEX)
public class AuthController {

    public static final String ROUTER_INDEX = "/api/user";

    @Autowired
    AccountServiceI accountService;

    @PostMapping(value = "/login")
    public ApiResult login(HttpServletRequest request, @RequestBody LoginDTO loginDTO) {
        return accountService.login(request,loginDTO);
    }

    @PostMapping(value = "/logout")
    public ApiResult logout(HttpServletRequest request ,  HttpServletResponse response) {
        return accountService.logout(request , response);
    }

}
