package com.sms.admin.security;

import com.google.gson.Gson;
import com.sms.admin.data.dto.UserToken;
import com.sms.admin.data.entity.AdminAccount;
import com.sms.admin.utils.PasswordUtils;
import com.sms.admin.utils.RedisUtil;
import com.sms.admin.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private Logger log = LoggerFactory.getLogger(getClass());

    /*@Autowired
    RedisUtil redisUtil;*/

    public AuthBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 自定义拦截方式
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException, AuthenticationException {
        RedisUtil redisUtil = (RedisUtil) SpringContextUtil.getBean("redisUtil");

        //String url = request.getRequestURI();
        //String method = request.getMethod();
        /*HttpSession session = request.getSession();
        if (session == null) {
            response.sendRedirect("/login");
            return;
        }
        SecurityContext sctx = (SecurityContext) session.getAttribute(Constant.SECURITY_CONTEXT);
        if (sctx == null) {
            response.sendRedirect("/login");
            return;
        }
        Authentication authentication = sctx.getAuthentication();
        AdminAccount adminAccount = (AdminAccount) authentication.getPrincipal();*/
        String token = redisUtil.findCurrentCookie(request);

        AdminAccount adminAccount = null;
        String tokens = redisUtil.findRedisAdminAccountDto(token);
        if (tokens.equals("")) {
            response.sendRedirect("/login");
            return;
        }

        UserToken userToken = new Gson().fromJson(PasswordUtils.base64DE(token), UserToken.class);
        adminAccount = new Gson().fromJson(tokens,AdminAccount.class);
        if (adminAccount.getUsername().equals((userToken.getUser()).getUsername())) {
            success(request,response,chain);
            //return;
        }
        //AdminAccount adminAccount = redisUtil.findRedisAdminAccount();
        // 如果是超级管理员放行。
        /*if (adminAccount.getUsername().equals(Constant.SUPER_ADMIN)) {
            success(request,response,chain);
            return;
        }*/
        /*if (url.indexOf("/api/private", 0) == 0) {
            if (invokeApi(adminAccount, url)) {
                success(request,response,chain);
                return;
            }
            throw new BaseException(ExceptionCode.API_NOT_PERMISSION_ERROR);
        } else if (url.indexOf("/pages", 0) == 0 && method.equals(HttpMethod.GET.name())) {
            if (url.equals(Constant.PAGES_INDEX)) {
                success(request,response,chain);
                return;
            }
            if (invokePages(adminAccount, url)) {
                success(request,response,chain);
            }
            response.sendRedirect(Constant.PAGES_INDEX);
        }*/
    }

    private void success(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
        chain.doFilter(request, response);
    }

    private boolean invokeApi(AdminAccount adminAccount, String url) {
        if (adminAccount.getAuthorities().isEmpty()) {
            return false;
        }
        AutoResource autoResource = null;
        for (GrantedAuthority ga : adminAccount.getAuthorities()) {
            if (ga instanceof AutoResource) {
                autoResource = (AutoResource) ga;
                if (autoResource.getUrl().equals(url)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean invokePages(AdminAccount adminAccount, String url) {
        if (adminAccount.getAuthorities().isEmpty()) {
            return false;
        }
        AutoResource autoResource = null;
        for (GrantedAuthority ga : adminAccount.getAuthorities()) {
            if (ga instanceof AutoResource) {
                autoResource = (AutoResource) ga;
                if (autoResource.getUrl().equals(url)) {
                    return true;
                }
            }
        }
        return false;
    }

}
