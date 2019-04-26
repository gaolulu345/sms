package com.sms.admin.service.implement;

import com.sms.admin.data.entity.AdminAccountLoginLog;
import com.sms.admin.ajax.ApiResult;
import com.sms.admin.common.Constant;
import com.sms.admin.dao.AdminAccountDao;
import com.sms.admin.dao.AdminAccountLoginLogDao;
import com.sms.admin.data.dto.AdminAccountDTO;
import com.sms.admin.data.dto.LoginDTO;
import com.sms.admin.exception.BaseException;
import com.sms.admin.exception.ExceptionCode;
import com.sms.admin.service.AccountServiceI;
import com.sms.admin.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

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
    public int updateLastLoginTime(int id) {
        return adminAccountDao.updateLastLoginTime(id, new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public ApiResult login(HttpServletRequest request, LoginDTO loginDTO) {
        if (StringUtil.isEmpty(loginDTO.getUsername()) || StringUtil.isEmpty(loginDTO.getPassword())) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING, "用户名或者密码不能为空");
        }
        AdminAccountDTO user = adminAccountDao.findDtoByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BaseException(ExceptionCode.PARAMETER_MISSING, loginDTO.getUsername() + "用户不存在");
        }
        String ip = loginDTO.getCip();
        if (null == loginDTO.getCip() || StringUtils.isEmpty(loginDTO.getCip())) {
            ip = getIpAddr(request);
        }
        if (!loginDTO.getPassword().equals(user.getPassword())) {
            loginlog(ip,user,false);
            throw new BaseException(ExceptionCode.PARAMETER_MISSING, loginDTO.getUsername() + "用户，密码错误");
        }
        // TODO 这里需要改进,超级管理员账号可以配置。这样避免吧自己也删除了。
        if (!Constant.SUPER_ADMIN.equals(user.getUsername())) {
            if (user.isDeleted()) {
                throw new BaseException(ExceptionCode.PARAMETER_MISSING, loginDTO.getUsername() + "用户已注销");
            }
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), user.getPassword());
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authRequest); //调用loadUserByUsername
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != authentication) {
            loginlog(ip,user,true);
            updateLastLoginTime(user.getId());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession();
            session.setAttribute(Constant.SECURITY_CONTEXT, SecurityContextHolder.getContext()); // 这个非常重要，否则验证后将无法登陆
            return ApiResult.ok(user);
        }
        loginlog(ip,user,false);
        return ApiResult.error(ExceptionCode.INVALID_ACCESS_EXCEPTION);
    }

    @Override
    public ApiResult logout(HttpServletRequest request , HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (session == null) {
            return ApiResult.error(ExceptionCode.INVALID_ACCESS_EXCEPTION);
        }
        SecurityContext sctx = (SecurityContext) session.getAttribute(Constant.SECURITY_CONTEXT);
        if (sctx == null) {
            return ApiResult.error(ExceptionCode.INVALID_ACCESS_EXCEPTION);
        }
        Authentication auth = sctx.getAuthentication();
        if (null != auth) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ApiResult.ok();
    }

    private void loginlog(String ip , AdminAccountDTO adminAccountDTO , boolean ok){

        AdminAccountLoginLog adminAccountLoginLog = new AdminAccountLoginLog();
        if (ok) {
            adminAccountLoginLog.sucess(adminAccountDTO.getUsername(),"登陆成功" , ip);
        }else {
            adminAccountLoginLog.failed(adminAccountDTO.getUsername(),"登陆失败" , ip);
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
