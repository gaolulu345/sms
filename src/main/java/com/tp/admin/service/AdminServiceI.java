package com.tp.admin.service;


import com.tp.admin.ajax.ApiResult;
import com.tp.admin.data.dto.AdminAccountDTO;
import com.tp.admin.data.dto.ChangePasswordDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.data.search.AdminSearch;
import com.tp.admin.data.search.RefundSearch;

import javax.servlet.http.HttpServletRequest;

public interface AdminServiceI {

    ApiResult register(HttpServletRequest request , AdminAccountDTO adminAccountDTO);

    ApiResult update(HttpServletRequest request , AdminAccount adminAccount);

    ApiResult list(HttpServletRequest request , AdminSearch adminSearch);

    ApiResult listExport(HttpServletRequest request , AdminSearch adminSearch);

    ApiResult bachUpdateDeleted(HttpServletRequest request , AdminSearch adminSearch);

    ApiResult resetPassword(HttpServletRequest request , AdminSearch adminSearch);

    ApiResult updatePassword(HttpServletRequest request, ChangePasswordDTO changePasswordDTO);



}
