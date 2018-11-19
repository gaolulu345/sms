package com.tp.admin.data.wash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WashSiteRequest {

    int deviceId;
    String orderId;
    String msg;
    String key;
    Long time;
    String sign;

}
