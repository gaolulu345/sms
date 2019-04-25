package com.tp.admin.controller;


import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.AdminAccountDTO;
import com.tp.admin.data.dto.ChangePasswordDTO;
import com.tp.admin.data.search.AdminSearch;
import com.tp.admin.service.AdminServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ApiResult update(HttpServletRequest request ,@RequestBody AdminAccountDTO adminAccountDTO){
        return adminService.update(request,adminAccountDTO);
    }

    @PostMapping(value = "/update/pw")
    public ApiResult updatePassword(HttpServletRequest request, @RequestBody ChangePasswordDTO changePasswordDTO){
        return adminService.updatePassword(request,changePasswordDTO);
    }

    @PostMapping(value = "/list")
    public ApiResult list(HttpServletRequest request ,@RequestBody AdminSearch adminSearch){
        return adminService.list(request,adminSearch);
    }

    @PostMapping(value = "/update/delete")
    public ApiResult bachUpdateDeleted(HttpServletRequest request ,@RequestBody AdminSearch adminSearch){
        return adminService.bachUpdateDeleted(request,adminSearch);
    }

    @PostMapping(value = "/reset/pw")
    public ApiResult resetPassword(HttpServletRequest request ,@RequestBody AdminSearch adminSearch){
        return adminService.resetPassword(request,adminSearch);
    }

    @PostMapping(value = "/login/log")
    public ApiResult loginLog(HttpServletRequest request ,@RequestBody AdminSearch adminSearch){
        return adminService.loginLog(request,adminSearch);
    }

    @GetMapping(value = "/export")
    public ResponseEntity<FileSystemResource> export(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "deleted") Boolean deleted){
        AdminSearch adminSearch = new AdminSearch();
        adminSearch.setDeleted(deleted);
        return adminService.adminExport(request, response, adminSearch);
    }


}
