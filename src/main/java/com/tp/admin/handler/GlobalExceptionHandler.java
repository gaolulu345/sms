package com.tp.admin.handler;

import com.tp.admin.ajax.ApiResult;
import com.tp.admin.exception.BaseException;
import com.tp.admin.exception.ExceptionCode;

import com.tp.admin.exception.NotLoginException;
import com.tp.admin.exception.PagesException;
import com.tp.admin.utils.StringUtil;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    Object handleException(HttpServletRequest req, Exception e) {
        if (req.getMethod().equals("POST")) {
            return ApiResult.error(ExceptionCode.UNKNOWN_EXCEPTION);
        } else {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("exception", e);
            mav.addObject("url", req.getRequestURL());
            mav.addObject("reason", e.getMessage());
            return mav;
        }
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    Object authenticationException(HttpServletRequest req, AuthenticationException e) {
        if (req.getMethod().equals("POST")) {
            return ApiResult.error(ExceptionCode.UNKNOWN_EXCEPTION);
        } else {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("exception", e);
            mav.addObject("url", req.getRequestURL());
            mav.addObject("reason", e.getMessage());
            return mav;
        }
    }


    @ExceptionHandler(BaseException.class)
    @ResponseBody
    Object handleBaseException(HttpServletRequest req, BaseException e) {
        if (req.getMethod().equals("POST")) {
            String code = e.getErrorCode();
            if (StringUtil.isEmpty(code)) {
                return ApiResult.error(ExceptionCode.UNKNOWN_EXCEPTION, e.getErrorMsg(), null);
            }
            return ApiResult.error(code, e.getErrorMsg(), null);
        } else {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("exception", e);
            mav.addObject("url", req.getRequestURL());
            mav.setViewName("error");
            mav.addObject("reason", e.getMessage());
            return mav;
        }
    }


    @ExceptionHandler(PagesException.class)
    @ResponseBody
    Object handlePagesException(HttpServletRequest req, PagesException e) {
        ModelAndView mav = new ModelAndView("unauthorized");
        mav.setViewName("unauthorized");
        return mav;
    }

    /**
     * 处理所有接口数据验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    Object handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException e) {
        if (req.getMethod().equals("POST")) {
            return ApiResult.error(ExceptionCode.PARAMETER_WRONG, e.getMessage(), null);
        } else {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("exception", e);
            mav.addObject("url", req.getRequestURL());
            mav.setViewName("error");
            mav.addObject("reason", e.getMessage());
            return mav;
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    Object handleMethodArgumentTypeMismatchException(HttpServletRequest req, MethodArgumentNotValidException e) {
        if (req.getMethod().equals("POST")) {
            return ApiResult.error(ExceptionCode.PARAMETER_WRONG, e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), null);
        } else {
            ModelAndView mav = new ModelAndView();
            mav.addObject("exception", e);
            mav.addObject("url", req.getRequestURL());
            mav.setViewName("error");
            mav.addObject("reason", e.getMessage());
            return mav;
        }
    }

}
