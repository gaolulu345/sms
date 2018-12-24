package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminEmployeeOperatingLog;
import com.tp.admin.data.search.AdminEmployeeSearch;

import java.util.List;

public interface AdminEmployeeOperatingLogDao {
    int insertMaintionEmployeeOperatingLog(AdminEmployeeOperatingLog adminEmployeeOperatingLog);

    int insertMerchantEmployeeOperatingLog(AdminEmployeeOperatingLog adminEmployeeOperatingLog);

    List<AdminEmployeeOperatingLog> list(AdminEmployeeSearch adminEmployeeSearch);

    int cntOfEmployeeLog(AdminEmployeeSearch adminEmployeeSearch);
}
