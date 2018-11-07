package com.tp.admin.data.entity;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMaintionEmployeeLogTerOperating {

    private int id;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private boolean deleted;
    private int terId;
    private int employeeId;
    private String username;
    private String title;
    private String intros;
    private int type;
    private boolean sucess;

    public void sucess(Integer terId, Integer employeeId, String username, String title, String intros,
                       int type) {
        this.terId = terId;
        this.employeeId = employeeId;
        this.username = username;
        this.title = title;
        this.intros = intros;
        this.type = type;
        this.sucess = true;
    }

    public void failed(Integer terId, Integer employeeId, String username, String title, String
            intros, int type) {
        this.terId = terId;
        this.employeeId = employeeId;
        this.username = username;
        this.title = title;
        this.intros = intros;
        this.type = type;
        this.sucess = false;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
