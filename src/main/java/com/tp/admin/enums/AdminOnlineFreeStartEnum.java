package com.tp.admin.enums;

public enum AdminOnlineFreeStartEnum {

    NOT_START(0,"一键下单未开启"),
    STARTED(1,"一键下单启动");

    private int value;
    private String desc;

    AdminOnlineFreeStartEnum(int value,String desc){
        this.value = value;
        this.desc = desc;
    }

    public int getValue(){
        return value;
    }

    public String getDesc(){
        return desc;
    }

    public static AdminOnlineFreeStartEnum getByValue(int value){
        for(AdminOnlineFreeStartEnum ec : AdminOnlineFreeStartEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }

}
