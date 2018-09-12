package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminAccountDao;
import com.tp.admin.data.dto.AdminAccountDTO;
import com.tp.admin.data.dto.ChangePasswordDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.data.search.AdminSearch;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.service.AdminServiceI;
import com.tp.admin.utils.PasswordUtils;
import com.tp.admin.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminServiceI {

    @Autowired
    AdminAccountDao adminAccountDao;

    @Override
    public ApiResult register(HttpServletRequest request, AdminAccountDTO adminAccountDTO) {
        AdminAccount adminAccount = new AdminAccount(adminAccountDTO);
        adminAccount.setPassword(PasswordUtils.defaultPassword());
        int res = adminAccountDao.insert(adminAccount);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult update(HttpServletRequest request, AdminAccount adminAccount) {
        AdminAccount old = adminAccountDao.findById(adminAccount.getId());
        if (null == old) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        int res = adminAccountDao.update(adminAccount);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult list(HttpServletRequest request, AdminSearch adminSearch) {
        adminSearch.build();
        List<AdminAccountDTO> list = adminAccountDao.listBySearch(adminSearch);
        int cnt = adminAccountDao.cntBySearch(adminSearch);
        adminSearch.setResult(list);
        adminSearch.setTotalCnt(cnt);
        return ApiResult.ok(adminSearch);
    }

    @Override
    public ApiResult listExport(HttpServletRequest request, AdminSearch refundSearch) {
        return null;
    }

    @Override
    public ApiResult bachUpdateDeleted(HttpServletRequest request, AdminSearch adminSearch) {
        int res = adminAccountDao.bachUpdateDeleted(adminSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult resetPassword(HttpServletRequest request, AdminSearch adminSearch) {
        int id = adminSearch.getId();
        AdminAccount adminAccount = adminAccountDao.findById(id);
        if (null == adminAccount) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        adminAccount.setPassword(PasswordUtils.defaultPassword());
        int res = adminAccountDao.update(adminAccount);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult updatePassword(HttpServletRequest request, ChangePasswordDTO changePasswordDTO) {
        if (StringUtils.isEmpty(changePasswordDTO.getOpw()) ||
                StringUtils.isEmpty(changePasswordDTO.getNpw()) ||
                StringUtils.isEmpty(changePasswordDTO.getSnpw())) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING, "illegal pw npw snpw ");
        }
        if (!changePasswordDTO.getNpw().equals(changePasswordDTO.getSnpw())) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING, "new password does not match");
        }
        AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
        adminAccount = adminAccountDao.findById(adminAccount.getId());
        if (!changePasswordDTO.getOpw().equals(adminAccount.getPassword())) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING, "old password does not match");
        }
        adminAccount.setPassword(changePasswordDTO.getNpw());
        int res = adminAccountDao.update(adminAccount);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }
}
