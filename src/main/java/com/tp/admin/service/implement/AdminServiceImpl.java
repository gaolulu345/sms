package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminAccountDao;
import com.tp.admin.dao.AdminPkAccountRolesDao;
import com.tp.admin.dao.AdminRolesDao;
import com.tp.admin.data.dto.AdminAccountDTO;
import com.tp.admin.data.dto.ChangePasswordDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.data.entity.AdminPkAccountRoles;
import com.tp.admin.data.entity.AdminRoles;
import com.tp.admin.data.entity.Refund;
import com.tp.admin.data.search.AdminSearch;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.manage.TransactionalServiceI;
import com.tp.admin.service.AdminServiceI;
import com.tp.admin.utils.PasswordUtils;
import com.tp.admin.utils.SessionUtils;
import com.tp.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminServiceI {

    @Autowired
    AdminAccountDao adminAccountDao;

    @Autowired
    AdminRolesDao adminRolesDao;

    @Autowired
    AdminPkAccountRolesDao adminPkAccountRolesDao;

    @Autowired
    TransactionalServiceI transactionalService;

    @Override
    public ApiResult register(HttpServletRequest request, AdminAccountDTO adminAccountDTO) {
        if(StringUtil.isEmpty(adminAccountDTO.getName()) ||
        StringUtil.isEmpty(adminAccountDTO.getUsername()) ||
        StringUtil.isEmpty(adminAccountDTO.getIntros()) ||
        adminAccountDTO.getRolesId() < 0 ){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminRoles roles = adminRolesDao.findById(adminAccountDTO.getRolesId());
        if (null == roles || roles.isDeleted() || !roles.isEnable()) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminAccount adminAccount = new AdminAccount(adminAccountDTO);
        adminAccount.setPassword(PasswordUtils.defaultPassword());
        transactionalService.register(adminAccount ,roles);
        return ApiResult.ok();
    }

    @Override
    public ApiResult update(HttpServletRequest request, AdminAccountDTO adminAccountDTO) {
        if(StringUtil.isEmpty(adminAccountDTO.getName()) ||
                StringUtil.isEmpty(adminAccountDTO.getIntros()) ||
                adminAccountDTO.getRolesId() < 0 ){
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminAccount old = adminAccountDao.findById(adminAccountDTO.getId());
        if (null == old) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
        AdminPkAccountRoles adminPkAccountRoles = adminPkAccountRolesDao.findByAdminId(old.getId());
        AdminAccount adminAccount = new AdminAccount(adminAccountDTO);
        adminAccount.setId(adminAccountDTO.getId());
        adminPkAccountRoles.setRolesId(adminAccountDTO.getRolesId());
        int res = adminPkAccountRolesDao.update(adminPkAccountRoles);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        res = adminAccountDao.update(adminAccount);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }

    @Override
    public ApiResult list(HttpServletRequest request, AdminSearch adminSearch) {
        adminSearch.builData();
        List<AdminAccountDTO> list = adminAccountDao.listBySearch(adminSearch);
        if (null != list && !list.isEmpty()) {
            int cnt = adminAccountDao.cntBySearch(adminSearch);
            adminSearch.setResult(list);
            adminSearch.setTotalCnt(cnt);
        }else{
            adminSearch.setTotalCnt(0);
        }
        return ApiResult.ok(adminSearch);
    }


    @Override
    public ApiResult bachUpdateDeleted(HttpServletRequest request, AdminSearch adminSearch) {
        if (null == adminSearch.getIds() || adminSearch.getIds().length == 0) {
            throw new BaseException(ExceptionCode.PARAMETER_WRONG);
        }
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
