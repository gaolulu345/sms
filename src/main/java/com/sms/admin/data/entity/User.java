package com.sms.admin.data.entity;

import com.sms.admin.enums.UserChanneEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private int id;
    private String phone;
    private Timestamp createTime;
    private Timestamp lastLoginTime;
    private String access;
    private int type;
    private boolean deleted;
    private String pw;
    private String wxid;
    private String miniWxid;
    private String wxUnionid;
    private String aliId;

    private String typeDesc;

    public void build(){
        this.typeDesc = UserChanneEnum.getByCode(this.type).getDesc();
    }
    
}
