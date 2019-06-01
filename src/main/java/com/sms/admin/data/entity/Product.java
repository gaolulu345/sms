package com.sms.admin.data.entity;

import com.sms.admin.data.dto.ProductDTO;
import com.sms.admin.data.search.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    Integer id;

    String proName;

    String proPicture;

    Integer proType;

    Integer oldPrice;

    Integer newPrice;

    String standards;

    Integer repertory;

    Integer saleNum;

    String place;

    Boolean online;

    Boolean deleted;

    Timestamp createTime;

    Timestamp modifyTime;

    public Product(OrderSearch orderSearch, ProductDTO productDTO) {
        this.id = orderSearch.getProductId();
        this.proName = orderSearch.getGoodName();
        this.standards = orderSearch.getStandards();
        this.repertory = productDTO.getRepertory() + orderSearch.getGoodNumber();
        this.place = orderSearch.getPlace();
        this.proPicture = orderSearch.getProPicture();
        this.oldPrice = orderSearch.getAmount()/orderSearch.getGoodNumber();
    }

    public Product(OrderSearch orderSearch){
        this.id = orderSearch.getProductId();
        this.proType = orderSearch.getProType();
        this.proName = orderSearch.getGoodName();
        this.standards = orderSearch.getStandards();
        this.repertory = orderSearch.getGoodNumber();
        this.place = orderSearch.getPlace();
        this.proPicture = orderSearch.getProPicture();
        this.oldPrice = orderSearch.getAmount()/orderSearch.getGoodNumber();
    }
}
