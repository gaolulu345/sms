package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminAccountLoginLog;
import com.tp.admin.data.search.AdminSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminAccountLoginLogDao {

    int insert(AdminAccountLoginLog adminAccountLoginLog);

    List<AdminAccountLoginLog> listBySearch(@Param("more") Boolean more , @Param("username") String username , @Param("pageSize") Integer pageSize , @Param("offset") Integer offset);

}
