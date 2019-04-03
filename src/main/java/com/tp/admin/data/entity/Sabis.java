package com.tp.admin.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sabis {

    private int id;
    private String title;
    private String intros;
    private Timestamp createTime;
    private String price;
    private Integer payPrice;
    private boolean cardEnable;
    private boolean ticketEnable;
    private int prov;
    private int city;
    private int area;
    private int terId;

    public void build(){
        if(null != payPrice && 0 != payPrice){
            this.price = String.valueOf(payPrice / 100.0);
        }
    }

}
