package com.tp.admin.data.wx;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxAccessToken {

    @SerializedName("access_token")
    String accessToken;

    @SerializedName("expires_in")
    Integer expiresIn;

    Integer errcode;

    String errmsg;

}
