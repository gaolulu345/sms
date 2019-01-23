package com.tp.admin.data.dto;


import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class PartnerUserWashCardDetailDTO {

    private Integer id;
    // 合作伙伴洗车卡Id
    private Integer partnerCardId;
    // logo 图片Id
    private Integer logoId;
    // logo 地址
    private String logo;
    // logo 字体
    private String fontColor;
    // 洗车卡名称
    private String name;
    // 洗车卡类型 com.tp.enums.ExCardTypeEnum
    private Integer type;
    // 使用次数
    private Integer cnt;
    // 是否无限次
    private Boolean cntLess;
    // 优惠价格
    private Integer finalPrice;
    // 折扣力度 100无折扣 0 免费。
    private Integer discount;
    // 是否过期
    private Boolean invalid;
    // 发卡单位名称
    private String createPartnerName;
    // 发行时间
    private Timestamp createTime;
    // 发行过期时间
    private Timestamp validTime;

    private Long createTimeMillis;

    private Long validTimeMillis;

    //private List<PartnerWashCardPkTerDTO> terList;

    public void build(){
        if (null != createTime) {
            createTimeMillis = createTime.getTime();
        }
        if (null != validTime) {
            validTimeMillis = validTime.getTime();
        }
    }

}
