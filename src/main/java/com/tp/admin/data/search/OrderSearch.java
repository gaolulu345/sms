package com.tp.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderSearch extends Search {

    private Integer status;
    private Integer type;
    private int[] terIds;

    @Override
    public void builData() {
        super.build();
        if (status != null && status < 0 ) {
            status = null;
        }
    }
}
