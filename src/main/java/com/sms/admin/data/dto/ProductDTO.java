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

    String typeName;

    Integer oldPrice;

    Integer newPrice;

    String standards;

    Integer repertory;

    Integer saleAll;

    String place;

    Boolean online;

    Boolean deleted;

    Timestamp createTime;

    Timestamp modifyTime;

    public void build() {
        if (null != oldPrice) {
            oldPrice = oldPrice / 100;
        }
        if (null != newPrice) {
            newPrice = newPrice / 100;
        }
    }
}
