package com.sms.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Supply {
    private Integer id;

    private String supplyCode;

    private String supplyName;

    private String contactName;

    private String contactPhone;

    private String contactAddress;

    private String fax;

    private String intros;

    private Timestamp createTime;

    private Timestamp modifyTime;
}
