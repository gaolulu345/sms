package com.sms.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PurchaseOrderSearch extends Search {

    Integer id;

    Integer proType;

    Integer proId;

    Integer saleNum;

    Integer saleValue;

    Integer netProfits;

    String intros;
    @Override
    protected void builData() {
        super.build();
    }
}
