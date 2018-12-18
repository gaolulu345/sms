package com.tp.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TemplateSearch extends Search {

    private String touser;//微信支付宝模板发送人
    private Integer formId;
    private Object data;
    private String page;
    private Integer type;//判断是支付宝还是微信

    private Integer templateId;


    @Override
    protected void builData() {
        super.build();
    }
}
