package com.sms.admin.dao;

import com.sms.admin.data.entity.AdminPkAccountRoles;

import java.util.List;

public interface AdminPkAccountRolesDao {

    int insert(AdminPkAccountRoles partnerPkRoles);

    int update(AdminPkAccountRoles partnerPkRoles);

    List<AdminPkAccountRoles> listByAdminId(int adminId);

    AdminPkAccountRoles findByAdminId(int adminId);

}
