package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminMaintionEmployee;
import org.apache.ibatis.annotations.Param;

public interface AdminMaintionEmployeeDao {

    AdminMaintionEmployee findByWxMiniId(@Param("code") String code);
}
