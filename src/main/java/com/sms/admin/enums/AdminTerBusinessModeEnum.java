package com.sms.admin.enums;

public enum AdminTerBusinessModeEnum {

    SINGLE_NO_PERSON(0,"单网点无人值守"),
    SINGLE_HAVE_PERSON(1, "单网点有人值守"),
    PROXY_ONE(2,"代理模式一"),
    PROXY_TWO(3,"代理模式二");

    private int value;
    private String desc;

    AdminTerBusinessModeEnum(int value,String desc){
        this.value = value;
        this.desc = desc;
    }

    public int getValue(){
        return value;
    }

    public String getDesc(){
        return desc;
    }

    public static AdminTerBusinessModeEnum getByValue(int value){
        for(AdminTerBusinessModeEnum ec : AdminTerBusinessModeEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }

}
