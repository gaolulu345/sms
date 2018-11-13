package com.tp.admin.data.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partner {

    private int id;
    private String name;
    private String pw;
    private Timestamp createTime;
    private Timestamp lastLoginTime;
    private String intros;
    private String contract;
    private String title;
    private boolean deleted;

}
