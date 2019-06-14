package com.sms.admin.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DataTotalDTO {

    Long orderTotal = 0L;
    //支出七天
    Long sevenDayMoneyTotal = 0L;
    //支出一天
    Long oneDayMoneyTotal = 0L;
    //收入一天
    Long oneDayReceiveMoneyTotal = 0L;
    //收入七天
    Long sevenDayReceiveMoneyTotal = 0L;
}