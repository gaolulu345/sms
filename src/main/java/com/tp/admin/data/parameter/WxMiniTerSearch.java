package com.tp.admin.data.parameter;

import com.tp.admin.data.search.Search;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxMiniTerSearch extends Search {

    Integer terId;

    Integer cityCode;

    Integer online;

    Integer status;

    String msg;

    @Override
    protected void builData() {
        super.build();
    }
}
