package com.tp.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminServiceInfo {

    private Integer id;
    private String key;
    private String value;
    private Integer type;
    private String detail;

}
