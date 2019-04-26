package com.sms.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 角色
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRoles {

    private int id;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private boolean enable;
    private boolean deleted;
    private String rolesName;
    private String details;
}
