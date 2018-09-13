package com.tp.admin.data.dto;


import com.github.crab2died.annotation.ExcelField;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class AdminAccountDTO {

    @ExcelField(title = "员工Id", order = 1)
    private int id;
    private int rolesId;
    @ExcelField(title = "登录账号", order = 2)
    private String username;
    @ExcelField(title = "昵称", order = 3)
    private String name;
    @ExcelField(title = "简介", order = 4)
    private String intros;
    @ExcelField(title = "创建时间", order = 5)
    private Timestamp createTime;
    @ExcelField(title = "修改时间", order = 6)
    private Timestamp modifyTime;
    private boolean enable;
    @ExcelField(title = "是否删除", order = 7)
    private boolean deleted;
    @ExcelField(title = "最后登录时间", order = 8)
    private Timestamp lastLoginTime;
}
