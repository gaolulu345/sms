package com.tp.admin.enums;

public enum OperationStateEnum {

    WASHING(1, "洗车"),
    PAUSED(2, "暂停"),
    NONE(3, "洗车结束");

    private int value;
    private String desc;

    private OperationStateEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static OperationStateEnum getByCode(int value) {
        for (OperationStateEnum ec : OperationStateEnum.values()) {
            if (ec.value == value) {
                return ec;
            }
        }
        return null;
    }
}
