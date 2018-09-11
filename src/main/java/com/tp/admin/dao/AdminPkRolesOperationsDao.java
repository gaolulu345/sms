package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminPkRolesOperations;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminPkRolesOperationsDao {

    int insert(AdminPkRolesOperations sysPkRolesOperations);

    int update(AdminPkRolesOperations sysPkRolesOperations);

    List<AdminPkRolesOperations> listByRolesId(int rolesId);

    List<AdminPkRolesOperations> listByPartnerId(int adminId);

    List<AdminPkRolesOperations> listByRolesIdAndIds(@Param("rolesId") int rolesId, @Param("ids") List<Integer> ids);




}
