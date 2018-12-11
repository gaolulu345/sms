package com.tp.admin.data.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TerRatationPictureSearch extends Search{

    Integer id;
    int[] ids;
    Integer terId;
    @JsonIgnore
    Timestamp createTime;
    Integer type;
    boolean enable;
    Timestamp enableTime;
    @JsonIgnore
    Timestamp modifyTime;
    String picture;
    boolean deleted;

    @Override
    protected void builData() {
        super.build();
    }
}
