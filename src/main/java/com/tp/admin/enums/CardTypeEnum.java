package com.tp.admin.enums;

public enum CardTypeEnum {
    PRICE(1, "一口价"),
    RATE(2, "折扣");

    private int value;
    private String desc;

    private CardTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static CardTypeEnum getByCode(int  value){
        for(CardTypeEnum ec : CardTypeEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }

}
