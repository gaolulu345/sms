package com.tp.admin.data.wx;

import lombok.Data;

@Data
public class WxTextMessageBody {

    String content;

    public WxTextMessageBody(String content) {
        this.content = content;
    }
}
