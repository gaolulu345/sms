package com.sms.admin.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    Integer id;

    String proName;

    String proPicture;

    Integer proType;

    Integer oldPrice;

    Integer newPrice;

    String standards;

    Integer repertory;

    //销量
    Integer saleNum;

    String place;

    Boolean online;

    Boolean deleted;

    Timestamp createTime;

    Timestamp modifyTime;
}
