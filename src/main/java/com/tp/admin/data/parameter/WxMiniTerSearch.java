package com.tp.admin.data.parameter;

import com.tp.admin.data.search.Search;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WxMiniTerSearch extends Search {

    String openId;

    Integer terId;

    Integer cityCode;

    Integer online;

    Integer status;

    String msg;

    @Override
    public void builData() {
        super.build();
    }
}
