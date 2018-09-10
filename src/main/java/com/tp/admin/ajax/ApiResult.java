package com.tp.admin.ajax;

import com.tp.admin.exception.ExceptionCode;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

@Data
public class ApiResult {
    
    private static final String DEFAULT_SUCCESS_RETURNMSG = "SUCCESS";
    private static final String SQL_FAILURE_RETURNMSG = "SQL_ERROR";
    
    private static final Object EMPTY_RESULT = new Object();

    private String code = ResultCode.SUCCESS.getCode();

    private String message = DEFAULT_SUCCESS_RETURNMSG;

    private Object data = null;
    
    private final static Logger logger = LoggerFactory.getLogger(ApiResult.class); 

    public static ApiResult ok(String message, Object obj) {
        ApiResult result = new ApiResult();
        result.setMessage(message);
        result.setData(obj);
        result.setCode(ExceptionCode.SUCCESS.getCode());
        return result;
    }

    public static ApiResult ok(Object obj) {
        ApiResult result = new ApiResult();
        result.setCode(ExceptionCode.SUCCESS.getCode());
        result.setMessage(DEFAULT_SUCCESS_RETURNMSG);
        result.setData(obj);
        return result;
    }

    public static ApiResult ok() {
        return ok(DEFAULT_SUCCESS_RETURNMSG, EMPTY_RESULT);
    }

    public static ApiResult error(ExceptionCode code, String message, Object obj) {
        ApiResult result = new ApiResult();
        result.setCode(code.getCode());
        result.setMessage(message);
        result.setData(obj == null ? EMPTY_RESULT : obj);
        logger.info(result.toString());
        if (code == ExceptionCode.DB_BUSY_EXCEPTION || code == ExceptionCode.DB_ERR_EXCEPTION) {
            result.setMessage(SQL_FAILURE_RETURNMSG);
        }
        return result;
    }
    
    public static ApiResult error(String code, String message, Object obj) {
        ApiResult result = new ApiResult();
        result.setCode(code);
        result.setMessage(message);
        result.setData(obj == null ? EMPTY_RESULT : obj);
        logger.info(result.toString());
        return result;
    }
    
    public static ApiResult error(ExceptionCode code, String message) {
        if (StringUtils.isEmpty(message)) {
            message = code.getMsg();
        }
        return error(code, message, EMPTY_RESULT);
    }
    
    public static ApiResult error(ExceptionCode code) {
        String msg = code.getMsg();
        return error(code, msg, EMPTY_RESULT);
    }
}
