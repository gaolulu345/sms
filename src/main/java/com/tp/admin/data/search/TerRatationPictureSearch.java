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

    @JsonIgnore
    Integer id;
    @JsonIgnore
    int[] ids;
    Integer deviceId;
    @JsonIgnore
    Timestamp createTime;
    @JsonIgnore
    Integer type;
    @JsonIgnore
    boolean enable;
    @JsonIgnore
    Timestamp enableTime;
    @JsonIgnore
    Timestamp modifyTime;
    @JsonIgnore
    String picture;
    boolean deleted;

    @Override
    protected void builData() {
        super.build();
    }
}
