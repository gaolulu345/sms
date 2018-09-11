package com.tp.admin.security;

public enum AuthResourceTypeEnum {

    DEFAULT(0,"其它"),
    MENU(1,"菜单"),
    PAGE(2,"页面"),
    OP(3,"操作");

    private int value;
    private String desc;

    private AuthResourceTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static AuthResourceTypeEnum getByCode(int  value){
        for(AuthResourceTypeEnum ec : AuthResourceTypeEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }
}
