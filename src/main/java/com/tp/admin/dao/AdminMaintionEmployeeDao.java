package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminMaintionEmployee;
import com.tp.admin.data.parameter.WxMiniRegisterDTO;
import org.apache.ibatis.annotations.Param;

public interface AdminMaintionEmployeeDao {

    AdminMaintionEmployee findByWxMiniId(@Param("code") String code);

    AdminMaintionEmployee findByPhone(@Param("phone") String phone);

    int insert(AdminMaintionEmployee employee);
}
