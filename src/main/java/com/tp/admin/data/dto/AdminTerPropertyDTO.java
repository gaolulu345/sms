package com.tp.admin.data.dto;

import com.tp.admin.enums.AdminNetMethodEnum;
import com.tp.admin.enums.AdminTerBusinessModeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTerPropertyDTO {

    private int terId;
    private String ip;
    private String port;
    private int netMethod;
    private String videoControl;
    private int bubbleLimit;
    private String terClientVersion;
    private int terBusiMode;
    private String terModel;
    private String terRemark;
    private int highLimit;
    private int wideLimit;
    private int startOnline;
    private String netMethodDesc;//联网方式描述
    private String terBusiModeDesc;//网店运营模式描述

    public void build(){
        this.terBusiModeDesc = AdminTerBusinessModeEnum.getByValue(this.terBusiMode).getDesc();
        this.netMethodDesc = AdminNetMethodEnum.getByValue(this.netMethod).getDesc();
    }




}
