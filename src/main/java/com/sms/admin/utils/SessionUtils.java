package com.sms.admin.utils;

import com.sms.admin.common.Constant;
import com.sms.admin.data.entity.AdminAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static AdminAccount findSessionAdminAccount(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session == null) {
            return null;
        }
        SecurityContext sctx = (SecurityContext)session.getAttribute(Constant.SECURITY_CONTEXT);
        if (sctx == null) {
            return null;
        }
        Authentication authentication = sctx.getAuthentication();
        AdminAccount adminAccount =(AdminAccount) authentication.getPrincipal();
        return adminAccount;
    }
}
