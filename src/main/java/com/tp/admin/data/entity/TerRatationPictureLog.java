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
    private String terRatationPictureId;
    private String adminName;
    private String info;
    private Timestamp createTime;

    public TerRatationPictureLog(String terRatationPictureId,String adminName,String info){
        this.terRatationPictureId = terRatationPictureId;
        this.adminName = adminName;
        this.info = info;
    }

}
