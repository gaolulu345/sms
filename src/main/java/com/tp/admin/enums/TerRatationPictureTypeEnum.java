package com.tp.admin.enums;

public enum TerRatationPictureTypeEnum {
    GLOD_AD_POSITION(0,"轮播广告"),
    SECOND_AD_POSITION(1,"广告位置");

    private int value;
    private String desc;

    TerRatationPictureTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static TerRatationPictureTypeEnum getByValue(int  value){
        for(TerRatationPictureTypeEnum ec : TerRatationPictureTypeEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }

}
