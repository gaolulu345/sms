package com.tp.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerUserWashCard {

    private int id;
    private int userId;
    private Boolean cntLess;    // 是否无限次
    private Integer cnt;        // 剩余次数
    private Integer type;       // 卡类型
    private Integer finalPrice; // 一口价
    private Integer discount;   // 折扣力度 100无折扣 0 免费。
    private int partnerId;
    private int partnerCardId;
    private Boolean deleted;
    private Boolean invalid;
    private Timestamp createTime;
    private Timestamp validTime;

}
