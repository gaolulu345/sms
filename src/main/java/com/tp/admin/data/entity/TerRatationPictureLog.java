package com.tp.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerRatationPictureLog {
    private Integer id;
    private String adminName;
    private String info;
    private Timestamp createTime;

    public TerRatationPictureLog(String adminName,String info){
        this.adminName = adminName;
        this.info = info;
    }

}
