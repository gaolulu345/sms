package com.tp.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.awt.image.IntegerComponentRaster;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminEmployeeOperatingLog {
    private Integer id;
    private Timestamp createTime;
    private String maintionId;
    private String merchantId;
    private String employeeName;
    private String operateName;
    private String title;
    private String intros;
    private Boolean success;
    private String msg;
    private Boolean deleted;
    private Integer opDesType;

}
