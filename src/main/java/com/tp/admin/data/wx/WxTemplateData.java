package com.tp.admin.data.wx;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class WxTemplateData {

    String value;
    String color;

    public WxTemplateData(String value, String color) {
        this.value = value;
        this.color = color;
    }
}
