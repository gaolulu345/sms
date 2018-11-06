package com.tp.admin.data.parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxMiniAuthDTO {

    String appId;

    String code;
}
