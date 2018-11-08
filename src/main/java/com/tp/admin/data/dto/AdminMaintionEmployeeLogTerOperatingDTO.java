package com.tp.admin.data.dto;

import com.tp.admin.enums.WashTerOperatingLogTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMaintionEmployeeLogTerOperatingDTO {

    private int id;
    private Timestamp createTime;
    private Long createTimestamp;
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
        typeDesc = WashTerOperatingLogTypeEnum.getByCode(type).getDesc();
        if(null != createTime){
            createTimestamp = createTimestamp.longValue();
        }
    }

}
