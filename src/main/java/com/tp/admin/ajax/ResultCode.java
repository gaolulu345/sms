package com.tp.admin.ajax;

public enum ResultCode {
    SUCCESS("200","成功"),
    UnknownException("1000","未知异常"),
    SystemException("1001","系统异常"),
    MyException("1002","业务错误"),
    InfoException("1003", "提示级错误"),
    DBException("1004","数据库操作异常"),
    ParamException("1005","参数验证错误"),
    KeyParaMissException("1006","关键参数缺失"),
    RepeatOperationException("1007","重复提交"),
    ShieldException("9999","该设备已被列入黑名单"),
    NoThisUserException("1008","用户不存在"),
    InvalidAccessException("2001", "授权无效，请登录"),
    ParamChangedException("1010","数据变更");

    private String code;
    private String msg;

    private ResultCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }

    public static ResultCode getByCode(String code){
        for(ResultCode ec : ResultCode.values()){
            if(ec.getCode().equals(code)){
                return ec;
            }
        }
        return null;
    }
}
