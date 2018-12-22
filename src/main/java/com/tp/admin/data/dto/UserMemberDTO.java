package com.tp.admin.data.dto;

import com.tp.admin.enums.CardTypeEnum;
import com.tp.admin.enums.UserChanneEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMemberDTO {
    private Integer id;
    private String avatar;
    private String city;
    private String nickname;
    private String phone;
    private Integer type;
    private Integer washCardType;
    private Integer cnt;//剩余次数
    private Integer partnerCardId;//洗车卡id
    private String washCardName;

    private String typeDesc;
    private String washCardTypeDesc;

    public void build(){
        if (type != null){
            this.typeDesc = UserChanneEnum.getByCode(this.type).getDesc();
        }
        if (washCardType != null){
            this.washCardTypeDesc = CardTypeEnum.getByCode(this.washCardType).getDesc();
        }
    }

}
