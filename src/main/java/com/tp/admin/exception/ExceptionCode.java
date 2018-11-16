package com.tp.admin.exception;

public enum ExceptionCode {
    
    SUCCESS("200","执行成功"),
    UNKNOWN_EXCEPTION("0","未知错误"),
    NOT_PERMISSION_ERROR("1","API暂不开放"),
    DB_ERR_EXCEPTION("1000","数据库操作失败"),
    DB_BUSY_EXCEPTION("1001","数据库繁忙"),
    PARAMETER_WRONG("1010","参数错误"),
    PARAMETER_MISSING("1011","缺少参数"),
    SIGN_FAILURE("1020","签名失败"),
    SIGN_ERROR("1021","签名错误"),
    REPEAT_OPERATION("1030", "重复操作"),
    NOT_TER("1080", "此编号网点不存在"),
    INVALID_ACCESS_EXCEPTION("2000","用户名或者密码错误"),
    NO_THIS_USER("2001","查无此用户"),
    NO_PERMIT("2002", "未登陆"),
    LOGIN_TIMEOUT("2003", "登录超时"),
    USER_NOT_PERMISSION("2004", "账号已经申请注册，暂未通过审核"),
    USER_PHONE_HAS_REGISTERED("2005", "该手机号已经申请注册"),
    USER_DELETE_REGISTERED("2006", "该账号权限已经被禁止使用"),
    ALI_OSS_OPEN_STORAGE_SERVICE_ERROR("9000","当访问对象存储服务 Open Storage Service 失败时抛出该异常类实例，请重新尝试"),

    SIGN_FAILURE_FOR_REMOTE_TER("3006","网点订单加签失败"),
    SIGN_ERROR_FOR_REMOTE_TER("3007","网点订单加签错误"),

    TER_STATUS_RESERT_ERROR("4007","网点状态复位操作失败"),
    TER_DEVICE_RESERT_ERROR("4008","网点设备复位操作失败"),

    NOT_PARTNER("5000","查无此合作伙伴"),

    ALI_OSS_REMOTE_ERROR("9001","尝试访问阿里云服务时的异常,请重新尝试"),
    ALI_OSS_UPDATE_ERROR("9002","上传文件失败,请重新尝试"),
    ALI_OSS_FILE_SAVE_ERROR("9003","上传文件保存失败,请重新尝试"),

    API_NOT_PERMISSION_ERROR("10000","数据未授权。"),
    PAGES_NOT_PERMISSION_ERROR("10001","页面未授权。");

    private String code;
    private String msg;

    ExceptionCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }

    public static ExceptionCode getByCode(String code){
        for(ExceptionCode ec : ExceptionCode.values()){
            if(ec.getCode().equals(code)){
                return ec;
            }
        }
        return null;
    }
}
