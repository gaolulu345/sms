package com.sms.admin.enums;

public enum OrderStatusEnum {
    DEFAULT(0, "未支付"),
    CANCEL(1, "已支付");
 
    private int value;
    private String desc;
 
    private OrderStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
 
    public int getValue() {
        return value;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static OrderStatusEnum getByCode(int  value){
        for(OrderStatusEnum ec : OrderStatusEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }
}
