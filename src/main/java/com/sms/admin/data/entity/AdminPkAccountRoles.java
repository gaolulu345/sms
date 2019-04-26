package com.sms.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPkAccountRoles {

    private int id;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private int adminId;
    private int rolesId;
    private boolean enable;

}
