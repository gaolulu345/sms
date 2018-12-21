package com.tp.admin.data.parameter;

import com.tp.admin.data.search.Search;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WxMiniSearch extends Search {

    String openId;

    Integer terId;

    Integer cityCode;

    Integer online;

    Integer status;

    String msg;

    List<Integer> ids;

    String[] imgs;

    Integer userType;//支付宝或者微信

    Integer washCardType;

    Integer opSource;

    @Override
    public void builData() {
        super.build();
    }
}
