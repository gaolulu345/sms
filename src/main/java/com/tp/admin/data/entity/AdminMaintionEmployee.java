package com.tp.admin.data.entity;


import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMaintionEmployee {
    @Expose
    private int id;
    private String miniWxId;
    private String wxUnionId;
    @Expose
    private Timestamp createTime;
    @Expose
    private Timestamp modifyTime;
    @Expose
    private boolean enable;
    @Expose
    private boolean deleted;
    private Timestamp lastLoginTime;

}
