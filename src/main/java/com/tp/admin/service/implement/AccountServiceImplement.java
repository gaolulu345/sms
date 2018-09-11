package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.AdminAccountDao;
import com.tp.admin.data.dto.AdminAccountDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.security.AutoResource;
import com.tp.admin.service.AccountServiceI;
import com.tp.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Service
public class AccountServiceImplement implements AccountServiceI {

    @Autowired
    AdminAccountDao adminAccountDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AdminAccount findByUsername(String username) {
        return adminAccountDao.findByUsername(username);
    }

    @Override
    public int updateLastLoginTime(int id) {
        return adminAccountDao.updateLastLoginTime(id,new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public Set<AutoResource> findAdminAutoResource(int id) {
        return new HashSet<>();
    }

    @Override
    public ApiResult login(HttpServletRequest request, AdminAccount adminAccount) {
        if (StringUtil.isEmpty(adminAccount.getUsername()) || StringUtil.isEmpty(adminAccount.getPassword())) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING, "neither name nor password should be empty when partner login");
        }
        AdminAccount user = findByUsername(adminAccount.getUsername());
        if (user == null) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING, "no user found math username:" + adminAccount.getUsername());
        }
        if (!adminAccount.getPassword().equals(user.getPassword())) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING, "no user found math username:" + adminAccount
                    .getUsername());
        }
        // TODO 这里需要改进,超级管理员账号可以配置。这样避免吧自己也删除了。
        if (!"TP_AUTO".equals(adminAccount.getUsername())) {
            if (user.isDeleted()) {
                throw new BaseException(ExceptionCode.PARAMETER_MISSING, "user deleted:" + adminAccount.getUsername());
            }
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(adminAccount
                .getUsername(), adminAccount.getPassword());
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authRequest); //调用loadUserByUsername
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != authentication) {
            updateLastLoginTime(user.getId());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession();
            session.setAttribute(Constant.SECURITY_CONTEXT, SecurityContextHolder.getContext()); // 这个非常重要，否则验证后将无法登陆
            AdminAccountDTO adminAccountDTO = new AdminAccountDTO();
            adminAccountDTO.setId(user.getId());
            adminAccountDTO.setName(user.getName());
            return ApiResult.ok(adminAccountDTO);
        }
        return ApiResult.error(ExceptionCode.INVALID_ACCESS_EXCEPTION);
    }

    @Override
    public ApiResult logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) {
            return ApiResult.error(ExceptionCode.INVALID_ACCESS_EXCEPTION);
        }
        SecurityContext sctx = (SecurityContext) session.getAttribute(Constant.SECURITY_CONTEXT);
        if (sctx == null) {
            return ApiResult.error(ExceptionCode.INVALID_ACCESS_EXCEPTION);
        }
        sctx.getAuthentication().setAuthenticated(false);
        session.invalidate();
        return ApiResult.ok();
    }
}
