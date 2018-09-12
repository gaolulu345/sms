package com.tp.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadLog {

    private int id;
    private String adminName;
    private String bucketName;
    private String fileKey;
    private Timestamp createTime;
    private String url;

    public FileUploadLog(String adminName , String fileKey){
        this.adminName = adminName;
        this.fileKey = fileKey;
    }


    public void buildUrl(String p){
        url = p + fileKey;
    }

}