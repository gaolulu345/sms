package com.sms.admin.data.search;

import lombok.Data;

@Data
public class SupplySearch extends Search {
    Integer id;

    String supplyName;

    String supplyCode;

    String contactName;

    String contactPhone;

    String contactAddress;

    String fax;

    String intros;

    Boolean deleted;

    @Override
    public void builData() {
        super.build();
    }
}
