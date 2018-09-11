package com.tp.admin.enums;

public enum OrderChannelEnum {
    DEFAULT(0,"未知"),
    IOS_APP(1,"ios_app"), 
    ANDROID_APP(2,"android_app"),
    WX_MINI_APP(3,"微信小程序"),
    ALI_MINI_APP(4,"支付宝小程序"),
    WEB(5,"网页");
 
    private int value;
    private String desc;
 
    private OrderChannelEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
 
    public int getValue() {
        return value;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static OrderChannelEnum getByCode(int  value){
        for(OrderChannelEnum ec : OrderChannelEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }
}
