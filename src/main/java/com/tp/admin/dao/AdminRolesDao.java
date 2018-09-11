package com.tp.admin.dao;

import com.tp.admin.data.entity.AdminRoles;
import com.tp.admin.data.search.SystemSearch;

import java.util.List;

public interface AdminRolesDao {

    AdminRoles findById(int id);

    int insert(AdminRoles sysRoles);

    int update(AdminRoles sysMenu);

    List<AdminRoles> listBySearch(SystemSearch systemSearch);

    int cntBySearch(SystemSearch systemSearch);

    int bachUpdateEnable(SystemSearch systemSearch);

    int bachUpdateDeleted(SystemSearch systemSearch);
}
