package com.sms.admin.data.dto;

import com.github.crab2died.annotation.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseOrderDTO {
    @ExcelField(title = "销售订单Id",order = 1)
    Integer id;

    //一级
    Integer proType;

    //二级
    Integer proId;
    @ExcelField(title = "销售量",order = 4)
    Integer saleNum;

    //销售总额
    @ExcelField(title = "销售总额（单位：元）", order = 5)
    Integer saleValue;
    @ExcelField(title = "净利润（单位：元）", order = 6)
    Integer netProfits;
    @ExcelField(title = "销售备注", order = 7)
    String intros;

    Timestamp createTime;
    @ExcelField(title = "销售商品名", order = 3)
    String proName;
    @ExcelField(title = "销售类型",order = 2)
    String typeName;

    public void build() {
        if (null != saleValue) {
            saleValue = saleValue / 100;
        }
        if (null != netProfits) {
            netProfits = netProfits / 100;
        }
    }


}
