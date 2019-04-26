package com.sms.admin.enums;

    public enum UserChanneEnum {
        PhoneMsg(0,"手其它"),
        PhonePw(1,"其它"),
        Wx(2,"微信"),
        MiniWx(3,"微信小程序"),
        Ali(4,"支付宝小程序");

    private int value;
    private String desc;

    UserChanneEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static UserChanneEnum getByCode(int  value){
        for(UserChanneEnum ec : UserChanneEnum.values()){
            if(ec.value == value){
                return ec;
            }
        }
        return null;
    }
}
