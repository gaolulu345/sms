package com.sms.admin.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductParentDTO {
    Integer id;

    Integer type;

    String typeName;

    Boolean enable;

    Boolean deleted;

    Timestamp createTime;

    Timestamp modifyTime;
}
