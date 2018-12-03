package com.tp.admin.enums;

public enum RefundStatusEnum {

    DEFAULT(0, "默认状态"),
    REQUEST_REFUND(1, "请求退款"),
    APPROVED(2, "请求通过审核"),
    REFUNDED(3, "已退款"),
    REQUEST_REJECTION(4, "请求被拒绝"),
    REFUNDING(5,"退款中");

    private int value;
    private String desc;

    RefundStatusEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static RefundStatusEnum getByValue(int  value){
        for(RefundStatusEnum ec : RefundStatusEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }

}
