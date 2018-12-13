package com.tp.admin.enums;

public enum MiniAutoTypeEnum {

    MINI_MERCHANT(1,"微信商家"),
    MINI_MAINTION(2,"微信维保"),
    MINI_ALI(3,"支付宝"),
    MINI_USER(4,"用户洗车小程序");

    private int value;
    private String desc;

    MiniAutoTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static MiniAutoTypeEnum getByCode(int value) {
        for (MiniAutoTypeEnum ec : MiniAutoTypeEnum.values()) {
            if (ec.value == value) {
                return ec;
            }
        }
        return null;
    }
}
