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

    private String orderCode;

    private String goodName;

    private String goodCompany;

    private Integer goodNumber;

    private Integer amount;

    private Integer supplyId;

    private Integer status;

    private Boolean deleted;


    @Override
    public void builData() {
        super.build();
        if (null != status && status < 0 ) {
            status = null;
        }
    }
}
