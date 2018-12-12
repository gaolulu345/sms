package com.tp.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TerPropertySearch extends Search {

    private Integer id;//网点设备id
    private Integer startOnline;

    private String openId;


    @Override
    protected void builData() {
        super.build();
    }
}
