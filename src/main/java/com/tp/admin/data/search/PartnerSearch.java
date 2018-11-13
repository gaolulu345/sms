package com.tp.admin.data.search;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PartnerSearch extends Search {

    @Override
    public void builData() {
        super.build();
    }
}
