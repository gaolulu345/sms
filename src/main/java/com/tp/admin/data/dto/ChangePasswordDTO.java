package com.tp.admin.data.dto;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    // 原始密码
    String opw;
    // 新密码
    String npw;
    // 第二次输入新密码
    String snpw;
}
