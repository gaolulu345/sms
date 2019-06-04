package com.sms.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductParentSearch extends Search {
    Integer id;

    String typeName;

    Boolean deleted;

    @Override
    public void builData() {
        super.build();
    }
}
