package com.tp.admin.data.result;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxJscodeSessionResult {

    @SerializedName("session_key")
    private String sessionKey;
    @SerializedName("openid")
    private String openId;
    @SerializedName("unionid")
    private String unionId;
}
