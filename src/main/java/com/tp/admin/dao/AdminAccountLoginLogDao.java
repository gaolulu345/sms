package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminAccountLoginLog;
import com.tp.admin.data.search.AdminSearch;

import java.util.List;

public interface AdminAccountLoginLogDao {

    int insert(AdminAccountLoginLog adminAccountLoginLog);

    List<AdminAccountLoginLog> listBySearch(AdminSearch adminSearch);

}
