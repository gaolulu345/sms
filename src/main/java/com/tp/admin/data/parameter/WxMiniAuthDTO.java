package com.tp.admin.data.parameter;

import com.tp.admin.enums.MiniAutoTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxMiniAuthDTO {

    String appId;

    String code;

    String openId;

    Integer type;//要授权的类型

    String typeDesc;

    public void build(){
        this.typeDesc = MiniAutoTypeEnum.getByCode(this.type).getDesc();
    }


}
