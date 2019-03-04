package com.tp.admin.data.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMaintionEmployee {
    private int id;
    private String miniWxId;
    @JsonIgnore
    private String wxUnionId;
    private String name;
    private String phone;
    @JsonIgnore
    private Timestamp createTime;
    @JsonIgnore
    private Timestamp modifyTime;
    private String formId;
    private boolean enable;
    //该维保人员是否允许接受故障短信通知
    private boolean enableSm;
    private boolean deleted;
    private Timestamp lastLoginTime;

}
