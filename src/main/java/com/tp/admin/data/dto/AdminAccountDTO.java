package com.tp.admin.data.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.crab2died.annotation.ExcelField;
import com.tp.admin.enums.AdminGenderEnum;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class AdminAccountDTO {

    @ExcelField(title = "员工Id", order = 1)
    private int id;
    private int rolesId;
    private String rolesName;
    @ExcelField(title = "登录账号", order = 3)
    private String username;

    @JsonIgnore
    private String password;

    @ExcelField(title = "昵称", order = 4)
    private String name;
    @ExcelField(title = "简介", order = 5)
    private String intros;
    @ExcelField(title = "创建时间", order = 10)
    private Timestamp createTime;
    @ExcelField(title = "修改时间", order = 11)
    private Timestamp modifyTime;
    private boolean enable;
    @ExcelField(title = "是否删除", order = 12)
    private boolean deleted;
    @ExcelField(title = "最后登录时间", order = 13)
    private Timestamp lastLoginTime;

    //用户编码
    @ExcelField(title = "员工编号", order = 2)
    private String usercode;

    private int gender;
    @ExcelField(title = "出生日期", order = 7)
    private Timestamp bornDate;
    @ExcelField(title = "联系电话", order = 8)
    private String telephone;
    @ExcelField(title = "住址", order = 9)
    private String address;
    @ExcelField(title = "性别", order = 6)
    private String genderDesc;

    public void build() {
        this.genderDesc = AdminGenderEnum.getByValue(this.gender).getValueDesc();
    }

}
