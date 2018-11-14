package com.tp.admin.data.dto;

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
    Long sevenDayMoneyTotal = 0L;
    Long oneDayMoneyTotal = 0L;
}