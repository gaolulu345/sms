package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.dao.AdminAccountDao;
import com.tp.admin.data.dto.AdminAccountDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.data.search.AdminSearch;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.service.AdminServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class AdminServiceImplement implements AdminServiceI {

    @Autowired
    AdminAccountDao adminAccountDao;

    @Override
    public ApiResult register(HttpServletRequest request, AdminAccountDTO adminAccountDTO) {
        AdminAccount adminAccount = new AdminAccount(adminAccountDTO);
        // 暂时采用MD5 32位编码
        String newPwd = "123456";
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(newPwd.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new BaseException(ExceptionCode.UNKNOWN_EXCEPTION);
        }
        adminAccount.setPassword(result);
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
    public ApiResult bachUpdateDeleted(HttpServletRequest request, AdminSearch adminSearch) {
        int res = adminAccountDao.bachUpdateDeleted(adminSearch);
        if (res == 0) {
            throw new BaseException(ExceptionCode.DB_BUSY_EXCEPTION);
        }
        return ApiResult.ok();
    }
}
