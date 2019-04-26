package com.sms.admin.data.search;

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

    private Integer id;

    private String goodName;

    private Integer supplyId;

    private Integer status;


    @Override
    public void builData() {
        super.build();
        if (null != status && status < 0 ) {
            status = null;
        }
    }
}
