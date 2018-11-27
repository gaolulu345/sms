package com.tp.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RefundSearch extends Search {

    int id;
    Integer orderId;
    Integer status;
    Integer reason;  // 现在用type枚举映射
    List<Integer> terIds = new ArrayList<>();

    @Override
    public void builData() {
        super.build();
    }
}
