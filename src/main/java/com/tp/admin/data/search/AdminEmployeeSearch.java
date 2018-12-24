package com.tp.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdminEmployeeSearch extends Search {

    Integer titleCode;
    Integer days;
    Integer opDesType;
    @Override
    protected void builData() {
        super.build();
    }
}
