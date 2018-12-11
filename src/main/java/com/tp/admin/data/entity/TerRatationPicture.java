package com.tp.admin.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tp.admin.enums.TerRatationPictureTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerRatationPicture {
    private Integer id;
    private Integer terId;
    @JsonIgnore
    private Timestamp createTime;
    @JsonIgnore
    private Timestamp modifyTime;
    private boolean deleted;
    private String picture;
    private boolean enable;
    @JsonIgnore
    private Timestamp enableTime;
    private Integer type;

    private String typeDesc;

    public void build(){
        if (this.type != null){
            this.typeDesc = TerRatationPictureTypeEnum.getByValue(this.type).getDesc();
        }
    }
}
