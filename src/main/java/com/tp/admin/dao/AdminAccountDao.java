package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminAccount;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

public interface AdminAccountDao {

    AdminAccount findByUsername(String username);

    int updateLastLoginTime(@Param("id") int id , @Param("time") Timestamp lastLoginTime);
}
