package com.tp.admin.dao;

import com.tp.admin.data.dto.AdminAccountDTO;
import com.tp.admin.data.entity.AdminAccount;
import com.tp.admin.data.entity.AdminAccountInfo;
import com.tp.admin.data.search.AdminSearch;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface AdminAccountInfoDao {

    int insert(AdminAccount adminAccount);

    int update(AdminAccount adminAccount);

    AdminAccountInfo findByAdminId(int id);

}
