package com.tp.admin.data.parameter;

import com.tp.admin.data.search.Search;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxMiniTerSearch extends Search {

    Integer cityCode;

    Integer online;

    Integer status;

    @Override
    protected void builData() {
        super.build();
    }
}
