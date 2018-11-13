package com.tp.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MerchantEmployeeSearch extends Search{

    Integer partnerId;

    Integer id;

    Boolean deleted;

    Boolean enable;

    int[] ids;

    @Override
    public void builData() {
        super.build();
    }

}




