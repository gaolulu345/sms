package com.tp.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerInfo {

    private int id;
    private int terId;
    private String title;
    private String address;
    private String geoCode;
    private boolean subjection;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private int status;
    private boolean deleted;
    private boolean online;
    private boolean enableTicket;
    private boolean enableCard;
    private String offlineDesc;
    private String cover;
    private String guideCover;
    
}
