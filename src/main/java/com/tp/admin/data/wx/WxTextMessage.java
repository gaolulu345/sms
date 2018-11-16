package com.tp.admin.data.wx;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WxTextMessage {

    String touser;
    String msgtype = "text";
    WxTextMessageBody text;

    public WxTextMessage(String touser, String body) {
        this.touser = touser;
        text = new WxTextMessageBody(body);
    }
}
