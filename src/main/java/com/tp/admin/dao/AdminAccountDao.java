package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminAccount;

public interface AdminAccountDao {

    AdminAccount findByUsername(String username);

    int updateLastLoginTime(int id);
}
