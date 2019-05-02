package com.sms.admin.data.dto;

import com.sms.admin.data.entity.AdminAccount;
import lombok.Data;

@Data
public class UserToken {

    long time;

    Object user;

    int randomnum;
}
