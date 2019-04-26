package com.sms.admin.data.dto;

import com.github.crab2died.annotation.ExcelField;
import com.sms.admin.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    @ExcelField(title = "订单Id", order = 1)
    private Integer id;
    @ExcelField(title = "订单编号", order = 2)
    private String orderCode;
    @ExcelField(title = "商品名称", order = 3)
    private String goodName;
    @ExcelField(title = "商品单位", order = 4)
    private String goodCompany;

    private Integer supplyId;
    @ExcelField(title = "供应商名称", order = 5)
    private String supplyName;
    @ExcelField(title = "商品数量", order = 6)
    private Integer goodNumber;
    @ExcelField(title = "金额（元）", order = 7)
    private Integer amount;

    private Integer status;
    @ExcelField(title = "订单状态", order = 8)
    private String statusDesc;
    @ExcelField(title = "创建时间", order = 9)
    private Timestamp createTime;

    public void build() {
        this.statusDesc = OrderStatusEnum.getByCode(this.status).getDesc();
    }




}
