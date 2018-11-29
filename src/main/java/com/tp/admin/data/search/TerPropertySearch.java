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

    private Integer terId;
    private Integer startOnline;


    @Override
    protected void builData() {
        super.build();
    }
}
