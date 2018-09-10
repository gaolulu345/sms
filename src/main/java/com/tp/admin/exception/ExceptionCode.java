package com.tp.admin.exception;

public enum ExceptionCode {
    
    SUCCESS("200","执行成功"),
    UNKNOWN_EXCEPTION("0","未知错误"),
    DB_ERR_EXCEPTION("1000","数据库操作失败"),
    DB_BUSY_EXCEPTION("1001","数据库繁忙"),
    PARAMETER_WRONG("1010","参数错误"),
    PARAMETER_MISSING("1011","缺少参数"),
    SIGN_FAILURE("1020","签名失败"),
    SIGN_ERROR("1021","签名错误"),
    REPEAT_OPERATION("1030", "重复操作"),
    
    INVALID_ACCESS_EXCEPTION("2000","用户名或者密码错误"),
    NO_THIS_USER("2001","查无此用户"),
    NO_PERMIT("2002", "未登陆"),
    LOGIN_TIMEOUT("2003", "登录超时"),

    REMOTE_TER_RESPONCE_ERROR("3000","远程网点返回信息失败"),
    REMOTE_TER_RESET_FAILURE("3001","远程网点复位失败"),

    REMOTE_TER_PUSH_IMAGE_ERROR("4000","远程推送轮播图片返回消息失败"),
    REMOTE_TER_PUSH_IMAGE_FAILURE("4001","远程推送轮播图片失败"),
    REMOTE_TER_NO_IMAGE_ERROR("4002","该网点未找到有效的轮播图片"),

    REMOTE_TER_STATUS_ERROR("4003","获取网点轮播图状态信息失败"),

    ALI_OSS_OPEN_STORAGE_SERVICE_ERROR("9000","当访问对象存储服务 Open Storage Service 失败时抛出该异常类实例，请重新尝试"),
    ALI_OSS_REMOTE_ERROR("9001","尝试访问阿里云服务时的异常,请重新尝试"),
    ALI_OSS_UPDATE_ERROR("9002","上传文件失败,请重新尝试"),
    ALI_OSS_FILE_SAVE_ERROR("9003","上传文件保存失败,请重新尝试"),

    API_NOT_PERMISSION_ERROR("10000","数据未授权。"),
    PAGES_NOT_PERMISSION_ERROR("10001","页面未授权。")
    ;

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
