package com.sms.admin.dao;

import com.sms.admin.data.entity.AdminAccount;
import com.sms.admin.data.entity.AdminAccountInfo;

public interface AdminAccountInfoDao {

    int insert(AdminAccount adminAccount);

    int update(AdminAccount adminAccount);

    AdminAccountInfo findByAdminId(int id);

}
