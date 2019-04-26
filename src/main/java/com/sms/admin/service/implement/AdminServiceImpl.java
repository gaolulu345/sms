package com.sms.admin.service.implement;

import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.sms.admin.dao.*;
import com.sms.admin.data.entity.AdminAccountLoginLog;
import com.sms.admin.data.entity.AdminPkAccountRoles;
import com.sms.admin.data.entity.AdminRoles;
import com.sms.admin.data.table.ResultTable;
import com.sms.admin.ajax.ApiResult;
import com.sms.admin.common.Constant;
import com.sms.admin.data.dto.AdminAccountDTO;
import com.sms.admin.data.dto.ChangePasswordDTO;
import com.sms.admin.data.entity.AdminAccount;
import com.sms.admin.data.search.AdminSearch;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.manage.TransactionalServiceI;
import com.sms.admin.service.AdminServiceI;
import com.sms.admin.utils.ExcelUtil;
import com.sms.admin.utils.PasswordUtils;
import com.sms.admin.utils.SessionUtils;
import com.sms.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
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
    AdminAccountLoginLogDao adminAccountLoginLogDao;

    @Autowired
    TransactionalServiceI transactionalService;

    @Autowired
    AdminAccountInfoDao adminAccountInfoDao;

    @Override
    public ApiResult register(HttpServletRequest request, AdminAccountDTO adminAccountDTO) {
        if(StringUtil.isEmpty(adminAccountDTO.getUsercode()) ||
        StringUtil.isEmpty(adminAccountDTO.getName()) ||
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
                null == adminAccountDTO.getBornDate() ||
                StringUtil.isEmpty(adminAccountDTO.getTelephone()) ||
                StringUtil.isEmpty(adminAccountDTO.getAddress()) ||
                adminAccountDTO.getGender() < 0 ||
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
        /*res = adminAccountDao.update(adminAccount);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }*/
        res = adminAccountInfoDao.update(adminAccount);
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
            Integer cnt = adminAccountDao.cntBySearch(adminSearch);
            adminSearch.setResult(list);
            adminSearch.setTotalCnt(cnt);
        }else{
            adminSearch.setTotalCnt(0);
        }
        return ApiResult.ok(new ResultTable(adminSearch));
    }

    @Override
    public ApiResult bachUpdateDeleted(HttpServletRequest request, AdminSearch adminSearch) {
        if (null == adminSearch.getIds() || adminSearch.getIds().length == 0 || null == adminSearch.getDeleted()) {
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
    public ApiResult loginLog(HttpServletRequest request, AdminSearch adminSearch) {
        AdminAccount adminAccount = SessionUtils.findSessionAdminAccount(request);
        Boolean more = adminSearch.getMore();
        if (null == more || more == false) {
            adminSearch.setPageIndex(1);
            adminSearch.setPageSize(5);
        }else{
            adminSearch.setPageIndex(1);
            adminSearch.setPageSize(50);
        }
        adminSearch.builData();
        List<AdminAccountLoginLog> list = adminAccountLoginLogDao.listBySearch(more , adminAccount.getUsername() ,
                adminSearch.getPageSize() , adminSearch.getOffset());
        if (null != list && !list.isEmpty()) {
            adminSearch.setResult(list);
        }
        return ApiResult.ok(new ResultTable(adminSearch));
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

    @Override
    public ResponseEntity<FileSystemResource> adminExport(HttpServletRequest request, HttpServletResponse response, AdminSearch adminSearch) {
        List<AdminAccountDTO> list = adminAccountDao.listBySearch(adminSearch);
        if (null != list && !list.isEmpty()) {
            for (AdminAccountDTO temp:list) {
                temp.build();
            }
        }

        String fileName = ExcelUtil.createXlxs(Constant.ADMIN_LIST, Math.random() * 10 + "",Math.random() * 10 + "");
        String path = System.getProperty(Constant.TMP_DIR) + Constant._XLSX_DIR;
        File pathFile = new File(path);
        if (!pathFile.exists()){
            pathFile.mkdirs();
        }
        try {
            ExcelUtils.getInstance().exportObjects2Excel(list,AdminAccountDTO.class,true,"sheet0", true,path + fileName);
        } catch (Excel4JException | IOException e) {
            e.printStackTrace();
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        File file = new File(path + fileName);
        return ExcelUtil.fileExcel(request,fileName,file);
    }


}
