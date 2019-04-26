package com.sms.admin.dao;

import com.sms.admin.data.dto.AdminAccountDTO;
import com.sms.admin.data.entity.AdminAccount;
import com.sms.admin.data.search.AdminSearch;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface AdminAccountDao {

    AdminAccountDTO findDtoByUsername(String username);

    AdminAccount findByUsername(String username);

    AdminAccount findById(int id);

    int insert(AdminAccount adminAccount);

    int update(AdminAccount adminAccount);

    int updateLastLoginTime(@Param("id") int id , @Param("time") Timestamp lastLoginTime);

    int bachUpdateDeleted(AdminSearch adminSearch);

    int cntBySearch(AdminSearch adminSearch);

    List<AdminAccountDTO> listBySearch(AdminSearch adminSearch);

}
