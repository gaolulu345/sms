package com.tp.admin.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMerchantEmployeeInfoDTO {

    private Integer id;
    private String openId;
    private String partnerTitle;
    private String username;
    private Integer level;
    private Boolean subjection;
}
