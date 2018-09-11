package com.tp.admin.data.dto;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class AdminAccountDTO {

    private int id;
    private String username;
    private String name;
    private String intros;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private boolean enable;
    private boolean deleted;
    private Timestamp lastLoginTime;
}
