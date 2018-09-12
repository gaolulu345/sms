package com.tp.admin.enums;

public enum RefundTypeEnum {

    MISTAKEN_PURCHASE(0, "误购，请求退款"),
    UNABLE_TO_START(1, "设备无法启动"),
    MIDWAY_FAILURE(2, "洗车中途故障"),
    NOT_SATISFIED(3, "洗车服务不满意"),
    OTHER(4, "其他原因");

    private int value;
    private String desc;
 
    RefundTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
 
    public int getValue() {
        return value;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public static RefundTypeEnum getByValue(int  value){
        for(RefundTypeEnum ec : RefundTypeEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }
    
}
