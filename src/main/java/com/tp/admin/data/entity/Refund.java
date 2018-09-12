package com.tp.admin.data.entity;

import com.tp.admin.enums.RefundStatusEnum;
import com.tp.admin.enums.RefundTypeEnum;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Refund {

    int id;
    int reason;  // 原因
    String amount;
    int orderId;
    int status;  // 进度
    String msg;  // 退款说明
    String phone;// 手机号
    boolean deleted;
    Timestamp modifyTime;
    Timestamp createTime;
    Timestamp checkTime;  // 审批时间
    Timestamp refundTime; // 退款时间
    String sysAdminNameCheck; // 审批管理员名称
    String sysAdminNamePay;   // 退款管理员名称
    String reasonDesc; // 原因秒速
    String statusDesc; // 进度描述

    public void build() {
        this.reasonDesc = RefundTypeEnum.getByValue(this.reason).getDesc();
        this.statusDesc = RefundStatusEnum.getByValue(this.status).getDesc();
    }

}
