package com.tp.admin.enums;

public enum AdminTemplateInfoEnum {

    WX_ORDER_INFO(1,"微信订单信息"),
    WX_REFUND_INFO(2,"微信退款通知"),
    MINI_SITE_RESET_PHOTO(3,"网点重置信息"),
    WASH_CARD_CHECK(4,"会员卡审核");

    private int value;
    private String desc;

    AdminTemplateInfoEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static AdminTemplateInfoEnum getByCode(int value) {
        for (AdminTemplateInfoEnum ec : AdminTemplateInfoEnum.values()) {
            if (ec.value == value) {
                return ec;
            }
        }
        return null;
    }

    public static AdminTemplateInfoEnum getByValue(String desc) {
        for (AdminTemplateInfoEnum ec : AdminTemplateInfoEnum.values()) {
            if (ec.desc.equals(desc)) {
                return ec;
            }
        }
        return null;
    }
}
