package com.tp.admin.enums;

public enum TerStatusEnum {

    DEFAULT(0, "初始状态"),
    ERROR(1, "故障"),
    CAR_EXCEPTION(2, "车辆位置异常"),
    START(3, "启动中"),
    WASHING(4, "洗车中"),
    DONE(5, "洗车完成"),
    PAUSED(6, "暂停中"),
    RESETING(7, "正在复位"),
    RESET_DONE(8, "复位完成"),
    READY(9, "准备就绪"),
    VALID(10, "空闲");
 
    private int value;
    private String desc;
 
    private TerStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
 
    public int getValue() {
        return value;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static TerStatusEnum getByCode(int  value){
        for(TerStatusEnum ec : TerStatusEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }
    
}
