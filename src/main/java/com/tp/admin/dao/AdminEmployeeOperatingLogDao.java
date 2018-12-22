package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminEmployeeOperatingLog;

public interface AdminEmployeeOperatingLogDao {
    int insertMaintionEmployeeOperatingLog(AdminEmployeeOperatingLog adminEmployeeOperatingLog);

    int insertMerchantEmployeeOperatingLog(AdminEmployeeOperatingLog adminEmployeeOperatingLog);
}
