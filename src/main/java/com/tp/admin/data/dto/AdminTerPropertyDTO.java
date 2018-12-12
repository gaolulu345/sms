package com.tp.admin.data.dto;

import com.tp.admin.enums.AdminNetMethodEnum;
import com.tp.admin.enums.AdminOnlineFreeStartEnum;
import com.tp.admin.enums.AdminTerBusinessModeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTerPropertyDTO {

    private Integer id;
    private Integer terId;
    //private String openId;
    //private String ip;
    //private String port;
    private Integer netMethod;
    private String videoControl;
    private Integer bubbleLimit;
    private String terClientVersion;
    private Integer terBusiMode;
    private String terModel;
    private String terRemark;
    private Integer highLimit;
    private Integer wideLimit;
    private Integer startOnline;
    private boolean adExist;
    private Integer screenWide;
    private Integer screenHigh;
    private String netMethodDesc;//联网方式描述
    private String terBusiModeDesc;//网店运营模式描述
    private String startOnlineDesc;//若为单网点有人值守，0表示未启动，1表示启动

    public void build(){
        if (terBusiMode != null){
            this.terBusiModeDesc = AdminTerBusinessModeEnum.getByValue(this.terBusiMode).getDesc();
        }
        if (netMethod != null){
            this.netMethodDesc = AdminNetMethodEnum.getByValue(this.netMethod).getDesc();
        }
        if (startOnline != null){
            this.startOnlineDesc = AdminOnlineFreeStartEnum.getByValue(this.startOnline).getDesc();
        }
    }




}
