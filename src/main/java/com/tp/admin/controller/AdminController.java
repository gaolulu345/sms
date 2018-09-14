package com.tp.admin.controller;


import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.AdminAccountDTO;
import com.tp.admin.data.dto.ChangePasswordDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.data.search.AdminSearch;
import com.tp.admin.service.AdminServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(AdminController.ROUTER_INDEX)
public class AdminController {

    public static final String ROUTER_INDEX = "/api/private/admin";

    @Autowired
    AdminServiceI adminService;

    @PostMapping(value = "/register")
    public ApiResult register(HttpServletRequest request ,@RequestBody AdminAccountDTO adminAccountDTO){
        return adminService.register(request,adminAccountDTO);
    }

    @PostMapping(value = "/update")
    public ApiResult update(HttpServletRequest request ,@RequestBody AdminAccount adminAccount){
        return adminService.update(request,adminAccount);
    }

    @PostMapping(value = "/update/pw")
    public ApiResult updatePassword(HttpServletRequest request, @RequestBody ChangePasswordDTO changePasswordDTO){
        return adminService.updatePassword(request,changePasswordDTO);
    }

    @PostMapping(value = "/list")
    public ApiResult list(HttpServletRequest request ,@RequestBody AdminSearch adminSearch){
        return adminService.list(request,adminSearch);
    }

    @PostMapping(value = "/list/exprot")
    ApiResult listExport(HttpServletRequest request , @RequestBody AdminSearch adminSearch){
        return adminService.listExport(request,adminSearch);
    }

    @PostMapping(value = "/update/delete")
    public ApiResult bachUpdateDeleted(HttpServletRequest request ,@RequestBody AdminSearch adminSearch){
        return adminService.bachUpdateDeleted(request,adminSearch);
    }

    @PostMapping(value = "/reset/pw")
    public ApiResult resetPassword(HttpServletRequest request ,@RequestBody AdminSearch adminSearch){
        return adminService.resetPassword(request,adminSearch);
    }






}
