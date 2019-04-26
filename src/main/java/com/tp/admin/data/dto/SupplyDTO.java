package com.tp.admin.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyDTO {
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

    private Boolean enable;

    private Boolean deleted;
}
