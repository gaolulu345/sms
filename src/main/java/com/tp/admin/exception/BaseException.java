package com.tp.admin.exception;


import org.apache.commons.lang.StringUtils;

public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误编码
     */
    private String errorCode;
    
    private String errorMsg;

    /**
     * 消息是否为属性文件中的Key
     */
    private boolean propertiesKey = true;

    /**
     * 构造一个基本异常.
     *
     * @param message
     *            信息描述
     */
    public BaseException(String message)
    {
        super(message);
    }
    
    public BaseException(ExceptionCode code) {
        super(code.getCode());
        this.errorCode = code.getCode();
        this.errorMsg = code.getMsg();
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode
     *            错误编码
     * @param message
     *            信息描述
     */
    public BaseException(String errorCode, String message)
    {
        this(errorCode, message, true);
    }
    
    public BaseException(ExceptionCode errorCode, String message)
    {
        this(errorCode.getCode(), StringUtils.isEmpty(message) ? errorCode.getMsg() : message, true);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode
     *            错误编码
     * @param message
     *            信息描述
     */
    public BaseException(String errorCode, String message, Throwable cause)
    {
        this(errorCode, message, cause, true);
    }
    
    public BaseException(ExceptionCode errorCode, String message, Throwable cause)
    {
        this(errorCode.getCode(), StringUtils.isEmpty(message) ? errorCode.getMsg() : message, cause, true);
    }
    
    /*
     * @param params
     *   错误参数
     */
    
    public BaseException(String errorCode, String message, String params)
    {
        this(errorCode,StringUtils.isEmpty(params) ? message : (message + "->" + params));
    }
    
    public BaseException(ExceptionCode errorCode, String message, String params)
    {
        this(errorCode.getCode(),StringUtils.isEmpty(params) ? message : (message + "->" + params));
    }
    
    
    public BaseException(String errorCode, String message, String params, Throwable cause)
    {
        this(errorCode, StringUtils.isEmpty(params) ? message : (message + "->" + params), cause, true);
    }
    
    public BaseException(ExceptionCode errorCode, String message, String params, Throwable cause)
    {
        this(errorCode.getCode(), StringUtils.isEmpty(params) ? message : (message + "->" + params), cause, true);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode
     *            错误编码
     * @param message
     *            信息描述
     * @param propertiesKey
     *            消息是否为属性文件中的Key
     */
    public BaseException(String errorCode, String message, boolean propertiesKey)
    {
        super(message);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
        this.setErrorMsg(message);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode
     *            错误编码
     * @param message
     *            信息描述
     */
    public BaseException(String errorCode, String message, Throwable cause, boolean propertiesKey)
    {
        super(message, cause);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
        this.setErrorMsg(message);
    }

    /**
     * 构造一个基本异常.
     *
     * @param message
     *            信息描述
     * @param cause
     *            根异常类（可以存入任何异常）
     */
    public BaseException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    public boolean isPropertiesKey()
    {
        return propertiesKey;
    }

    public void setPropertiesKey(boolean propertiesKey)
    {
        this.propertiesKey = propertiesKey;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    
}
