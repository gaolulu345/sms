package com.tp.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderSearch extends Search {

    private Integer status;
    private Integer orderId;
    private Integer type;
    private List<Integer> terIds = new ArrayList<>();

    @Override
    public void builData() {
        super.build();
        if (status != null && status < 0 ) {
            status = null;
        }
    }
}
