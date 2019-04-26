package com.sms.admin.dao;

import com.sms.admin.data.entity.AdminPkRolesMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminPkRolesMenuDao {

    int insert(AdminPkRolesMenu sysPkRolesMenu);

    int update(AdminPkRolesMenu sysPkRolesMenu);

    List<AdminPkRolesMenu> listByRolesId(int rolesId);

    List<AdminPkRolesMenu> listByAdminId(int adminId);

    List<AdminPkRolesMenu> listByRolesIdAndIds(@Param("rolesId") int rolesId, @Param("ids") int[] ids);



}
