package com.tp.admin.data.dto;

import com.tp.admin.enums.WashTerStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerInfoDTO {

    private int id;
    private String statusDesc;
    //若该网点为故障时，将具体的故障信息保存自该变量
    private String faultDesc;
    private int status;
    private String title;
    private String code;
    private String address;
    private String geoCode;
    private String cover;
    private boolean online;

    public void build(){
        this.statusDesc = WashTerStateEnum.getByCode(this.status).getDesc();
    }
}