package com.tp.admin.service.implement;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.common.Constant;
import com.tp.admin.dao.AdminAccountDao;
import com.tp.admin.dao.AdminAccountLoginLogDao;
import com.tp.admin.data.dto.AdminAccountDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.data.entity.AdminAccountLoginLog;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;
import com.tp.admin.security.AutoResource;
import com.tp.admin.service.AccountServiceI;
import com.tp.admin.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class AccountServiceImpl implements AccountServiceI {

    private Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    AdminAccountDao adminAccountDao;

    @Autowired
    AdminAccountLoginLogDao adminAccountLoginLogDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AdminAccount findByUsername(String username) {
        return adminAccountDao.findByUsername(username);
    }

    @Override
    public int updateLastLoginTime(int id) {
        return adminAccountDao.updateLastLoginTime(id, new Timestamp(System.currentTimeMillis()));
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
            loginlog(request,adminAccount,false);
            throw new BaseException(ExceptionCode.PARAMETER_MISSING, "no user found math username:" + adminAccount
                    .getUsername());
        }

        // TODO 这里需要改进,超级管理员账号可以配置。这样避免吧自己也删除了。
        if (!Constant.SUPER_ADMIN.equals(adminAccount.getUsername())) {
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
            loginlog(request,adminAccount,true);
            updateLastLoginTime(user.getId());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession();
            session.setAttribute(Constant.SECURITY_CONTEXT, SecurityContextHolder.getContext()); // 这个非常重要，否则验证后将无法登陆
            AdminAccountDTO adminAccountDTO = new AdminAccountDTO();
            adminAccountDTO.setId(user.getId());
            adminAccountDTO.setName(user.getName());

            return ApiResult.ok(adminAccountDTO);
        }
        loginlog(request,adminAccount,false);
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

    private void loginlog(HttpServletRequest request , AdminAccount adminAccount , boolean ok){
        String ip = getIpAddr(request);
        AdminAccountLoginLog adminAccountLoginLog = new AdminAccountLoginLog();
        if (ok) {
            adminAccountLoginLog.sucess(adminAccount.getUsername(),"登陆成功" , ip);
        }else {
            adminAccountLoginLog.failed(adminAccount.getUsername(),"登陆失败" , ip);
        }
        int res = adminAccountLoginLogDao.insert(adminAccountLoginLog);
        if (0 == res) {
            log.warn("登录日志记录异常");
        }
    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = null;
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("X-Real-IP");
        }
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
