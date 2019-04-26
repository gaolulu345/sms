package com.sms.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RangeSearch extends Search {

    int year;
    int month;
    int day;
    // 日期周期，单位日。
    int dateCycle;
    String startTime;
    String endTime;
    // default
    //int status = OrderStatusEnum.ASK_CHECK.getValue();

    List ids;

    @Override
    public void builData() {
        super.build();
    }
}
