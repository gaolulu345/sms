package com.sms.admin.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *@Author feri
 *@Date Created in 2018/10/24 11:36
 */
public class CrosFilter implements Filter {
    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //System.err.println("CROS……");
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        response.setHeader("Access-Control-Allow-Origin","*");//支持跨域的域名  *任意
        response.setHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,REQUEST");//请求方式
        response.setHeader("Access-Control-Allow-Headers","*");//消息头 x-request-with
        response.setHeader("Access-Control-Max-Age","1800");//时间 单位秒

        filterChain.doFilter(servletRequest,response);



    }

    @Override
    public void destroy() {

    }
}
