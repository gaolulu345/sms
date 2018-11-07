package com.tp.admin.data.dto;

import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class AdminMaintionEmployeeLogTerOperatingDTO {

    private int id;
    private Timestamp createTime;
    private int terId;
    private String terTitle;
    private int employeeId;
    private String username;
    private String title;
    private String intros;
    private int type;
    private boolean sucess;
    private String typeDesc;

    public void build(){
        typeDesc = WashTerOperatingLogTypeEnum.getByCode(terId).getDesc();
    }

}
