package com.tp.admin.enums;

public enum AdminEmployeeOperatingLogTypeEnum {
    DISABLE_EMPLOYEE(0,"禁用员工"),
    ENABLE_EMPLOYEE(1,"启用员工");

    private int value;
    private String desc;

    AdminEmployeeOperatingLogTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static AdminEmployeeOperatingLogTypeEnum getByCode(int value) {
        for (AdminEmployeeOperatingLogTypeEnum ec : AdminEmployeeOperatingLogTypeEnum.values()) {
            if (ec.value == value) {
                return ec;
            }
        }
        return null;
    }
}
