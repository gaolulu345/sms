package com.tp.admin.enums;

public enum OrderStatusEnum {
    DEFAULT(0, "创建"),
    CANCEL(1, "取消付款"),
    ASK_CHECK(2, "已支付");
 
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
