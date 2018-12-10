package com.tp.admin.data.entity;

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

    private String typeDesc;

    public void build(){
        if (this.type != null){
            this.typeDesc = UserChanneEnum.getByCode(this.type).getDesc();
        }
    }
}
