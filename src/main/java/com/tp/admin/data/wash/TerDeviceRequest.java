package com.tp.admin.data.wash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerDeviceRequest {
    int deviceId;
    List<String> pictures;
    int terId;
    String msg;
    String key;
    Long time;
    String sign;
}
