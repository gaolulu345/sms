package com.sms.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private int id;
    private int terId;
    private int userId;
    private int status;
    private int operationId;  //清除这两个字段后会使查找历史有效订单变得负责，故而保留
    private int refundId;
    private boolean deleted;
    
    private int amount;
    private int sabisId;
    private int sabisTrimId;
    private int ticketId;
    private int cardId;
    private int type;//区分阿里订单和微信订单
    private int channel;
    
    private String alipayStr;
    private String wxpayStr;
    private boolean noticed;
    
    private Timestamp createTime;
    private Timestamp payTime;
    
}
