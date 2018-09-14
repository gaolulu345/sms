package com.tp.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RefundSearch extends  Search {

    int id;
    Integer orderId;
    Integer status;
    Integer reason;  // 现在用type枚举映射
    private String startTime;
    private String endTime;

    @Override
    public void builData() {
        super.build();
    }
}
