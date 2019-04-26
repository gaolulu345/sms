package com.sms.admin.data.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AdminServiceSearch extends Search {

    private String appId;

    private String appSecret;

    private String appPublicKey;

    private String appPrivateKey;

    @Override
    protected void builData() {
        super.build();
    }
}
