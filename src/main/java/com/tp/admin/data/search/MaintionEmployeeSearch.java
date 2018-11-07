package com.tp.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MaintionEmployeeSearch extends Search {

    Boolean deleted;

    Boolean enable;

    int[] ids;

    @Override
    public void builData() {
        super.build();
    }

}
