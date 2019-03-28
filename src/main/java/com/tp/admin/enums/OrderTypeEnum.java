package com.tp.admin.enums;

public enum OrderTypeEnum {
    ALIPAY(0, "支付宝"),
    WXPAY(1, "微信钱包"),
    TEST(2, "测试"),
    FREE(3, "免费"),
    MERCHANT(4,"商户一键启动"),
    MAINTAIM(5,"维保一键启动");
 
    private int value;
    private String desc;
 
    private OrderTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
 
    public int getValue() {
        return value;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static OrderTypeEnum getByCode(int  value){
        for(OrderTypeEnum ec : OrderTypeEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }
}
