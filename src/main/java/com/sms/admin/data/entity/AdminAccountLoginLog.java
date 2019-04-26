package com.sms.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAccountLoginLog {

    private int id;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private boolean deleted;
    private String username;
    private String msg;
    private String ip;
    private boolean sucess;

    public void sucess(String username , String msg , String ip){
        this.username = username;
        this.msg = msg;
        this.ip = ip;
        this.sucess = true;
    }

    public void failed(String username , String msg , String ip){
        this.username = username;
        this.msg = msg;
        this.ip = ip;
        this.sucess = false;
    }

}
