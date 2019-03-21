package com.tp.admin.data.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String pw;
    @JsonIgnore
    private Timestamp createTime;
    @JsonIgnore
    private Timestamp lastLoginTime;
    private String intros;
    private String contract;
    private String title;
    private boolean deleted;

    private boolean subjection;

}
