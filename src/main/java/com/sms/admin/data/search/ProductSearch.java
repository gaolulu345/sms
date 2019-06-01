package com.sms.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductSearch extends Search {
    String proName;

    Integer proType;

    String standards;

    Integer productId;

    Integer oldPrice;

    Integer newPrice;

    Boolean online;

    public ProductSearch(OrderSearch orderSearch) {
        this.proType = orderSearch.getProType();
        this.productId = orderSearch.getProductId();
    }


    @Override
    public void builData() {
        super.build();
    }
}
