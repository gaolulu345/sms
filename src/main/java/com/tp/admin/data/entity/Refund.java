package com.tp.admin.data.entity;

import com.github.crab2died.annotation.ExcelField;
import com.tp.admin.enums.RefundStatusEnum;
import com.tp.admin.enums.RefundTypeEnum;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Refund {


    @ExcelField(title = "退款编号", order = 1)
    int id;
    int reason;  // 原因
    @ExcelField(title = "退款金额/元", order = 4)
    int amount;
    @ExcelField(title = "洗车订单编号", order = 2)
    int orderId;
    int status;  // 进度
    @ExcelField(title = "留言", order = 10)
    String msg;  // 退款说明
    @ExcelField(title = "联系人电话", order = 11)
    String phone;// 手机号
    boolean deleted;
    Timestamp modifyTime;
    Timestamp createTime;
    @ExcelField(title = "审批时间", order = 7)
    Timestamp checkTime;  // 审批时间
    @ExcelField(title = "退款时间", order = 9)
    Timestamp refundTime; // 退款时间
    @ExcelField(title = "审批人", order = 6)
    String sysAdminNameCheck; // 审批管理员名称
    @ExcelField(title = "退款人", order = 8)
    String sysAdminNamePay;   // 退款管理员名称




    @ExcelField(title = "退款原因描述", order = 3)
    String reasonDesc; // 原因描述
    @ExcelField(title = "退款进度", order = 5)
    String statusDesc; // 进度描述
    long createTimeMillis;

    // 退款人Id
    int userId;

    public void build() {
        this.reasonDesc = RefundTypeEnum.getByValue(this.reason).getDesc();
        this.statusDesc = RefundStatusEnum.getByValue(this.status).getDesc();
        if (null != createTime) {
            createTimeMillis = createTime.getTime();
        }
    }

}
