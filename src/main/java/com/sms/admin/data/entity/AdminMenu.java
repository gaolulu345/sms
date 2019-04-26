package com.sms.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 菜单
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMenu {

    private int id;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private boolean enable;
    private boolean deleted;
    private String menuName;
    private String details;
    private String resource;
    private int orderBy;
}
