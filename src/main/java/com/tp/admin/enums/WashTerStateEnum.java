package com.tp.admin.enums;

public enum WashTerStateEnum {
    DEFAULT(0,"初始化"),
    ERROR(1,"故障"),
    CAR_EXCEPTION(2,"车位异常"),
    START(3,"启动中"),
    WASHING(4,"洗车中"),
    DONE(5,"洗车完成"),
    PAUSED(6,"暂停"),
    RESETING(7,"复位中"),
    RESET_DONE(8,"复位完成"),
    READY(9,"就绪"),
    VALID(10,"空闲");
 
    private int value;
    private String desc;
 
    WashTerStateEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
 
    public int getValue() {
        return value;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static WashTerStateEnum getByCode(int  value){
        for(WashTerStateEnum ec : WashTerStateEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }
}
