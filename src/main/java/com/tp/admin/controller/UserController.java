package com.tp.admin.controller;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.search.OrderSearch;
import com.tp.admin.data.search.UserSearch;
import com.tp.admin.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(UserController.ROUTER_INDEX)
public class UserController {

    public static final String ROUTER_INDEX = "/api/private/user";

    @Autowired
    UserServiceI userService;

    @PostMapping(value = "/list")
    public ApiResult list(HttpServletRequest request , @RequestBody UserSearch userSearch){
        return userService.list(request,userSearch);
    }

}
