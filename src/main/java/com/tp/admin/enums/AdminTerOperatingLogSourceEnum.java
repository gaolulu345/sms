package com.tp.admin.enums;

public enum AdminTerOperatingLogSourceEnum {

    MAINTAUN(0, "维保"),
    MERCHANT(1, "商户"),
    TER_EMPLOYEE(2,"网点值班人员");

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
