package com.tp.admin.data.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TerPropertySearch extends Search {

    @JsonIgnore
    private Integer id;//网点设备id

    @JsonIgnore
    private String openId;


    @Override
    protected void builData() {
        super.build();
    }
}
