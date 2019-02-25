package com.tp.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

//故障信息
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerFaultInfo {
    private Integer id;
    private String code;
    private Integer terId;
    //网点名称
    private String terTitle;
    private String faultType;
    private String faultDescribe;
    private Timestamp createTime;
    private String info;
    //故障信息编号
    private String faultDescribeCode;
    //洗车机当前状态
    private Integer state;
}
