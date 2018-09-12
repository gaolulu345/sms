package com.tp.admin.data.dto;

import com.tp.admin.enums.OrderChannelEnum;
import com.tp.admin.enums.OrderTypeEnum;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderDTO {

    private int id;
    private int terId;
    private String terTitle;
    private int userId;
    private int status;  // 订单状态
    private int operationId; // 洗车操作id
    private int ticketId;
    private int cardId;
    private int amount;
    private int type;
    private int channel;
    private Timestamp payTime;

    private String operationDesc; // 洗车操作状态
    private String channelDesc; //订单来源
    private String typeDesc; //区分阿里订单和微信订单

    public void build(){
        this.typeDesc = OrderTypeEnum.getByCode(this.type).getDesc();
        this.channelDesc = OrderChannelEnum.getByCode(channel).getDesc();
    }

}
