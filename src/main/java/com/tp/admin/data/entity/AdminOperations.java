package com.tp.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 功能操作
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOperations {

    private int id;
    private Timestamp createTime;
    private Timestamp modifyTime;
    private boolean enable;
    private boolean deleted;
    private String operationsName;
    private String details;
    private String resource;
    private int menuId;

    // 目前就根据资源的路径去重复吧
    @Override
    public boolean equals(Object obj) {
        AdminOperations adminOperations = (AdminOperations)obj;
        return resource.equals(adminOperations.resource);
    }

    @Override
    public int hashCode() {
        String in =  resource;
        return in.hashCode();
    }

}
