package com.sms.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderSearch extends Search {

    private Integer id;

    private String orderCode;

    //这里是进货商品父类型
    private Integer proType;

    //这里是新添加的，进货商品Id
    private Integer productId;

    //下面两个是新添加的规格和原产地
    private String standards;

    private String place;

    private String goodName;

    private String goodCompany;

    private Integer goodNumber;

    //这里是新添加的进货商品的图片
    private String proPicture;

    private Integer amount;

    private Integer supplyId;

    private Integer status;

    private Boolean deleted;


    @Override
    public void builData() {
        super.build();
        if (null != status && status < 0 ) {
            status = null;
        }
    }
}
