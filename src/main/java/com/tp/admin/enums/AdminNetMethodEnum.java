package com.tp.admin.enums;

public enum AdminNetMethodEnum {

    HAVE_WIRED(0,"有线宽带"),
    NO_WIRED(1,"无线宽带");

    private int value;
    private String desc;

    AdminNetMethodEnum(int value,String desc){
        this.value = value;
        this.desc = desc;
    }

    public int getValue(){
        return value;
    }

    public String getDesc(){
        return desc;
    }

    public static AdminNetMethodEnum getByValue(int value){
        for(AdminNetMethodEnum ec : AdminNetMethodEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }
}
