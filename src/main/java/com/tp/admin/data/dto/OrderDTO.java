package com.tp.admin.data.dto;

import com.github.crab2died.annotation.ExcelField;
import com.tp.admin.enums.OrderChannelEnum;
import com.tp.admin.enums.OrderStatusEnum;
import com.tp.admin.enums.OrderTypeEnum;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderDTO {

    @ExcelField(title = "订单Id", order = 1)
    private int id;
    private int terId;
    @ExcelField(title = "网点名称", order = 2)
    private String terTitle;
    private int userId;

    private int status;  // 订单状态
    private int operationId; // 洗车操作id
    // 优惠券
    private int ticketId;
    // 洗车卡
    private int cardId;
    @ExcelField(title = "支付金额/分", order = 4)
    private int amount;
    private int type;
    private int channel;
    @ExcelField(title = "支付时间", order = 5)
    private Timestamp payTime;

    private String operationDesc; // 洗车操作状态

    @ExcelField(title = "订单状态", order = 3)
    private String statusDesc; //订单状态
    @ExcelField(title = "订单来源", order = 7)
    private String channelDesc; //订单来源
    @ExcelField(title = "支付方式", order = 6)
    private String typeDesc; //区分阿里订单和微信订单

    // 洗车卡名称
    private String cardTitle;
    // 优惠券名称
    private String ticketTitle;

    public void build(){
        this.statusDesc = OrderStatusEnum.getByCode(this.status).getDesc();
        this.typeDesc = OrderTypeEnum.getByCode(this.type).getDesc();
        this.channelDesc = OrderChannelEnum.getByCode(channel).getDesc();
    }

}
