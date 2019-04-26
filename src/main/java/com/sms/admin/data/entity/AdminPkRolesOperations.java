package com.sms.admin.data.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminPkRolesOperations {

    private int id;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private int rolesId;
    private int operationsId;
    private boolean enable;
}
