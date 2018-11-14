package com.tp.admin.data.entity;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTerOperatingLog {

    private int id;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private boolean deleted;
    private int terId;
    private Integer maintionId;
    private Integer merchantId;
    private String username;
    private String title;
    private String intros;
    private int type;
    private int opSource; // 操作来源
    private boolean sucess;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
