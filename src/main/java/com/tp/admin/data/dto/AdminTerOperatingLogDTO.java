package com.tp.admin.data.dto;

import com.tp.admin.enums.AdminTerOperatingLogSourceEnum;
import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminTerOperatingLogDTO {

    private int id;
    private Timestamp createTime;
    private Long createTimestamp;
    private int terId;
    private String terTitle;
    private int maintionId;
    private int merchantId;
    private String maintionUsername;
    private String merchantUsername;
    private String username;
    private String title;
    private String intros;
    private int type;
    private int opSource;
    private String imgs;
    private String[] img;
    private boolean success;
    private String opSourceDesc;
    private String typeDesc;

    public void build(){
        typeDesc = WashTerOperatingLogTypeEnum.getByCode(type).getDesc();
        opSourceDesc = AdminTerOperatingLogSourceEnum.getByCode(opSource).getDesc();
        if(null != createTime){
            createTimestamp = createTime.getTime();
        }
        if (null == username) {
            if (AdminTerOperatingLogSourceEnum.MAINTAUN.getValue() == opSource) {
                username = maintionUsername;
            }else if(AdminTerOperatingLogSourceEnum.MERCHANT.getValue() == opSource){
                username = merchantUsername;
            }
        }
        if (null != imgs && imgs.trim().length() != 0) {
            try{
                img = imgs.split(",");
            }catch (Exception e){
                img = new String[]{};
            }
        }
    }

}
