package com.tp.admin.data.wx;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class WxTemplateMessage {

    String accessToken;
    String touser;
    String templateId;
    String formId;
    String page;


    public WxTemplateMessage(String accessToken, String touser, String templateId, String formId, String page) {
        this.accessToken = accessToken;
        this.touser = touser;
        this.templateId = templateId;
        this.formId = formId;
        this.page = page;
    }
}
