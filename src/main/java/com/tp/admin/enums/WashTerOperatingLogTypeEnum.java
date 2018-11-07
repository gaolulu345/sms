package com.tp.admin.enums;

public enum WashTerOperatingLogTypeEnum {

    TER_RESET(0,"远程网点复位"),
    TER_RESET_STATE(1,"重置网点状态"),
    ONLINE(2,"网点上线"),
    NOT_ONLINE(3,"网点下线");

    private int value;
    private String desc;

    WashTerOperatingLogTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static WashTerOperatingLogTypeEnum getByCode(int  value){
        for(WashTerOperatingLogTypeEnum ec : WashTerOperatingLogTypeEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }

}
