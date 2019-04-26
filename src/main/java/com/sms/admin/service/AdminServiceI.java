package com.sms.admin.service;


import com.sms.admin.ajax.ApiResult;
import com.sms.admin.data.dto.AdminAccountDTO;
import com.sms.admin.data.dto.ChangePasswordDTO;
import com.sms.admin.data.search.AdminSearch;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AdminServiceI {

    ApiResult register(HttpServletRequest request , AdminAccountDTO adminAccountDTO);

    ApiResult update(HttpServletRequest request , AdminAccountDTO adminAccountDTO);

    ApiResult list(HttpServletRequest request , AdminSearch adminSearch);

    ApiResult bachUpdateDeleted(HttpServletRequest request , AdminSearch adminSearch);

    ApiResult resetPassword(HttpServletRequest request , AdminSearch adminSearch);

    ApiResult loginLog(HttpServletRequest request , AdminSearch adminSearch);

    ApiResult updatePassword(HttpServletRequest request, ChangePasswordDTO changePasswordDTO);

    ResponseEntity<FileSystemResource> adminExport(HttpServletRequest request, HttpServletResponse response, AdminSearch adminSearch);



}
