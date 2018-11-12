package com.tp.admin.enums;

public enum AdminTerOperatingLogSourceEnum {

    DEFAULT(0, "维保"),
    PAUSED(1, "商户");

    private int value;
    private String desc;

    AdminTerOperatingLogSourceEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static AdminTerOperatingLogSourceEnum getByCode(int value) {
        for (AdminTerOperatingLogSourceEnum ec : AdminTerOperatingLogSourceEnum.values()) {
            if (ec.value == value) {
                return ec;
            }
        }
        return null;
    }
}
