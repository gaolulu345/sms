package com.sms.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseOrder {
    Integer id;

    //一级
    Integer proType;

    //二级
    Integer proId;

    Integer saleNum;

    Integer saleValue;

    Integer netProfits;

    String intros;

    Timestamp createTime;

    Boolean enable;

    Boolean deleted;

}
