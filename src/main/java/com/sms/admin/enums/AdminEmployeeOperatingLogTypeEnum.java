package com.sms.admin.enums;

public enum AdminEmployeeOperatingLogTypeEnum {
    DISABLE_EMPLOYEE(0,"禁用员工"),
    ENABLE_EMPLOYEE(1,"修改员工审批状态"),
    ENABLE_AND_BIND(2,"激活 绑定合作伙伴"),
    UNBIND(3,"解禁员工"),
    ENABLESM(4,"修改员工接收故障通知短信权限");



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
