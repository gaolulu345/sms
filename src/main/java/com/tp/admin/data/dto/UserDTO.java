package com.tp.admin.data.dto;

import com.tp.admin.enums.UserChanneEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private int id;
    private String avatar;
    private String city;
    private String nickname;
    private int gender;
    private String phone;
    private String access;
    private int type;
    private String aliId;
    private String miniWxid;
    private Timestamp createTime;
    private Timestamp lastLoginTime;

    private String typeDesc;

    public void build(){
        this.typeDesc = UserChanneEnum.getByCode(this.type).getDesc();
    }
}
