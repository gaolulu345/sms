package com.tp.admin.data.entity;

import com.tp.admin.enums.MiniAutoTypeEnum;
import com.tp.admin.enums.UserChanneEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminTemplateInfo {
    private Integer id;
    private String templateId;
    private Integer type;
    private String templateDesc;
    private Integer serviceType;

    private String typeDesc;
    private String serviceTypeDesc;

    public void build(){
        if (this.type != null){
            this.typeDesc = UserChanneEnum.getByCode(this.type).getDesc();
        }
        if (this.serviceType != null){
            this.serviceTypeDesc = MiniAutoTypeEnum.getByCode(this.serviceType).getDesc();
        }
    }
}
