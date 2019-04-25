package com.tp.admin.enums;

public enum AdminGenderEnum {
    UN_KNOW(0,"未知"),
    MAN(1,"男"),
    WOMAN(2,"女");


    private int value;
    private String valueDesc;

    AdminGenderEnum(int value,String valueDesc){
        this.value = value;
        this.valueDesc = valueDesc;
    }

    public int getValue(){
        return value;
    }

    public String getValueDesc() {
        return valueDesc;
    }

    public static AdminGenderEnum getByValue(int value){
        for(AdminGenderEnum ec : AdminGenderEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }
}
