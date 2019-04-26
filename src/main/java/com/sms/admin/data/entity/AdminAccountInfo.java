package com.sms.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountInfo {

    private int id;
    private String intros;
    private String name;
    private int gender;
    private Timestamp bornDate;
    private String telephone;
    private String address;
    private int adminId;


}
